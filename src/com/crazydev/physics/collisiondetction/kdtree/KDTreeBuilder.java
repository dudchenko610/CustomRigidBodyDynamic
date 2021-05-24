package com.crazydev.physics.collisiondetction.kdtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import com.crazydev.math.Rectangle;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.physics.Body;
import com.crazydev.physics.Contact;
import com.crazydev.physics.World;
import com.crazydev.physics.collisiondetction.ContactPool;
import com.crazydev.physics.collisiondetction.OverlapTester;
import com.crazydev.physics.collisiondetction.TreeNode;
import com.crazydev.physics.collisiondetction.concurrency.ThreadManager;
import com.crazydev.physics.fixtures.Fixture;

import android.util.Log;

public class KDTreeBuilder {
	
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private ArrayList<Body>     bodies;
	private KDTreeNodePool      kdTreeNodePool;
	private ThreadManager       threadManager;
	
	public KDTreeBuilder(ArrayList<Body> bodies) {
		this.bodies         = bodies;
		this.kdTreeNodePool = new KDTreeNodePool();
		this.threadManager  = new ThreadManager(2);
	}
	
	private int n    = 18;
	private int L [] = new int[n];
	private int R [] = new int[n];
	
	private float collideCost = 15;
	
	ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
	
	public ArrayList<Contact> getManifolds() {
		this.contacts.clear();
		
		boolean x_mode = true;
		
		TreeNode currentNode =  this.kdTreeNodePool.getKDTreeNode();
		currentNode.area.corners[0].set( -50.0f, -50.0f);
		currentNode.area.corners[1].set( 200.0f, -50.0f);
		currentNode.area.corners[2].set( 200.0f, 200.0f);
		currentNode.area.corners[3].set( -50.0f, 200.0f);
		
		currentNode.level = 1;
		currentNode.nodeBodies.clear();
		currentNode.nodeBodies.addAll(this.bodies);
		
		float min_x = 0;
		float max_x = 0;
		float min_y = 0;
		float max_y = 0;
		
		float width  = 0;
		float height = 0;
		
		boolean condition = true;
		
		float depth = 8;
		
		int n = -1;
		
		while (condition) {
			
			n = (int) (this.n - currentNode.level * 1.2f);
			
			if (currentNode.level == depth || currentNode.nodeBodies.size() < 4) {  // 2 - error
				currentNode.isChecked = true;

				TreeNode node = currentNode;
				currentNode = currentNode.parent;
				
				if ( node.nodeBodies.size() > 1 && node.left == null && node.right == null) {

					this.threadManager.addBodies(node);
		
				}
				
				if (currentNode == null) {
					break;
				}
				
				continue;
				
			}
			
			if (currentNode.left == null && currentNode.right == null) {
				
				currentNode.left         = this.kdTreeNodePool.getKDTreeNode();
				currentNode.right        = this.kdTreeNodePool.getKDTreeNode();
				currentNode.left.level   = currentNode.level + 1;
				currentNode.right.level  = currentNode.level + 1;
				currentNode.left.parent  = currentNode;
				currentNode.right.parent = currentNode;
				currentNode.isChecked = false;
				currentNode.isChecked = false;
				
				currentNode.left.nodeBodies.clear();
				currentNode.right.nodeBodies.clear();
				
			} else {
				
				condition = !(currentNode.left.isChecked && currentNode.right.isChecked && currentNode.level == 1);
				
				if (!condition) {
					break;
				}
				
				if (currentNode.isChecked) {
					if (!currentNode.left.isChecked) {
						currentNode = currentNode.left;
						continue;
					}
					
					if (!currentNode.right.isChecked) {
						currentNode = currentNode.right;
						continue;
					}
					
					currentNode = currentNode.parent;
					continue;
				}
			
			}
			
			
			if (currentNode.area.corners[1].x - currentNode.area.corners[0].x >
				currentNode.area.corners[3].y - currentNode.area.corners[0].y) {
				x_mode = true;
			} else {
				x_mode = false;
			}
			
			if (x_mode) {
				min_x = currentNode.area.corners[0].x;
				max_x = currentNode.area.corners[2].x;
				min_y = currentNode.area.corners[0].y;
				max_y = currentNode.area.corners[2].y;
				
				width  = max_x - min_x;
				height = max_y - min_y;
				
			} else {
				min_x = currentNode.area.corners[0].y;
				max_x = currentNode.area.corners[2].y;
				
				min_y = currentNode.area.corners[0].x;
				max_y = currentNode.area.corners[2].x;
				
				width  = max_x - min_x;
				height = max_y - min_y;
				
			}
			
			float n_w   = width / n;

			for (int i = 0; i < n; i ++) {
				this.L[i] = 0;
				this.R[i] = 0;
			}
			
			Rectangle aabb_i;
			float diff;
			
			int sum_left  = 0;
			int sum_right = 0;
			
			int index = -1;
			
			int size = currentNode.nodeBodies.size();

			for (int i = 0; i < size; i ++) {
				aabb_i = currentNode.nodeBodies.get(i).getFixture().getAABB();
				
				diff = (x_mode ? aabb_i.corners[0].x : aabb_i.corners[0].y) - min_x;
				index = (int) (diff / n_w);
				index = index < 0 ? 0 : (index > (n - 1) ? n - 1 : index);
				
				this.L[index] ++;
				sum_left ++;
				
				diff = (x_mode ? aabb_i.corners[2].x : aabb_i.corners[2].y) - min_x;
				index = (int) (diff / n_w);
				
				index = index < 0 ? 0 : (index > (n - 1) ? n - 1 : index);
				
				this.R[index] ++;
				sum_right ++;
				
			}
		
			int l     = sum_left - this.L[0];
			int c_l   = 0;
			this.L[0] = sum_left;  
			
			int r         = sum_right - this.R[n - 1];
			int c_r       = 0;
			this.R[n - 1] = sum_right;
			
			for (int i = 1; i < n; i ++) {
				c_l = this.L[i];
				this.L[i] = l;
				l -= c_l;
				
				c_r = this.R[n - i - 1];
				this.R[n - i - 1] = r;
				r -= c_r;
				
			}
			
			
			float square = width * height;
			
			float min_SAH  =  9999999;
			float theBestX = -1;

			for (int i = 1 + (int) (0.5f * currentNode.level); i < n - (int) (0.5f * currentNode.level); i ++) {
				float w_l = n_w * i + min_x;
				float w_r = max_x - w_l;

				float s_l = w_l * height;
				float s_r = w_r * height;

				float SAH = (s_l * this.R[i - 1] + s_r * this.L[i]) / square;
				
				// https://studfile.net/preview/1815628/
			
				float collideCost = (this.collideCost / (currentNode.level * 0.01f));

				
				float collidedBodiesCost = (sum_left - this.R[i - 1] - this.L[i]) * collideCost;
				SAH += collidedBodiesCost;
				

				if (SAH < min_SAH) {
					min_SAH   = SAH;
					theBestX  = w_l;
				}
			}
			
			
			if (x_mode) {
				World.vertexBatcher2D.
				addLine(new Vector2D(theBestX, currentNode.area.corners[0].y),
						new Vector2D(theBestX, currentNode.area.corners[2].y), new Vector3D(1, 0, 0));
			} else {
				World.vertexBatcher2D.
				addLine(new Vector2D(currentNode.area.corners[0].x, theBestX),
						new Vector2D(currentNode.area.corners[2].x, theBestX), new Vector3D(1, 0, 0));
			}
			
			
			if (x_mode) {
				
				currentNode.left.area.corners[0].set(currentNode.area.corners[0]);
				currentNode.left.area.corners[1].set(theBestX, currentNode.area.corners[0].y);
				currentNode.left.area.corners[2].set(theBestX, currentNode.area.corners[2].y);
				currentNode.left.area.corners[3].set(currentNode.area.corners[3]);
				
				
				currentNode.right.area.corners[0].set(theBestX, currentNode.area.corners[0].y);
				currentNode.right.area.corners[1].set(currentNode.area.corners[1]);
				currentNode.right.area.corners[2].set(currentNode.area.corners[2]);
				currentNode.right.area.corners[3].set(theBestX, currentNode.area.corners[2].y);
				
				n_w = theBestX - min_x;
				
				Body body;
				
				for (int i = 0; i < size; i ++) {
					body   = currentNode.nodeBodies.get(i);
					
					aabb_i = body.getFixture().getAABB();
					
					diff = aabb_i.corners[0].x - min_x;
					if (diff >= 0) {
		
						index = (int) (diff / n_w);
						index = index < 0 ? 0 : (index > 1 ? 1 : index);
						
						if (index == 0) {
							if (!currentNode.left.nodeBodies.contains(body)) {
								currentNode.left.nodeBodies.add(body);
							}
						} else {
							if (!currentNode.right.nodeBodies.contains(body)) {
								currentNode.right.nodeBodies.add(body);
							}
						}
						
					}
					
					
					diff = aabb_i.corners[1].x - min_x;
					
					
					if (diff >= 0) {
						index = (int) (diff / n_w);
						index = index < 0 ? 0 : (index > 1 ? 1 : index);
						
						if (index == 0) {
							if (!currentNode.left.nodeBodies.contains(body)) {
								currentNode.left.nodeBodies.add(body);
							}
							
						} else {
							if (!currentNode.right.nodeBodies.contains(body)) {
								currentNode.right.nodeBodies.add(body);
							}
						}
					}
					
					
					
				}
				
			} else {
				
				currentNode.left.area.corners[0].set(currentNode.area.corners[0]);
				currentNode.left.area.corners[1].set(currentNode.area.corners[1]);
				currentNode.left.area.corners[2].set(currentNode.area.corners[1].x, theBestX);
				currentNode.left.area.corners[3].set(currentNode.area.corners[0].x, theBestX);
				
				
				currentNode.right.area.corners[0].set(currentNode.area.corners[0].x, theBestX);
				currentNode.right.area.corners[1].set(currentNode.area.corners[1].x, theBestX);
				currentNode.right.area.corners[2].set(currentNode.area.corners[2]);
				currentNode.right.area.corners[3].set(currentNode.area.corners[3]);
				
				n_w = theBestX - min_x;
				
				Body body;
				
				for (int i = 0; i < size; i ++) {
					body   = currentNode.nodeBodies.get(i);
					
					aabb_i = body.getFixture().getAABB();
					
					
					diff = aabb_i.corners[0].y - min_x;
					
					if (diff >= 0) {
						index = (int) (diff / n_w);
						index = index < 0 ? 0 : (index > 1 ? 1 : index);
						
						if (index == 0) {
							if (!currentNode.left.nodeBodies.contains(body)) {
								currentNode.left.nodeBodies.add(body);
							}
						} else {
							if (!currentNode.right.nodeBodies.contains(body)) {
								currentNode.right.nodeBodies.add(body);
							}
						}
					}
					

					diff = aabb_i.corners[3].y - min_x;
					
					if (diff >= 0) { 
						index = (int) (diff / n_w);
						index = index < 0 ? 0 : (index > 1 ? 1 : index);
						
						if (index == 0) {
							if (!currentNode.left.nodeBodies.contains(body)) {
								currentNode.left.nodeBodies.add(body);
							}
							
						} else {
							if (!currentNode.right.nodeBodies.contains(body)) {
								currentNode.right.nodeBodies.add(body);
							}
						}
						
					}
					
				}
				
			}
	
			currentNode.isChecked = true;
			currentNode = currentNode.left;
	
			
		}
		
		
		while (!this.threadManager.areManifoldsReady()) {
		}
			
		this.threadManager.fillManifolds(contacts);
			
	
		Contact contact;
		
		for (int i = 0; i < contacts.size(); i ++) {
			contact = contacts.get(i);
			
			if (contact != null) {
				
			//	vertexBatcher2D.addLine(contact.pointA, contact.pointA.copy().add(contact.normal.copy().multiplyScalar(contact.penetration)), new Vector3D(0, 1, 0));
				
		//		World.vertexBatcher2D.addLine(contact.pointA, contact.bodyA.transform.position, new Vector3D(0, 1, 0));
		//		World.vertexBatcher2D.addLine(contact.pointB, contact.bodyB.transform.position, new Vector3D(0, 1, 0));
				
		/*		World.vertexBatcher2D.addPoint(contact.pointA, new Vector3D(1, 0, 0)); // red
				World.vertexBatcher2D.addPoint(contact.pointB, new Vector3D(0, 1, 0)); // green
			
				World.vertexBatcher2D.addLine(contact.pointB, contact.pointB.copy().add(contact.normal), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0), 0.3f);
			*/
			}
			
		}
		
		
		this.kdTreeNodePool.reset();
		this.threadManager.reset();
		
		return this.contacts;
	}

}
