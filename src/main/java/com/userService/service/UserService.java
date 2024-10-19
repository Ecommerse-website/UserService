package com.userService.service;

import java.util.List;
import java.util.Optional;

import com.userService.model.UserDto;
import com.userService.model.Users;

public interface UserService {
	
	Users updateUser(Users userDetails, long id);
	
	Optional<Users> getUser(long id);
	
	List<Users> getAllUsers();
	
	String deleteUser(long id);
}
