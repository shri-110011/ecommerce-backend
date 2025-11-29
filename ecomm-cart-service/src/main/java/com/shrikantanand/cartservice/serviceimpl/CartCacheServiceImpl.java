package com.shrikantanand.cartservice.serviceimpl;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shrikantanand.cartservice.dto.CartItemMutationResponse;
import com.shrikantanand.cartservice.enumeration.ItemQtyUpdateType;
import com.shrikantanand.cartservice.model.CartItem;
import com.shrikantanand.cartservice.service.CartCacheService;

@Service
public class CartCacheServiceImpl implements CartCacheService {
	
	private static final int CART_TTL_DAYS = 2;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public CartItemMutationResponse addItemToCart(String cartId, Integer productId) {
		boolean isItemPresent = isItemInCart(cartId, productId);
		String status;
		String message;
		if(!isItemPresent) {
			redisTemplate.opsForHash().put(cartId, Integer.toString(productId), "1");
			setTTLForCart(cartId);
			status = "SUCCESS";
			message = "Item added to cart successfully";
		}
		else {
			status = "FAILED";
			message = "Item already present in cart";
		}
		CartItem addedItem = new CartItem(productId, 1);
		CartItemMutationResponse response = new CartItemMutationResponse(status, message, 
				cartId, addedItem);
		return response;
	}
	
	private boolean isCartPresent(String cartId) {
		return redisTemplate.hasKey(cartId);
	}
	
	private boolean isItemInCart(String cartId, Integer productId) {
		return redisTemplate.opsForHash().hasKey(cartId, Integer.toString(productId));
	}
	
	private void setTTLForCart(String cartId) {
		redisTemplate.expireAt(cartId, Instant.now().plus(Duration.ofDays(CART_TTL_DAYS)));
	}

	@Override
	public CartItemMutationResponse updateItemQuantity(String cartId, 
			Integer productId, ItemQtyUpdateType updateType) {
		boolean isCartPresent = isCartPresent(cartId);
		if(!isCartPresent) {
			CartItemMutationResponse response = new CartItemMutationResponse("FAILED", 
					"Cart not present", cartId, null);
			return response;
		}
		boolean isItemPresent = isItemInCart(cartId, productId);
		if(!isItemPresent) {
			CartItemMutationResponse response = new CartItemMutationResponse("FAILED", 
					"Item not present in cart", cartId, null);
			return response;
		}
		if(updateType == ItemQtyUpdateType.INCREMENT) {
			Integer currentQty = Integer.parseInt((String)redisTemplate.opsForHash()
					.get(cartId, Integer.toString(productId)));
			Integer newQty = currentQty + 1;
			redisTemplate.opsForHash().put(cartId, Integer.toString(productId), 
					Integer.toString(newQty));
			setTTLForCart(cartId);
			CartItem updatedItem = new CartItem(productId, newQty);
			CartItemMutationResponse response = new CartItemMutationResponse("SUCCESS", 
					"Item quantity incremented successfully", cartId, updatedItem);
			return response;
		}
		else if(updateType == ItemQtyUpdateType.DECREMENT) {
			Integer currentQty = Integer.parseInt((String)redisTemplate.opsForHash()
					.get(cartId, Integer.toString(productId)));
			CartItemMutationResponse response = null;
			if(currentQty > 1) {
				Integer newQty = currentQty - 1;
				redisTemplate.opsForHash().put(cartId, Integer.toString(productId), 
						Integer.toString(newQty));
				CartItem updatedItem = new CartItem(productId, newQty);
				response = new CartItemMutationResponse("SUCCESS", 
						"Item quantity decremented successfully", cartId, updatedItem);
			}
			else {
				redisTemplate.opsForHash().delete(cartId, Integer.toString(productId));
				CartItem updatedItem = new CartItem(productId, 0);
				response = new CartItemMutationResponse("SUCCESS", 
						"Item removed from cart as quantity reached zero", cartId, updatedItem);
			}
			setTTLForCart(cartId);
			return response;
		}
		return null;
	}

	@Override
	public List<CartItem> getCartItems(String cartId) {
		Map<Object, Object> itemsMap = redisTemplate.opsForHash().entries(cartId);
		setTTLForCart(cartId);
		if(itemsMap != null && !itemsMap.isEmpty()) {
			List<CartItem> itemList = itemsMap.entrySet().stream()
					.map(entry -> new CartItem(Integer.parseInt((String)entry.getKey()), 
							Integer.parseInt((String)entry.getValue())))
					.toList();
			return itemList;
		}
		return List.of();
	}
	
}
