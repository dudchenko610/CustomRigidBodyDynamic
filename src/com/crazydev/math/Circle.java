package com.crazydev.math;

public class Circle {
	
	public Vector2D center = new Vector2D();
	public float radius;
	
	public Circle (float x, float y, float radius) {
		this.center.set(x, y);
		this.radius = radius;
	}
	
	public Circle (Vector2D center, float radius) {
		this.center.set(center);
		this.radius = radius;
	}
	
	public Circle (float radius) {
		this.center.set(0, 0);
		this.radius = radius;
	}
	
}
