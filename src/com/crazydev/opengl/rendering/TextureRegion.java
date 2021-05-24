package com.crazydev.opengl.rendering;

import com.crazydev.math.Vector2D;
import com.crazydev.opengl.Texture;

public class TextureRegion {
	
	private Vector2D [] textureVerts;
	private Vector2D center;
	
	public TextureRegion (Texture texture, Vector2D ... pointsOnTexture) {
		this.textureVerts = new Vector2D[pointsOnTexture.length + 1];
		
		for (int i = 0; i < pointsOnTexture.length; i ++) {
			textureVerts[i + 1] = new Vector2D();
			textureVerts[i + 1].set(pointsOnTexture[i].x / texture.width, pointsOnTexture[i].y / texture.height);
		}
		
		float minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
		float minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
		
		for (int i = 0; i < pointsOnTexture.length; i ++) {
			float x =  textureVerts[i + 1].x;
			float y =  textureVerts[i + 1].y;
			
			if (x < minX) {
				minX = x;
			}
			
			if (x > maxX) {
				maxX = x;
			}
			
			if (y < minY) {
				minY = y;
			}
			
			if (y > maxY) {
				maxY = y;
			}
		}
		
		float posX = (minX + maxX) / 2.0f;
		float posY = (minY + maxY) / 2.0f;
		
		this.center          = new Vector2D(posX, posY);
		this.textureVerts[0] = new Vector2D(posX, posY);
		
	
		
		
	}
	
	public Vector2D[] getVertices() {
		return this.textureVerts;
	}
	
	public Vector2D getCenter() {
		return this.center;
	}
	
}
