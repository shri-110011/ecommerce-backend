package com.shrikantanand.productservice.serviceimpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrikantanand.productservice.dao.CategoryRepository;
import com.shrikantanand.productservice.dto.AddCategoryResponse;
import com.shrikantanand.productservice.dto.CategoryProductsDTO;
import com.shrikantanand.productservice.dto.CategorySummary;
import com.shrikantanand.productservice.dto.ProductSummaryDTO;
import com.shrikantanand.productservice.entity.Category;
import com.shrikantanand.productservice.exception.CategoryNotFoundException;
import com.shrikantanand.productservice.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	private final int PRODUCT_CATEGORIES_TTL_DAYS;
	
	private final int CATEGORY_PRODUCTS_TTL_MINUTES;
	
	private final String PRODUCT_CATEGORIES_KEY = "product_categories";
	
	public CategoryServiceImpl(@Value("${product.categories.ttl.days}") int productCategoriesTTLDays, 
			@Value("${category.products.ttl.minutes}") int categoryProductsTTLMinutes) {
		this.PRODUCT_CATEGORIES_TTL_DAYS = productCategoriesTTLDays;
		this.CATEGORY_PRODUCTS_TTL_MINUTES = categoryProductsTTLMinutes;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategorySummary> getCategories() throws JsonProcessingException {
		// Check if categories are present in Redis cache.
		// If present, retrieve from cache and return.
		// If not present, retrieve from database, store in cache and return.
		String json = (String)redisTemplate.opsForValue().get(PRODUCT_CATEGORIES_KEY);
		if (json != null) {
			return objectMapper.readValue(json, new TypeReference<List<CategorySummary>>() {});
		}
		else {
			List<Category> categories = categoryRepository.findAll();
			List<CategorySummary> categorySummaries = categories.stream()
					.map(category -> new CategorySummary(category.getCategoryId(), 
							category.getCategoryName(), category.getIsActive()))
					.collect(Collectors.toList());
			json = objectMapper.writeValueAsString(categorySummaries);
			redisTemplate.opsForValue().set("product_categories", json, 
					Duration.ofDays(PRODUCT_CATEGORIES_TTL_DAYS));
			return categorySummaries;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryProductsDTO getProductsByCategory(byte categoryId) 
			throws JsonProcessingException {
		final String CATEGORY_PRODUCTS_KEY = "category:" + categoryId + ":products";
		String json = (String)redisTemplate.opsForValue()
				.get("category:" + categoryId + ":products");
		if(json != null) {
			return objectMapper.readValue(json, CategoryProductsDTO.class);
		}
		else {
			Category category = categoryRepository.findById(categoryId)
					.orElseThrow(() -> 
					new CategoryNotFoundException("Category id: " + categoryId + " not found!"));
			List<ProductSummaryDTO> productDTOs = category.getProducts()
					.stream()
					.map(product -> new ProductSummaryDTO(product))
					.collect(Collectors.toList());
			CategoryProductsDTO categoryProductsDTO = new CategoryProductsDTO(category.getCategoryId(), 
					category.getCategoryName(), productDTOs);
			json = objectMapper.writeValueAsString(categoryProductsDTO);
			redisTemplate.opsForValue().set(CATEGORY_PRODUCTS_KEY, json, 
					Duration.ofMinutes(CATEGORY_PRODUCTS_TTL_MINUTES));
			return categoryProductsDTO;
		}
	}

	@Override
	@Transactional
	public AddCategoryResponse addCategory(String categoryName) {
		// Here we assume that category names are unique.
		Category category = new Category();
		category.setCategoryName(categoryName);
		category.setIsActive('Y');
		final LocalDateTime now = LocalDateTime.now();
		final String createdBy = "ADMIN";
		category.setCreatedDateTime(now);
		category.setCreatedBy(createdBy);
		category.setLastUpdatedDateTime(now);
		category.setLastUpdatedBy(createdBy);
		categoryRepository.save(category);
		Byte categoryId = category.getCategoryId();
		final String message = "Category added successfully!";
		AddCategoryResponse response = new AddCategoryResponse(categoryId, 
				categoryName, now, createdBy, 'Y', message);
		return response;
	}

}
