package com.crazydev.opengl;

public class MonoFont {
	
	public final Texture texture;
	public final int glyphWidth;
	public final int glyphHeight;
	public final TextureRegion[] glyphs = new TextureRegion[96];
	
	public MonoFont(Texture texture, int offsetX, int offsetY, int glyphsPerRow, int glyphWidth, int glyphHeight) {
		this.texture = texture;
		this.glyphWidth = glyphWidth;
		this.glyphHeight = glyphHeight;
		int x = offsetX;
		int y = offsetY;
		
		for (int i = 0; i < 96; i++) {
			glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
			x += glyphWidth;
			if (x == offsetX + glyphsPerRow * glyphWidth) {
				x = offsetX;
				y += glyphHeight;
			}
		}
	}
	
/*	public void drawText(UISpriteBatcher batcher, String text, float x, float y, float width, float height) {
		int length = text.length();
		for(int i = 0; i < length; i++) {
			int c = text.charAt(i) - ' '; // ' ' = 32
			if (c < 0 || c > glyphs.length - 1) {
				continue;
			}
			
			TextureRegion glyph = glyphs[c];
			batcher.drawSprite(x, y, width, height, glyph);
			x += width;
		}
	}*/

}
