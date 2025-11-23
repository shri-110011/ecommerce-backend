package com.shrikantanand.userservice.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(" select u.isActive from User u where u.id = :id ")
	Character findUserStatus(int id);

	Optional<User> findByEmailId(String emailId);
	
}
