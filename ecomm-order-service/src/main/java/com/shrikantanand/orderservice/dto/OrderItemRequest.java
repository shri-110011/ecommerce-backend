package com.shrikantanand.orderservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class OrderItemRequest {

	@NotNull(message = "must not be null")
	private Integer productId;
	
	@NotNull(message = "must not be null")
	@Positive(message = "must be a postive integer")
	private Integer requestedQuantity;
	
	@NotNull(message = "must not be null")
	@Positive(message = "must be a postive integer")
	private BigDecimal pricePerUnit;
	
	@NotNull(message = "must not be null")
	@Positive(message = "must be a postive integer")
	private Integer priceVersion;
	
}
