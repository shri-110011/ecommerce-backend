package com.shrikantanand.cartservice.serviceimpl;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shrikantanand.cartservice.dto.CartItemMutationResponse;
import com.shrikantanand.cartservice.enumeration.ItemQtyUpdateType;
import com.shrikantanand.cartservice.model.CartItem;
import com.shrikantanand.cartservice.service.CartCacheService;

import jakarta.annotation.PostConstruct;

@Service
public class CartCacheServiceImpl implements CartCacheService {
	
	private static final int CART_TTL_DAYS = 2;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	private HashOperations<String, String, String> hashOps;
	
	@PostConstruct
	public void init() {
	    this.hashOps = redisTemplate.opsForHash();
	}

	@Override
	public CartItemMutationResponse addItemToCart(int cartId, int productId) {
		String cartKey = getCartKey(cartId);
		String productQuantityHashKey = getProductQuantityHashKey(productId);
		boolean isItemPresent = isItemInCart(cartKey, productQuantityHashKey);
		String status;
		String message;
		if(!isItemPresent) {
			hashOps.put(cartKey, productQuantityHashKey, "1");
			setTTLForCart(cartKey);
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
	
	private boolean isCartPresent(String cartKey) {
		return redisTemplate.hasKey(cartKey);
	}
	
	private boolean isItemInCart(String cartKey, String productQuantityHashKey) {
		return hashOps.hasKey(cartKey, productQuantityHashKey);
	}
	
	private String getCartKey(int cartId) {
		return "cart:" + cartId;
	}
	
	private String getProductQuantityHashKey(int productId) {
		return "product:" + productId + ":quantity";
	}
	
	private void setTTLForCart(String cartKey) {
		redisTemplate.expireAt(cartKey, Instant.now().plus(Duration.ofDays(CART_TTL_DAYS)));
	}

	@Override
	public CartItemMutationResponse updateItemQuantity(int cartId, 
			int productId, ItemQtyUpdateType updateType) {
		String cartKey = getCartKey(cartId);
		boolean isCartPresent = isCartPresent(cartKey);
		if(!isCartPresent) {
			CartItemMutationResponse response = new CartItemMutationResponse("FAILED", 
					"Cart not present", cartId, null);
			return response;
		}
		String productQuantityHashKey = getProductQuantityHashKey(productId);
		boolean isItemPresent = isItemInCart(cartKey, productQuantityHashKey);
		if(!isItemPresent) {
			CartItemMutationResponse response = new CartItemMutationResponse("FAILED", 
					"Item not present in cart", cartId, null);
			return response;
		}
		if(updateType == ItemQtyUpdateType.INCREMENT) {
			Integer currentQty = Integer.parseInt(
					hashOps.get(cartKey, productQuantityHashKey)
			);
			Integer newQty = currentQty + 1;
			hashOps.put(cartKey, productQuantityHashKey, 
					Integer.toString(newQty));
			setTTLForCart(cartKey);
			CartItem updatedItem = new CartItem(productId, newQty);
			CartItemMutationResponse response = new CartItemMutationResponse("SUCCESS", 
					"Item quantity incremented successfully", cartId, updatedItem);
			return response;
		}
		else if(updateType == ItemQtyUpdateType.DECREMENT) {
			Integer currentQty = Integer.parseInt(
					hashOps.get(cartKey, productQuantityHashKey)
			);
			CartItemMutationResponse response = null;
			if(currentQty > 1) {
				Integer newQty = currentQty - 1;
				hashOps.put(cartKey, productQuantityHashKey, 
						Integer.toString(newQty));
				CartItem updatedItem = new CartItem(productId, newQty);
				response = new CartItemMutationResponse("SUCCESS", 
						"Item quantity decremented successfully", cartId, updatedItem);
			}
			else {
				hashOps.delete(cartKey, productQuantityHashKey);
				CartItem updatedItem = new CartItem(productId, 0);
				response = new CartItemMutationResponse("SUCCESS", 
						"Item removed from cart as quantity reached zero", cartId, updatedItem);
			}
			setTTLForCart(cartKey);
			return response;
		}
		return null;
	}
	
	private int getProductId(String productQuantityHashKey) {
		return Integer.parseInt(productQuantityHashKey.split(":")[1]);
	}

	@Override
	public List<CartItem> getCartItems(int cartId) {
		String cartKey = getCartKey(cartId);
		Map<String, String> itemsMap = hashOps.entries(cartKey);
		setTTLForCart(cartKey);
		if(itemsMap != null && !itemsMap.isEmpty()) {
			List<CartItem> itemList = itemsMap.entrySet().stream()
					.map(entry -> new CartItem(getProductId(entry.getKey()), 
							Integer.parseInt((String)entry.getValue())))
					.toList();
			return itemList;
		}
		return List.of();
	}
	
}
