package com.shrikantanand.userservice.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shrikantanand.userservice.dao.UserRepository;
import com.shrikantanand.userservice.dto.AddUserRequest;
import com.shrikantanand.userservice.dto.AddUserResponse;
import com.shrikantanand.userservice.entity.User;
import com.shrikantanand.userservice.enumeration.UserStatus;
import com.shrikantanand.userservice.exception.UserException;
import com.shrikantanand.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public AddUserResponse registerUser(AddUserRequest request) {
		userRepository.findByEmailId(request.getEmailId())
			.ifPresent(u -> {
				int statusCode = 400;
				String message = "User with email " + request.getEmailId() + " already exists.";
				throw new UserException(statusCode, message);
			});
		
		User user = new User();
		user.setUserName(request.getUserName());
		user.setEmailId(request.getEmailId());
		user.setIsActive('Y');
		userRepository.save(user);
		
		AddUserResponse response = new AddUserResponse(user.getUserId(), 
				user.getUserName(), user.getEmailId(), 
				user.getIsActive());
		
		return response;
	}

	@Override
	public String validateUser(int id) {
		Character isActive = userRepository.findUserStatus(id);
		if(isActive == null) {
			return UserStatus.INVALID.name();
		}
		else if(isActive == 'Y') {
			return UserStatus.ACTIVE.name();
		}
		else {
			return UserStatus.INACTIVE.name();
		}
	}

}
