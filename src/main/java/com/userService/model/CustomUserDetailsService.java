package com.userService.model;

import com.userService.repo.AuthRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthRepo authRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println("username "+username);
        Optional<Users> userData = this.authRepo.findByUsername(username);
        System.out.println("userData "+userData);
        return userData.map(CustomUserDetails::new).orElseThrow(()->new UsernameNotFoundException("User not found with name :- "+username));
    }
}
