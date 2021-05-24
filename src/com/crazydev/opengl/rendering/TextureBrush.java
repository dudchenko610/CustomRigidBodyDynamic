package com.crazydev.opengl.rendering;

import com.crazydev.math.Vector2D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.opengl.VertexBinder2D;

import android.util.Log;

public class TextureBrush extends Brush {

	private Animation animation;
	
	public TextureBrush(VertexBatcher2D vertexBatcher2D, Animation animation) {
		super(vertexBatcher2D);
		
		this.animation = animation;
		
	}

	@Override
	public void setup(float[] vertices, short[] indices) {
		this.vertices = vertices;
		this.indices  = indices;
		
		// setTexture
		
		int elementsPerVertex = this.getElementsPerVertex();
		int len = this.vertices.length / elementsPerVertex;
		
		TextureRegion mainFrame   = this.animation.getMainFrame();
		Vector2D[] textureVertices = mainFrame.getVertices();
		
		for (int i = 0; i < len; i ++) {
			vertices[i * elementsPerVertex + 2] = (float) textureVertices[i].x;
			vertices[i * elementsPerVertex + 3] = (float) textureVertices[i].y;
			
			Log.d("drawing", "x " + textureVertices[i].x + " y = " + textureVertices[i].y);
		}
		
	}
	
	@Override
	public void draw(float deltaTime) {
		this.vertexBatcher2D.addVerticesTexturedToOpenGL(vertices, indices);
	}

	@Override
	public int getElementsPerVertex() {
		return VertexBinder2D.STRIDE_TEXTURED;
	}


}
