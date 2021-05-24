package com.crazydev.base.framework;

import java.util.List;

import com.crazydev.base.framework.Input.KeyEvent;
import com.crazydev.base.implementation.OpenGLWorker;
import com.crazydev.math.Vector2D;
import com.crazydev.opengl.Camera2D;
import com.crazydev.opengl.ShaderProgram;
import com.crazydev.opengl.VertexBatcher2D;

public abstract class Screen {
	
    protected final OpenGLWorker  openGLWorker;
    protected final ShaderProgram shaderProgram;
    protected final Camera2D      camera2D;
    protected final VertexBatcher2D vertexBatcher2D;

    public Screen(OpenGLWorker openGLWorker) {
        this.openGLWorker  = openGLWorker;
        this.shaderProgram = openGLWorker.getShaderProgram();
        this.camera2D      = openGLWorker.getCamera2D();
        this.vertexBatcher2D = openGLWorker.getVertexBatcher2D();
        
    }
    
    public abstract void present(float deltaTime);
    
    public abstract void update(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
    
    public void transitTo(Screen screen, int effectType) {
    	switch (effectType) {
    	
    	}
    }
    

}