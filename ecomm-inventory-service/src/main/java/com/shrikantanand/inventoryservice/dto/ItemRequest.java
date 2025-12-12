package com.shrikantanand.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class ItemRequest {
	
	@NotNull(message = "must not be null")
	private Integer productId;
	
	@NotNull(message = "must not be null")
	@Positive(message = "must be a postive integer")
	private Integer requestedQuantity;

}
