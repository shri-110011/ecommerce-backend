package com.shrikantanand.inventoryservice.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ReserveItemsRequest {
	
	@NotNull(message = "must not be null")
	@NotEmpty(message = "must not be empty")
	private List<ItemRequest> items;

}
