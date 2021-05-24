package com.crazydev.opengl;

import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glTexParameteri;

import java.io.IOException;
import java.io.InputStream;

import com.crazydev.base.framework.FileIO;
import com.crazydev.base.implementation.OpenGLWorker;

import static android.opengl.GLES20.glGenerateMipmap;
import android.opengl.GLUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;

public class Texture {
	
	private static final String TAG = "TextureHelper";
	
	private FileIO fileIO;
	private String fileName;
	public int texture;
	int minFilter;
	int magFilter;
	public int width;
	public int height;
	
	public Texture(OpenGLWorker game, String fileName) {
		this.fileIO = game.getFileIO();
		this.fileName = fileName;
		load();
	}
	
	private void load() {
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);
		texture = textureObjectIds[0];
		
		InputStream in = null;
		
		try {
			in = fileIO.readAsset(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			
			glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
			GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GL_NEAREST, GL_LINEAR);
			glBindTexture(GL_TEXTURE_2D, 0);
			bitmap.recycle();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					
				}
			}
		}
		
	
	}

	
	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter); // mipmapping
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
	}
	
	public void reload() {
		load();
	//	bind();
		setFilters(minFilter, magFilter);
	//	glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}

	public void dispose() {
		glBindTexture(GL_TEXTURE_2D, texture);
		int[] textureIds = {texture};
		glDeleteTextures(1, textureIds, 0);
	}
	
	public static int loadTextureFromResourse(Context context, int resourceId) {
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);
		
		if (textureObjectIds[0] == 0) {
		
			Log.w(TAG, "Could not generate a new OpenGL texture object");
			
			return 0;
		}
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
		
		if (bitmap == null) {
			Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			
			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); // mipmapping
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, 0); // unbinding the texture to avoid further changes
		
		return textureObjectIds[0];
	}
	
	public static int loadTextureFromAssets(OpenGLWorker game, String name) {
		final int[] textureObjectIds = new int[1];
		try {
		
			glGenTextures(1, textureObjectIds, 0);
		
			if (textureObjectIds[0] == 0) {
				
				Log.w(TAG, "Could not generate a new OpenGL texture object");
				
				return 0;
			}
			
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
		
			Bitmap bitmap;
			bitmap = BitmapFactory.decodeStream(game.getFileIO().readAsset(name));
		
		
		
			glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); // mipmapping
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
			GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		
			bitmap.recycle();
			glGenerateMipmap(GL_TEXTURE_2D);
		
			glBindTexture(GL_TEXTURE_2D, 0); // unbinding the texture to avoid further changes
		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return textureObjectIds[0];
	}
	
	
	
	
}
