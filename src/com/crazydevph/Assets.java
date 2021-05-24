package com.crazydevph;

import com.crazydev.base.implementation.OpenGLWorker;
import com.crazydev.opengl.Texture;
import com.crazydev.opengl.TextureRegion;

public class Assets {
	
	public static Texture crate;

	public static void load(OpenGLWorker game) {
 
		crate = new Texture(game, "crate.png");
		
	}
	
	public static void reload() {
		
	}
	
	

}
