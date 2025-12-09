package com.shrikantanand.inventoryservice.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.shrikantanand.inventoryservice.dto.ProductLifecycleEvent;
import com.shrikantanand.inventoryservice.enumeration.ProductLifecycleEventType;
import com.shrikantanand.inventoryservice.service.InventoryService;

@Service
public class ProductLifecyleEventConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductLifecyleEventConsumer.class);
	
	@Autowired
	private InventoryService inventoryService;
	
	@KafkaListener(topics = "product.lifecycle.events",
			containerFactory = "productLifecycleEventListenerFactory")
	public void productLifecycleEventConsumer(ConsumerRecord<Integer, ProductLifecycleEvent> record, 
			Acknowledgment ack) {
		ProductLifecycleEvent event = record.value();
		long offset = record.offset();
		logger.info("Received product-lifecycle-event: {} with offset: {}", event, offset);
		if(event.getEventType() == ProductLifecycleEventType.PRODUCT_ADDED) {			
			inventoryService.addNewInventory(event.getProductId());
		}
		ack.acknowledge();
	}

}
