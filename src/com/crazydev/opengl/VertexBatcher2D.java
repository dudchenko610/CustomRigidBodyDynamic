package com.crazydev.opengl;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;

import com.crazydevph.Assets;
import com.crazydev.base.implementation.OpenGLWorker;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;

import android.util.Log;

public class VertexBatcher2D {

	private float [] verticesColored;  // BIG ENOUGH
	private short [] indicesColored;   // BIG ENOUGH - no problems with indices - just pass it when call draw() method
	
	private int indexVertexOffset_colored = 0;   // indexOffset/ STRIDE = numVertices when I will use color 9 will be needed
	int indicesCurrentTop_colored = 0;
	
	
	private float [] verticesLines;
	private float [] verticesPoints;
	
	private int vertLinesOffset  = 0;
	private int vertPointsOffset = 0;
	
	
	private float [] verticesUI;
	private short [] indicesUI;
	
	private int indexVertexUIOffset = 0;
	private int indicesUICurrentTop = 0;
	
	private ShaderProgram  shaderProgram;
	private Camera2D       camera2D;
	private VertexBinder2D vertexBinder2D;

	public VertexBatcher2D(OpenGLWorker openGLWorker, int verticesSize, int maxPointsLines) {
		this.shaderProgram    = openGLWorker.getShaderProgram();
		this.camera2D         = openGLWorker.getCamera2D();
		
		this.verticesColored  = new float[verticesSize];
		this.indicesColored   = new short[verticesSize]; // on a eye
		this.vertexBinder2D   = new VertexBinder2D(this.shaderProgram, verticesColored.length, indicesColored.length);
		
		this.verticesLines    = new float[maxPointsLines * 6 * 2];
		this.verticesPoints   = new float[maxPointsLines * 6 * 1];
		
		this.verticesUI       = new float[verticesSize / 4];
		this.indicesUI        = new short[verticesSize / 4];
	}
	
	public void addVerticesIndicesOfView(float[] vertices, short[] indices) {
		for (int i = 0; i < vertices.length; i ++) {
			this.verticesUI[i + indexVertexUIOffset] = vertices[i];
			
		}
		
		int indexIndicesOffset = indexVertexUIOffset / VertexBinder2D.STRIDE_COLORED;
		
		for (int i = indicesUICurrentTop; i < indicesUICurrentTop + indices.length; i ++) {
			
			this.indicesUI[i] = (short) (indices[i - indicesUICurrentTop] + indexIndicesOffset);
		}
		
		this.indicesUICurrentTop += indices.length;
		this.indexVertexUIOffset += vertices.length;
	}

	public void depictViews() {
		// maybe later here will be placed shaderProgram.setUniform();
		
		this.shaderProgram.setUniforms(camera2D.getVP_matrix(), 0, true);
		
		this.vertexBinder2D.bindDataColor();
		this.vertexBinder2D.setVerticesOld(verticesUI, 0, indexVertexUIOffset);
		this.vertexBinder2D.setIndicesColor(indicesUI, 0, indicesUICurrentTop);
		
		this.vertexBinder2D.draw(GL_TRIANGLES, indicesUICurrentTop, true);
				
		this.vertexBinder2D.unbindDataColor();
	}

	public float [] getVerticesUI() {
		return verticesUI;
	}
	
	public int getVertexIndexUIOffset() {
		return indexVertexUIOffset;
	}
	
	public void depictMarkerShapes() {
		
		this.shaderProgram.setUniforms(camera2D.getVP_matrix(), 0, true);
		
		
		this.vertexBinder2D.bindDataColor();
		this.vertexBinder2D.setVerticesOld(verticesPoints, 0, vertPointsOffset);
		this.vertexBinder2D.draw(GL_POINTS, vertPointsOffset / VertexBinder2D.STRIDE_COLORED);
		this.vertexBinder2D.unbindDataColor();
		
		this.vertexBinder2D.bindDataColor();
		this.vertexBinder2D.setVerticesOld(verticesLines, 0, vertLinesOffset);
		this.vertexBinder2D.draw(GL_LINES, vertLinesOffset / VertexBinder2D.STRIDE_COLORED);
		this.vertexBinder2D.unbindDataColor();
		
	}
	
	public void addPoint(Vector2D position, Vector3D color) {
		this.verticesPoints[vertPointsOffset ++] = position.x;
		this.verticesPoints[vertPointsOffset ++] = position.y;
		
		this.verticesPoints[vertPointsOffset ++] = color.x;
		this.verticesPoints[vertPointsOffset ++] = color.y;
		this.verticesPoints[vertPointsOffset ++] = color.z;
		this.verticesPoints[vertPointsOffset ++] = 1.0f;
	}
	
	public void addLine(Vector2D p1, Vector2D p2, Vector3D color1, Vector3D color2, float alpha) {
		this.verticesLines[vertLinesOffset ++] = p1.x;
		this.verticesLines[vertLinesOffset ++] = p1.y;
		
		this.verticesLines[vertLinesOffset ++] = color1.x;
		this.verticesLines[vertLinesOffset ++] = color1.y;
		this.verticesLines[vertLinesOffset ++] = color1.z;
		this.verticesLines[vertLinesOffset ++] = alpha;
		
		this.verticesLines[vertLinesOffset ++] = p2.x;
		this.verticesLines[vertLinesOffset ++] = p2.y;
		
		this.verticesLines[vertLinesOffset ++] = color2.x;
		this.verticesLines[vertLinesOffset ++] = color2.y;
		this.verticesLines[vertLinesOffset ++] = color2.z;
		this.verticesLines[vertLinesOffset ++] = alpha;
	}
	
	public void addLine(Vector2D p1, Vector2D p2, Vector3D color, float alpha) {
		this.verticesLines[vertLinesOffset ++] = p1.x;
		this.verticesLines[vertLinesOffset ++] = p1.y;
		
		this.verticesLines[vertLinesOffset ++] = color.x;
		this.verticesLines[vertLinesOffset ++] = color.y;
		this.verticesLines[vertLinesOffset ++] = color.z;
		this.verticesLines[vertLinesOffset ++] = alpha;
		
		this.verticesLines[vertLinesOffset ++] = p2.x;
		this.verticesLines[vertLinesOffset ++] = p2.y;
		
		this.verticesLines[vertLinesOffset ++] = color.x;
		this.verticesLines[vertLinesOffset ++] = color.y;
		this.verticesLines[vertLinesOffset ++] = color.z;
		this.verticesLines[vertLinesOffset ++] = alpha;
	}
	
	public void addLine(Vector2D p1, Vector2D p2, Vector3D color) {
		addLine(p1, p2, color, 1.0f);
	}
	
	public void deleteAllPointsAndLines() {
		this.vertLinesOffset  = 0;
		this.vertPointsOffset = 0;
	}
	
	/** COLOR **/
	
	private short[] indexesColored = new short[10000];
	private int indexColored     = 0;
	private int vertexColoredInd = 0;
	
	public void clearVerticesBufferColor() {
		this.vertexBinder2D.clearVerticesBufferColor();
		this.indexColored = 0;
		vertexColoredInd = 0;
	}
	
	public void addVerticesColoredToOpenGL(float[] vertices, short[] indices) {
		this.vertexBinder2D.setVerticesColored(vertices);
		
		int indOffset = vertexColoredInd / VertexBinder2D.STRIDE_COLORED;

		for (int i = 0; i < indices.length; i ++) {
			indexesColored[indexColored ++] = (short) (indices[i] + indOffset);
		}
		
		vertexColoredInd += vertices.length;
	}
	
	public void drawColoredBodies() {
		
		this.shaderProgram.setUniforms(camera2D.getVP_matrix(), 0, true);
		
		this.vertexBinder2D.setIndicesColor(indexesColored, 0, indexColored);
		
		this.vertexBinder2D.bindDataColor();
		this.vertexBinder2D.draw(GL_TRIANGLES, indexColored, true);
		this.vertexBinder2D.unbindDataColor();
		
		this.vertexBinder2D.clearVerticesBufferColor();
		this.indexColored = 0;
		vertexColoredInd = 0;
	}
	
	/** TEXTURE **/
	
	private short[] indexesTextured = new short[10000];
	private int indexTextured       = 0;
	private int vertexTexturedInd   = 0;
	
	public void clearVerticesBufferTexture() {
		this.vertexBinder2D.clearVerticesBufferTexture();
		this.indexTextured     = 0;
		this.vertexTexturedInd = 0;
	}
	
	public void addVerticesTexturedToOpenGL(float[] vertices, short[] indices) {
		this.vertexBinder2D.setVerticesTextured(vertices);
		
		int indOffset = vertexTexturedInd / VertexBinder2D.STRIDE_TEXTURED;

		for (int i = 0; i < indices.length; i ++) {
			indexesTextured[indexTextured ++] = (short) (indices[i] + indOffset);
		}
		
		vertexTexturedInd += vertices.length;
	}
	
	public void drawTexturedBodies() {
		
		this.shaderProgram.setUniforms(camera2D.getVP_matrix(), Assets.crate.texture, false);
		this.shaderProgram.setAlpha(1.0f);
		
		this.vertexBinder2D.setIndicesTexture(indexesTextured, 0, indexTextured);
		
		this.vertexBinder2D.bindDataTexture();
		this.vertexBinder2D.draw(GL_TRIANGLES, indexTextured, false);
		this.vertexBinder2D.unbindDataTexture();
		
		this.vertexBinder2D.clearVerticesBufferTexture();
		this.indexTextured = 0;
		vertexTexturedInd = 0;
	}
	
	
}
