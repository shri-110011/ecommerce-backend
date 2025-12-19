package com.shrikantanand.orderservice.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Value;

@Value
public class PlaceOrderResponse {
	
	private Integer orderId;
	
	private List<OrderItemResponse> orderedItems;
	
	private BigDecimal totalSum;

}
