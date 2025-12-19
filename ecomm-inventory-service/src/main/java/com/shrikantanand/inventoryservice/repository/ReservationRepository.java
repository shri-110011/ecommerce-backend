package com.shrikantanand.inventoryservice.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.inventoryservice.entity.Reservation;
import com.shrikantanand.inventoryservice.enumeration.ReservationStatus;

import jakarta.persistence.LockModeType;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			select r from Reservation r where r.reservationId = :reservationId
			""")
	Reservation findByIdForUpdate(Integer reservationId);

	// We need to skip locked rows so as to prevent dead lock in the case when 
	// there are multiple instances of this inventory-service and the reservation 
	// clean up scheduler on each one of those is allowed to run at the scheduled 
	// time.
	@Query(value = """
			select r.reservation_id from reservation r 
			where r.expiration_datetime <= now() 
			and r.status = :status 
			for update skip locked
			""", nativeQuery = true)
	List<Integer> getExpiredReservationsForUpdate(String status, PageRequest ofSize);
	
	@EntityGraph(attributePaths = {"reservedItems"})
	@Query("""
			select r from Reservation r 
			where r.reservationId in :reservationIds
			""")
	List<Reservation> getReservationsAlongWithReservedItems(List<Integer> reservationIds);

}
