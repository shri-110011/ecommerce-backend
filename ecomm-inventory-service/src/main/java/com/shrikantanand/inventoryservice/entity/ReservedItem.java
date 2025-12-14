package com.shrikantanand.inventoryservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inventory_event_reservation")
@Data
public class ReservedItem {
	
	@Id
	@Column(name = "inventory_event_id")
	private Integer inventoryEventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "created_by")
	private String createdBy;

}
