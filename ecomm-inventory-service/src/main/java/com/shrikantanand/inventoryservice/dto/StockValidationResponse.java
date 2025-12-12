package com.shrikantanand.inventoryservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class StockValidationResponse {
	
	private List<ItemResponse> availableItems;
	
	private List<UnavailableItemResponse> unavailableItems;
	
	private List<InvalidItemResponse> invalidItems;

}
