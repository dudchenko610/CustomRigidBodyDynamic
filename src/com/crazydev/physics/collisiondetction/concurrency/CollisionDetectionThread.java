package com.crazydev.physics.collisiondetction.concurrency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.crazydev.math.Rectangle;
import com.crazydev.physics.Body;
import com.crazydev.physics.Contact;
import com.crazydev.physics.collisiondetction.ContactPool;
import com.crazydev.physics.collisiondetction.OverlapTester;
import com.crazydev.physics.collisiondetction.TreeNode;

import android.util.Log;

public class CollisionDetectionThread extends Thread {
	
	private volatile boolean isAlive = true;
	
	public CopyOnWriteArrayList<TreeNode> leafs;
	
	private OverlapTester overlapTester;
	
	public ContactPool contactPool;
	public HashSet<Contact> contacts;
	
	private ArrayList<ArrayList<Body>> cells;
	private int[] indexes;
	
	
	public CollisionDetectionThread() {

		this.leafs         = new CopyOnWriteArrayList<TreeNode>();
		this.overlapTester = new OverlapTester();
		this.contacts     = new HashSet<Contact>();
		this.contactPool  = new ContactPool();
		this.cells         = new  ArrayList<ArrayList<Body>>();
		this.indexes       = new int[3000];
		
		for (int i = 0; i < 3000; i ++) {
			cells.add(new ArrayList<Body>());
		}
		
	}
	
	@Override
	public void run() {
		
		int index_l     = 0;
		int index_r     = 0;
		
		int index_x_1   = 0;
		int index_x_2   = 0;
		int index_y_1   = 0;
		int index_y_2   = 0;
		
		int n_w     	= 0;
		int n_h     	= 0;
		float x     	= 0;
		float y         = 0;
		float x_min  	= 0;
		float x_max     = 0;
		float y_min     = 0;
		float y_max     = 0;
		
		int counter_arr = 0;
		int index       = 0;
		
		int square      = 0;
		int ww          = 0;
		int hh          = 0;
		
		ArrayList<Body> bodiesInCell = null;
		int size = 0;
		
		Body body1;
		Body body2;
		Contact contact;
		int id_hash_key = 0;

		
		while (isAlive) {
			
			long o = System.nanoTime();
			
			while (this.leafs.size() > 0) {

				TreeNode node = this.leafs.get(0);
				
				ArrayList<Body> bodies = node.nodeBodies;
				
				float d = 0;
				
				Rectangle aabb;
				
				float w = 0;
				float h = 0;
				
				for (int i = 0; i < bodies.size(); i ++) {
					Body b = bodies.get(i);
					
					aabb = b.getFixture().getAABB();
					w = aabb.corners[1].x - aabb.corners[0].x;
					h = aabb.corners[3].y - aabb.corners[0].y;
					
					if (w > d) {
						d = w;
					}
					
					if (h > d) {
						d = h;
					}
				
				}
				
				if (d > 5f) {
					d = 5f;
				}
				
				x_min = node.area.corners[0].x;
				x_max = node.area.corners[1].x;
				y_min = node.area.corners[0].y;
				y_max = node.area.corners[3].y;
				
				n_w = (int) Math.ceil( (x_max - x_min) / d);
				n_h = (int) Math.ceil( (y_max - y_min) / d);
				
				for (int i = 0; i < bodies.size(); i ++) {
					Body b = bodies.get(i);
					aabb = b.getFixture().getAABB();
					
					x = aabb.corners[0].x - x_min;
					index_x_1 = (int) (x / d);
					index_x_1 = index_x_1 < 0 ? 0 : (index_x_1 > (n_w - 1) ? (n_w - 1) : index_x_1);
					
					x = aabb.corners[1].x - x_min;
					index_x_2 = (int) (x / d);
					index_x_2 = index_x_2 < 0 ? 0 : (index_x_2 > (n_w - 1) ? (n_w - 1) : index_x_2);
					
					y = aabb.corners[0].y - y_min;
					index_y_1 = (int) (y / d);
					index_y_1 = index_y_1 < 0 ? 0 : (index_y_1 > (n_h - 1) ? (n_h - 1) : index_y_1);
					
					y = aabb.corners[3].y - y_min;
					index_y_2 = (int) (y / d);
					index_y_2 = index_y_2 < 0 ? 0 : (index_y_2 > (n_h - 1) ? (n_h - 1) : index_y_2);
					
					index_l = n_w * index_y_1 + index_x_1;
					index_r = n_w * index_y_1 + index_x_2;
					
					ww = index_r   - index_l   + 1;
					hh = index_y_2 - index_y_1 + 1;
					
					square = ww * hh;
					
					for (int j = 0; j < square; j ++) {
						index = index_l + j % ww + (j / ww) * n_w;
						
						this.cells.get(index).add(b);
						this.indexes[counter_arr ++] = index;
					}
					
				}
				
				for (int i = 0; i < counter_arr; i ++) {
					
					bodiesInCell = this.cells.get(this.indexes[i]);
					size = bodiesInCell.size();
					
					for (int j = 0; j < size; j ++) {
						for (int k = j + 1; k < size; k ++) {
							contact = this.contactPool.getContact();
							body1 = bodiesInCell.get(j);
							body2 = bodiesInCell.get(k);
							
							Contact m = this.overlapTester.overlapBodies(contact, body1, body2);
							
							if (m != null) {
								
								id_hash_key = 0;
								
								if (body1.unique_id < body2.unique_id) {
									
									id_hash_key = (body1.unique_id << 16) | body2.unique_id;
									
									
								} else {
									id_hash_key = (body2.unique_id << 16) | body1.unique_id;
								}
								
								m.hash_id  = id_hash_key;
								
								if (!this.contacts.contains(m)) {
									this.contacts.add(m);
								}
								
							}
							
						}
					}
					
					
					bodiesInCell.clear();
				}
				
				
				// area testing algorithm
				
				counter_arr = 0;
				this.leafs.remove(node);
				
				try {
					TimeUnit.MICROSECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}

		}
	}
	
	public void addBodies(TreeNode node) {
		
		this.leafs.add(node);

	
	}
	
	public void finish() {
		this.isAlive = false;
	}
	
}
