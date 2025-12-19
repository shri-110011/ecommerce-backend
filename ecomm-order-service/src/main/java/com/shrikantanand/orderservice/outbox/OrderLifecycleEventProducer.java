package com.shrikantanand.orderservice.outbox;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.shrikantanand.orderservice.dto.OrderLifecycleEvent;

@Service
public class OrderLifecycleEventProducer {
	
	@Autowired
	KafkaTemplate<Integer, OrderLifecycleEvent> kafkaTemplate;
	
	private static final String TOPIC = "order.lifecycle.events";
	
	public boolean publishOrderEvent(OrderLifecycleEvent event) {
		try {
			// We need to partition by reservationId to make sure that messages 
			// associated with a reservation id are processed by a single consumer 
			// at any time.
			kafkaTemplate.send(TOPIC, event.getReservationId(), event).get(3, TimeUnit.SECONDS);
			return true;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return false;
		}
	}

}
