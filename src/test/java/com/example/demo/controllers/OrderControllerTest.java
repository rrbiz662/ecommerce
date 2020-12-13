package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class OrderControllerTest {
	private OrderController orderController;	
	private UserRepository userRepository = mock(UserRepository.class);
	private OrderRepository orderRepository = mock(OrderRepository.class);
	
	@Before
	public void setUp() {
		orderController = new OrderController();
		TestUtils.InjectObjects(orderController, "userRepository", userRepository);
		TestUtils.InjectObjects(orderController, "orderRepository", orderRepository);
	}
	
	@Test
	public void submitOrderSuccess() {
		User user = TestUtils.createUser();
		
		user.getCart().addItem(TestUtils.createItem());	
		
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(user);
		
		ResponseEntity<UserOrder> response = orderController.submit(TestUtils.username);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getItems().size(), 1);
		assertEquals(response.getBody().getTotal(), new BigDecimal(5));
		
	}
	
	@Test
	public void submitOrderNotFound() {
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(null);
		
		ResponseEntity<UserOrder> response = orderController.submit(TestUtils.username);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void getOrdersForUserSuccess() {
		User user = TestUtils.createUser();
		UserOrder order = null;
		List<UserOrder> orders = new ArrayList<UserOrder>();
		
		user.getCart().addItem(TestUtils.createItem());
		order = createOrder(user);
		orders.add(order);
		
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(user);
		when(orderRepository.findByUser(user)).thenReturn(orders);
		
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(TestUtils.username);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().size(), 1);
	}
	
	@Test
	public void getOrdersForUserNotFound() {		
		when(userRepository.findByUsername(TestUtils.username)).thenReturn(null);
		
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(TestUtils.username);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	private UserOrder createOrder(User user){
		UserOrder order = new UserOrder();
		BigDecimal total = new BigDecimal(0); 
		
		order.setId(1L);
		order.setUser(user);
		order.setItems(user.getCart().getItems());
		
		for (Item item : order.getItems()) {
			total.add(item.getPrice());
		}
		
		order.setTotal(total);
		
		return order;
	}	
}
