package com.shrikantanand.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.inventoryservice.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}
