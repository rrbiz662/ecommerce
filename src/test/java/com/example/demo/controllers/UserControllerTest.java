package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {
	
	private UserController userController;	
	private UserRepository userRepository = mock(UserRepository.class);
	private CartRepository cartRepository = mock(CartRepository.class);
	private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
	
	@Before
	public void setUp() {
		userController = new UserController();
		TestUtils.InjectObjects(userController, "userRepository", userRepository);
		TestUtils.InjectObjects(userController, "cartRepository", cartRepository);
		TestUtils.InjectObjects(userController, "passwordEncoder", passwordEncoder);		
	}
	
	@Test
	public void createUserSuccess() throws Exception {
		String hashedPw = "thisishashed";
		when(passwordEncoder.encode(TestUtils.password)).thenReturn(hashedPw);
		CreateUserRequest userRequest = new CreateUserRequest();
		
		userRequest.setUsername(TestUtils.username);
		userRequest.setPassword(TestUtils.password);
		userRequest.setConfirmPassword(TestUtils.password);
		
		ResponseEntity<User> response = userController.createUser(userRequest);
		
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		User user = response.getBody();
		
		assertEquals(TestUtils.username, user.getUsername());
		assertEquals(hashedPw, user.getPassword());
	}
	
	@Test
	public void createUserInvalidPassword() {
		CreateUserRequest userRequest = new CreateUserRequest();
		
		userRequest.setUsername(TestUtils.username);
		userRequest.setPassword(TestUtils.password);
		userRequest.setConfirmPassword("2");
		
		ResponseEntity<User> response = userController.createUser(userRequest);
		
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void findUserByIdSuccess() {
		when(userRepository.findById(TestUtils.userId)).thenReturn(Optional.of(TestUtils.createUser()));
		
		ResponseEntity<User> response = userController.findById(TestUtils.userId);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}	
	
	@Test
	public void findUserByUsernameSucces() {
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(TestUtils.createUser());
		
		ResponseEntity<User> response = userController.findByUserName(TestUtils.username);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void findUserByUsernameNotFound() {
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(null);
		
		ResponseEntity<User> response = userController.findByUserName(TestUtils.username);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
}
