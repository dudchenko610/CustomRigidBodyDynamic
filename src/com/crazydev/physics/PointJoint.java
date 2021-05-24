package com.crazydev.physics;

import com.crazydev.math.Matrix2x2;
import com.crazydev.math.Polygon;
import com.crazydev.math.Vector6D;
import com.crazydev.math.Vector2D;
import com.crazydev.physics.fixtures.Fixture;
import com.crazydev.physics.fixtures.Fixture.FixtureType;
import com.crazydev.physics.fixtures.PolygonFixture;

public class PointJoint extends Joint {
	
	public Vector2D initialVertexA;
	public Vector2D initialVertexB;
	
	public Vector2D vertexA;
	public Vector2D vertexB;
	
	private float beta = 0.33f;
	
	public PointJoint(Body bodyA, Body bodyB, Vector2D vertexA, Vector2D vertexB) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
		
		this.initialVertexA = vertexA.copy();
		this.initialVertexB = vertexB.copy();
		
		this.vertexA = vertexA.copy();
		this.vertexB = vertexA.copy();
		
	}
	
	public PointJoint(Body bodyA, Body bodyB, int vertexA, int vertexB) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
	
		Fixture fixtureA = bodyA.getFixture();
		Fixture fixtureB = bodyB.getFixture();
		
		if (fixtureA.type == FixtureType.POLYGON && fixtureB.type == FixtureType.POLYGON && vertexA > -1 & vertexB > -1) {
			Polygon polygonA = ((PolygonFixture) fixtureA).getInitialPolygon();
			Polygon polygonB = ((PolygonFixture) fixtureB).getInitialPolygon();
			
			this.initialVertexA = polygonA.points[vertexA];
			this.initialVertexB = polygonB.points[vertexB];
			
			this.vertexA = initialVertexA.copy();
			this.vertexB = initialVertexB.copy();
			
		}
		
	}

	@Override
	public void precomputeConstants() {
		
		Matrix2x2 rotMatrix;
		
		Vector2D pA;
		Vector2D cA;
		
		Vector2D pB;
		Vector2D cB;
		
		rotMatrix = bodyA.transform.getRotationMatrix();
		this.vertexA.set(this.initialVertexA).multiplyMatrix(rotMatrix).add(bodyA.transform.position);
		
		rotMatrix = bodyB.transform.getRotationMatrix();
		this.vertexB.set(this.initialVertexB).multiplyMatrix(rotMatrix).add(bodyB.transform.position);
		
		// precompute mass, Jacobian and bias
		
		this.massMatrix.set(bodyB.mass.invMass, bodyB.mass.invMass,
							 bodyB.mass.invInertia,
							 bodyA.mass.invMass, bodyA.mass.invMass,
							 bodyA.mass.invInertia);
		
		cA = bodyA.transform.position;
		pA = this.vertexA;
		
		cB = bodyB.transform.position;
		pB = this.vertexB;
		
		// compute the Jacobians (they don't change in the iterations)
		
		pB_sub_pA.set(pB).subtract(pA);
		pA_sub_pB.set(pA).subtract(pB);
		
		pB_sub_cB.set(pB).subtract(cB);
		pA_sub_cA.set(pA).subtract(cA);
		
		this.jacobianMatrix.set(pB_sub_pA.x, pB_sub_pA.y, Vector2D.crossProduct(pA_sub_pB, pB_sub_cB),
								pA_sub_pB.x, pA_sub_pB.y, Vector2D.crossProduct(pB_sub_pA, pA_sub_cA));
		
		this.jacobianMatrix.multiplyScalar(2.0f);
		
		this.inv_mass_X_jacobian.set(this.massMatrix).multiplyVector(this.jacobianMatrix);
		
		float C            = Vector2D.dotProduct(pA_sub_pB, pA_sub_pB);
		this.bias          = this.beta / World.DT * C;
		this.effectiveMass = 1.0f / (Vector6D.dotProduct(this.jacobianMatrix, this.inv_mass_X_jacobian));
	}

	@Override
	public void solveConstraint() {
		this.velocity.set(bodyB.linearVelocity.x, bodyB.linearVelocity.y,
				   bodyB.angularVelocity,
				   bodyA.linearVelocity.x, bodyA.linearVelocity.y,
				   bodyA.angularVelocity);


		if (Math.abs(this.effectiveMass) >= 1e+7) {
			return;
		}

		float lambda = -(Vector6D.dotProduct(this.jacobianMatrix, this.velocity) + this.bias) * this.effectiveMass;

		v2.set(this.jacobianMatrix).multiplyScalar(lambda);
		v1.set(this.massMatrix).multiplyVector(v2);

		this.velocity.add(v1);

		bodyB.linearVelocity.set(this.velocity.e0, this.velocity.e1);
		bodyB.angularVelocity = this.velocity.e2;

		bodyA.linearVelocity.set(this.velocity.e3, this.velocity.e4);
		bodyA.angularVelocity = this.velocity.e5;
		
	}
	
}
