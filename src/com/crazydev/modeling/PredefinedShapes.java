package com.crazydev.modeling;

import com.crazydev.math.Vector2D;

import android.util.FloatMath;
import android.util.Log;

public class PredefinedShapes {
	
	// this sweet word - predefined (((
	
	public static final int TYPE_SQUARE     = 0;
	public static final int TYPE_CIRCLE     = 1;
	public static final int TYPE_HEXAGONE   = 2;
	public static final int TYPE_POLYGONE_1 = 3;
	public static final int TYPE_BLOCK_1    = 4;
	
	private static final float PI = (float) Math.PI;

	public static final float[] SHAPE_SQUARE = new float[] {
			0.00f,  0.00f,  0.0f, 1.0f, 0.0f, 1.0f,
		   -0.50f, -0.50f,  0.0f, 1.0f, 0.0f, 1.0f,
		    0.50f, -0.50f,  0.0f, 1.0f, 0.0f, 1.0f,
		    0.50f,  0.50f,  0.0f, 1.0f, 0.0f, 1.0f,
		   -0.50f,  0.50f,  0.0f, 1.0f, 0.0f, 1.0f,
	};
	
	public static final short[] SHAPE_SQUARE_INDICES = new short[] {
			1, 2, 3,
			3, 1, 4,
	};
	
	public static final float[] SHAPE_POLYGONE_1 = new float[] {
			0.00f,  0.00f,  1.0f, 0.0f, 0.0f, 1.0f,
			0.90f,  0.80f,  1.0f, 0.0f, 0.0f, 1.0f,
			0.00f,  1.00f,  1.0f, 0.0f, 0.0f, 1.0f,
		   -0.70f,  0.80f,  1.0f, 0.0f, 0.0f, 1.0f,
		   -0.40f, -0.75f,  1.0f, 0.0f, 0.0f, 1.0f,
		    0.20f, -0.75f,  1.0f, 0.0f, 0.0f, 1.0f,
		    0.99f,  0.00f,  1.0f, 0.0f, 0.0f, 1.0f,
	};
	
	public static final short[] SHAPE_POLYGONE_1_INDICES = new short[] {
			1, 2, 3,
			3, 1, 6,
			6, 3, 4,
			4, 6, 5,
	};
	
	public static final float[] SHAPE_BLOCK_1 = new float[] {
			0.00f,  0.00f,  1.0f, 0.0f, 0.0f, 1.0f,
		    0.00f, -0.50f,  0.0f, 1.0f, 0.0f, 1.0f,
		    0.20f, -0.40f,  0.0f, 0.0f, 1.0f, 1.0f,
		    0.20f,  0.40f,  1.0f, 1.0f, 0.0f, 1.0f,
		    0.00f,  0.50f,  0.0f, 1.0f, 1.0f, 1.0f,
		   -0.20f,  0.40f,  0.0f, 1.0f, 1.0f, 1.0f,
		   -0.20f, -0.40f,  0.0f, 1.0f, 1.0f, 1.0f,
	};
	
	public static short[] computeDefaultIndexes(int verticesNum) {
		int indicesAmount = verticesNum * 3 - 3;
		short [] indices = new short[indicesAmount];
		
		short j = 1;
		short k = 2;
		
		for (int i = 0; i < indices.length; i ++) {
			
			if (i == indices.length - 1) {
				indices[i] = 1;
				break;
			}
			
			if (i % 3 == 0) {
				indices[i] = 0;
				continue;
			}
			
			if (i % 3 == 1) {
				indices[i] = j ++;
				continue;
			}
			
			if (i % 3 == 2) {
				indices[i] = k ++;
				continue;
			}
			
		}
		
		return indices;
	}
	
	public static float[] createCircle(int verticesNum, int stride, float radius) {
		float [] vertices = new float[verticesNum * stride + stride];
		vertices[0] = 0f;
		vertices[1] = 0f;
		
		vertices[2] = 1f;
		vertices[3] = 0f;
		vertices[4] = 0f;
		vertices[5] = 1f;
		
		float cos = FloatMath.cos((float) (2 * PI) / verticesNum);
		float sin = FloatMath.sin((float) (2 * PI) / verticesNum);
		
		Vector2D vec = new Vector2D(radius, 0);
		
		for (int i = stride; i < vertices.length; i += stride) {
			
			float X = vec.x * cos - vec.y * sin;
			float Y = vec.x * sin + vec.y * cos;
			
			vec.set(X, Y);
			
			vertices[i + 0] = X;
			vertices[i + 1] = Y; 
			
			vertices[i + 2] = 1f;
			vertices[i + 3] = 0f;
			vertices[i + 4] = 0f;
			vertices[i + 5] = 1f;
			
			if (i == vertices.length - stride) {
				vertices[i + 2] = 1f;
				vertices[i + 3] = 0f;
				vertices[i + 4] = 0f;
				vertices[i + 5] = 1f;
			}
			
			
			
		}
		
		return vertices;
	}
	
	
}
