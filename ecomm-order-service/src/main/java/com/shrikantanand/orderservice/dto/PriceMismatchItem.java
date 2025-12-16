package com.shrikantanand.orderservice.dto;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class PriceMismatchItem {

	private Integer productId;
	
	private BigDecimal currentPricePerUnit;
	
	private Integer currentPriceVersion;
	
	private BigDecimal givenPricePerUnit;
	
	private Integer givenPriceVersion;
	
}
