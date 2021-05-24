package com.crazydev.physics;

import java.util.ArrayList;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Vector6D;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.physics.collisiondetction.kdtree.KDTreeBuilder;
import com.crazydev.physics.dynamic.Force;
import com.crazydev.physics.dynamic.Mass;

import android.util.Log;

public class World {
	
	private ArrayList<Body>  bodies = new ArrayList<Body>();
	private ArrayList<Joint> joints = new ArrayList<Joint>();
	
	private Vector2D gravityAcceleration = new Vector2D();
	
	private int iterationNumber = 30;
	private float beta = 0.33f;
	
	public static float DT       = 1 / 60f;
	public static float FRICTION = 1.0f;
	
	// 0.05 - 20 - 300 is ok
	
	private KDTreeBuilder kdTreeBuilder;
	private ArrayList<Contact> contacts;
	
	private Vector2D forceApplicationPoint;
	
	public static VertexBatcher2D vertexBatcher2D;
	
	public World(Vector2D gravityAcceleration, VertexBatcher2D vertexBatxher2D) {
		this.kdTreeBuilder         = new KDTreeBuilder(this.bodies);
		this.forceApplicationPoint = new Vector2D();
		this.gravityAcceleration.set(gravityAcceleration);
		
		this.vertexBatcher2D = vertexBatxher2D;
	}
	
	public void addBody(Body body) {
		
		if (!Float.isInfinite(body.mass.mass)) {
			body.addForce(this.gravityAcceleration.copy().multiplyScalar(body.mass.mass), -1);
		}
				
		this.bodies.add(body);
	}
	
	public Body getBody(int i) {
		return this.bodies.get(i);
	}
	
	public int getBodyCount() {
		return this.bodies.size();
	}
	
	public void addJoint(Joint joint) {
		this.joints.add(joint);
	}
	
	public void removeJoint(Joint joint) {
		this.joints.remove(joint);
	}
	
	public void u() {
		this.contacts = this.kdTreeBuilder.getManifolds();
	}
	
	public void update(float dt) {
		
	//	dt = 1.0f / 100.0f;
		
		/** 1. Collision detection **/
		this.contacts = this.kdTreeBuilder.getManifolds();
		
		/** 2. Integrate forces and torques and compute tentative velocities**/
		
		int s1 = this.bodies.size();
		int s2 = this.joints.size();
		int s3 = this.contacts.size();
		
		Mass mass;
		Force force;
		Matrix2x2 rotationMatrix;
		
		Body body;
		ArrayList<Force> forces;
		int forcesSize = 0;
		
		for (int i = 0; i < s1; i ++) {
			body = this.bodies.get(i);
			mass = body.mass;
			
			if (mass.invMass == 0) {
				continue;
			}
			
			rotationMatrix = body.transform.getRotationMatrix();
			forces         = body.forces;
			forcesSize     = forces.size();
			
			for (int j = 0; j < forcesSize; j ++) {
				force = forces.get(j);
				force.apply(body, rotationMatrix, DT);
				
			//	force.depictForce(this.vertexBatcher2D);

			}
			
		}
		
		/** 3.2. Correct velocity errors for joints **/
		
		for (int i = 0; i < s2 + s3; i ++) {
			if (i < s2) {
				this.joints.get(i).precomputeConstants();
			} else {
				this.contacts.get(i - s2).precomputeConstants();
			}
			
		}
		
		for (int i = 0; i < this.iterationNumber; i ++) {
			
			for (int j = 0; j < s2 + s3; j ++) {
				if (j < s2) {
					this.joints.get(j).solveConstraint();
				} else {
					this.contacts.get(j - s2).solveConstraint();
				}

			}
			
		}
		
		/** 3.1. Correct velocity errors for contacts **/
		
	/*	for (int i = 0; i < s3; i ++) {
			
		}
		
		for (int i = 0; i < this.iterationNumber; i ++) {
			for (int j = 0; j < s3; j ++) {
						
			}
		}*/

		
		/** 4. Update positions **/
		
		for (int i = 0; i < s1; i ++) {
			body = this.bodies.get(i);
			
			body.transform.position.add(World.DT * body.linearVelocity.x,
										World.DT * body.linearVelocity.y);
			body.transform.angle += World.DT * body.angularVelocity;
			
			body.updateForceApplicationPoints();
		}
		
	}
	
	
}
