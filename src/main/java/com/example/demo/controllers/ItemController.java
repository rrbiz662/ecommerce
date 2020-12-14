package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	private Logger log = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info("Retrieving items...");
		
		List<Item> items = itemRepository.findAll();
		
		log.info("Items retrieved successfully.");		
		
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.info("Retrieving item by ID...");
		
		Item item = itemRepository.findById(id).get();
		
		log.info("Item retrieved by ID successfully.");
		
		return ResponseEntity.ok(item);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		log.info("Retrieving items by name...");
		
		List<Item> items = itemRepository.findByName(name);
		
		log.info("Items retrieved by name successfully.");
		
		if(items == null || items.isEmpty()) {
			log.error("Item " + name + " does not exist.");			
			return ResponseEntity.notFound().build();
		}
		else {
			return ResponseEntity.ok(items);
		}	
	}
	
}
