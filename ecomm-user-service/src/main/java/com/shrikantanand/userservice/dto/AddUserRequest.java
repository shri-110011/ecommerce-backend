package com.shrikantanand.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class AddUserRequest {
	
	@NotNull(message = "cannot be null")
	@NotBlank(message = "must contain at least one non-whitespace character")
	private String userName;
	
	@NotNull(message = "cannot be null")
	@NotBlank(message = "must contain at least one non-whitespace character")
	private String emailId;

}
