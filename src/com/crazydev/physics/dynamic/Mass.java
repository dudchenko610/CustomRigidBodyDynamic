package com.crazydev.physics.dynamic;

public class Mass {
	
	public float mass;
	public float invMass;
	
	public float inertia;
	public float invInertia;
	
	/** INFINITY MASS **/
	public Mass() {
		this.mass       = Float.POSITIVE_INFINITY;
		this.invMass    = 0;
		
		this.inertia    = Float.POSITIVE_INFINITY;
		this.invInertia = 0;
	}
	
	/** ARBITRARY MASS **/
	public Mass(float mass, float inertia) {
		
		if (mass == 0) {
			this.mass    = Float.POSITIVE_INFINITY;
			this.invMass = 0;
		} else {
			this.mass    = mass;
			this.invMass = 1 / mass;
		}
		
		if (inertia == 0) {
			this.inertia = Float.POSITIVE_INFINITY;
			this.invMass = 0;
		} else {
			this.inertia    = inertia;
			this.invInertia = 1 / inertia;
		}
		
		
		
	}
}
