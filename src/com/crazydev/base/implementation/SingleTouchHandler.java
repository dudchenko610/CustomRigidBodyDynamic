package com.crazydev.base.implementation;

import java.util.ArrayList;
import java.util.List;

import com.crazydev.base.framework.Pool;
import com.crazydev.base.framework.Input.TouchEvent;
import com.crazydev.base.framework.Pool.PoolObjectFactory;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SingleTouchHandler implements TouchHandler {
	
	private boolean isTouched;
	private int touchX;
	private int touchY;
	private Pool<TouchEvent> touchEventPool;
	private List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	
	private float scaleX;
	private float scaleY;
	
	public SingleTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				
				return new TouchEvent();
			}
		};
		
		touchEventPool = new Pool<TouchEvent> (factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized(this) {
			TouchEvent touchEvent = touchEventPool.newObject();
			
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}
			
			touchEvent.x = touchX = (int) (event.getX() * scaleX);
			touchEvent.y = touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);
			
			return true;
		}
	}
	
	public boolean isTouchDown(int pointer) {
		synchronized(this) {
			if (pointer == 0) {
				return isTouched;
			} else {
				return false;
			}
		}
	}
	
	public int getTouchX(int pointer) {
		synchronized(this) {
			return touchX;
		}
	}
	
	public int getTouchY(int pointer) {
		synchronized(this) {
			return touchY;
		}
	}
	
	public List<TouchEvent> getTouchEvents() {
		synchronized(this) {
			int lenght = touchEvents.size();
			for (int i = 0; i < lenght; i++) {
				touchEventPool.free(touchEvents.get(i));
			}
			
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
}
