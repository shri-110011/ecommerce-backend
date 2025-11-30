package com.shrikantanand.productservice.service;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shrikantanand.productservice.dto.AddProductRequest;
import com.shrikantanand.productservice.dto.AddProductResponse;
import com.shrikantanand.productservice.dto.ProductSummaryDTO;

public interface ProductService {
	
	public ProductSummaryDTO getProductById(int productId) 
			throws JsonProcessingException;
	
	public BigDecimal getProductPrice(int productId);
	
	public AddProductResponse addProduct(AddProductRequest request);

}
