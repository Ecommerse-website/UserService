package com.userService.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userService.model.Users;
import com.userService.service.UserServiceImpl;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Users>> getUser(@PathVariable long id){
		return new ResponseEntity<>(userServiceImpl.getUser(id), HttpStatus.OK);
		
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<List<Users>> getAll(){
		System.out.println("get all users called");
		return new ResponseEntity<List<Users>>(userServiceImpl.getAllUsers(), HttpStatus.OK);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Users> update(@RequestBody Users user, @PathVariable long id){
		return new ResponseEntity<Users>(userServiceImpl.updateUser(user, id), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable long id) {
		userServiceImpl.deleteUser(id);
	}
	
	

	
}









