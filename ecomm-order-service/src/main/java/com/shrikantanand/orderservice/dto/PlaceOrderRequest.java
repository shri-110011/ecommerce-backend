package com.shrikantanand.orderservice.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class PlaceOrderRequest {
	
	@NotNull(message = "must not be null")
	private Integer userId;
	
	@NotNull(message = "must not be null")
	@NotEmpty(message = "must not be empty")
	@Valid
	private List<OrderItemRequest> items;

}
