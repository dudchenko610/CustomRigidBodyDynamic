package com.crazydev.physics.dynamic;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Vector2D;

public class Transform {
	
	public float angle;
	public Matrix2x2 rotationMatrix;
	public Vector2D position;
	
	private float lastAngle = -1;
	
	public Transform() {
		this.position       = new Vector2D(0f, 0f);
		this.angle          = 0.0f;
		this.rotationMatrix = new Matrix2x2();
	}
	
	public Matrix2x2 getRotationMatrix() {
		
		if (this.angle != this.lastAngle) {
			this.rotationMatrix.elements[0] = (float)  Math.cos(this.angle);
			this.rotationMatrix.elements[1] = (float) -Math.sin(this.angle);
			this.rotationMatrix.elements[2] = -this.rotationMatrix.elements[1];
			this.rotationMatrix.elements[3] =  this.rotationMatrix.elements[0];
			
			this.lastAngle = this.angle;
		}
		
		return this.rotationMatrix;
	} 
	
}
