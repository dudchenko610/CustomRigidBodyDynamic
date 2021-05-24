package com.crazydev.ui;

import java.util.Arrays;

import com.crazydev.base.framework.Input.TouchEvent;
import com.crazydev.collisiondetection.OverlapTester;
import com.crazydev.math.Rectangle;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.modeling.ShapeModeler;
import com.crazydev.opengl.Camera2D;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.opengl.rendering.Brush;

import android.util.Log;

public class RectangularButton extends View {
	
	public enum ClickType {
		TYPE_RESIZE, TYPE_REDUCE_ALPHA, TYPE_ALPHA_BOUNDS
	}
	
	private ClickType clickType;
	
	private float scaleX = 1.0f;
	private float scaleY = 1.0f;
	
	protected Rectangle AABB = new Rectangle();
	
	public RectangularButton(VertexBatcher2D vertexBatcher2D, Brush brush, float width, float height, ClickType clickType) {
		super(vertexBatcher2D, brush, width, height);
		
		this.clickType = clickType;
	
		float w = width  / 2;
		float h = height / 2;
		
		this.verts             = new float [] {-w, -h, w, -h, w, h, -w, h};
		this.elementsPerVertex = brush.getElementsPerVertex();
		this.vertices          = new float[(this.verts.length / 2 + 1) * elementsPerVertex];
		this.indices           = ShapeModeler.computeDefaultIndexes(vertices.length / elementsPerVertex);
	
		AABB.corners[0].set(verts[0], verts[1]);
		AABB.corners[1].set(verts[2], verts[3]);
		AABB.corners[2].set(verts[4], verts[5]);
		AABB.corners[3].set(verts[6], verts[7]);
		
		this.brush.setup(vertices, indices);
	}

	@Override
	public void onDown() {
		switch (clickType) {
		case TYPE_RESIZE:
			this.scaleX = 0.9f;
			this.scaleY = 0.9f;
			break;
		case TYPE_REDUCE_ALPHA:
			
			break;
		case TYPE_ALPHA_BOUNDS:
			
			break;
		}
		
	}

	@Override
	public void onUp() {
		switch (clickType) {
		case TYPE_RESIZE:
			this.scaleX = 1.0f;
			this.scaleY = 1.0f;
			break;
		case TYPE_REDUCE_ALPHA:
			
			break;
		case TYPE_ALPHA_BOUNDS:
			
			break;
		}
		
	}
	
	@Override
	public void setPosition(Vector2D position) {
		
	//	Vector2D delta = position.copy().subtract(this.position);
		
		float x = 2 * (Camera2D.ZOOM / Camera2D.MAX_ZOOM);
		float y = 2 * (Camera2D.ZOOM / Camera2D.MAX_ZOOM);
		
		AABB.corners[0].set(verts[0] * x + position.x, verts[1] * y + position.y);
		AABB.corners[1].set(verts[2] * x + position.x, verts[3] * y + position.y);
		AABB.corners[2].set(verts[4] * x + position.x, verts[5] * y + position.y);
		AABB.corners[3].set(verts[6] * x + position.x, verts[7] * y + position.y);
		
	//	Log.d("delta", "delta = " + delta.toString());
		
		super.setPosition(position);
		
	}
	
	@Override
	public void draw(float deltaTime) {
		// prepare arrays
						
		vertices[0] = this.position.x;
		vertices[1] = this.position.y;
						
		float x = 2 * scaleX * (Camera2D.ZOOM / Camera2D.MAX_ZOOM);
		float y = 2 * scaleX * (Camera2D.ZOOM / Camera2D.MAX_ZOOM);
		
		vertices[    elementsPerVertex + 0] = verts[0] * x + this.position.x;
		vertices[    elementsPerVertex + 1] = verts[1] * y + this.position.y;
		
		vertices[2 * elementsPerVertex + 0] = verts[2] * x + this.position.x;
		vertices[2 * elementsPerVertex + 1] = verts[3] * y + this.position.y;
		
		vertices[3 * elementsPerVertex + 0] = verts[4] * x + this.position.x;
		vertices[3 * elementsPerVertex + 1] = verts[5] * y + this.position.y;
		
		vertices[4 * elementsPerVertex + 0] = verts[6] * x + this.position.x;
		vertices[4 * elementsPerVertex + 1] = verts[7] * y + this.position.y;
						
		brush.draw(deltaTime);
		
	}

	@Override
	public void update(TouchEvent event, Vector2D realPos) {
		
	//	Log.d("tagg", Arrays.toString(verts));
		
		boolean inView = OverlapTester.pointInRectangle(AABB, realPos);
		
		if (inView) {
			if (event.type == TouchEvent.TOUCH_DOWN   && !this.isTouched) {
				this.isTouched = true;
				this.onDown();
			}
			
			if (event.type == TouchEvent.TOUCH_UP     && this.isTouched) {
				this.isTouched = false;
				this.onUp();
				
				if (on—lickListener != null) {
					on—lickListener.onClick(this);
				}
			}
			
		} else {
			if (event.type == TouchEvent.TOUCH_DRAGGED && this.isTouched && inView) {
				this.onUp();
				this.isTouched = false;
			}
			
			if (event.type == TouchEvent.TOUCH_UP      && this.isTouched && inView) {
				this.onUp();
				this.isTouched = false;
			}
		}
		
		
	}

	
}
