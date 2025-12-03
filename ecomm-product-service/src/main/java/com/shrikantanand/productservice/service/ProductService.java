package com.shrikantanand.productservice.service;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shrikantanand.productservice.dto.AddProductRequest;
import com.shrikantanand.productservice.dto.AddProductResponse;
import com.shrikantanand.productservice.dto.PriceValidationItem;
import com.shrikantanand.productservice.dto.PriceValidationResponse;
import com.shrikantanand.productservice.dto.ProductSummaryDTO;
import com.shrikantanand.productservice.dto.UpdateProductPriceResponse;

public interface ProductService {
	
	public ProductSummaryDTO getProductById(int productId) 
			throws JsonProcessingException;
	
	public BigDecimal getProductPrice(int productId);
	
	public AddProductResponse addProduct(AddProductRequest request);
	
	public UpdateProductPriceResponse updateProductPrice(int productId, 
			BigDecimal newPrice);
	
	public PriceValidationResponse validatePrice(List<PriceValidationItem> items);

}
