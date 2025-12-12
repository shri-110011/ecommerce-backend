package com.shrikantanand.inventoryservice.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shrikantanand.inventoryservice.dto.InvalidItemResponse;
import com.shrikantanand.inventoryservice.dto.ItemRequest;
import com.shrikantanand.inventoryservice.dto.ItemResponse;
import com.shrikantanand.inventoryservice.dto.ProductStockDTO;
import com.shrikantanand.inventoryservice.dto.ReserveItemsRequest;
import com.shrikantanand.inventoryservice.dto.ReserveItemsResponse;
import com.shrikantanand.inventoryservice.dto.StockValidationResponse;
import com.shrikantanand.inventoryservice.dto.UnavailableItemResponse;
import com.shrikantanand.inventoryservice.entity.Inventory;
import com.shrikantanand.inventoryservice.enumeration.InvalidItemReason;
import com.shrikantanand.inventoryservice.enumeration.UnavailableItemReason;
import com.shrikantanand.inventoryservice.repository.InventoryRepository;
import com.shrikantanand.inventoryservice.service.InventoryService;
import com.shrikantanand.inventoryservice.service.RedisService;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private RedisService redisService;

	@Override
	@Transactional
	public void addNewInventory(int productId) {
		// This check is required to make the productLifecycleEventConsumer 
		// idempotent. That is event if the message is passed multiple times 
		// we should not end up inserting multiple records into inventory 
		// table.
		int rows = inventoryRepository.findByProductId(productId);
		if(rows > 0) return;
		Inventory inventory = new Inventory();
		inventory.setProductId(productId);
		inventory.setActualStock(0);
		final LocalDateTime now = LocalDateTime.now();
		final String createdBy = "CRON_JOB";
		inventory.setCreatedDateTime(now);
		inventory.setCreatedBy(createdBy);
		inventory.setLastUpdatedDateTime(now);
		inventory.setLastUpdatedBy(createdBy);
		inventoryRepository.save(inventory);
	}

	@Override
	@Transactional
	public StockValidationResponse validateStock(List<ItemRequest> items) {
		List<Integer> productIds = items.stream()
				.map(e -> e.getProductId())
				.collect(Collectors.toList());
		List<Integer> productIdsOfItemsNotPresentInCache = 
				redisService.getProductIdsOfItemsNotPresentInCache(productIds);
		if(!productIdsOfItemsNotPresentInCache.isEmpty()) {
			List<ProductStockDTO> productStockDTOs = 
					inventoryRepository.getProductsStockInfo(productIdsOfItemsNotPresentInCache);
			redisService.loadProductsStockInfoIntoCache(productStockDTOs);
		}
		// Validate the stock
		List<ItemResponse> availableItems = new ArrayList<>();
		List<UnavailableItemResponse> unavailableItems = new ArrayList<>();
		List<InvalidItemResponse> invalidItems = new ArrayList<>();
		List<String> validationResult = redisService.validateStock(items);
		for(int i = 0; i < items.size(); i++) {
			int productId = items.get(i).getProductId();
			int requestedQty = items.get(i).getRequestedQuantity();
			String productQuantityKey = redisService.getProductQuantityKey(productId);
			String validationResultCode = validationResult.get(2*i+1);
			if(validationResultCode.equals("-2")) {
				InvalidItemResponse invalidItemResponse = new InvalidItemResponse(productId, requestedQty, 
						InvalidItemReason.PRODUCT_NOT_FOUND);
				invalidItems.add(invalidItemResponse);
			}
			else if(validationResultCode.equals("-1")) {
				UnavailableItemResponse unavailableItemResponse = new UnavailableItemResponse(productId, 
						requestedQty, UnavailableItemReason.OUT_OF_STOCK);
				unavailableItems.add(unavailableItemResponse);
			}
			else if(validationResultCode.equals("0")) {
				UnavailableItemResponse unavailableItemResponse = new UnavailableItemResponse(productId, 
						requestedQty, UnavailableItemReason.INSUFFICIENT_STOCK);
				unavailableItems.add(unavailableItemResponse);
			}
			else if(validationResultCode.equals("1")) {
				ItemResponse availableItemResponse = new ItemResponse(productId, requestedQty);
				availableItems.add(availableItemResponse);
			}
		}
		StockValidationResponse response = new StockValidationResponse(availableItems, 
				unavailableItems, invalidItems);
		return response;
	}

	@Override
	public ReserveItemsResponse reserveItems(ReserveItemsRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
