package com.shrikantanand.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shrikantanand.productservice.dto.CategoryProductsDTO;
import com.shrikantanand.productservice.dto.ErrorResponse;
import com.shrikantanand.productservice.dto.ProductDetailDTO;
import com.shrikantanand.productservice.entity.Category;
import com.shrikantanand.productservice.exception.CategoryNotFoundException;
import com.shrikantanand.productservice.exception.ProductNotFoundException;
import com.shrikantanand.productservice.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/categories")
	public List<Category> getCategories() {
		return productService.getCategories();
	}
	
	@GetMapping
	public CategoryProductsDTO getProductsByCategory(@RequestParam byte categoryId) {
		return productService.getProductsByCategory(categoryId);
	}
	
	@GetMapping("/{productId}")
	public ProductDetailDTO getProductById(@PathVariable int productId) {
		return productService.getProductById(productId);
	}
	
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
			CategoryNotFoundException ex) {
	    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            System.currentTimeMillis());
	    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFoundException(
			ProductNotFoundException ex) {
	    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            System.currentTimeMillis());
	    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

}
