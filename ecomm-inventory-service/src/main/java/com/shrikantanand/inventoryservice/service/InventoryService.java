package com.shrikantanand.inventoryservice.service;

import com.shrikantanand.inventoryservice.api.response.ApiResponse;
import com.shrikantanand.inventoryservice.dto.ReserveItemsRequest;
import com.shrikantanand.inventoryservice.dto.ReserveItemsResponse;
import com.shrikantanand.inventoryservice.dto.StockValidationRequest;
import com.shrikantanand.inventoryservice.dto.StockValidationResponse;

public interface InventoryService {
	
	public void addNewInventory(int productId);
	
	public StockValidationResponse validateStock(StockValidationRequest request);
	
	public ApiResponse<ReserveItemsResponse> reserveItems(ReserveItemsRequest request);
	
	public void processOrderConfirmedEvent(Integer reservationId);
	
	public void processOrderCancelledEvent(Integer reservationId);
	
	public void cleanupExpiredReservations();

}
