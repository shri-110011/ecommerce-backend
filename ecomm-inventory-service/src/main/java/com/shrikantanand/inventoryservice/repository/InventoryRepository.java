package com.shrikantanand.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.inventoryservice.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

}
