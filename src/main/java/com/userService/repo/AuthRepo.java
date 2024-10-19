package com.userService.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userService.model.Users;

public interface AuthRepo extends JpaRepository<Users, Long>{

	Optional<Users> findByUsername(String username);

	

}
