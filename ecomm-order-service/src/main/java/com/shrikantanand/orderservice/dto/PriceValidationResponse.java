package com.shrikantanand.orderservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class PriceValidationResponse {
	
	private List<PriceMismatchItem> priceMismatchItems;
	
	private List<Integer> invalidProductIds;

}
