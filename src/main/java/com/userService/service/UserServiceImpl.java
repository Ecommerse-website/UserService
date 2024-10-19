package com.userService.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userService.model.Users;
import com.userService.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public Users updateUser(Users userDetails, long id) {
		return userRepo.findById(id).map((user) -> {
			user.setUsername(userDetails.getUsername());
			user.setPassword(userDetails.getPassword());
			user.setEmail(userDetails.getEmail());
			return userRepo.save(user);
		}).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public Optional<Users> getUser(long id) {
		return userRepo.findById(id);
	}

	@Override
	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public String deleteUser(long id) {
		Optional<Users> user = userRepo.findById(id);
		if(user.isPresent()) {
			userRepo.deleteById(id);
			return "User is deleted successfully";
		}
		return "User is not found";
	}

	
}
