package com.crazydev.ui;

import com.crazydev.base.framework.OnClickListener;
import com.crazydev.base.framework.Input.TouchEvent;
import com.crazydev.collisiondetection.OverlapTester;
import com.crazydev.math.Rectangle;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.modeling.PredefinedShapes;
import com.crazydev.modeling.ShapeModeler;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.opengl.VertexBinder2D;
import com.crazydev.opengl.rendering.Brush;

import android.util.Log;

public abstract class View {
	
	private static int ID = 0;
	public final int id;
	
	protected OnClickListener on—lickListener;
	protected VertexBatcher2D vertexBatcher2D;
	protected float width;
	protected float height;

	protected Vector2D position;
	protected boolean isTouched = false;
	
	protected Brush brush;
	
	protected int elementsPerVertex;
	
	protected short[] indices;
	protected float[] vertices;
	protected float[] verts;
	
	public View (VertexBatcher2D vertexBatcher2D, Brush brush, float width, float height) {
		
		this.id = ID;
		ID ++;
		
		this.vertexBatcher2D = vertexBatcher2D;
		this.brush           = brush;     

		this.width           = width;
		this.height          = height;
		this.position        = new Vector2D(0f, 0f);
		
		UICoordinator.addView(this);
	//	setBounds(width, height);
			
	}
	
	
	public void setPosition(Vector2D position) {
		
		this.position.set(position);
		
	}
	
	
	public void setOnClickListener(OnClickListener on—lcikListener) {
		this.on—lickListener = on—lcikListener;
	}
	
	public abstract void update(TouchEvent event, Vector2D realPos);
	
	public abstract void draw(float deltaTime);
	
	public abstract void onDown();
	public abstract void onUp();
	
	
}
