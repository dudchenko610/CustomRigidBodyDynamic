package com.crazydev.base.implementation;

import android.view.View.OnTouchListener;

import java.util.List;

import com.crazydev.base.framework.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener {
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<TouchEvent> getTouchEvents();
}
