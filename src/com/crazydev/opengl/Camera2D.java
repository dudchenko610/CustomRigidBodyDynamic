package com.crazydev.opengl;

import com.crazydev.base.framework.Input.TouchEvent;
import com.crazydev.math.Vector2D;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class Camera2D {
	
	private GLSurfaceView glSurfaceView;
	
	public Vector2D position;
	public static float ZOOM = 1;
	public static float MAX_ZOOM = 12;
	public float frustumWidth, frustumHeight;
	
	private final float[] V_matrix = new float[16];  
	private final float[] P_matrix = new float[16];
	private final float[] VP_matrix = new float[16]; 
	
	public float ACTUAL_WIDTH;
    public float ACTUAL_HEIGHT;

    public float ACTUAL_START_WIDTH;
    public float ACTUAL_START_HEIGHT;
    
    private float xComponent_ratio = 1;
    private float yComponent_ratio = 1;
    
    private float width;
    private float height;
	
    private float eyeX = 0;
    private float eyeY = 0;
    private float eyeZ = 1.0f;

    private float centerX = 0;
    private float centerY = 0;
    private float centerZ = 0;

    private float upX = 0f;
    private float upY = 1f;
    private float upZ = 0f;
    
    private int glSurfaceViewWidth;
    private int glSurfaceViewHeight;
	
	public Camera2D(GLSurfaceView glSurfaceView) {
		this.glSurfaceViewWidth  = glSurfaceView.getWidth();
		this.glSurfaceViewHeight = glSurfaceView.getHeight();
		
	}
	
	public void setFrustumParameters(float frustumWidth, float frustumHeight) {
		this.frustumWidth = frustumWidth;
		this.frustumHeight = frustumHeight;
		this.position = new Vector2D(frustumWidth / 2, frustumHeight / 2);
		this.ZOOM = 1.0f;
	}
	
	public float left, right, bottom, top;
	
	public void setViewportAndMatrices() {
		left    =   position.x - (width * ZOOM) / 2.0f;
        left   *=   xComponent_ratio;
        right   =   position.x + (width * ZOOM) / 2.0f;
        right  *=   xComponent_ratio;
        bottom  =   position.y - (height * ZOOM) / 2.0f;
        bottom *=   yComponent_ratio;
        top     =   position.y + (height * ZOOM) / 2.0f;
        top    *=   yComponent_ratio;

        ACTUAL_WIDTH  = right - left;
        ACTUAL_HEIGHT = top - bottom;

        Log.d("mylog", "width = "  + ACTUAL_WIDTH);
        Log.d("mylog", "height = " + ACTUAL_HEIGHT);
        
        // right - left
        // top - bottom

        float near = 1.0f;
        float far = 5.0f;

        Matrix.frustumM(P_matrix, 0, left, right, bottom, top, near, far);

        Matrix.setLookAtM(V_matrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        Matrix.multiplyMM(VP_matrix, 0, P_matrix, 0, V_matrix, 0);
        
	}
	
    public void setSides(float width, float height) {
        this.width  = width;
        this.height = height;
        this.position = new Vector2D(0, 0);

        this.setViewportAndMatrices();
    }

    public void setRatio(float horizontal_comp, float vertical_comp) {
        this.xComponent_ratio = horizontal_comp;
        this.yComponent_ratio = vertical_comp;
        this.ACTUAL_START_WIDTH  = horizontal_comp * width;
        this.ACTUAL_START_HEIGHT = vertical_comp * height;
        
        this.ACTUAL_WIDTH  = ACTUAL_START_WIDTH;
        this.ACTUAL_HEIGHT = ACTUAL_START_HEIGHT;

        this.setViewportAndMatrices();
    }

    public void translateViewport(Vector2D delta) {
        this.position.add(delta);

        this.setViewportAndMatrices();
    }

    public void setViewPort(Vector2D point) {
        this.position.set(point);
    }

    public void zoom(float delta) {
    	
        float d = ZOOM + delta / 5;
        
        if (d > 12 || d < 0.2f) {
            return;
        }
        
        Log.d("log", "zoom = " + Camera2D.ZOOM);
        
        this.ZOOM = d;
        
        this.setViewportAndMatrices();
    }

    public float getZoom() {
        return ZOOM;
    }
	
	public float [] getVP_matrix() {
		return VP_matrix;
	}
	
	public void touchToWorld(TouchEvent event) {
		event.touchPosition.set((event.x / (float) glSurfaceViewWidth)  * ACTUAL_WIDTH,
							(1 - event.y / (float) glSurfaceViewHeight) * ACTUAL_HEIGHT);
	}
	
	public void touchToWorld(Vector2D touch, TouchEvent event) {
		touch.x = (event.x / (float) glSurfaceViewWidth) * ACTUAL_WIDTH;
		touch.y = (1 - event.y / (float) glSurfaceViewHeight) * ACTUAL_HEIGHT;
	}
	
	public void touchToWorld(Vector2D touch) {
		touch.x = (touch.x / (float) glSurfaceViewWidth) * ACTUAL_WIDTH;
		touch.y = (1 - touch.y / (float) glSurfaceViewHeight) * ACTUAL_HEIGHT;
	}
	
	public void touchToWorld_no_zoom(TouchEvent event) {
        event.touchPosition.set((event.x / (float) glSurfaceViewWidth) * ACTUAL_START_WIDTH,
                (1 - event.y / (float) glSurfaceViewHeight ) * ACTUAL_START_HEIGHT);
    }
	
}
