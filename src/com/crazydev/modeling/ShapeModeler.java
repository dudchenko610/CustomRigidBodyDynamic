package com.crazydev.modeling;


import com.crazydev.math.Circle;
import com.crazydev.math.Polygon;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.opengl.Texture;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.opengl.rendering.Animation;
import com.crazydev.opengl.rendering.Brush;
import com.crazydev.opengl.rendering.ColorBrush;
import com.crazydev.opengl.rendering.TextureBrush;
import com.crazydev.opengl.rendering.TextureRegion;
import com.crazydev.physics.Body;
import com.crazydev.physics.dynamic.Mass;
import com.crazydev.physics.fixtures.CircleFixture;
import com.crazydev.physics.fixtures.Fixture;
import com.crazydev.physics.fixtures.PolygonFixture;

import android.util.Log;

public class ShapeModeler {
	
	public static Body createBodyFromTexture(Mass mass, VertexBatcher2D vertexBatcher2D, Texture texture, float xScale, float yScale, Vector2D ... texturePoints) {
		
		// 1. Form brush & main frame from points
		TextureRegion mainFrame = new TextureRegion(texture, texturePoints);
		Animation animation = new Animation(false, mainFrame);
		Brush brush = new TextureBrush(vertexBatcher2D, animation);
		
	//	Brush brush = new ColorBrush(vertexBatcher2D, new Vector3D(1, 1, 0), 1);
		
		// 2. Form fixture
		
		Vector2D[] texModPoints  = mainFrame.getVertices();
		Vector2D[] fixturePoints = new Vector2D[texturePoints.length];
		
		Vector2D pos = mainFrame.getCenter();

		for (int i = 0; i < texturePoints.length; i ++) {
			fixturePoints[i] = new Vector2D();
			
			fixturePoints[i].x = (texModPoints[i + 1].x - pos.x) * xScale;
			fixturePoints[i].y = (texModPoints[i + 1].y - pos.y) * yScale;  // y swap
			
		}
		
		Polygon polygon = new Polygon(fixturePoints);
		Fixture fixture = new PolygonFixture(brush, polygon);
		
		Body body = new Body(fixture, mass);

		return body;
	}
	
	public static Body createBodyFromTexture(Mass mass, VertexBatcher2D vertexBatcher2D, Texture texture, Vector2D position, float radiusTexture, float radiusReal) {
		
		Vector2D[] verts = new Vector2D[50];
		
		float x = 0;
		float y = 0;		
		
		float _2_PI = (float) (2 * Math.PI);
		
		for (int i = 0; i < verts.length; i ++) {
			double d = i *_2_PI / verts.length;
			
			x = (float) (radiusTexture * Math.cos(d));
			y = (float) (radiusTexture * Math.sin(d));
			
			verts[i] = new Vector2D(x + position.x, y + position.y);
		}
		
		TextureRegion mainFrame = new TextureRegion(texture, verts);
		Animation animation = new Animation(false, mainFrame);
		Brush brush = new TextureBrush(vertexBatcher2D, animation);
		
		Circle circle = new Circle(0, 0, radiusReal);
		Fixture fixture = new CircleFixture(brush, circle);
		
		Body body = new Body(fixture, mass);
		
		return body;
	}
	
	public static Body createBodyFromColor(Mass mass, VertexBatcher2D vertexBatcher2D, Vector3D color, float alpha, float radius) {
		
		Brush brush   = new ColorBrush(vertexBatcher2D, color, alpha);
		Circle circle = new Circle(radius);
		Fixture fixture = new CircleFixture(brush, circle);
		
		Body body = new Body(fixture, mass);
		
		return body;
	}
	
	public static Body createBodyFromColor(Mass mass, VertexBatcher2D vertexBatcher2D, Vector3D color, float alpha, float xScale, float yScale, Vector2D ... points) {
		Brush brush = new ColorBrush(vertexBatcher2D, color, alpha);
		
		Vector2D[] ps = new Vector2D[points.length];
		
		for (int i = 0; i < points.length; i ++) {
			ps[i] = points[i].copy();
			ps[i].x *= xScale;
			ps[i].y *= yScale;
			
		}
		
		Polygon polygon = new Polygon(ps);
		Fixture fixture = new PolygonFixture(brush, polygon);
		
		Body body = new Body(fixture, mass);
		
		return body;
	}
	
	public static short[] computeDefaultIndexes(int verticesNum) {
		int indicesAmount = verticesNum * 3 - 3;
		short [] indices = new short[indicesAmount];
		
		short j = 1;
		short k = 2;
		
		for (int i = 0; i < indices.length; i ++) {
			
			if (i == indices.length - 1) {
				indices[i] = 1;
				break;
			}
			
			if (i % 3 == 0) {
				indices[i] = 0;
				continue;
			}
			
			if (i % 3 == 1) {
				indices[i] = j ++;
				continue;
			}
			
			if (i % 3 == 2) {
				indices[i] = k ++;
				continue;
			}
			
		}
		
		return indices;
	}

}
