package com.crazydev.physics;

import java.util.ArrayList;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Polygon;
import com.crazydev.math.Vector2D;
import com.crazydev.physics.dynamic.Transform;
import com.crazydev.physics.fixtures.Fixture;
import com.crazydev.physics.fixtures.PolygonFixture;
import com.crazydev.physics.dynamic.Force;
import com.crazydev.physics.dynamic.LinearForce;
import com.crazydev.physics.dynamic.Mass;

public class Body {
	
	public static int unique_id_counter;
	public int unique_id;
	
	protected Fixture   fixture;
	public Transform transform;
	
	public Vector2D linearVelocity;
	public float angularVelocity;
	
	public ArrayList<Force> forces;
	public Mass mass;
	
	public Body(Fixture fixture, Mass mass) {
		this.fixture        = fixture;
		this.transform      = new Transform();
		this.linearVelocity = new Vector2D();
		
		this.mass           = mass;
		this.forces         = new ArrayList<Force>();
		
		this.unique_id = Body.unique_id_counter ++;
	}
	
	public void removeForce(Force force) {
		this.forces.remove(force);
	}	
	
	public void addForce(Force force) {
		this.forces.add(force);
	}
	
	public void addForce(Vector2D force, int forcePointIndex) {
		switch(this.fixture.type) {
		case POLYGON:
			
			if (forcePointIndex < 0) {
				this.forces.add(new LinearForce(force, new Vector2D(0, 0)));
			} else {
				Polygon p = ((PolygonFixture) this.fixture).getInitialPolygon();
				this.forces.add(new LinearForce(force, p.points[forcePointIndex]));
			}
			
			break;
		case CIRCLE:
			
			this.forces.add(new LinearForce(force, new Vector2D(0, 0)));
			break;
		}
		
	}
	
	public void translate(Vector2D dL) {
		this.transform.position.add(dL);
		this.updateForceApplicationPoints();
	}
	
	public void translate(float x, float y) {
		this.transform.position.add(x, y);
		this.updateForceApplicationPoints();
	}
	
	public void rotate(float angle) {
		this.transform.angle = angle;
		this.updateForceApplicationPoints();
	}
	
	public void draw(float deltaTime) {
		this.fixture.draw(this.transform, deltaTime);
	}
	
	public Fixture getFixture() {
		return this.fixture;
	}
	
	protected void updateForceApplicationPoints() {
		
		Matrix2x2 rotMatrix = this.transform.getRotationMatrix();
		
		for (int i = 0; i < this.forces.size(); i ++) {
			this.forces.get(i).updateApplicationPoint(rotMatrix, this.transform.position);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		return ((Body) object).unique_id == this.unique_id;
	}
	
	@Override
	public int hashCode() {
		return this.unique_id;
	}

}
