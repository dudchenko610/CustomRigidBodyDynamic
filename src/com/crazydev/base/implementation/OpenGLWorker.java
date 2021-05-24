package com.crazydev.base.implementation;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.crazydev.base.framework.Audio;
import com.crazydev.base.framework.FileIO;
import com.crazydev.base.framework.Input;
import com.crazydev.base.framework.Screen;
import com.crazydev.math.Vector2D;
import com.crazydev.opengl.Camera2D;
import com.crazydev.opengl.ShaderProgram;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.util.Constants;
import com.crazydevph.Assets;
import com.crazydevph.R;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public abstract class OpenGLWorker extends Activity implements Renderer {
	
	enum  GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	
	private GLSurfaceView glView;
	
	private GLGameState state = GLGameState.Initialized;
	private Object stateChanged = new Object();
	private long startTime = System.nanoTime();
	private WakeLock wakeLock;
	
	private float verticalRatio   = 1;
	private float horizontalRatio = 1;
	
	
	private Screen          screen;
	private Audio           audio;
	private Input           input;
	private FileIO          fileIO;
	private ShaderProgram   shaderProgram;
	private Camera2D        camera2D;
	private VertexBatcher2D vertexBatcher2D;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		
		glView = new GLSurfaceView(this);
		glView.setEGLContextClientVersion(2);
		glView.setRenderer(this);
		setContentView(glView);
		
		fileIO   = new AndroidFileIO(this);
		audio    = new AndroidAudio(this);
		input    = new AndroidInput(this, glView, 1, 1);
		
		Assets.load(this);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
		
	}
	
	@Override
	public void onPause() {
		synchronized(stateChanged) {
			if (isFinishing()) {
				state = GLGameState.Finished;
			} else {
				state = GLGameState.Paused;
			}
			
			while(true) {
				try {
					 stateChanged.wait();
					 break;
				} catch (InterruptedException e) {
					
				}
			}
		}

		wakeLock.release();
		glView.onPause();
		super.onPause();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		shaderProgram = new ShaderProgram(this, glView, R.raw.vertex_shader, R.raw.fragment_shader);
		shaderProgram.useProgram(); 
		
		camera2D = new Camera2D(glView);
		camera2D.setSides(Constants.GAME_SCREEN_WIDTH, Constants.GAME_SCREEN_HEIGHT);
		camera2D.setViewPort(new Vector2D(5, 5));
		
		vertexBatcher2D = new VertexBatcher2D(this, 100000, 1000);
		
		Assets.load(this);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
		
		float ratio = 1f;
		
		if (width > height) {
			ratio = (float) width / height;
			
			horizontalRatio = ratio;
			verticalRatio = 1;
			
		} else {
			ratio = (float) height / width;
			
			horizontalRatio = 1;
			verticalRatio = ratio;
			
		}
		
		camera2D.setRatio(horizontalRatio, verticalRatio);
		camera2D.setViewportAndMatrices();
		
		synchronized(stateChanged) {
			if (state == GLGameState.Initialized) {
				screen = getStartScreen();
				
			}
			
			state = GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0f, 0f, 0f, 0f);
		GLGameState state = null;
		
		synchronized(stateChanged) {
			state = this.state;
		}
		
		if (state == GLGameState.Running) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			
			screen.update(deltaTime);
			screen.present(deltaTime);
			
		}
		
		if (state == GLGameState.Paused) {
			screen.pause();
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
		
		if (state == GLGameState.Finished ) {
			screen.pause();
			screen.dispose();
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}
	
	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen newScreen) {
		if (screen == null) {
			throw new IllegalArgumentException("Screen must not be null");
		}
		this.screen.pause();
		this.screen.dispose();
		newScreen.resume();
		newScreen.update(0);
		this.screen = newScreen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}

	public Screen getStartScreen() {
		return null;
	}
	
	public Camera2D	getCamera2D() {
		return this.camera2D;
	}
	
	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
	
	public VertexBatcher2D getVertexBatcher2D() {
		return this.vertexBatcher2D;
	}
	
	


}
