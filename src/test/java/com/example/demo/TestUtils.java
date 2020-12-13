package com.example.demo;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

public class TestUtils {
	
	public static long userId = 1; 
	public static String username = "testuser";
	public static String password = "12345678";
	
	public static long cartId = 1;
	
	public static long itemId = 1;
	public static String itemName = "testitem";
	public static String itemDescription = "test item";	
	
	public static void InjectObjects(Object target, String fieldName, Object toInject) {
		boolean wasPrivate = false;
		
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			
			if(!field.isAccessible()) {
				field.setAccessible(true);
				wasPrivate = true;
			}
			
			field.set(target, toInject);
			
			if(wasPrivate) {
				field.setAccessible(false);
			}
			
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static User createUser() {
		User user = new User();
		
		user.setId(userId);
		user.setUsername(username);
		user.setPassword(password);
		user.setCart(createCart(user));
		
		return user;
	}
	
	public static Cart createCart(User user) {
		List<Item> items = new ArrayList<Item>();
		BigDecimal total = new BigDecimal(0);		
		Cart cart = new Cart();
		
		cart.setId(cartId);
		cart.setItems(items);
		cart.setTotal(total);
		cart.setUser(user);
		
		return cart;
	}
	
	public static Item createItem() {
		BigDecimal price = new BigDecimal(5);
		Item item = new Item();
		
		item.setId(itemId);
		item.setName(itemName);
		item.setPrice(price);
		item.setDescription(itemDescription);
		
		return item;
	}	
}
