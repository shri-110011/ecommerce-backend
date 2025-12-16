package com.shrikantanand.orderservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class OrderResponse {
	
	private Integer orderId;
	
	private List<OrderItemResponse> orderedItems;

}
