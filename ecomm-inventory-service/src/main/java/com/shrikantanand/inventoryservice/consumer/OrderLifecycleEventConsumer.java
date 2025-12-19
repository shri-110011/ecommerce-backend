package com.shrikantanand.inventoryservice.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.shrikantanand.inventoryservice.dto.OrderLifecycleEvent;
import com.shrikantanand.inventoryservice.enumeration.OrderLifecycleEventType;
import com.shrikantanand.inventoryservice.service.InventoryService;

@Service
public class OrderLifecycleEventConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderLifecycleEventConsumer.class);
	
	@Autowired
	private InventoryService inventoryService;
	
	@KafkaListener(topics = "order.lifecycle.events",
			containerFactory = "orderLifecycleEventListenerFactory",
			groupId = "inventory-service.order-lifecycle")
	public void orderLifecycleEventConsumer(ConsumerRecord<Integer, OrderLifecycleEvent> record, 
			Acknowledgment ack) {
		OrderLifecycleEvent event = record.value();
		long offset = record.offset();
		logger.info("Received order-lifecycle-event: {} with offset: {}", event, offset);
		if(event.getEventType() == OrderLifecycleEventType.ORDER_CONFIRMED) {
			// Consume only active reservations
			inventoryService.processOrderConfirmedEvent(event.getReservationId());
		}
		else if(event.getEventType() == OrderLifecycleEventType.ORDER_CANCELLED) {
			// Cancel only consumed reservations and free the reserved stock
			inventoryService.processOrderCancelledEvent(event.getReservationId());
		}
		ack.acknowledge();
	}

}
