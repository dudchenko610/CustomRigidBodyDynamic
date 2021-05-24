package com.crazydev.opengl;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;

import static android.opengl.GLES20.glUniform4f;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class ShaderProgram {
	
	// Uniform constants
	public static final String U_MVPMATRIX = "u_MVPMatrix";
	public static final String U_MVMATRIX = "u_MVMatrix";
	public static final String U_TEXTURE_UNIT = "u_TextureUnit";
	public static final String U_COLOR = "u_Color";
	public static final String U_TIME = "u_Time";
	public static final String U_ALPHA = "u_Alpha";
	public static final String U_HASCOLOR = "u_HasColor";
	public static final String U_ENABLELIGHTING = "u_EnableLighting";
	public static final String U_LIGHTPOS = "u_LightPos";
			
	// Attribute constants
	public static final String A_POSITION = "a_Position";
	public static final String A_COLOR = "a_Color";
	public static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
	public static final String A_NORMAL = "a_Normal";
			
	public static final String A_DIRECTION_VECTOR = "a_DirectionVector";
	public static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
	
	private GLSurfaceView glView;
	
	private int program;
	
	private int uMVPmatrixLocation;
	private int uMVmatrixLocation;
	
	private int uTextureUnitLocation;
	private int uHasColorLocation;
	private int uEnableLightingLocation;
	private int uLightPosLocation;
	
	private int aColorLocation;
	private int aPositionLocation;
	private int aTextureCoordinatesLocation;
	private int aNormalLocation;
	
	private final int uAlphaLocation;
	
	private Vector2D ratio;
	
	public ShaderProgram(Context context, GLSurfaceView glView, int vertexShaderResourceId, int fragmentShaderResourceId) {
		this.glView = glView;
		
		// Compile the program and link the program
		program = ShaderHelper.buildProgram(FileUtils.readTextFileFromResource(context, vertexShaderResourceId),
				(FileUtils.readTextFileFromResource(context, fragmentShaderResourceId)));
		
		uMVPmatrixLocation          = glGetUniformLocation(program, U_MVPMATRIX);
		uMVmatrixLocation           = glGetUniformLocation(program, U_MVMATRIX);
		uTextureUnitLocation        = glGetUniformLocation(program, U_TEXTURE_UNIT);
		uAlphaLocation              = glGetUniformLocation(program, U_ALPHA);
		uHasColorLocation           = glGetUniformLocation(program, U_HASCOLOR);
		uEnableLightingLocation     = glGetUniformLocation(program, U_ENABLELIGHTING);
		uLightPosLocation           = glGetUniformLocation(program, U_LIGHTPOS);
		
		aColorLocation              = glGetAttribLocation(program, A_COLOR);
		aPositionLocation           = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
		aNormalLocation             = glGetAttribLocation(program, A_NORMAL);
	
		ratio = new Vector2D(0, 0);
	}
	
	public void setUniforms(float[] matrix, int texture, boolean hasColor) {
		glUniformMatrix4fv(uMVPmatrixLocation, 1, false, matrix, 0);
		
		if (texture != 0) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture);
			glUniform1i(uTextureUnitLocation, 0);
		}
		
		if (hasColor) {
			glUniform1i(uHasColorLocation, 1);
		} else {
			glUniform1i(uHasColorLocation, 0);
		}
		
	}
	
	public void setUniforms(float[] MVP_matrix, int texture, boolean hasColor, boolean isLight) {
		glUniformMatrix4fv(uMVPmatrixLocation, 1, false, MVP_matrix, 0);
		if (hasColor) {
			glUniform1i(uHasColorLocation, 1);
		} else {
			glUniform1i(uHasColorLocation, 0);
			if (texture != 0) {
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, texture);
				glUniform1i(uTextureUnitLocation, 0);
			}
		}
		
		if (isLight) {
			glUniform1i(uEnableLightingLocation, 1);
		} else {
			glUniform1i(uEnableLightingLocation, 0);
		}
		
	}
	
	public void setLightPosition(Vector3D position) {
		glUniform3f(uLightPosLocation, position.x, position.y, position.z);
	}
	
	public void setAlpha(float alpha) {
		glUniform1f(uAlphaLocation, alpha);
	}
	
	public int getPositionAttributeLocation () {
		return aPositionLocation;
	}
	
	public int getColorAttributeLocation() {
		return aColorLocation;
	}
	
	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}

	public int getNormalAttributeLocation() {
		return aNormalLocation;
	}

	// ratio-related methods

	public int getViewWidth() {
		return glView.getWidth();
	}

	public int getViewHeight() {
		return glView.getHeight();
	}
	
	public void useProgram() {
		glUseProgram(program);
	}

}
