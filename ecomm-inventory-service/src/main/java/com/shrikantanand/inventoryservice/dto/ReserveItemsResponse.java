package com.shrikantanand.inventoryservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Value;

@Value
public class ReserveItemsResponse {
	
	@JsonInclude(Include.NON_NULL)
	private Integer reservationId;
	
	private int status;
	
	private String message;
	
	private List<ReservedItemResponse> reservedItems;

}
