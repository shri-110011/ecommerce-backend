package com.shrikantanand.orderservice.service;

import com.shrikantanand.orderservice.api.response.ApiResponse;
import com.shrikantanand.orderservice.dto.CancelOrderResponse;
import com.shrikantanand.orderservice.dto.PlaceOrderRequest;
import com.shrikantanand.orderservice.dto.PlaceOrderResponse;

public interface OrderService {
	
	public ApiResponse<PlaceOrderResponse> placeOrder(PlaceOrderRequest request);
	
	public ApiResponse<CancelOrderResponse> cancelOrder(int orderId);

}
