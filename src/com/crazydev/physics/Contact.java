package com.crazydev.physics;

import java.util.ArrayList;

import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.math.Vector6D;

public class Contact extends Constraint {
	
	public int hash_id = 0;
	
	public Vector2D normal = new Vector2D();  // From A to B
	public Vector2D pointA = new Vector2D();
	public Vector2D pointB = new Vector2D();
	
	public float penetration;
	public float lambdaAccumulated;
	
	public Vector6D jacobianMatrixTangent 	   = new Vector6D();
	public Vector2D tangent 			       = new Vector2D();
	public Vector6D inv_mass_X_jacobianTangent = new Vector6D();
	public float    effectiveMassTangent;
	
	@Override
	public boolean equals(Object object) {
		return ((Contact) object).hash_id == this.hash_id;
	}
	
	@Override
	public int hashCode() {
		return this.hash_id;
	}

	@Override
	public void precomputeConstants() {
		
		this.massMatrix.set(bodyA.mass.invMass, bodyA.mass.invMass, bodyA.mass.invInertia,
							   bodyB.mass.invMass, bodyB.mass.invMass, bodyB.mass.invInertia);
		
		pA_sub_cA.set(this.pointA).subtract(bodyA.transform.position);
		pB_sub_cB.set(this.pointB).subtract(bodyB.transform.position);
		
		this.jacobianMatrix.set( normal.x, normal.y,                      	// Jn_v_linear_A
								 Vector2D.crossProduct(pA_sub_cA, normal),  // Jn_v_ang_A
								-normal.x, -normal.y, 					    // Jn_v_linear_B
								-Vector2D.crossProduct(pB_sub_cB, normal)); // Jn_v_ang_B
		
		this.tangent.set(-this.normal.y, this.normal.x);
		
		this.jacobianMatrixTangent.set( tangent.x, tangent.y,                      	// Jt_v_linear_A
				 						Vector2D.crossProduct(pA_sub_cA, tangent),  // Jt_v_ang_A
				 					   -tangent.x, -tangent.y, 					    // Jt_v_linear_B
				 					   -Vector2D.crossProduct(pB_sub_cB, tangent));	// Jt_v_ang_B
		
		this.inv_mass_X_jacobianTangent.set(this.massMatrix).multiplyVector(this.jacobianMatrixTangent);
		this.effectiveMassTangent = 1.0f /  Vector6D.dotProduct(this.jacobianMatrixTangent, this.inv_mass_X_jacobianTangent);
		
		
		pA_sub_pB.set(this.pointA).subtract(this.pointB);
		
		float C = Vector2D.dotProduct(pA_sub_pB, normal);
		float vPerNormal = 0;
		
		this.inv_mass_X_jacobian.set(this.massMatrix).multiplyVector(this.jacobianMatrix);
		
		this.bias              = ( 0.5f) / World.DT * ((C < 0) ? C : 0) + 0.2f * vPerNormal;
		this.lambdaAccumulated = 0;
		this.effectiveMass     = 1.0f / Vector6D.dotProduct(this.jacobianMatrix, this.inv_mass_X_jacobian);
		
	}

	@Override
	public void solveConstraint() {
		
		this.velocity.set(bodyA.linearVelocity.x, bodyA.linearVelocity.y, bodyA.angularVelocity,
							 bodyB.linearVelocity.x, bodyB.linearVelocity.y, bodyB.angularVelocity);
		
		
		if (Math.abs(this.effectiveMass) >= 1e+50) {
			return;
		}
		
		float lambda = -(Vector6D.dotProduct(this.jacobianMatrix, this.velocity) + this.bias) * this.effectiveMass;
		
		// clamp accumulated impulse to 0
		if (this.lambdaAccumulated + lambda < 0) {
			lambda = -this.lambdaAccumulated;
		}
		
		this.lambdaAccumulated += lambda;
		
		v2.set(this.jacobianMatrix).multiplyScalar(lambda);
		v1.set(this.massMatrix).multiplyVector(v2);
		
		this.velocity.add(v1);
		
		bodyA.linearVelocity.set(this.velocity.e0, this.velocity.e1);
		bodyA.angularVelocity = this.velocity.e2;
		
		bodyB.linearVelocity.set(this.velocity.e3, this.velocity.e4);
		bodyB.angularVelocity = this.velocity.e5;

		// friction stuff
		
		if (Math.abs(this.effectiveMassTangent) >= 1e+50) {
			return;
		}
		
		float lambdaFriction = -(Vector6D.dotProduct(this.jacobianMatrixTangent, this.velocity) + 0 * this.bias) * this.effectiveMassTangent;
		
		if (lambdaFriction > World.FRICTION * lambda) {
			lambdaFriction = World.FRICTION * lambda;
		} else if (lambdaFriction < -World.FRICTION * lambda) {
			 lambdaFriction = -World.FRICTION * lambda;
		}
		
		v2.set(this.jacobianMatrixTangent).multiplyScalar(lambdaFriction);
		v1.set(this.massMatrix).multiplyVector(v2);
		
		this.velocity.add(v1);
		
		bodyA.linearVelocity.set(this.velocity.e0, this.velocity.e1);
		bodyA.angularVelocity = this.velocity.e2;
		
		bodyB.linearVelocity.set(this.velocity.e3, this.velocity.e4);
		bodyB.angularVelocity = this.velocity.e5;
		
	}
}
