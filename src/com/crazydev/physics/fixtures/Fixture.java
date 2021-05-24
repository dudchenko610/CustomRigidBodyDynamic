package com.crazydev.physics.fixtures;

import com.crazydev.math.Rectangle;
import com.crazydev.opengl.rendering.Brush;
import com.crazydev.physics.dynamic.Transform;

public abstract class Fixture {
	
	protected Brush brush;
	
	protected float[] vertices;
	protected short[] indices;
	
	protected int elementsPerVertex;
	
	protected Rectangle AABB = new Rectangle();
	
	public enum FixtureType {
		POLYGON,
		CIRCLE
	};
	
	public FixtureType type;
	
	public Fixture (Brush brush) {
		this.brush = brush;
	}
	
	public abstract void draw(Transform transform, float deltaTime);
	
	public Rectangle getAABB() {
		return this.AABB;
	}
}
