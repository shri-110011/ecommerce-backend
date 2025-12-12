package com.shrikantanand.cartservice.service;

import java.util.List;

import com.shrikantanand.cartservice.dto.CartItemMutationResponse;
import com.shrikantanand.cartservice.enumeration.ItemQtyUpdateType;
import com.shrikantanand.cartservice.model.CartItem;

public interface CartCacheService {
	
	public CartItemMutationResponse addItemToCart(int cartId, int productId);
	
	public CartItemMutationResponse updateItemQuantity(int cartId, 
			int productId, ItemQtyUpdateType updateType);
	
	public List<CartItem> getCartItems(int cartId);

}
