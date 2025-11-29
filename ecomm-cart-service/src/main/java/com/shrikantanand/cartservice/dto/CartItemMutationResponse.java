package com.shrikantanand.cartservice.dto;

import com.shrikantanand.cartservice.model.CartItem;

import lombok.Value;

@Value
public class CartItemMutationResponse  {
	
	private String status;
	
	private String message;
	
	private String cartId;
	
	private CartItem item;

}
