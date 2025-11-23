package com.shrikantanand.userservice.service;

import com.shrikantanand.userservice.dto.AddUserRequest;
import com.shrikantanand.userservice.dto.AddUserResponse;

public interface UserService {
	
	AddUserResponse registerUser(AddUserRequest request);
	
	String validateUser(int id);

}
