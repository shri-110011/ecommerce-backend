package com.shrikantanand.productservice.outbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductEventOutboxScheduler {
	
	@Autowired
	private ProductEventOutboxService productEventOutboxService;
	
	@Scheduled(fixedDelay = 2000)
	public void processProductEvents() {
		productEventOutboxService.processPendingProductEvents();
	}

}
