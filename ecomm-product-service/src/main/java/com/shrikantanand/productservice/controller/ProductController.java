package com.shrikantanand.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shrikantanand.productservice.dto.AddProductRequest;
import com.shrikantanand.productservice.dto.AddProductResponse;
import com.shrikantanand.productservice.dto.ErrorResponse;
import com.shrikantanand.productservice.dto.ProductSummaryDTO;
import com.shrikantanand.productservice.exception.ProductNotFoundException;
import com.shrikantanand.productservice.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/{productId}")
	public ProductSummaryDTO getProductById(@PathVariable int productId) 
			throws JsonProcessingException {
		return productService.getProductById(productId);
	}
	
	@PostMapping
	public AddProductResponse addProduct(@RequestBody AddProductRequest request) {
		return productService.addProduct(request);
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
