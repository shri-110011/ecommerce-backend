package com.shrikantanand.orderservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class ReserveItemsResponse {
	
	private Integer reservationId;
	
	private List<ReservedItemResponse> reservedItems;

}
