package com.crazydev.ui;

import java.util.ArrayList;

import com.crazydev.base.framework.Input.TouchEvent;
import com.crazydev.collisiondetection.OverlapTester;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.opengl.Texture;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.opengl.rendering.Brush;
import com.crazydev.opengl.rendering.ColorBrush;

public class UICoordinator {
	private static ArrayList<View> views = new ArrayList<View>();
	
	
	public static RectangularButton createRectangularButton(VertexBatcher2D vertexBatcher2D, float width, float height, Vector3D color, float alpha) {
		
		Brush brush = new ColorBrush(vertexBatcher2D, color, alpha);
		RectangularButton rectButton = new RectangularButton(vertexBatcher2D, brush, width, height, RectangularButton.ClickType.TYPE_RESIZE);
		
		return rectButton;
	}
	
	public static RectangularButton createRectangularButton(VertexBatcher2D vertexBatcher2D, float width, float height, Texture texture) {
		
		return null;
	}
	
	public static void addView(View view) {
		views.add(view);
	}
	
	public static void drawViews(float deltaTime) {
		for (View view : views) {
			view.draw(deltaTime);
		}
	}
	
	public static void updateViews(TouchEvent event, Vector2D realPos) {
		for (View view : views) {
			view.update(event, realPos);
			
		}
	}
	
	public static boolean isInViewArea(Vector2D realPos) {
		for (View view : views) {
			
			if (OverlapTester.pointInRectangle(((RectangularButton) view).AABB, realPos)) {
				return true;
			}		
		}
		
		return false;
	}
	
}
