package com.shrikantanand.orderservice.service;

import com.shrikantanand.orderservice.api.response.ApiResponse;
import com.shrikantanand.orderservice.dto.OrderRequest;
import com.shrikantanand.orderservice.dto.OrderResponse;

public interface OrderService {
	
	public ApiResponse<OrderResponse> placeOrder(OrderRequest request);

}
