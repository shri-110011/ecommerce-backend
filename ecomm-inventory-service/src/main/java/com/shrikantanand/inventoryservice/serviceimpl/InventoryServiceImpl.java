package com.shrikantanand.inventoryservice.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shrikantanand.inventoryservice.api.response.ApiResponse;
import com.shrikantanand.inventoryservice.api.response.ErrorDetail;
import com.shrikantanand.inventoryservice.dto.InvalidItemResponse;
import com.shrikantanand.inventoryservice.dto.ItemRequest;
import com.shrikantanand.inventoryservice.dto.ItemResponse;
import com.shrikantanand.inventoryservice.dto.ProductStockDTO;
import com.shrikantanand.inventoryservice.dto.ReserveItemsRequest;
import com.shrikantanand.inventoryservice.dto.ReserveItemsResponse;
import com.shrikantanand.inventoryservice.dto.ReservedItemResponse;
import com.shrikantanand.inventoryservice.dto.StockValidationRequest;
import com.shrikantanand.inventoryservice.dto.StockValidationResponse;
import com.shrikantanand.inventoryservice.dto.UnavailableItemResponse;
import com.shrikantanand.inventoryservice.entity.Inventory;
import com.shrikantanand.inventoryservice.entity.InventoryEvent;
import com.shrikantanand.inventoryservice.entity.Reservation;
import com.shrikantanand.inventoryservice.entity.ReservedItem;
import com.shrikantanand.inventoryservice.enumeration.InvalidItemReason;
import com.shrikantanand.inventoryservice.enumeration.InventoryEventType;
import com.shrikantanand.inventoryservice.enumeration.ReservationErrorCode;
import com.shrikantanand.inventoryservice.enumeration.ReservationStatus;
import com.shrikantanand.inventoryservice.enumeration.UnavailableItemReason;
import com.shrikantanand.inventoryservice.exception.StockReservationFailedException;
import com.shrikantanand.inventoryservice.repository.InventoryEventRepository;
import com.shrikantanand.inventoryservice.repository.InventoryRepository;
import com.shrikantanand.inventoryservice.repository.ReservationRepository;
import com.shrikantanand.inventoryservice.service.InventoryService;
import com.shrikantanand.inventoryservice.service.RedisService;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private InventoryEventRepository inventoryEventLogRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private RedisService redisService;
	
	private final int RESERVATION_TTL_MINUTES;
	
	public InventoryServiceImpl(@Value("${reservation.ttl.minutes}") int ttlMinutes) {
		this.RESERVATION_TTL_MINUTES = ttlMinutes;
	}

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
	public StockValidationResponse validateStock(StockValidationRequest request) {
		// This method is to quickly check the stock availability during checkout 
		// or during products page load.
		List<ItemRequest> items = request.getItems();
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
	@Transactional
	public ApiResponse<ReserveItemsResponse> reserveItems(ReserveItemsRequest request) {
		// This method is for deducting the actual stock from inventory and to 
		// enter the reserved items info in db.
		final Integer userId = request.getUserId();
		final String updatedBy = "SYSTEM";
		final LocalDateTime now = LocalDateTime.now();
		// Create a reservation.
		Reservation reservation = new Reservation();
		reservation.setUserId(userId);
		reservation.setStatus(ReservationStatus.ACTIVE);
		LocalDateTime expirationDateTime = now.plusMinutes(RESERVATION_TTL_MINUTES);
		reservation.setExpirationDateTime(expirationDateTime);
		reservation.setCreatedDateTime(now);
		reservation.setCreatedBy(updatedBy);
		reservation.setLastUpdatedDateTime(now);
		reservation.setLastUpdatedBy(updatedBy);
		for(ItemRequest item : request.getItems()) {
			// Send one update query for each order item.
			int rowsAffected = inventoryRepository.deductStock(item.getProductId(), 
					item.getRequestedQuantity(), now, updatedBy);
			// If the update query result is 0 for any order item fail-fast.
			if(rowsAffected == 0) {
				ErrorDetail errorDetail = new ErrorDetail("productId", item.getProductId());
				throw new StockReservationFailedException(ReservationErrorCode.INSUFFICIENT_STOCK, 
						List.of(errorDetail));
			}
			else {
				// Add inventory event corresponding to this item to be reserved.
				InventoryEvent inventoryEvent = new InventoryEvent();
				inventoryEvent.setProductId(item.getProductId());
				inventoryEvent.setEventType(InventoryEventType.RESERVATION);
				inventoryEvent.setQuantity(item.getRequestedQuantity());
				inventoryEvent.setCreatedDateTime(now);
				inventoryEvent.setCreatedBy(updatedBy);
				inventoryEventLogRepository.save(inventoryEvent);
				// Create a reserved item.
				ReservedItem reservedItem = new ReservedItem();
				reservedItem.setInventoryEventId(inventoryEvent.getEventId());
				reservedItem.setReservation(reservation);
				reservedItem.setProductId(item.getProductId());
				reservedItem.setQuantity(item.getRequestedQuantity());
				reservedItem.setCreatedDateTime(now);
				reservedItem.setCreatedBy(updatedBy);
				reservation.addReservedItem(reservedItem);
			}
		}
		// Save the reservation and the reserved items.
		Reservation savedReservation = reservationRepository.save(reservation);
		// Send the reservation successful response only when all update queries 
		// result is 1.
		String message = "All items reserved successfully!";
		List<ReservedItemResponse> reservedItems = getReservedItemResponse(request.getItems());
		ReserveItemsResponse response = new ReserveItemsResponse(savedReservation.getReservationId(), 
				reservedItems);
		return ApiResponse.success(message, response);
	}
	
	private List<ReservedItemResponse> getReservedItemResponse(List<ItemRequest> items) {
		List<ReservedItemResponse> result = new ArrayList<>();
		for(ItemRequest item : items) {
			ReservedItemResponse reservedItemResponse = 
					new ReservedItemResponse(item.getProductId(), item.getRequestedQuantity());
			result.add(reservedItemResponse);
		}
		return result;
	}

	@Override
	@Transactional
	public void processOrderConfirmedEvent(Integer reservationId) {
		// We need to lock the reservation row while fetching it 
		// because we have this read, modify update sequence of operations here.
		// If we don't lock then the scheduler doing the clean up of expired 
		// and active reservations may pick this one.
		Reservation reservation = reservationRepository.findByIdForUpdate(reservationId);
		if(reservation != null && reservation.getStatus() == ReservationStatus.ACTIVE) {
			reservation.setStatus(ReservationStatus.CONSUMED);
			reservation.setLastUpdatedDateTime(LocalDateTime.now());
			reservation.setLastUpdatedBy("SYSTEM");
		}
	}

	@Override
	@Transactional
	public void processOrderCancelledEvent(Integer reservationId) {
		// Here we don't need any lock on the reservation row here because 
		// no other process apart from this consumer is going to pick the 
		// reservation once it is in consumed state.
		Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
		final LocalDateTime now = LocalDateTime.now();
		final String updatedBy = "SYSTEM";
		if(reservation != null && reservation.getStatus() == ReservationStatus.CONSUMED) {
			List<ReservedItem> reserveItems = reservation.getReservedItems();
			reserveItems.stream()
			.forEach(e -> {
				inventoryRepository.increaseStock(e.getProductId(), 
						e.getQuantity(), now, updatedBy);
				// Add inventory event corresponding to this item to be reserved.
				InventoryEvent inventoryEvent = new InventoryEvent();
				inventoryEvent.setProductId(e.getProductId());
				inventoryEvent.setEventType(InventoryEventType.ADJUSTMENT);
				inventoryEvent.setQuantity(e.getQuantity());
				inventoryEvent.setCreatedDateTime(now);
				inventoryEvent.setCreatedBy(updatedBy);
				inventoryEvent.setReason("Freeing up stock due to order cancellation");
				inventoryEventLogRepository.save(inventoryEvent);
			});
		}
		reservation.setStatus(ReservationStatus.CANCELLED);
		reservation.setLastUpdatedDateTime(now);
		reservation.setLastUpdatedBy(updatedBy);
	}

	@Override
	@Transactional
	public void cleanupExpiredReservations() {
		// We need to lock the reservation rows while fetching the expired and active 
		// reservations so as to prevent lost update problems i.e. if the ORDER_CONFIRMED 
		// consumer is holding a lock on expired and active reservation row and has still 
		// not changed the reservation status to CONSUMED then still we would be able 
		// to read that row here if we don't get lock before reading.
		List<Integer> reservationIds = 
				reservationRepository.getExpiredReservationsForUpdate(ReservationStatus.ACTIVE.name(), 
						PageRequest.ofSize(100));
		if(reservationIds.isEmpty()) return;
		// For each reservation get the reserved items and group these reserved 
		// items by product id.
		List<Reservation> reservations = 
				reservationRepository.getReservationsAlongWithReservedItems(reservationIds);
		Map<Integer, Integer> productIdsToQuantityToBeFreed = new HashMap<>();
		final LocalDateTime now = LocalDateTime.now();
		final String updatedBy = "CRON_JOB";
		for(Reservation reservation : reservations) {
			reservation.getReservedItems()
			.stream()
			.forEach(e -> {
				productIdsToQuantityToBeFreed.merge(e.getProductId(), e.getQuantity(), 
						Integer::sum);
			});
			reservation.setStatus(ReservationStatus.CANCELLED);
			reservation.setLastUpdatedDateTime(now);
			reservation.setLastUpdatedBy(updatedBy);
		}
		// For each productId increment the stock in inventory table 
		// and also add a record in inventory_event_log table.
		productIdsToQuantityToBeFreed.entrySet()
		.stream()
		.forEach(e -> {
			int productId = e.getKey();
			int quantity = e.getValue();
			inventoryRepository.increaseStock(productId, quantity, now, updatedBy);
			// Add inventory event corresponding to this item to be freed.
			InventoryEvent inventoryEvent = new InventoryEvent();
			inventoryEvent.setProductId(productId);
			inventoryEvent.setEventType(InventoryEventType.ADJUSTMENT);
			inventoryEvent.setQuantity(quantity);
			inventoryEvent.setCreatedDateTime(now);
			inventoryEvent.setCreatedBy(updatedBy);
			inventoryEvent.setReason("Freeing up expired and active reservations");
			inventoryEventLogRepository.save(inventoryEvent);
		});
	}

}
