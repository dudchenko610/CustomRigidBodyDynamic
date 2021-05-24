package com.crazydev.physics.collisiondetction;

import java.util.ArrayList;

import com.crazydev.physics.Contact;

public class ContactPool {
	
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private int index = 0;
	
	public ContactPool() {
		for (int i = 0; i < 700; i ++) {
			this.contacts.add(new Contact());
		}
		
	}
	
	public Contact getContact() {
		return this.contacts.get(index ++);
	}
	
	public void reset() {
		this.index = 0;
	}
}
