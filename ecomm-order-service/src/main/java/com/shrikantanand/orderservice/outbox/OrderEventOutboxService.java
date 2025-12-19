package com.shrikantanand.orderservice.outbox;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.shrikantanand.orderservice.dao.OrderEventOutboxRepository;
import com.shrikantanand.orderservice.dao.OrderRepository;
import com.shrikantanand.orderservice.dto.OrderLifecycleEvent;
import com.shrikantanand.orderservice.dto.OrderReservationId;
import com.shrikantanand.orderservice.entity.OrderEventOutbox;

import jakarta.transaction.Transactional;

@Component
public class OrderEventOutboxService {
	
	@Autowired
	private OrderEventOutboxRepository orderEventOutboxRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderLifecycleEventProducer orderEventProducer;
	
	@Transactional
	public void processPendingOrderEvents() {
		List<OrderEventOutbox> events = 
				orderEventOutboxRepository.getAllPendingProductEvents(PageRequest.ofSize(100));
		if(events.isEmpty()) return;
		List<Integer> orderIds = events.stream()
				.map(e -> e.getOrderId())
				.collect(Collectors.toList());
		List<OrderReservationId> orderReservationIds = orderRepository.findReservationIdsforOrderIds(orderIds);
		Map<Integer, Integer> orderIdToReservationId = orderReservationIds.stream()
				.collect(Collectors.toMap(OrderReservationId::getOrderId, 
						OrderReservationId::getReservationId));
		Set<Integer> processedOrderEventIds = new HashSet<>();
		events.stream()
		.forEach(e -> {
			int orderId = e.getOrderId();
			int reservationId = orderIdToReservationId.get(orderId);
			OrderLifecycleEvent event = new OrderLifecycleEvent(reservationId, 
					e.getEventType());
			// We are waiting for the producer to get acknowledgement from 
			// message broker after it has published the message.
			boolean result = orderEventProducer.publishOrderEvent(event);
			if(result) processedOrderEventIds.add(e.getEventId());
		});
		final String lastUpdateddBy = "CRON_JOB";
		for(OrderEventOutbox event : events) {
			final LocalDateTime now = LocalDateTime.now();
			if(processedOrderEventIds.contains(event.getEventId())) {
				event.setIsProcessed('Y');
			}
			else {
				int curRetryCount = event.getRetryCount();
				event.setRetryCount(curRetryCount + 1);
				LocalDateTime nextRetryAt = getNextRetryAt(curRetryCount);
				event.setNextRetryAt(nextRetryAt);
			}
			event.setLastUpdatedDateTime(now);
			event.setLastUpdatedBy(lastUpdateddBy);
		}	
	}
	
	private LocalDateTime getNextRetryAt(int retryCount) {
		final long BASE_DELAY_MS  = 2000;
		final long MAX_DELAY_MS = 60000;
		long delay = BASE_DELAY_MS  * (1L << retryCount);
		delay = Math.min(delay, MAX_DELAY_MS);
		// We are using ThreadLocalRandom.current() because it will give each 
		// thread a random number generator.
		// jitter value would range from 0 to delay and follows uniform distribution.
		long jitter = ThreadLocalRandom.current().nextLong(delay);
		LocalDateTime nextRetryAt = LocalDateTime.now().plusNanos(jitter * 1_000_000);
		return nextRetryAt;
	}

}
