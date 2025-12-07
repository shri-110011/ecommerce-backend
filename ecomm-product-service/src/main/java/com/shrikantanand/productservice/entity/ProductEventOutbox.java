package com.shrikantanand.productservice.entity;

import java.time.LocalDateTime;

import com.shrikantanand.productservice.enumeration.ProductEventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product_event_outbox")
@Data
public class ProductEventOutbox {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Integer eventId;
	
	@Column(name = "event_type")
	@Enumerated(EnumType.STRING)
	private ProductEventType eventType;
	
	private Integer productId;
	
	@Column(name = "is_processed")
	private Character isProcessed;
	
	@Column(name = "retry_count")
	private Integer retryCount;
	
	@Column(name = "next_retry_at")
	private LocalDateTime nextRetryAt;
	
	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "last_updated_datetime")
	private LocalDateTime lastUpdatedDateTime;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

}
