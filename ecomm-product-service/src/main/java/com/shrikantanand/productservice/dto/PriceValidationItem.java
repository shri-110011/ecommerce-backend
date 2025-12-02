package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class PriceValidationItem {
	
	private Integer productId;
	
	private BigDecimal pricePerUnit;

}
