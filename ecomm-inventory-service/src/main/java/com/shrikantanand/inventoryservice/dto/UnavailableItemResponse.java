package com.shrikantanand.inventoryservice.dto;

import com.shrikantanand.inventoryservice.enumeration.UnavailableItemReason;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class UnavailableItemResponse extends ItemResponse {
	
	@NotNull(message = "must not be null")
	private UnavailableItemReason reason;

	public UnavailableItemResponse(Integer productId, Integer requestedQuantity, 
			UnavailableItemReason reason) {
		super(productId, requestedQuantity);
		this.reason = reason;
	}

}
