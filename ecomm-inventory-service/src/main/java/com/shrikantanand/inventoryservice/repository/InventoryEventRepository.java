package com.shrikantanand.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.inventoryservice.entity.InventoryEvent;

public interface InventoryEventRepository extends JpaRepository<InventoryEvent, Integer> {

}
