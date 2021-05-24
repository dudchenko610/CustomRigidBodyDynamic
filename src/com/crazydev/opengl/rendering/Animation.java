package com.crazydev.opengl.rendering;

public class Animation {
	
	private TextureRegion[] textureRegions;
	private boolean isLooped = false;
	private float delay = 0;
	private int index   = 0;
	
	public Animation(boolean isLooped, TextureRegion ... regions) {
		this.isLooped = isLooped;
		textureRegions = new TextureRegion[10];
		
		for (int i = 0; i < Math.min(10, regions.length); i ++) {
			textureRegions[index ++] = regions[i];
		}
	}
	
	public TextureRegion getMainFrame() {
		return this.textureRegions[0];
	}

	public TextureRegion getFrame() {
		return this.textureRegions[0];
	}
}
