package com.shrikantanand.inventoryservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class ReserveItemsResponse {
	
	private List<ReservedItemResponse> reservedItems;
	
	private List<UnavailableItemResponse> unavailableItems;
	
	private List<InvalidItemResponse> invalidItems;

}
