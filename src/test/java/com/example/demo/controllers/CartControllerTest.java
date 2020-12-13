package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import  static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class CartControllerTest {
	private CartController cartController;
	
	private UserRepository userRepository = mock(UserRepository.class);
	private CartRepository cartRepository = mock(CartRepository.class);
	private ItemRepository itemRepository = mock(ItemRepository.class);
		
	@Before
	public void setUp() {
		cartController = new CartController();
		TestUtils.InjectObjects(cartController, "userRepository", userRepository);
		TestUtils.InjectObjects(cartController, "cartRepository", cartRepository);
		TestUtils.InjectObjects(cartController, "itemRepository", itemRepository);		
	}
	
	@Test
	public void addToCartSuccess() {	
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		
		// Update cart request
		cartRequest.setUsername(TestUtils.username);
		cartRequest.setItemId(TestUtils.itemId);
		cartRequest.setQuantity(1);

		// Add item to cart
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(TestUtils.createUser());
		when(itemRepository.findById(TestUtils.itemId)).thenReturn(Optional.of(TestUtils.createItem()));
		ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
		
		// Ck item was added
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getUser().getUsername(), "testuser");
		assertEquals(response.getBody().getItems().size(), 1);
	}
	
	@Test
	public void addToCartUserNotFound() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		
		// Add item to cart
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(null);
		ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void addToCartItemNotFound() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		when(userRepository.findByUsername(TestUtils.username)).thenReturn(TestUtils.createUser());
		when(itemRepository.findById(TestUtils.itemId)).thenReturn(Optional.empty());
		ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void removeFromCartSuccess() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		
		// Update cart Request
		cartRequest.setUsername(TestUtils.username);
		cartRequest.setItemId(TestUtils.itemId);
		cartRequest.setQuantity(1);

		// Need to add item to empty cart before we can remove
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(TestUtils.createUser());
		when(itemRepository.findById(TestUtils.itemId)).thenReturn(Optional.of(TestUtils.createItem()));
		ResponseEntity<Cart> addResponse = cartController.addTocart(cartRequest);
		
		// Ck item was added
		assertEquals(addResponse.getStatusCode(), HttpStatus.OK);
		assertEquals(addResponse.getBody().getItems().size(), 1);
		
		// Remove item
		when(addResponse.getBody().getUser().getCart()).thenReturn(addResponse.getBody().getUser().getCart());
		ResponseEntity<Cart> removeResponse = cartController.removeFromcart(cartRequest);		

		// Ck item was removed
		assertEquals(removeResponse.getStatusCode(), HttpStatus.OK);
		assertEquals(removeResponse.getBody().getItems().size(), 0);		
	}	
	
	@Test
	public void removeFromCartUserNotFound() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		
		// Add item to cart
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(null);
		ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void removeFromCartItemNotFound() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		when(userRepository.findByUsername(TestUtils.username)).thenReturn(TestUtils.createUser());
		when(itemRepository.findById(TestUtils.itemId)).thenReturn(Optional.empty());
		ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
}
