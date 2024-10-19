package com.userService.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.userService.binding.Request;
import com.userService.model.Users;
import com.userService.repo.AuthRepo;

@Service
public class AuthService {
	
	@Autowired
	private AuthRepo authRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
    
 

	public String register(Users user) {
		Optional<Users> user1 = authRepo.findByUsername(user.getUsername());
		if(user1.isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			authRepo.save(user);
			return "User is added successfully";
		}
		return "User with username "+user.getUsername()+" is already present.";
	}

	
	public boolean loginUser (Request request){
		Users validateUser = authRepo.findByUsername(request.getUsername()).get();
        return BCrypt.checkpw(request.getPassword(), validateUser.getPassword());

    }




}
