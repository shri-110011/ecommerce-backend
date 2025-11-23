package com.shrikantanand.userservice.dto;

import lombok.Value;

@Value
public class AddUserResponse {
	
	private int userId;
	
	private String userName;
	
	private String emailId;
	
	private char isActive;

}
