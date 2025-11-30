package com.shrikantanand.productservice.dto;

import lombok.Value;

@Value
public class CategorySummary {

	private Byte categoryId;
	
	private String categoryName;
	
	private Character isActive;
	
}
