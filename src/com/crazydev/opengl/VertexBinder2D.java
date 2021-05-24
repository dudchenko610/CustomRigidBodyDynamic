package com.crazydev.opengl;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.crazydev.util.Constants;

public class VertexBinder2D {
	
	public static final int STRIDE_TEXTURED = Constants.POSITION_COMPONENT_COUNT_2D + Constants.TEXTURE_COORDINATES_COMPONENT_COUNT;
	public static final int STRIDE_COLORED  = Constants.POSITION_COMPONENT_COUNT_2D + Constants.COLOR_COMPONENT_COUNT;
	
	private final ShaderProgram shaderProgram;
	
	private IntBuffer intBufferColored;
	private ShortBuffer shortBufferColored;
	
	private IntBuffer intBufferTextured;
	private ShortBuffer shortBufferTextured;
	
	private int[] tempBuffer;
	
	private int STRIDE_C = STRIDE_COLORED * Constants.BYTES_PER_FLOAT;
	private int STRIDE_T = STRIDE_TEXTURED * Constants.BYTES_PER_FLOAT;
	
	
	public VertexBinder2D(ShaderProgram shaderProgram, int sizeOfVerticesArray, int sizeOfIndicesArray) {
		this.shaderProgram = shaderProgram;
		this.tempBuffer = new int[sizeOfVerticesArray];

		
		intBufferColored = ByteBuffer
				.allocateDirect(sizeOfVerticesArray * Integer.SIZE)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		
		shortBufferColored = ByteBuffer
				.allocateDirect(sizeOfIndicesArray * Constants.BYTES_PER_SHORT)
				.order(ByteOrder.nativeOrder())
				.asShortBuffer();		
		
		intBufferTextured = ByteBuffer
				.allocateDirect(sizeOfVerticesArray * Integer.SIZE)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		
		shortBufferTextured = ByteBuffer
				.allocateDirect(sizeOfIndicesArray * Constants.BYTES_PER_SHORT)
				.order(ByteOrder.nativeOrder())
				.asShortBuffer();
		
	}
	
	public void setIndicesColor(short[] indices, int offset, int length) {
		this.shortBufferColored.clear();
		this.shortBufferColored.put(indices, offset, length);
		this.shortBufferColored.flip();
	}
	
	public void setVerticesOld(float[] vertices, int offset, int length) {
		this.intBufferColored.clear();
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			tempBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		}
		
		this.intBufferColored.put(tempBuffer, offset, length);
	}
	
	public void setVerticesColored(float[] vertices) {
	
		for (int i = 0; i < vertices.length; i++) {
			tempBuffer[i] = Float.floatToRawIntBits(vertices[i]);
		}
		
		this.intBufferColored.put(tempBuffer, 0, vertices.length);
	}
	
	public void clearVerticesBufferColor() {
		this.intBufferColored.clear();
	}

	public void setIndicesTexture(short[] indices, int offset, int length) {
		this.shortBufferTextured.clear();
		this.shortBufferTextured.put(indices, offset, length);
		this.shortBufferTextured.flip();
	}
	
	public void setVerticesTextured(float[] vertices) {
		
		for (int i = 0; i < vertices.length; i++) {
			tempBuffer[i] = Float.floatToRawIntBits(vertices[i]);
		}
		
		this.intBufferTextured.put(tempBuffer, 0, vertices.length);
	}

	public void clearVerticesBufferTexture() {
		this.intBufferTextured.clear();
	}
	
	
	public void bindDataColor() {
		
		int offset = 0;
		intBufferColored.position(offset);
		glVertexAttribPointer(shaderProgram.getPositionAttributeLocation(), Constants.POSITION_COMPONENT_COUNT_2D, GL_FLOAT, false, STRIDE_C, intBufferColored);
		glEnableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
		offset += Constants.POSITION_COMPONENT_COUNT_2D;
		
		intBufferColored.position(offset);
		glVertexAttribPointer(shaderProgram.getColorAttributeLocation(), Constants.COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_C, intBufferColored);
		glEnableVertexAttribArray(shaderProgram.getColorAttributeLocation());
	
	}
	
	public void bindDataTexture() {
		int offset = 0;
		intBufferTextured.position(offset);
		glVertexAttribPointer(shaderProgram.getPositionAttributeLocation(), Constants.POSITION_COMPONENT_COUNT_2D, GL_FLOAT, false, STRIDE_T, intBufferTextured);
		glEnableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
		offset += Constants.POSITION_COMPONENT_COUNT_2D;
		
		intBufferTextured.position(offset);
		glVertexAttribPointer(shaderProgram.getTextureCoordinatesAttributeLocation(), Constants.TEXTURE_COORDINATES_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_T, intBufferTextured);
		glEnableVertexAttribArray(shaderProgram.getTextureCoordinatesAttributeLocation());
		
	}
	
	public void unbindDataColor() {
		glDisableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
		glDisableVertexAttribArray(shaderProgram.getColorAttributeLocation());

	}
	
	public void unbindDataTexture() {
		glDisableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
		glDisableVertexAttribArray(shaderProgram.getTextureCoordinatesAttributeLocation());
	}
	
	
	public void draw(int primiriveType, int numVertices) {
		glDrawArrays(primiriveType, 0, numVertices);
	}
	
	
	// indices
	public void draw(int primiriveType, int numVertices, boolean color) {
		shortBufferColored.position(0);
		shortBufferTextured.position(0);
		glDrawElements(primiriveType, numVertices, GL_UNSIGNED_SHORT, color ? shortBufferColored : shortBufferTextured);
	
	}

}
