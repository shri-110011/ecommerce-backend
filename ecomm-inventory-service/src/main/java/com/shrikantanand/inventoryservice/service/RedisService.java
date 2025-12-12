package com.shrikantanand.inventoryservice.service;

import java.util.List;

import com.shrikantanand.inventoryservice.dto.ItemRequest;
import com.shrikantanand.inventoryservice.dto.ProductStockDTO;

public interface RedisService {
	
	public void initializeCacheOnStartup();
	
	public void loadProductsStockInfoIntoCache(List<ProductStockDTO> productStockInfo);
	
	public List<Integer> getProductIdsOfItemsNotPresentInCache(List<Integer> productIds);
	
	public List<String> validateStock(List<ItemRequest> items);
	
	public String getProductQuantityKey(int productId);

}
