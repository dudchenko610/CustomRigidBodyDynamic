package com.crazydev.collisiondetection;

import com.crazydev.math.Circle;
import com.crazydev.math.Rectangle;
import com.crazydev.math.Vector2D;

import android.util.Log;

public class OverlapTester {


	public static boolean overlapCircles(Circle c1, Circle c2) {
		float distance = c1.center.distanceSquared(c2.center);
		float radiusSum = c1.radius + c2.radius;
		return distance  <= radiusSum * radiusSum;
	}
	
	public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
		
		if (r1.corners[0].x < r2.corners[1].x &&
			r2.corners[0].x < r1.corners[1].x &&
			r1.corners[0].y < r2.corners[3].y  &&
			r2.corners[0].y < r1.corners[3].y) {
			
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean pointInRectangle(Rectangle r, Vector2D p) {
		return r.corners[0].x <= p.x && r.corners[1].x >= p.x &&
			   r.corners[0].y <= p.y && r.corners[3].y >= p.y;  
	}
	
	public static boolean pointInRectangle(float[] points, Vector2D p) {
		
		if (points.length != 8) {
			return false;
		}
		
		return points[0] <= p.x && points[2] >= p.x &&
			   points[1] <= p.y && points[7] >= p.y;  
	}
	
	
	
	public static boolean pointInCircle(Circle c, Vector2D p) {
		return c.center.distanceSquared(p) < c.radius * c.radius;
	}
	
	public static boolean pointInCircle(Circle c, float x, float y) {
		return c.center.distanceSquared(x, y) < c.radius * c.radius;
	}
	


}
