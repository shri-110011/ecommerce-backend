package com.shrikantanand.orderservice.outbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderEventOutboxScheduler {
	
	@Autowired
	private OrderEventOutboxService orderEventOutboxService;
	
	@Scheduled(fixedDelay = 30000)
	public void processProductEvents() {
		orderEventOutboxService.processPendingOrderEvents();
	}

}
