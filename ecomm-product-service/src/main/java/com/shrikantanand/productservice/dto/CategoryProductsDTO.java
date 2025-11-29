package com.shrikantanand.productservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class CategoryProductsDTO {
	
	private int categoryId;
	
	private String categoryName;
	
	private List<ProductDetailDTO> products;

}
