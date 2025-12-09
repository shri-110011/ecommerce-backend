package com.shrikantanand.inventoryservice.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shrikantanand.inventoryservice.entity.Inventory;
import com.shrikantanand.inventoryservice.repository.InventoryRepository;
import com.shrikantanand.inventoryservice.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	@Transactional
	public void addNewInventory(int productId) {
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

}
