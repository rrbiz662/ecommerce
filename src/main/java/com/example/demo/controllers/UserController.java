package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {	
	private Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		log.info("Retrieving user by ID...");
		
		User user = userRepository.findById(id).get();
		
		log.info("User retrieved by ID successfully.");

		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		log.info("Retrieving user by username...");
		
		User user = userRepository.findByUsername(username);
		
		log.info("User retrieved by username successfully.");
		
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Creating user...");
		
		User user = new User();
		Cart cart = new Cart();
		user.setUsername(createUserRequest.getUsername());
		
		if(!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword()) ||
				createUserRequest.getPassword().length() < 8) {
			log.error("Password does not pass valid password criteria.");			
			return ResponseEntity.badRequest().build();
		}	

		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));	
		
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
		
		log.info("User created successfully.");
		
		return ResponseEntity.ok(user);
	}
	
}
