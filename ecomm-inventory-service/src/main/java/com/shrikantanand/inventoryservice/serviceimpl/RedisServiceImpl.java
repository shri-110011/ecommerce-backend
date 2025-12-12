package com.shrikantanand.inventoryservice.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shrikantanand.inventoryservice.dto.ItemRequest;
import com.shrikantanand.inventoryservice.dto.ProductStockDTO;
import com.shrikantanand.inventoryservice.repository.InventoryRepository;
import com.shrikantanand.inventoryservice.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private DefaultRedisScript<List> stockValidationScript;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	@EventListener(ContextRefreshedEvent.class)
	@Transactional(readOnly = true)
	public void initializeCacheOnStartup() {
		List<Integer> productIds = getProductIdsOfItemsNotPresentInCache(List.of(1000003, 1000008));
		List<Integer> productIdsOfItemsNotPresentInCache = 
				getProductIdsOfItemsNotPresentInCache(productIds);
		if(!productIdsOfItemsNotPresentInCache.isEmpty()) {
			List<ProductStockDTO> productStockDTOs = 
					inventoryRepository.getProductsStockInfo(productIds);
			loadProductsStockInfoIntoCache(productStockDTOs);
		}
	}

	@Override
	public void loadProductsStockInfoIntoCache(List<ProductStockDTO> productStockDTOs) {
		if(productStockDTOs.isEmpty()) return;
		logger.info("Products whose stock info is to be loaded into Redis...");
		logger.info(productStockDTOs.toString());
		productStockDTOs.stream()
		.forEach(e -> {
			String productQuantityKey = getProductQuantityKey(e.getProductId());
			String actualStock = Integer.toString(e.getActualStock());
			redisTemplate.opsForValue().set(productQuantityKey, actualStock);
		});	
	}
	
	public String getProductQuantityKey(int productId) {
		return "product:" + productId + ":quantity";
	}

	@Override
	public List<Integer> getProductIdsOfItemsNotPresentInCache(List<Integer> productIds) {
		if(productIds.isEmpty()) return List.of();
		List<Integer> productIdsOfItemsNotPresentInCache = new ArrayList<>();
		for (Integer productId : productIds) {
			String productQuantityKey = getProductQuantityKey(productId);
			if(!redisTemplate.hasKey(productQuantityKey)) {
				productIdsOfItemsNotPresentInCache.add(productId);
			}
		}
		logger.info("Products whose stock info is not present in cache: " + productIdsOfItemsNotPresentInCache);
		return productIdsOfItemsNotPresentInCache;
	}

	@Override
	public List<String> validateStock(List<ItemRequest> items) {
		List<String> keys = new ArrayList<>();
        List<String> args = new ArrayList<>();
        for (ItemRequest item : items) {
        	String productQuantityKey = getProductQuantityKey(item.getProductId());
            keys.add(productQuantityKey);
            args.add(String.valueOf(item.getRequestedQuantity()));
        }
		List<String> validationResult = redisTemplate.execute(stockValidationScript, keys, args.toArray());
		return validationResult;
	}

}
