package com.shrikantanand.orderservice.dto;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class OrderItemResponse {
	
	private Integer productId;
	
	private Integer requestedQuantity;
	
	private BigDecimal pricePerUnit;
	
	private Integer priceVersion;

}
