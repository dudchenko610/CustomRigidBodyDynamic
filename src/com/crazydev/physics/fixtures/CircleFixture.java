package com.crazydev.physics.fixtures;

import com.crazydev.math.Circle;
import com.crazydev.math.Vector2D;
import com.crazydev.modeling.ShapeModeler;
import com.crazydev.opengl.rendering.Brush;
import com.crazydev.physics.dynamic.Transform;

import android.util.Log;

public class CircleFixture extends Fixture {

	protected Circle circle;
	
	private int verticesAmount = 50;
	private float[] verts;
	
	private static float _2_PI = (float) (2 * Math.PI);
	
	public CircleFixture(Brush brush, Circle circle) {
		super(brush);
		
		this.circle = circle;
		this.type   = Fixture.FixtureType.CIRCLE;
		
		this.elementsPerVertex = brush.getElementsPerVertex();
		this.vertices          = new float[(this.verticesAmount + 1) * elementsPerVertex];
		this.verts             = new float[(this.verticesAmount + 1) * 2];
		
		float x = 0;
		float y = 0;
		
		
		int j = 0;
		for (int i = 0; i < this.verticesAmount; i ++) {
			
			double d = i *_2_PI / verticesAmount;
			
			x = (float) (this.circle.radius * Math.cos(d));
			y = (float) (this.circle.radius * Math.sin(d));
			
			
			this.verts[j ++] = x;
			this.verts[j ++] = y;
		}
		
		this.indices = ShapeModeler.computeDefaultIndexes(vertices.length / elementsPerVertex );
		
		this.AABB.setupAABB(new Vector2D(0.0f,  0.0f), circle.radius * 2, circle.radius * 2);
		this.brush.setup(vertices, indices);
	}

	@Override
	public void draw(Transform transform, float deltaTime) {
		float pos_x = (float) transform.position.x;
		float pos_y = (float) transform.position.y;

		this.AABB.setupAABB(pos_x - circle.radius,
							pos_x + circle.radius, 
							pos_y - circle.radius, 
							pos_y + circle.radius);
		
		double angle = transform.angle;
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
		
		float x;
		float y;
		
		float X;
		float Y;
		
		vertices[0] = pos_x;
		vertices[1] = pos_y;
		
		circle.center.x = pos_x;
		circle.center.y = pos_y;
		
		int j = 0;
		
		for (int i = 0; i < this.verticesAmount; i ++) {
			
			x = this.verts[j ++];
			y = this.verts[j ++];
			
			X = x * cos - y * sin;
			Y = x * sin + y * cos;
			
			vertices[(i + 1) * elementsPerVertex + 0] = (float) X + pos_x;
			vertices[(i + 1) * elementsPerVertex + 1] = (float) Y + pos_y;
			
		}
		
		brush.draw(deltaTime);
		
	}
	
	public Circle getCircle() {
		return circle;
	}

}
