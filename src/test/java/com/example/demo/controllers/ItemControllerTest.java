package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {
	private ItemController itemController;	
	private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
	
	@Before
	public void setUp() {
		itemController = new ItemController();
		TestUtils.InjectObjects(itemController, "itemRepository", itemRepository);
	}
	
	@Test
	public void getItemsSuccess() {
		List<Item> items = new ArrayList<Item>();
		items.add(TestUtils.createItem());
		
		Mockito.when(itemRepository.findAll()).thenReturn(items);
		
		ResponseEntity<List<Item>> response = itemController.getItems();
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void getItemByIdSuccess() {
		Mockito.when(itemRepository.findById(TestUtils.itemId)).thenReturn(Optional.of(TestUtils.createItem()));		

		ResponseEntity<Item> response = itemController.getItemById(TestUtils.itemId);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void getItemsByNameSuccess() {
		List<Item> items = new ArrayList<Item>();
		items.add(TestUtils.createItem());
		
		Mockito.when(itemRepository.findByName(TestUtils.itemName)).thenReturn(items);
		
		ResponseEntity<List<Item>> response = itemController.getItemsByName(TestUtils.itemName);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);		
	}
	
	@Test
	public void getItemsByNameNotFound() {
		Mockito.when(itemRepository.findByName(TestUtils.itemName)).thenReturn(null);		

		ResponseEntity<List<Item>> response = itemController.getItemsByName(TestUtils.itemName);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);		
	}
}
