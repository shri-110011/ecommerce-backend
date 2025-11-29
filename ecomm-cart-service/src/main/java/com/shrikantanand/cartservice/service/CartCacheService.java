package com.shrikantanand.cartservice.service;

import java.util.List;

import com.shrikantanand.cartservice.dto.CartItemMutationResponse;
import com.shrikantanand.cartservice.enumeration.ItemQtyUpdateType;
import com.shrikantanand.cartservice.model.CartItem;

public interface CartCacheService {
	
	public CartItemMutationResponse addItemToCart(String cartId, Integer productId);
	
	public CartItemMutationResponse updateItemQuantity(String cartId, 
			Integer productId, ItemQtyUpdateType updateType);
	
	public List<CartItem> getCartItems(String cartId);

}
