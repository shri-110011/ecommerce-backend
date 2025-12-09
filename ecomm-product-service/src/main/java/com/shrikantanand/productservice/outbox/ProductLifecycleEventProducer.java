package com.shrikantanand.productservice.outbox;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.shrikantanand.productservice.dto.ProductLifecycleEvent;

@Service
public class ProductLifecycleEventProducer {
	
	@Autowired
	KafkaTemplate<Integer, ProductLifecycleEvent> kafkaTemplate;
	
	private static final String TOPIC = "product.lifecycle.events";
	
	public boolean publishProductEvent(ProductLifecycleEvent event) {
		try {
			kafkaTemplate.send(TOPIC, event).get(3, TimeUnit.SECONDS);
			return true;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return false;
		}
	}

}
