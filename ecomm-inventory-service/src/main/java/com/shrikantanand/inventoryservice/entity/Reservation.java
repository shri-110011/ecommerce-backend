package com.shrikantanand.inventoryservice.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.shrikantanand.inventoryservice.enumeration.ReservationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private Integer reservationId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	
	@Column(name = "expiration_datetime")
	private LocalDateTime expirationDateTime;
	
	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "last_updated_datetime")
	private LocalDateTime lastUpdatedDateTime;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;
	
	@OneToMany(mappedBy = "reservation", 
			cascade = {CascadeType.MERGE, CascadeType.PERSIST, 
					CascadeType.REFRESH, CascadeType.DETACH})
	private List<ReservedItem> reservedItems;
	
	public void addReservedItem(ReservedItem item) {
		if(reservedItems == null) {
			reservedItems = new ArrayList<>();
		}
		reservedItems.add(item);
		item.setReservation(this);
	}

}
