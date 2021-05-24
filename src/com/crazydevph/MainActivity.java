package com.crazydevph;

import com.crazydev.base.framework.Screen;
import com.crazydev.base.implementation.OpenGLWorker;
import com.crazydev.base.screens.Space2DScreen;

public class MainActivity extends OpenGLWorker {

	@Override
	public Screen getStartScreen() {
		return new Space2DScreen(this);
	}
	
}
