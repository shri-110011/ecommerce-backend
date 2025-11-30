package com.shrikantanand.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.shrikantanand.productservice.dto.AddCategoryResponse;
import com.shrikantanand.productservice.dto.CategoryProductsDTO;
import com.shrikantanand.productservice.dto.CategorySummary;
import com.shrikantanand.productservice.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public List<CategorySummary> getCategories() throws JsonMappingException, 
	JsonProcessingException {
		return categoryService.getCategories();
	}
	
	@GetMapping("/{categoryId}/products")
	public CategoryProductsDTO getProductsByCategory(@PathVariable byte categoryId) 
			throws JsonProcessingException {
		return categoryService.getProductsByCategory(categoryId);
	}
	
	@PostMapping
	public AddCategoryResponse addCategory(@RequestParam String categoryName) {
		return categoryService.addCategory(categoryName);
	}

}
