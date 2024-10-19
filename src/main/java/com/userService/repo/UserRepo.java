package com.userService.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userService.model.Users;


public interface UserRepo extends JpaRepository<Users, Long>{

	

}
