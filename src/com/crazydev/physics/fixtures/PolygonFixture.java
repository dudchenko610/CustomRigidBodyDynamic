package com.crazydev.physics.fixtures;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Polygon;
import com.crazydev.math.Vector2D;
import com.crazydev.modeling.ShapeModeler;
import com.crazydev.opengl.rendering.Brush;
import com.crazydev.physics.dynamic.Transform;

public class PolygonFixture extends Fixture {

	private Polygon polygon;
	private Vector2D verts [];
	private Vector2D actualVerts [];
	
	public PolygonFixture(Brush brush, Polygon polygon) {
		super(brush);
		
		this.polygon = polygon;
		this.type   = Fixture.FixtureType.POLYGON;
		
		this.verts             = polygon.points;
		this.elementsPerVertex = brush.getElementsPerVertex();
		this.vertices          = new float[(this.verts.length + 1) * elementsPerVertex];
		this.indices           = ShapeModeler.computeDefaultIndexes(vertices.length / elementsPerVertex);
		this.brush.setup(vertices, indices);
		
		this.actualVerts       = new Vector2D[this.verts.length];
		for (int i = 0; i < this.actualVerts.length; i ++) {
			this.actualVerts[i] = this.verts[i].copy();
		}
		
	}

	private Vector2D currentPos = new Vector2D();
	
	@Override
	public void draw(Transform transform, float deltaTime) {
		float pos_x 			 = transform.position.x;
		float pos_y 			 = transform.position.y;
		Matrix2x2 rotationMatrix = transform.getRotationMatrix();
		
		float X;
		float Y;
		
		vertices[0] = pos_x;
		vertices[1] = pos_y;
		
		float minX = Integer.MAX_VALUE;
		float minY = Integer.MAX_VALUE;
		
		float maxX = Integer.MIN_VALUE;
		float maxY = Integer.MIN_VALUE;
		
		for (int i = 0; i < verts.length; i ++) {
			this.currentPos.set((float) verts[i].x, (float) verts[i].y);
			
			this.currentPos.multiplyMatrix(rotationMatrix).add(pos_x, pos_y);
			
			X = this.currentPos.x;
			Y = this.currentPos.y;
			
			if (X < minX) {
				minX = X;
			} 

			if (X > maxX) {
				maxX = X;
			}
			
			if (Y < minY) {
				minY = Y;
			} 
			
			if (Y > maxY) {
				maxY = Y;
			}
			
			vertices[(i + 1) * elementsPerVertex + 0] = X;
			vertices[(i + 1) * elementsPerVertex + 1] = Y;
			
			this.actualVerts[i].set(X, Y);
			
		}
		
		this.AABB.setupAABB(minX, maxX, minY, maxY);
		
		brush.draw(deltaTime);
		
	}
	
	public Polygon getInitialPolygon() {
		return this.polygon;
	}
	
	public Vector2D[] getVertices() {
		return this.actualVerts;
	}
	
}
