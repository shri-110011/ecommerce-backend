package com.shrikantanand.orderservice.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shrikantanand.orderservice.api.response.ApiResponse;
import com.shrikantanand.orderservice.api.response.ErrorDetail;
import com.shrikantanand.orderservice.client.InventoryClient;
import com.shrikantanand.orderservice.client.ProductClient;
import com.shrikantanand.orderservice.dao.OrderRepository;
import com.shrikantanand.orderservice.dto.ItemRequest;
import com.shrikantanand.orderservice.dto.OrderItemRequest;
import com.shrikantanand.orderservice.dto.OrderItemResponse;
import com.shrikantanand.orderservice.dto.OrderRequest;
import com.shrikantanand.orderservice.dto.OrderResponse;
import com.shrikantanand.orderservice.dto.PriceValidationItem;
import com.shrikantanand.orderservice.dto.PriceValidationResponse;
import com.shrikantanand.orderservice.dto.ReserveItemsRequest;
import com.shrikantanand.orderservice.entity.Order;
import com.shrikantanand.orderservice.entity.OrderItem;
import com.shrikantanand.orderservice.entity.OrderItemKey;
import com.shrikantanand.orderservice.enumeration.OrderErrorCode;
import com.shrikantanand.orderservice.enumeration.OrderStatus;
import com.shrikantanand.orderservice.exception.PriceValidationFailedException;
import com.shrikantanand.orderservice.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductClient productClient;
	
	@Autowired
	private InventoryClient inventoryClient;
	
	@Value("${gateway.service.base-url}")
	private String gatewayServiceBaseUrl;

	@Override
	@Transactional
	public ApiResponse<OrderResponse> placeOrder(OrderRequest request) {
		List<PriceValidationItem> items = mapToPriceValidationItemList(request.getItems());
		PriceValidationResponse priceValidationResponse = productClient.validateItemsPrice(items);
		
		if(!priceValidationResponse.getInvalidProductIds().isEmpty()) {
			ErrorDetail detail = new ErrorDetail("INVALID_PRODUCT_IDS", 
					priceValidationResponse.getInvalidProductIds());
			throw new PriceValidationFailedException(OrderErrorCode.INVALID_ITEM, 
					List.of(detail));
		}
		else if(!priceValidationResponse.getPriceMismatchItems().isEmpty()) {
			ErrorDetail detail = new ErrorDetail("priceMismatchItems", 
					priceValidationResponse.getPriceMismatchItems());
			throw new PriceValidationFailedException(OrderErrorCode.PRICE_MISMATCH, 
					List.of(detail));
		}
		
		ReserveItemsRequest reserveItemsRequest = mapToReserveItemRequest(request);
		inventoryClient.reserveItems(reserveItemsRequest);

		Order order = new Order();
		order.setUserId(request.getUserId());
		order.setTotalAmount(getTotalAmount(request));
		order.setStatus(OrderStatus.CONFIRMED);
		LocalDateTime now = LocalDateTime.now();
		order.setCreatedDateTime(now);
		order.setLastUpdatedDateTime(now);
		
		request.getItems()
		.stream()
		.forEach(item -> {
			OrderItemKey orderItemKey = new OrderItemKey();
			orderItemKey.setProductId(item.getProductId());
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemKey(orderItemKey);
			orderItem.setQuantity(item.getRequestedQuantity());
			orderItem.setPriceAtPurchase(item.getPricePerUnit());
			orderItem.setPriceVersion(item.getPriceVersion());
			order.addOrderItem(orderItem);
		});
		
		orderRepository.save(order);
		
		OrderResponse orderResponse = new OrderResponse(order.getOrderId(), 
				mapToOrderItemResponseList(request.getItems()));
		String message = "Order created successfully!";
		
		return ApiResponse.success(message, orderResponse);
	}
	
	private List<PriceValidationItem> mapToPriceValidationItemList(List<OrderItemRequest> items) {
		return items.stream()
				.map(e -> new PriceValidationItem(e.getProductId(), 
						e.getPricePerUnit(), 
						e.getPriceVersion()))
				.collect(Collectors.toList());
	}
	
	private ReserveItemsRequest mapToReserveItemRequest(OrderRequest request) {
		List<ItemRequest> items = request.getItems()
				.stream()
				.map(e -> new ItemRequest(e.getProductId(), e.getRequestedQuantity()))
				.collect(Collectors.toList());
		return new ReserveItemsRequest(items, request.getUserId());
	}
	
	private BigDecimal getTotalAmount(OrderRequest request) {
		return request.getItems()
		.stream()
		.map(e -> e.getPricePerUnit()
				.multiply(BigDecimal.valueOf(e.getRequestedQuantity())))
		.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private List<OrderItemResponse> mapToOrderItemResponseList(List<OrderItemRequest> items) {
		return items.stream()
				.map(e -> new OrderItemResponse(e.getProductId(), 
						e.getRequestedQuantity(), e.getPricePerUnit(), 
						e.getPriceVersion()))
				.collect(Collectors.toList());
	}

}
