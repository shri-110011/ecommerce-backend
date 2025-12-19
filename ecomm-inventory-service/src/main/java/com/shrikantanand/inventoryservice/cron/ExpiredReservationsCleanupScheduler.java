package com.shrikantanand.inventoryservice.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shrikantanand.inventoryservice.service.InventoryService;

@Component
public class ExpiredReservationsCleanupScheduler {
	
	@Autowired
	InventoryService inventoryService;
	
	@Scheduled(fixedDelay = 120000)
	public void cleanupExpiredReservations() {
		inventoryService.cleanupExpiredReservations();
	}

}
