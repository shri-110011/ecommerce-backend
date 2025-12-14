package com.shrikantanand.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.inventoryservice.entity.ReservedItem;

public interface ReservedItemRepository extends JpaRepository<ReservedItem, Integer> {

}
