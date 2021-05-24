package com.crazydev.physics.dynamic;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Vector2D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.physics.Body;

public abstract class Force {
	
	public abstract void apply(Body body, Matrix2x2 rotMatrix, float dt);
	public abstract void updateApplicationPoint(Matrix2x2 rotMatrix, Vector2D position);
	public abstract void depictForce(VertexBatcher2D vertexBatcher2D);
	
}
