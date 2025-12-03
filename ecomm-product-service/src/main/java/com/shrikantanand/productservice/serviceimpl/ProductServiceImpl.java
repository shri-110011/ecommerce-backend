package com.shrikantanand.productservice.serviceimpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrikantanand.productservice.dao.CategoryRepository;
import com.shrikantanand.productservice.dao.ProductPriceHistoryRepository;
import com.shrikantanand.productservice.dao.ProductRepository;
import com.shrikantanand.productservice.dto.AddProductRequest;
import com.shrikantanand.productservice.dto.AddProductResponse;
import com.shrikantanand.productservice.dto.PriceMismatchItem;
import com.shrikantanand.productservice.dto.PriceValidationItem;
import com.shrikantanand.productservice.dto.PriceValidationResponse;
import com.shrikantanand.productservice.dto.ProductDetailsDTO;
import com.shrikantanand.productservice.dto.ProductSummaryDTO;
import com.shrikantanand.productservice.dto.UpdateProductPriceResponse;
import com.shrikantanand.productservice.entity.Product;
import com.shrikantanand.productservice.entity.ProductPriceHistory;
import com.shrikantanand.productservice.exception.CategoryNotFoundException;
import com.shrikantanand.productservice.exception.ProductNotFoundException;
import com.shrikantanand.productservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductPriceHistoryRepository productPriceHistoryRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	private static final int PRODUCT_PRICE_TTL_SECONDS = 60;
	
	private static final int PRODUCT_DETAILS_TTL_HOURS = 1;
	
	@Override
	@Transactional(readOnly = true)
	public ProductSummaryDTO getProductById(int productId) 
			throws JsonProcessingException {
		final String PRODUCT_DETAILS_KEY = "product:" + productId + ":details";
		String json = (String) redisTemplate.opsForValue()
				.get(PRODUCT_DETAILS_KEY);
		if(json != null) {
			ProductDetailsDTO productDetails = objectMapper.readValue(json, ProductDetailsDTO.class);
			BigDecimal price = getProductPrice(productId);
			return new ProductSummaryDTO(productDetails.getProductId(), 
					productDetails.getProductName(), price);
		}
		else {
			ProductSummaryDTO productSummary = productRepository.findById(productId)
					.map(product -> new ProductSummaryDTO(product))
					.orElseThrow(() -> {
						throw new ProductNotFoundException("Product id: " + productId + " not found!");
					});
			ProductDetailsDTO productDetails = new ProductDetailsDTO(
					productSummary.getProductId(), productSummary.getProductName());
			json = objectMapper.writeValueAsString(productDetails);
			redisTemplate.opsForValue().set(PRODUCT_DETAILS_KEY, json, 
					Duration.ofHours(PRODUCT_DETAILS_TTL_HOURS));
			final String PRODUCT_PRICE_KEY = "product:" + productId + ":price";
			final String productPrice = productSummary.getPrice().toString();
			redisTemplate.opsForValue().set(PRODUCT_PRICE_KEY, productPrice, 
					Duration.ofSeconds(PRODUCT_PRICE_TTL_SECONDS));
			return productSummary;
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal getProductPrice(int productId) {
		final String PRODUCT_PRICE_KEY = "product:" + productId + ":price";
		String priceStr = (String)redisTemplate.opsForValue().get(PRODUCT_PRICE_KEY);
		if(priceStr != null) {
			return new BigDecimal(priceStr);
		}
		else {
			BigDecimal price = productRepository.getProductPrice(productId);
			redisTemplate.opsForValue().set(PRODUCT_PRICE_KEY, price.toString(), 
					Duration.ofSeconds(PRODUCT_PRICE_TTL_SECONDS));
			return price;
		}
	}

	@Override
	@Transactional
	public AddProductResponse addProduct(AddProductRequest request) {
		Byte categoryId = request.getCategoryId();
		if(!categoryRepository.existsById(categoryId)) {
			throw new CategoryNotFoundException("Category id: " + categoryId + " not found!");
		}
		Product product = new Product();
		product.setProductName(request.getProductName());
		product.setPrice(request.getProductPrice());
		product.setCategory(categoryRepository.getReferenceById(categoryId));
		product.setIsActive('Y');
		final LocalDateTime now = LocalDateTime.now();
		final String createdBy = "ADMIN";
		product.setCreatedDateTime(now);
		product.setCreatedBy(createdBy);
		product.setLastUpdatedDateTime(now);
		product.setLastUpdatedBy(createdBy);
		productRepository.save(product);
		
		ProductPriceHistory productPriceHistory = new ProductPriceHistory();
		productPriceHistory.setProduct(product);
		productPriceHistory.setNewPrice(request.getProductPrice());
		productPriceHistory.setCurrentPriceVersion(1);
		productPriceHistory.setCreatedBy(createdBy);
		productPriceHistory.setCreatedDateTime(now);
		productPriceHistoryRepository.save(productPriceHistory);
		Integer productId = product.getProductId();
		final String message = "Product added successfully!";
		AddProductResponse response = new AddProductResponse(productId, 
				request.getProductName(), request.getProductPrice(), categoryId, 
				now, createdBy, 'Y', message);
		return response;
	}

	@Override
	@Transactional
	public UpdateProductPriceResponse updateProductPrice(int productId, BigDecimal newPrice) {
		Product product = productRepository.getProductForUpdate(productId)
				.orElseThrow(() -> {
					throw new ProductNotFoundException("Product id: " + productId + " not found!");
				});
		BigDecimal oldPrice = product.getPrice();
		if(oldPrice.compareTo(newPrice) == 0) {
			final String message = "New price is same as the old price. No update performed.";
			UpdateProductPriceResponse response = new UpdateProductPriceResponse(
					productId, product.getProductName(), oldPrice, 
					newPrice, null, null, message);
			return response;
		}
		Integer latestPriceVersion = productRepository.getLatestPriceVersion(productId);
		final LocalDateTime now = LocalDateTime.now();
		final String updatedBy = "ADMIN";
		product.setPrice(newPrice);
		product.setPriceVersion(latestPriceVersion + 1);
		product.setLastUpdatedDateTime(now);
		product.setLastUpdatedBy(updatedBy);
		ProductPriceHistory productPriceHistory = new ProductPriceHistory();
		productPriceHistory.setProduct(product);
		productPriceHistory.setOldPrice(oldPrice);
		productPriceHistory.setNewPrice(newPrice);
		productPriceHistory.setCurrentPriceVersion(latestPriceVersion + 1);
		productPriceHistory.setCreatedDateTime(now);
		productPriceHistory.setCreatedBy(updatedBy);
		productPriceHistoryRepository.save(productPriceHistory);
		final String message = "Product price updated successfully!";
		UpdateProductPriceResponse response = new UpdateProductPriceResponse(
				productId, product.getProductName(), oldPrice, 
				newPrice, now, updatedBy, message);
		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public PriceValidationResponse validatePrice(List<PriceValidationItem> items) {
		List<Integer> productIds = items.stream()
				.map(item -> item.getProductId())
				.toList();
		List<PriceValidationItem> fetchedProductPrices = 
				productRepository.getProductsPrice(productIds);
		Map<Integer, BigDecimal> currentPriceMap = fetchedProductPrices.stream()
				.collect(Collectors.toMap(
						item -> item.getProductId(), 
						item -> item.getPricePerUnit()
				));
		List<PriceMismatchItem> priceMismatchItems = new ArrayList<>();
		List<Integer> invalidProductIds = new ArrayList<>();
		for(PriceValidationItem item : items) {
			if(!currentPriceMap.containsKey(item.getProductId())) {
				invalidProductIds.add(item.getProductId());
			}
			else {
				BigDecimal actualPricePerUnit = currentPriceMap.get(item.getProductId());
				if(actualPricePerUnit.compareTo(item.getPricePerUnit()) != 0) {
					PriceMismatchItem mismatchItem = new PriceMismatchItem(
							item.getProductId(), 
							actualPricePerUnit, 
							item.getPricePerUnit());
					priceMismatchItems.add(mismatchItem);
				}
			}
		}
		PriceValidationResponse response = new PriceValidationResponse(priceMismatchItems, 
				invalidProductIds);
		return response;
	}

}
