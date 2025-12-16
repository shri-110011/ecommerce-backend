package com.shrikantanand.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ReservedItemResponse extends ItemResponse {

	@NotNull(message = "must not be null")
	private Integer reservedQuantity;

	public ReservedItemResponse(Integer productId, Integer requestedQuantity) {
		super(productId, requestedQuantity);
		this.reservedQuantity = requestedQuantity;
	}
	
	
	
}
