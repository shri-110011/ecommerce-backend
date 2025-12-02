package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class PriceMismatchItem {

	private Integer productId;
	
	private BigDecimal currentPricePerUnit;
	
	private BigDecimal givenPricePerUnit;
	
}
