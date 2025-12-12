package com.shrikantanand.inventoryservice.dto;

import com.shrikantanand.inventoryservice.enumeration.InvalidItemReason;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidItemResponse extends ItemResponse {
	
	private InvalidItemReason reason;

	public InvalidItemResponse(Integer productId, Integer requestedQuantity, 
			InvalidItemReason reason) {
		super(productId, requestedQuantity);
		this.reason = reason;
	}

}
