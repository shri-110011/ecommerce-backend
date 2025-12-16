package com.shrikantanand.orderservice.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ReserveItemsRequest {
	
	@NotNull(message = "must not be null")
	@NotEmpty(message = "must not be empty")
	private List<ItemRequest> items;
	
	@NotNull(message = "must not be null")
	private Integer userId;

}
