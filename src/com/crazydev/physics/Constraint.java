package com.crazydev.physics;

import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector6D;

public abstract class Constraint {
	
	// unchangable while iterations
	
	public Body bodyA;
	public Body bodyB;
	
	public Vector6D massMatrix          = new Vector6D();
	public Vector6D jacobianMatrix      = new Vector6D();
	public Vector6D inv_mass_X_jacobian = new Vector6D();
	
	public float  bias;
	public float  effectiveMass;
	
	// changable while iterations
	public Vector6D velocity  = new Vector6D();
	
	// helper variables
	public Vector2D pA_sub_pB = new Vector2D();
	
	public Vector2D pB_sub_cB = new Vector2D();
	public Vector2D pA_sub_cA = new Vector2D();
	
	public Vector6D v1        = new Vector6D();
	public Vector6D v2        = new Vector6D();
	
	public abstract void precomputeConstants();
	public abstract void solveConstraint();
	
}
