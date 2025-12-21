package com.shrikantanand.orderservice.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shrikantanand.orderservice.api.response.ApiResponse;
import com.shrikantanand.orderservice.api.response.ErrorDetail;
import com.shrikantanand.orderservice.client.InventoryClient;
import com.shrikantanand.orderservice.client.ProductClient;
import com.shrikantanand.orderservice.dao.OrderEventOutboxRepository;
import com.shrikantanand.orderservice.dao.OrderRepository;
import com.shrikantanand.orderservice.dto.CancelOrderResponse;
import com.shrikantanand.orderservice.dto.ItemRequest;
import com.shrikantanand.orderservice.dto.OrderItemRequest;
import com.shrikantanand.orderservice.dto.OrderItemResponse;
import com.shrikantanand.orderservice.dto.PlaceOrderRequest;
import com.shrikantanand.orderservice.dto.PlaceOrderResponse;
import com.shrikantanand.orderservice.dto.PriceValidationItem;
import com.shrikantanand.orderservice.dto.PriceValidationResponse;
import com.shrikantanand.orderservice.dto.ReserveItemsRequest;
import com.shrikantanand.orderservice.dto.ReserveItemsResponse;
import com.shrikantanand.orderservice.entity.Order;
import com.shrikantanand.orderservice.entity.OrderEventOutbox;
import com.shrikantanand.orderservice.entity.OrderItem;
import com.shrikantanand.orderservice.entity.OrderItemKey;
import com.shrikantanand.orderservice.enumeration.OrderErrorCode;
import com.shrikantanand.orderservice.enumeration.OrderLifecycleEventType;
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
	
	@Autowired
	private OrderEventOutboxRepository orderEventOutboxRepository;

	@Override
	@Transactional
	public ApiResponse<PlaceOrderResponse> placeOrder(PlaceOrderRequest request) {
		List<PriceValidationItem> items = mapToPriceValidationItemList(request.getItems());
		PriceValidationResponse priceValidationResponse = productClient.validateItemsPrice(items);
		
		if(!priceValidationResponse.getInvalidProductIds().isEmpty()) {
			ErrorDetail detail = new ErrorDetail("invalidProductIds", 
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
		ReserveItemsResponse reserveItemsResponse = inventoryClient.reserveItems(reserveItemsRequest);

		Order order = new Order();
		order.setUserId(request.getUserId());
		order.setReservationId(reserveItemsResponse.getReservationId());
		BigDecimal totalAmount = getTotalAmount(request);
		order.setTotalAmount(totalAmount);
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
		
		OrderEventOutbox orderEventOutbox = new OrderEventOutbox();
		orderEventOutbox.setEventType(OrderLifecycleEventType.ORDER_CONFIRMED);
		orderEventOutbox.setOrderId(order.getOrderId());
		orderEventOutbox.setIsProcessed('N');
		orderEventOutbox.setRetryCount(0);
		orderEventOutbox.setNextRetryAt(now);
		orderEventOutbox.setCreatedDateTime(now);
		final String createdBy = "SYSTEM";
		orderEventOutbox.setCreatedBy(createdBy);
		orderEventOutbox.setLastUpdatedDateTime(now);
		orderEventOutbox.setLastUpdatedBy(createdBy);
		orderEventOutboxRepository.save(orderEventOutbox);
		
		PlaceOrderResponse response = new PlaceOrderResponse(order.getOrderId(), 
				mapToOrderItemResponseList(request.getItems()), totalAmount);
		final String message = "Order created successfully!";
		return ApiResponse.success(message, response);
	}
	
	private List<PriceValidationItem> mapToPriceValidationItemList(List<OrderItemRequest> items) {
		return items.stream()
				.map(e -> new PriceValidationItem(e.getProductId(), 
						e.getPricePerUnit(), 
						e.getPriceVersion()))
				.collect(Collectors.toList());
	}
	
	private ReserveItemsRequest mapToReserveItemRequest(PlaceOrderRequest request) {
		List<ItemRequest> items = request.getItems()
				.stream()
				.map(e -> new ItemRequest(e.getProductId(), e.getRequestedQuantity()))
				.collect(Collectors.toList());
		return new ReserveItemsRequest(items, request.getUserId());
	}
	
	private BigDecimal getTotalAmount(PlaceOrderRequest request) {
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

	@Override
	@Transactional
	public ApiResponse<CancelOrderResponse> cancelOrder(int orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if(order == null) {
			String message = "Order cancellation failed!";
			String errorCode = OrderErrorCode.INVALID_ORDER_ID.name();
			ErrorDetail errorDetail = new ErrorDetail("orderId", orderId);
			return ApiResponse.failure(message, errorCode, List.of(errorDetail));
		}
		if(order.getStatus() != OrderStatus.CANCELLED) {
			order.setStatus(OrderStatus.CANCELLED);
			LocalDateTime now = LocalDateTime.now();
			order.setLastUpdatedDateTime(now);
			
			OrderEventOutbox orderEventOutbox = new OrderEventOutbox();
			orderEventOutbox.setEventType(OrderLifecycleEventType.ORDER_CANCELLED);
			orderEventOutbox.setOrderId(orderId);
			orderEventOutbox.setIsProcessed('N');
			orderEventOutbox.setRetryCount(0);
			orderEventOutbox.setNextRetryAt(now);
			orderEventOutbox.setCreatedDateTime(now);
			final String createdBy = "SYSTEM";
			orderEventOutbox.setCreatedBy(createdBy);
			orderEventOutbox.setLastUpdatedDateTime(now);
			orderEventOutbox.setLastUpdatedBy(createdBy);
			orderEventOutboxRepository.save(orderEventOutbox);
		}
		
		String message = "Order cancelled successfully!";
		CancelOrderResponse response = new CancelOrderResponse(orderId);
		return ApiResponse.success(message, response);
	}

}
