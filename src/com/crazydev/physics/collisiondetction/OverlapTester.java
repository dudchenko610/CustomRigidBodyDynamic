package com.crazydev.physics.collisiondetction;

import com.crazydev.math.Circle;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.physics.Body;
import com.crazydev.physics.Contact;
import com.crazydev.physics.collisiondetction.kdtree.KDTreeBuilder;
import com.crazydev.physics.fixtures.CircleFixture;
import com.crazydev.physics.fixtures.Fixture;
import com.crazydev.physics.fixtures.PolygonFixture;

import android.text.InputFilter.LengthFilter;
import android.util.Log;

public class OverlapTester {
	
	
	private Fixture fixture1;
	private Fixture fixture2;
	
	public Contact overlapBodies(Contact contact, Body body1, Body body2) {
		
		fixture1 = body1.getFixture();
		fixture2 = body2.getFixture();
		
		switch (fixture1.type) {
		case POLYGON:
			
			switch (fixture2.type) {
			case POLYGON:
				return overlapPolygonPolygon(contact, body1, body2);
			case CIRCLE:
				return overlapPolygonCircle(contact, body1, body2);
			}
			break;
		case CIRCLE:
			
			switch (fixture2.type) {
			case POLYGON:
				return overlapPolygonCircle(contact, body2, body1);
			case CIRCLE:
				return overlapCircleCircle(contact, body1, body2);
			}
			
			
		}
		
		return null;
	}
	
	private Vector2D normal  = new Vector2D();
	private Vector2D point   = new Vector2D();
	
	private Vector2D sPoint1 = new Vector2D();
	private Vector2D sPoint2 = new Vector2D();
	
	private Vector2D ssPoint1 = new Vector2D();
	private Vector2D ssPoint2 = new Vector2D();
	
	private Vector2D sNormal1 = new Vector2D();
	private Vector2D sNormal2 = new Vector2D();
	
	private Vector2D ssNormal1 = new Vector2D();
	private Vector2D ssNormal2 = new Vector2D();
	
	float firstMin = Integer.MAX_VALUE;
	float seconMin = Integer.MAX_VALUE;
	
	public Contact overlapPolygonPolygon(Contact contact, Body b1, Body b2) {
		
		contact.pointA.set(0, 0);
		contact.pointB.set(0, 0);
		contact.penetration = 1000000.0f;
		
		PolygonFixture p1 = (PolygonFixture) b1.getFixture();
		PolygonFixture p2 = (PolygonFixture) b2.getFixture();
		
		Vector2D [] v1 = p1.getVertices();
		Vector2D [] v2 = p2.getVertices();
		
		float penetration = 0;

		this.firstMin = Integer.MAX_VALUE;
		this.seconMin = Integer.MAX_VALUE;
		
		for (int i = 0; i < v1.length; i ++) {
			this.normal.set(v1[ (i + 1) % v1.length]).subtract(v1[i]);
			this.normal.turnClockWise();
			this.normal.normalize();
			
			// find support on another body
			
			boolean b = true;
			float minPenetration1 = Integer.MAX_VALUE;
			
			for (int j = 0; j < v2.length; j ++) {
				this.point.set(v2[j]).subtract(v1[i]);
				
				penetration = Vector2D.dotProduct(this.point, this.normal);
				
				if (penetration < 0) {
					b = false;
					
					if (penetration < minPenetration1) {
						
						minPenetration1 = penetration;
						this.sPoint1.set(v2[j]);
						
						this.sNormal1.set(this.normal);
					
					}
				}
				
				
			}
			
			minPenetration1 = -minPenetration1;
			
			if (b) {
				return null;
			} else {
				if (minPenetration1 < this.firstMin) {
					this.firstMin = minPenetration1;
					this.ssPoint1.set(this.sPoint1);
					this.ssNormal1.set(this.sNormal1);
					
				}
			}
			
			
			minPenetration1 = Integer.MAX_VALUE;
			
		}
		
		for (int i = 0; i < v2.length; i ++) {
			this.normal.set(v2[ (i + 1) % v2.length]).subtract(v2[i]);
			this.normal.turnClockWise();
			this.normal.normalize();
			
			boolean b = true;
			float minPenetration2 = Integer.MAX_VALUE;
			
			for (int j = 0; j < v1.length; j ++) {
				this.point.set(v1[j]).subtract(v2[i]);
				
				penetration = Vector2D.dotProduct(this.point, this.normal);
				
				if (penetration < 0) {
					b = false;
					
					if (penetration < minPenetration2) {
						
						minPenetration2 = penetration;
						this.sPoint2.set(v1[j]);
						this.sNormal2.set(this.normal);
						
					}
				}
				
				
			}
			
			
			minPenetration2 = - minPenetration2;
			
			if (b) {
				return null;
			} else {
				if (minPenetration2 < this.seconMin) {
					this.seconMin = minPenetration2;
					this.ssPoint2.set(this.sPoint2);
					this.ssNormal2.set(this.sNormal2);
					
				}
			}
			
			minPenetration2 = Integer.MAX_VALUE;
		
		}

	
		if (this.firstMin < this.seconMin) {
			contact.bodyB = b1;
			contact.bodyA = b2;
			contact.normal.set(this.ssNormal1);
			contact.pointA.set(this.ssPoint1);
			contact.pointB.set(this.ssNormal1).multiplyScalar(this.firstMin).add(this.ssPoint1);
			contact.penetration = this.firstMin;
			
			/*this.ssPoint2.set(contact.pointB).subtract(contact.pointA);
			
			if (Vector2D.dotProduct(this.ssPoint2, contact.normal) < 0) {
				contact.normal.multiplyScalar(-1);
			}*/
			
		} else { 
			
			contact.bodyB = b2;
			contact.bodyA = b1;
			contact.pointB.set(this.ssPoint2);
			contact.pointA.set(this.ssNormal2).multiplyScalar(this.seconMin).add(this.ssPoint2);
			contact.normal.set(this.ssNormal2);
			contact.penetration = this.seconMin;
			
		/*	this.ssPoint2.set(contact.pointB).subtract(contact.pointA);
			
			if (Vector2D.dotProduct(this.ssPoint2, contact.normal) < 0) {
				contact.normal.multiplyScalar(-1);
			}*/
				
		}
			
		return contact;
		
	}
	
	private Vector2D v1 = new Vector2D();

	public Contact overlapCircleCircle(Contact contact, Body b1, Body b2) {
		
		CircleFixture p1 = (CircleFixture) b1.getFixture();
		CircleFixture p2 = (CircleFixture) b2.getFixture();
		
		Circle c1 = p1.getCircle();
		Circle c2 = p2.getCircle();
		
		float squaredLength = v1.set(c1.center).subtract(c2.center).lengthSquared();
		float squaredRadius = c1.radius + c2.radius;
		squaredRadius *= squaredRadius;
		
		if (squaredLength < squaredRadius) {
			v1.normalize();
			contact.normal.set(v1);
			contact.pointB.set(v1).multiplyScalar(+c2.radius).add(c2.center);
			contact.pointA.set(v1).multiplyScalar(-c1.radius).add(c1.center);
			
			contact.bodyA = b1;
			contact.bodyB = b2;
			
			contact.penetration = c1.radius + c2.radius - (float) Math.sqrt(squaredLength);
			
			return contact;
		}
		
		return null;
	}
	
	
	private Vector2D _AB = new Vector2D();
	private Vector2D _BA = new Vector2D();
	private Vector2D _AC = new Vector2D();
	private Vector2D _BC = new Vector2D();
	private Vector2D vC  = new Vector2D();
	private Vector2D vN  = new Vector2D();
	
	public Contact overlapPolygonCircle(Contact contact, Body b1, Body b2) {
		
		PolygonFixture f1 = (PolygonFixture) b1.getFixture();
		Vector2D[] v 	  = f1.getVertices();
		
		CircleFixture f2  = (CircleFixture) b2.getFixture();
		Circle circle     = f2.getCircle();
		
		float dot;
		float distanceSquared;
		float radiusSquared = circle.radius * circle.radius;;
		boolean inside = true;
		
		// in case of D minimal center penetration
		float projection;
		float bestDistance = Integer.MIN_VALUE;
		int indexVertex = -1;
		
		for (int i = 0; i < v.length; i++) {
			this.vC.set(v[i]);
			this.vN.set(v[ (i + 1) % v.length]);
			
			this._AC.set(circle.center).subtract(this.vC);
			
			this._AB.set(this.vN).subtract(this.vC);
			this._BA.set(this._AB).multiplyScalar(-1);
			
			this.normal.set(this._AB);
			this.normal.turnClockWise();
			this.normal.normalize();
			
			projection = Vector2D.dotProduct(this._AC, this.normal);
			
			if (projection > 0) {
				bestDistance = projection;
				indexVertex  = i;
				inside       = false;
			}
			
			// if projection less than zero than circle's center is inside of rectangle
			
			// actually in this case more means less :))
			if (inside && projection > bestDistance) {
				bestDistance = projection;
				indexVertex  = i;
			}
			
		}
		
		if (indexVertex == -1) {
		//	Log.d("blaad", "--------- 1");
			
			return null;
		}
		
		
		if (inside) {
		//	Log.d("blaad", "region inside");
			
			// weak inside this block
			this.vC.set(v[indexVertex]);
			
			this._AC.set(circle.center).subtract(this.vC);
			
			contact.bodyA = b1;
			contact.bodyB = b2;
			
			contact.normal.set(this._AC).normalize().multiplyScalar(-1);
			contact.normal.turnClockWise();
			
			contact.pointA.set(contact.normal).multiplyScalar(circle.radius).add(circle.center);
			contact.pointB.set(this.vC);
			
			return contact;
			
		} else {
			this.vC.set(v[indexVertex]);
			this.vN.set(v[ (indexVertex + 1) % v.length]);
			
		//	KDTreeBuilder.vertexBatcher2D.addLine(vC, vN, new Vector3D(1, 0, 0));
			
			this._AC.set(circle.center).subtract(this.vC);
			this._BC.set(circle.center).subtract(this.vN);
			
			this._AB.set(this.vN).subtract(this.vC);
			this._BA.set(this._AB).multiplyScalar(-1);
			
			this.normal.set(this._AB);
			this.normal.turnClockWise();
			this.normal.normalize();
			
			dot = Vector2D.dotProduct(this._AC, this._AB);
			
			if (dot < 0) {
				// region A
				contact.bodyA = b1;
				contact.bodyB = b2;
				
				contact.normal.set(this._AC).multiplyScalar(-1).normalize();
				contact.pointB.set(contact.normal).multiplyScalar(circle.radius).add(circle.center);
				contact.pointA.set(vC);
				
			//	Log.d("blaad", "region A");
				
				distanceSquared = this._AC.lengthSquared();
				if (distanceSquared < radiusSquared) {
					// collision
						
					contact.bodyA = b1;
					contact.bodyB = b2;
						
					contact.normal.set(this._AC).multiplyScalar(-1).normalize();
					contact.pointB.set(contact.normal).multiplyScalar(circle.radius).add(circle.center);
					contact.pointA.set(vC);
						
					return contact;
				}
					
			} else {
				
				
				dot = Vector2D.dotProduct(this._BC, this._BA);
					
				if (dot < 0) {
					// region C
					
			//		Log.d("blaad", "region C");
						
					distanceSquared = this._BC.lengthSquared();
					if (distanceSquared < radiusSquared) {
						// collision
							
						contact.bodyA = b1;
						contact.bodyB = b2;
							
						contact.normal.set(this._BC).multiplyScalar(-1).normalize();
						contact.pointB.set(contact.normal).multiplyScalar(circle.radius).add(circle.center);
						contact.pointA.set(vN);
							
						return contact;
					}
						
				} else {
					// region B
					
				//	Log.d("blaad", "region B");
					
				//	dot = Vector2D.dotProduct(this._AC, this.normal);
					
					if (bestDistance < circle.radius) {
						
						contact.bodyA = b2;
						contact.bodyB = b1;
							
						contact.normal.set(this.normal);
						contact.pointA.set(contact.normal).multiplyScalar(-circle.radius).add(circle.center);
							
						this._AB.normalize();
						this._AB.multiplyScalar(Vector2D.dotProduct(this._AB, this._AC));
							
						contact.pointB.set(this.vC).add(this._AB);
							
						return contact;
							
					}
						
				}
			}
			
		}
		

		return null;
	}
	
	
}
