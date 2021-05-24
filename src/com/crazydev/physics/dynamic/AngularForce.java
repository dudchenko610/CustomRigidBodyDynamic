package com.crazydev.physics.dynamic;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Vector2D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.physics.Body;

public class AngularForce extends Force {

	public float torque;
	
	public AngularForce(float torque) {
		this.torque = torque;
	}
	
	@Override
	public void apply(Body body, Matrix2x2 rotMatrix, float dt) {
		body.angularVelocity += dt * body.mass.invInertia * torque;
	}
	
	@Override
	public void updateApplicationPoint(Matrix2x2 rotMatrix, Vector2D position) {
		
	}

	@Override
	public void depictForce(VertexBatcher2D vertexBatcher2D) {
		// TODO Auto-generated method stub
		
	}

}
