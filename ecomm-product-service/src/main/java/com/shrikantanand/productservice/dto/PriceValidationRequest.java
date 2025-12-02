package com.shrikantanand.productservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class PriceValidationRequest {
	
	private List<PriceValidationItem> items;

}
