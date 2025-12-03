package com.shrikantanand.inventoryservice.entity;

import java.time.LocalDateTime;

import com.shrikantanand.inventoryservice.enumeration.InventoryEventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inventory_event_log")
@Data
public class InventoryEventLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Integer eventId;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "event_type")
	private InventoryEventType eventType;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "reason")
	private String reason;

}
