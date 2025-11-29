package com.shrikantanand.productservice.service;

import java.util.List;

import com.shrikantanand.productservice.dto.CategoryProductsDTO;
import com.shrikantanand.productservice.dto.ProductDetailDTO;
import com.shrikantanand.productservice.entity.Category;

public interface ProductService {
	
	public List<Category> getCategories();
	
	public CategoryProductsDTO getProductsByCategory(byte categoryId);
	
	public ProductDetailDTO getProductById(int productId);

}
