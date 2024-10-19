package com.userService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.userService.model.Users;
import com.userService.repo.AuthRepo;

public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private AuthRepo authRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = authRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		return new User(user.getUsername(), user.getPassword(), null);
	}
}
