package com.shrikantanand.cartservice.model;

import java.util.List;

import lombok.Data;

@Data
public class Cart {

	private Integer cartId;
	
	private List<CartItem> items;
	
}
