package com.crazydev.opengl.rendering;

import com.crazydev.opengl.VertexBatcher2D;

public abstract class Brush {
	
	protected VertexBatcher2D vertexBatcher2D;
	protected float[] vertices;
	protected short[] indices;
	
	public Brush(VertexBatcher2D vertexBatcher2D) {
		this.vertexBatcher2D = vertexBatcher2D;
	}
	
	public abstract void draw(float deltaTime);
	public abstract int getElementsPerVertex();
	public abstract void setup(float[] vertices, short[] indices);
	
}
