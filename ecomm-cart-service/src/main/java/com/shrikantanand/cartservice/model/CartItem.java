package com.shrikantanand.cartservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
	
	private Integer productId;
	
	private Integer quantity;

}
