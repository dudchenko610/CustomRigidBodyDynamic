package com.crazydev.math;

public class Vector6D {
	
	public float e0;
	public float e1;
	public float e2;
	public float e3;
	public float e4;
	public float e5;
	
	public Vector6D() {
		
	}
	
	public Vector6D(float e1, float e2, float e3, float e4, float e5, float e6) {
		
		this.e0 = e1;
		this.e1 = e2;
		this.e2 = e3;
		this.e3 = e4;
		this.e4 = e5;
		this.e5 = e6;
		
	}
	
	public Vector6D(Vector6D other) {
		
		this.e0 = other.e0;
		this.e1 = other.e1;
		this.e2 = other.e2;
		this.e3 = other.e3;
		this.e4 = other.e4;
		this.e5 = other.e5;
		
	}
	
	public Vector6D add(Vector6D other) {
		
		this.e0 += other.e0;
		this.e1 += other.e1;
		this.e2 += other.e2;
		this.e3 += other.e3;
		this.e4 += other.e4;
		this.e5 += other.e5;
		
		return this;
	}
	
	public Vector6D subtract(Vector6D other) {
		
		this.e0 -= other.e0;
		this.e1 -= other.e1;
		this.e2 -= other.e2;
		this.e3 -= other.e3;
		this.e4 -= other.e4;
		this.e5 -= other.e5;
		
		return this;
	}
	
	public Vector6D set(Vector6D other) {
		
		this.e0 = other.e0;
		this.e1 = other.e1;
		this.e2 = other.e2;
		this.e3 = other.e3;
		this.e4 = other.e4;
		this.e5 = other.e5;
		
		return this;
	}
	
	public Vector6D set(float e1, float e2, float e3, float e4, float e5, float e6) {
		
		this.e0 = e1;
		this.e1 = e2;
		this.e2 = e3;
		this.e3 = e4;
		this.e4 = e5;
		this.e5 = e6;
		
		return this;
	}
	
	public Vector6D multiplyScalar(float scalar) {
		
		this.e0 *= scalar;
		this.e1 *= scalar;
		this.e2 *= scalar;
		this.e3 *= scalar;
		this.e4 *= scalar;
		this.e5 *= scalar;
		
		return this;
	}
	
	public Vector6D multiplyVector(Vector6D other) {
		
		this.e0 *= other.e0;
		this.e1 *= other.e1;
		this.e2 *= other.e2;
		this.e3 *= other.e3;
		this.e4 *= other.e4;
		this.e5 *= other.e5;
		
		return this;
	}
	
	public float dotProduct(Vector6D other) {
		
		return this.e0 * other.e0 + 
			   this.e1 * other.e1 +
			   this.e2 * other.e2 +
			   this.e3 * other.e3 +
			   this.e4 * other.e4 +
			   this.e5 * other.e5;
	}
	
	public Vector6D copy() {
		return new Vector6D(this.e0, this.e1, this.e2, this.e3, this.e4, this.e5);
	}
	
	public static float dotProduct(Vector6D vector1, Vector6D vector2) {
		
		return vector1.e0 * vector2.e0 + 
			   vector1.e1 * vector2.e1 +
		       vector1.e2 * vector2.e2 +
			   vector1.e3 * vector2.e3 +
			   vector1.e4 * vector2.e4 +
			   vector1.e5 * vector2.e5;
	}
	
}
