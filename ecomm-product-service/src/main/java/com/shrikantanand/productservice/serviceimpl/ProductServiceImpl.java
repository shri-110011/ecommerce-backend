package com.shrikantanand.productservice.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shrikantanand.productservice.dao.CategoryRepository;
import com.shrikantanand.productservice.dao.ProductRepository;
import com.shrikantanand.productservice.dto.CategoryProductsDTO;
import com.shrikantanand.productservice.dto.ProductDetailDTO;
import com.shrikantanand.productservice.entity.Category;
import com.shrikantanand.productservice.exception.CategoryNotFoundException;
import com.shrikantanand.productservice.exception.ProductNotFoundException;
import com.shrikantanand.productservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryProductsDTO getProductsByCategory(byte categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> 
				new CategoryNotFoundException("Category id: " + categoryId + " not found!"));
		List<ProductDetailDTO> productDTOs = category.getProducts()
				.stream()
				.map(product -> new ProductDetailDTO(product))
				.collect(Collectors.toList());
		return new CategoryProductsDTO(category.getCategoryId(), 
				category.getCategoryName(), productDTOs);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ProductDetailDTO getProductById(int productId) {
		return productRepository.findById(productId)
				.map(product -> new ProductDetailDTO(product))
				.orElseThrow(() -> {
					throw new ProductNotFoundException("Product id: " + productId + " not found!");
				});
	}

}
