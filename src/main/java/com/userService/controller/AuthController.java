package com.userService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userService.binding.Request;
import com.userService.binding.Response;
import com.userService.model.Users;
import com.userService.repo.AuthRepo;
import com.userService.service.AuthService;
import com.userService.service.JwtService;



@RestController 
@RequestMapping("/auth")
public class AuthController {

	private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthRepo repo;
	
	@Autowired
	private UserDetailsService details;
	
	@Autowired
	private JwtService service;
	
	@Autowired
	private KafkaTemplate<String, Users> template;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Users user){
		logger.info("user {}",user);
		//authService.register(user);
		
		 // Publish an event to Kafka
		template.send("user-registration", user);
		return new ResponseEntity<>(authService.register(user), HttpStatus.CREATED);
		
	}
	
//	@PostMapping("/register")
//	public ResponseEntity<String> register(@RequestBody Users user){
//		logger.info("user {}",user);
//		return new ResponseEntity<>(authService.register(user), HttpStatus.CREATED);
//		
//	}
	
	@PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody Request request) {

       logger.info("login method called");
        boolean validate = authService.loginUser(request);
        if (validate){
        	Users validateUser= repo.findByUsername(request.getUsername()).get();
            UserDetails userDetails = details.loadUserByUsername(validateUser.getUsername());
            String token = this.service.generateToken(userDetails);
            Response response = Response.builder()
                    .jwtToken(token)
                    .username(userDetails.getUsername()).build();
            return ResponseEntity.ok(response);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    
//	@PostMapping("/login")
//	public String loginuser(@RequestBody Request request) {		
//		Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//		
//		if(authentication.isAuthenticated()) {
//			return authService.generateTokenForUser(request.getUsername());
//		}else {
//			throw new RuntimeException("Invalid Access");
//		}
//		
//		
//	}

//	@GetMapping("/validate")
//    public String validateToken(@RequestParam("token") String token){
//        authService.validateToken(token);
//        return "Token is validate";
//    }

}
