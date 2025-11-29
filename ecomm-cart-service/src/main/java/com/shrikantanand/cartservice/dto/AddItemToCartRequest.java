package com.shrikantanand.cartservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class AddItemToCartRequest {
	
	@NotNull(message = "cannot be null")
	private Integer productId;
	
	@NotNull(message = "cannot be null")
	@Positive(message = "must be a positive integer")
	private Integer quantity;

}
