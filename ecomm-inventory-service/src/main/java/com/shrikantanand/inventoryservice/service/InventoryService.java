package com.shrikantanand.inventoryservice.service;

import java.util.List;

import com.shrikantanand.inventoryservice.dto.ItemRequest;
import com.shrikantanand.inventoryservice.dto.ReserveItemsRequest;
import com.shrikantanand.inventoryservice.dto.ReserveItemsResponse;
import com.shrikantanand.inventoryservice.dto.StockValidationResponse;

public interface InventoryService {
	
	public void addNewInventory(int productId);
	
	public StockValidationResponse validateStock(List<ItemRequest> items);
	
	public ReserveItemsResponse reserveItems(ReserveItemsRequest request);

}
