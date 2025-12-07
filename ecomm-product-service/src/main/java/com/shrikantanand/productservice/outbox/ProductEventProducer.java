package com.shrikantanand.productservice.outbox;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.shrikantanand.productservice.dto.ProductEvent;

@Service
public class ProductEventProducer {
	
	@Autowired
	KafkaTemplate<Integer, ProductEvent> kafkaTemplate;
	
	private static final String TOPIC = "product-event-topic";
	
	public boolean publishProductEvent(ProductEvent event) {
		try {
			kafkaTemplate.send(TOPIC, event).get(1, TimeUnit.SECONDS);
			return true;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return false;
		}
	}

}
