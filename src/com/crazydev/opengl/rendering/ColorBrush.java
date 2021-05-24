package com.crazydev.opengl.rendering;

import java.util.Random;

import com.crazydev.math.Vector3D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.opengl.VertexBinder2D;

public class ColorBrush extends Brush {

	private Vector3D color;
	private float alpha;
	
	public ColorBrush(VertexBatcher2D vertexBatcher2D, Vector3D color, float alpha) {
		super(vertexBatcher2D);
		
		this.color = color;
		this.alpha = alpha;
		
	}

	@Override
	public void setup(float[] vertices, short[] indices) {
		this.vertices = vertices;
		this.indices  = indices;
		
		// setColor
		int elementsPerVertex = this.getElementsPerVertex();
		int len = this.vertices.length / elementsPerVertex;
		
		for (int i = 0; i < len; i ++) {
			
			vertices[i * elementsPerVertex + 2] = this.color.x;
			vertices[i * elementsPerVertex + 3] = this.color.y;
			vertices[i * elementsPerVertex + 4] = this.color.z;
			vertices[i * elementsPerVertex + 5] = alpha;
			
		}
	}
	
	@Override
	public void draw(float deltaTime) {
		this.vertexBatcher2D.addVerticesColoredToOpenGL(vertices, indices);
	}

	@Override
	public int getElementsPerVertex() {
		return VertexBinder2D.STRIDE_COLORED;
	}


}
