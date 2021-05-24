package com.crazydev.util;

import android.util.Log;

public class FPSCounter {
	long startTime = System.nanoTime();
	int frames = 0;
	
	
	
	public void logFPS() {
		frames++;
		if (System.nanoTime() - startTime >= 1_000_000_000L) {
			Log.d("FPSCounter2", "fps: " + frames);
			frames = 0;
			startTime = System.nanoTime();
		}
	}
	
	
	
	
	long startTime1;
	double metering1;
	
	public void startMetering1() {
		startTime1 = System.nanoTime();
	}
	
	public void finishMetering1() {
		long curr  = System.nanoTime();
		long delay = curr - startTime1;
		
		metering1 = (double) delay / 1_000_000_000L;
		
		startTime1 = System.nanoTime();
	}
	
	long startTime2;
	double metering2;
	
	public void startMetering2() {
		startTime2 = System.nanoTime();
	}
	
	public void finishMetering2() {
		long curr  = System.nanoTime();
		long delay = curr - startTime1;
		
		metering2 = (double) delay / 1_000_000_000L;
		
		startTime2 = System.nanoTime();
	}
	
}
