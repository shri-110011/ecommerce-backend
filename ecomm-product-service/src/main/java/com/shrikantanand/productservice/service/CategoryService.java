package com.shrikantanand.productservice.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shrikantanand.productservice.dto.AddCategoryResponse;
import com.shrikantanand.productservice.dto.CategoryProductsDTO;
import com.shrikantanand.productservice.dto.CategorySummary;

public interface CategoryService {
	
	public List<CategorySummary> getCategories() throws JsonProcessingException;
	
	public CategoryProductsDTO getProductsByCategory(byte categoryId) 
			throws JsonProcessingException;
	
	public AddCategoryResponse addCategory(String categoryName);

}
