package com.crazydev.math;

public class Rectangle {
	
	public Vector2D [] corners = new Vector2D[4];
	
	public Rectangle() {
		
		for (int i = 0; i < 4; i ++) {
			corners[i] = new Vector2D();
		}
	}
	
	public Rectangle(Vector2D lowerLeft, Vector2D lowerRight, Vector2D upperRight, Vector2D upperLeft) {
		this();
		
		corners[0].set(lowerLeft);
		corners[1].set(lowerRight);
		corners[2].set(upperRight);
		corners[3].set(upperLeft);
	}
	
	public Rectangle(Vector2D center, float width, float height) {
		this();
		
		float halfWidth  = width / 2;
		float halfHeight = height / 2;
		
		corners[0].set(center).add(-halfWidth, -halfHeight);
		corners[1].set(center).add( halfWidth, -halfHeight);
		corners[2].set(center).add( halfWidth,  halfHeight);
		corners[3].set(center).add(-halfWidth,  halfHeight);
		
	}
	
	public void move(Vector2D deltaL) {
		for (int i = 0; i < 4; i ++) {
			corners[i].add(deltaL);
		}
	}
	
	public void set(float x, float y) {
		for (int i = 0; i < 4; i ++) {
			corners[i].add(x - corners[i].x, y - corners[i].y);
		}
	}
	
	public void set(Rectangle rectangle) {
		this.corners[0].set(rectangle.corners[0]);
		this.corners[1].set(rectangle.corners[1]);
		this.corners[2].set(rectangle.corners[2]);
		this.corners[3].set(rectangle.corners[3]);
	}
	
	public void setupAABB(float minX, float maxX, float minY, float maxY) {
		corners[0].set(minX, minY);
		corners[1].set(maxX, minY);
		corners[2].set(maxX, maxY);
		corners[3].set(minX, maxY);
	}
	
	public void setupAABB(Vector2D center, float width, float height) {
		float halfWidth  = (float) width / 2;
		float halfHeight = (float) height / 2;
		
		corners[0].set(center).add(-halfWidth, -halfHeight);
		corners[1].set(center).add( halfWidth, -halfHeight);
		corners[2].set(center).add( halfWidth,  halfHeight);
		corners[3].set(center).add(-halfWidth,  halfHeight);
	}
	
	@Override
	public String toString() {
		return "RECTANGLE: P0: " + corners[0].toString() + 
						 " P1: " + corners[1].toString() +
						 " P2: " + corners[2].toString() +
						 " P3: " + corners[3].toString();
	}
	
	
}
