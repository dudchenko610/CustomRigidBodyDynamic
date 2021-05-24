package com.crazydev.physics.dynamic;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.physics.Body;

public class LinearForce extends Force {

	public Vector2D f;
	public Vector2D forcePoint;
	
	public Vector2D initialForcePoint;
	
	private Vector2D forceApplicationPoint = new Vector2D();
	
	public LinearForce(Vector2D force, Vector2D forcePoint) {
		this.f                 = force.copy();
		this.initialForcePoint = forcePoint.copy();
		this.forcePoint        = new Vector2D();
		
	}
	
	@Override
	public void apply(Body body, Matrix2x2 rotMatrix, float dt) {
		// linear motion is simply the integrated force, divided by the mass
		body.linearVelocity.add(dt * body.mass.invMass * this.f.x, dt * body.mass.invMass * this.f.y);
	
		// angular motion depends on the force application point as well as via the torque
		if (!this.forcePoint.equals(body.transform.position)) {
			this.forceApplicationPoint.set(this.initialForcePoint).multiplyMatrix(rotMatrix);
			float torque = Vector2D.crossProduct(this.forceApplicationPoint, this.f);
			
			body.angularVelocity += dt * body.mass.invInertia * torque;
		}
	}

	@Override
	public void updateApplicationPoint(Matrix2x2 rotMatrix, Vector2D position) {
		this.forcePoint.set(this.initialForcePoint).multiplyMatrix(rotMatrix).add(position);
	}
	
	@Override
	public void depictForce(VertexBatcher2D vertexBatcher2D) {
		vertexBatcher2D.addLine(forcePoint, forcePoint.copy().add(f.copy().normalize()), new Vector3D(1, 0, 0));
	}
	

}
