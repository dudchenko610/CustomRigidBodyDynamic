package com.crazydev.physics.collisiondetction.concurrency;

import java.util.ArrayList;

import com.crazydev.physics.Contact;
import com.crazydev.physics.collisiondetction.TreeNode;

import android.util.Log;

public class ThreadManager {
	
	private int index = 0;
	private int n;
	
	public ArrayList<CollisionDetectionThread> threads = new ArrayList<CollisionDetectionThread>();
	
	public ThreadManager(int n) {
		this.n = n;
		
		for (int i = 0; i < n; i ++) {
			CollisionDetectionThread collisionDetectionThread = new CollisionDetectionThread();
			threads.add(collisionDetectionThread);
			collisionDetectionThread.start();
		}
	}
	
	public void addBodies(TreeNode node) {
		this.threads.get(index ++).addBodies(node);
		index %= n;
	}
	
	public boolean areManifoldsReady() {
		
		for (int i = 0; i < n; i ++) {
			if (this.threads.get(i).leafs.size() != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public void fillManifolds(ArrayList<Contact> contacts) {
		contacts.clear();
		
		CollisionDetectionThread thread;
		for (int i = 0; i < n; i ++) {
			thread = threads.get(i);
			
			contacts.addAll(thread.contacts);
			
			thread.contacts.clear();
			thread.contactPool.reset();
			
		}
	}
	
	public void reset() {
		this.index = 0;
	}
	
}
