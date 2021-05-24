package com.crazydev.base.screens;

import java.util.List;
import java.util.Random;

import com.crazydev.base.framework.OnClickListener;
import com.crazydev.base.framework.Screen;
import com.crazydevph.Assets;
import com.crazydev.base.framework.Input.KeyEvent;
import com.crazydev.base.framework.Input.TouchEvent;
import com.crazydev.base.implementation.OpenGLWorker;
import com.crazydev.math.Polygon;
import com.crazydev.math.Rectangle;
import com.crazydev.math.Vector2D;
import com.crazydev.math.Vector3D;
import com.crazydev.modeling.PredefinedShapes;
import com.crazydev.modeling.ShapeModeler;
import com.crazydev.opengl.Camera2D;
import com.crazydev.opengl.Texture;
import com.crazydev.opengl.VertexBatcher2D;
import com.crazydev.opengl.rendering.Brush;
import com.crazydev.opengl.rendering.ColorBrush;
import com.crazydev.physics.Body;
import com.crazydev.physics.Contact;
import com.crazydev.physics.PointJoint;
import com.crazydev.physics.SpringJoint;
import com.crazydev.physics.World;
import com.crazydev.physics.dynamic.AngularForce;
import com.crazydev.physics.dynamic.Force;
import com.crazydev.physics.dynamic.Mass;
import com.crazydev.physics.fixtures.Fixture;
import com.crazydev.physics.fixtures.PolygonFixture;
import com.crazydev.ui.RectangularButton;
import com.crazydev.ui.UICoordinator;
import com.crazydev.ui.View;
import com.crazydev.util.Constants;
import com.crazydev.util.FPSCounter;

import android.util.Log;

public class Space2DScreen extends Screen implements OnClickListener{
	
	private OpenGLWorker openGLWorker;
	
	private FPSCounter fpsCounter;
	
	private RectangularButton button1;
	private RectangularButton button2;
	private RectangularButton button3;
	private RectangularButton button4;
	private RectangularButton button5;
	private RectangularButton button6;
	private RectangularButton button7;
	
	private RectangularButton button8;
	private RectangularButton button9;
	
	private World world;
	
	private boolean b = true;
	
	private PointJoint jointL = null;
	
	public Space2DScreen(OpenGLWorker openGLWorker) {
		super(openGLWorker);

		this.openGLWorker = openGLWorker;
		this.fpsCounter   = new FPSCounter();
		
		float width  = camera2D.ACTUAL_START_WIDTH;
		float height = camera2D.ACTUAL_START_HEIGHT;
	
		button1 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(1, 0, 0), 1f);
	//	button1.setPosition(new Vector2D(100, 50));
		button1.setOnClickListener(this);
		
		button2 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(0, 1, 0), 1f);
		button2.setOnClickListener(this);
		
		button3 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(0, 0, 1), 1f);
		button3.setOnClickListener(this);
		
		button4 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(0, 1, 1), 1f);
		button4.setOnClickListener(this);
		
		button5 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(1, 0, 1), 1f);
		button5.setOnClickListener(this);
		
		button6 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(1, 1, 0), 1f);
		button6.setOnClickListener(this);
		
		button7 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(0, 1, 1), 1f);
		button7.setOnClickListener(this);
		
		button8 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(1, 1, 0), 1f);
		button8.setOnClickListener(this);
		
		button9 = UICoordinator.createRectangularButton(vertexBatcher2D, 14.0f, 5.0f, new Vector3D(0, 1, 1), 1f);
		button9.setOnClickListener(this);
		
	    
	    this.world = new World(new Vector2D(0, -9.8f), this.vertexBatcher2D);
	    
	    
	    Vector2D[] pointsTriangle = {
	    		new Vector2D( 0.0f,  1.0f), 
				new Vector2D(-1.0f, -1.0f), 
				new Vector2D( 1.0f, -1.0f)
	    };
	    
	    Vector2D[] pointsBlock = {
	    		new Vector2D( -0.5f, -0.1f), 
				new Vector2D(  0.5f, -0.1f), 
				new Vector2D(  0.5f,  0.1f),
				new Vector2D( -0.5f,  0.1f)
	    };
	    
	    Vector2D[] pointsBlock2 = {
	    		new Vector2D( -0.5f, -0.1f), 
				new Vector2D(  0.5f, -0.1f), 
				new Vector2D(  0.5f,  0.1f),
				new Vector2D( -0.5f,  0.1f)
	    };
	    
	    
	    Body block1 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block1.translate(width / 2 + 0, height + 32);
	    block1.rotate(-0.2f);
	    
	    Body block2 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block2.translate(width / 2 + 21, height + 30);
	    
	    Body block3 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block3.translate(width / 2 + 42, height + 30);
	    
	    Body block4 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block4.translate(width / 2 + 63, height + 30);
	    
	    Body block5 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block5.translate(width / 2 + 84, height + 30);
	    
	    Body block6 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block6.translate(width / 2 + 105, height + 30);
	    
	    Body block7 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block7.translate(width / 2 + 126, height + 30);
	    
	    Body block8 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 20, 6, pointsBlock);
	    block8.translate(width / 2 + 147, height + 30);
	    block8.rotate(0.2f);
	    
	    Body vehicle = ShapeModeler.createBodyFromColor(new Mass(10f, 8f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 10, 14, pointsBlock);
	    vehicle.translate(5, height + 50);
	    
	    Body wheel1 = ShapeModeler.createBodyFromTexture(new Mass(0.2f, 0.5f), vertexBatcher2D, Assets.crate, new Vector2D(128, 128), 50f, 1);
	    wheel1.translate(-3.0f + 5, -3.0f + height + 50);
	    
	    Body wheel2 = ShapeModeler.createBodyFromTexture(new Mass(0.2f, 0.5f), vertexBatcher2D, Assets.crate, new Vector2D(128, 128), 50f, 1);
	    wheel2.translate(+3.0f + 5, -3.0f + height + 50);
		
	    SpringJoint vehicleWheel1Joint = new SpringJoint(vehicle, wheel1, new Vector2D(-3.0f, -3.0f), new Vector2D(0.0f, 0.0f), 2f, 1f);
	    SpringJoint vehicleWheel2Joint = new SpringJoint(vehicle, wheel2, new Vector2D(+3.0f, -3.0f), new Vector2D(0.0f, 0.0f), 2f, 1f);
	    
	    Body vehicle_2 = ShapeModeler.createBodyFromColor(new Mass(30f, 6f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 10, 14, pointsBlock);
	    vehicle_2.translate(5 + 26, height + 38);
	//    vehicle_2.rotate((float) Math.PI);
	    
	    Body wheel1_2 = ShapeModeler.createBodyFromTexture(new Mass(5f, 1f), vertexBatcher2D, Assets.crate, new Vector2D(128, 128), 50f, 1);
	    wheel1_2.translate(-3.0f + 1 + 30, -3.0f + 50);
	    
	    Body wheel2_2 = ShapeModeler.createBodyFromTexture(new Mass(5f, 1f), vertexBatcher2D, Assets.crate, new Vector2D(128, 128), 50f, 1);
	    wheel2_2.translate(+3.0f + 1 + 30, -3.0f + 50);
		
	    SpringJoint vehicleWheel1Joint_2 = new SpringJoint(vehicle_2, wheel1_2, new Vector2D(-3.0f, -3.0f), new Vector2D(0.0f, 0.0f), 2f, 1f);
	    SpringJoint vehicleWheel2Joint_2 = new SpringJoint(vehicle_2, wheel2_2, new Vector2D(+3.0f, -3.0f), new Vector2D(0.0f, 0.0f), 2f, 1f);
	    
	    Body vehicle_3 = ShapeModeler.createBodyFromColor(new Mass(4, 3f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 10, 20, pointsBlock2);
	    vehicle_3.translate(5 + 65, height + 35);
	 //   vehicle_3.rotate((float) Math.PI);
	    
	    Body triangle2 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 2, 2, pointsTriangle);
	    triangle2.translate(width / 2 - 10, height + 2);
		
	    Body triangle3 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 2, 2, pointsTriangle);
	    triangle3.translate(width / 2 - 14f, height + 2);
	    
	    Body triangle4 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 2, 2, pointsTriangle);
	    triangle4.translate(width / 2 - 18f, height + 2);
	    
	    Body triangle5 = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 2, 2, pointsTriangle);
	    triangle5.translate(width / 2 - 22f, height + 2);
		
		Vector2D[] pointsHexagon = {
				new Vector2D( 0.0f, -0.50f), 
				new Vector2D( 0.5f, -0.25f), 
				new Vector2D( 0.5f,  0.25f),
				new Vector2D( 0.0f,  0.50f),
				new Vector2D(-0.5f,  0.25f),
				new Vector2D(-0.5f, -0.25f)
				
		};
		
		Body hexagon = ShapeModeler.createBodyFromColor(new Mass(5, 5), vertexBatcher2D, new Vector3D(0, 0, 1), 1f, 13f, 12, pointsHexagon);
		hexagon.translate(-1, height);
		
		Vector2D [] pointsLink = {
				new Vector2D( 0.00f,  1.00f),
				new Vector2D(-0.50f,  0.75f),
				new Vector2D(-0.50f, -0.75f),
				new Vector2D( 0.00f, -1.00f),
				new Vector2D( 0.50f, -0.75f),
				new Vector2D( 0.50f,  0.75f),
		};
		
		
		Body linkBase = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 2, 2, pointsTriangle);
		linkBase.translate(width / 2 - 4, height + 2);
		linkBase.rotate((float) Math.PI);
		
		Body link0 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link0.translate(width / 2, height);
		link0.rotate((float) Math.PI / 2);
		
		Body link1 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link1.translate(width / 2 + 8, height);
		link1.rotate((float) Math.PI / 2);
		
		Body link2 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link2.translate(width / 2 + 16, height);
		link2.rotate((float) Math.PI / 2);
		
		Body link3 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link3.translate(width / 2 + 24, height);
		link3.rotate((float) Math.PI / 2);
		
		Body link4 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link4.translate(width / 2 + 32, height);
		link4.rotate((float) Math.PI / 2);
		
		Body link5 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link5.translate(width / 2 + 40, height);
		link5.rotate((float) Math.PI / 2);
		
		Body link6 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link6.translate(width / 2 + 48, height);
		link6.rotate((float) Math.PI / 2);
		
		Body link7 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link7.translate(width / 2 + 56, height);
		link7.rotate((float) Math.PI / 2);
		
		Body link8 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		link8.translate(width / 2 + 64, height);
		link8.rotate((float) Math.PI / 2);
		
		Body linkL = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		linkL.translate(width / 2 + 72, height); // + 56
		linkL.rotate((float) Math.PI / 2);
		
		
		PointJoint joint1 = new PointJoint(linkBase, link0, 0, 0);
		PointJoint joint2 = new PointJoint(link0, link1, 3, 0);
		PointJoint joint3 = new PointJoint(link1, link2, 3, 0);
		PointJoint joint4 = new PointJoint(link2, link3, 3, 0);
		PointJoint joint5 = new PointJoint(link3, link4, 3, 0);
		PointJoint joint6 = new PointJoint(link4, link5, 3, 0);
	//	SpringJoint joint7 = new SpringJoint(link5, link6, 3, 0, 400f, 0.1f);
		PointJoint joint7 = new PointJoint(link5, link6, 3, 0);
		PointJoint joint8 = new PointJoint(link6, link7, 3, 0);
		PointJoint joint9 = new PointJoint(link7, link8, 3, 0);
		
		this.jointL  = new PointJoint(link8, linkL, 3, 0);
		
		
		Body body6 = ShapeModeler.createBodyFromColor(new Mass(1 / 15f, 1f), vertexBatcher2D, new Vector3D(0, 0, 1), 0.4f, 4, 4, pointsLink);
		body6.translate(width / 2 + 25, height + 12);
		body6.rotate((float) Math.PI / 2);
		body6.addForce(new Vector2D(0, -9.8f), -1);
		
		this.world.addBody(linkBase);
		this.world.addBody(link0);
		this.world.addBody(link1);
		this.world.addBody(link2);
		this.world.addBody(link3);
		this.world.addBody(link4);
		this.world.addBody(link5);
		this.world.addBody(link6);
		this.world.addBody(link7);
		this.world.addBody(link8);
		this.world.addBody(linkL);
		
		this.world.addBody(triangle2);
		this.world.addBody(triangle3);
		this.world.addBody(triangle4);
		this.world.addBody(triangle5);
		this.world.addBody(block1);
		this.world.addBody(block2);
		this.world.addBody(block3);
		this.world.addBody(block4);
		
		this.world.addBody(vehicle);
		this.world.addBody(wheel1); // 19
		this.world.addBody(wheel2); // 20
		
		this.world.addJoint(vehicleWheel1Joint);
		this.world.addJoint(vehicleWheel2Joint);
		
	//	this.world.addBody(vehicle_2);
	//	this.world.addBody(wheel1_2); // 19
	//	this.world.addBody(wheel2_2); // 20

		this.world.addBody(block5);
		this.world.addBody(block6);
		this.world.addBody(block7);
		this.world.addBody(block8);
		
	//	this.world.addBody(vehicle_3);
	//	this.world.addBody(vehicle);
		
		this.world.addJoint(vehicleWheel1Joint_2);
		this.world.addJoint(vehicleWheel2Joint_2);
		
		this.world.addJoint(joint1);
		this.world.addJoint(joint2);
		this.world.addJoint(joint3);
		this.world.addJoint(joint4);
		this.world.addJoint(joint5);
		this.world.addJoint(joint6);
		this.world.addJoint(joint7);
		this.world.addJoint(joint8);
		this.world.addJoint(joint9);
		this.world.addJoint(jointL);
	
		
		camera2D.zoom(25f);
		
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println(cores);
		
		Log.d("coresAm","cores = " + cores);
		
	}
	
/*	private float minPenetration1 = 0;
	private float minPenetration2 = 0;*/
	
	@Override
	public void present(float deltaTime) {
		
		vertexBatcher2D.clearVerticesBufferColor();
		vertexBatcher2D.clearVerticesBufferTexture();
		
		for (int i = 0; i < this.world.getBodyCount(); i++) {
			Body body = (Body) this.world.getBody(i);
			body.draw(deltaTime);
			
	//		Rectangle aabb = body.getFixture().getAABB();
			
		/*	vertexBatcher2D.addLine(aabb.corners[0], aabb.corners[1], new Vector3D(1, 0, 0));
			vertexBatcher2D.addLine(aabb.corners[1], aabb.corners[2], new Vector3D(1, 0, 0));
			vertexBatcher2D.addLine(aabb.corners[2], aabb.corners[3], new Vector3D(1, 0, 0));
			vertexBatcher2D.addLine(aabb.corners[3], aabb.corners[0], new Vector3D(1, 0, 0));*/
		}
		

		vertexBatcher2D.drawColoredBodies();
		vertexBatcher2D.drawTexturedBodies();
		
		vertexBatcher2D.clearVerticesBufferColor();
		
		UICoordinator.drawViews(deltaTime);
		
		
		vertexBatcher2D.drawColoredBodies();
		
	//	vertexBatcher2D.depictViews();
		vertexBatcher2D.depictMarkerShapes();
		vertexBatcher2D.deleteAllPointsAndLines();
		
		fpsCounter.logFPS();
		
		Log.d("bodies", "bodies amount = " + this.world.getBodyCount());
	}
	
    private Vector2D touchedDown = new Vector2D();
    private Vector2D diff = new Vector2D();
    private Vector2D delta = new Vector2D();
    private boolean moving  = false;
    private boolean zooming = false;
    private float pLength = 0;
	
	@Override
	public void update(float deltaTime) {
		
		
		if (this.g) {
			this.world.update(deltaTime);
		} else {
			this.world.u();
		}
		
		List <TouchEvent> touchEvents = openGLWorker.getInput().getTouchEvents();

		int max = 0;

        for (int i = 0; i < touchEvents.size(); i ++) {
            if (touchEvents.get(i).pointer > max) {
                max = touchEvents.get(i).pointer;
            }
        }

        if (max > 1) {
            moving  = false;
            zooming = false;
            return;
        }

        if (max == 1) {
            moving = false;

            if (touchEvents.size() > 1) {
                for (int i = 0; i < touchEvents.size(); i += 2) {

                    if (i + 1 == touchEvents.size()) {
                        break;
                    }

                    TouchEvent event_0 = touchEvents.get(i + 0);
                    TouchEvent event_1 = touchEvents.get(i + 1);
                    
                    camera2D.touchToWorld_no_zoom(event_0);
                    camera2D.touchToWorld_no_zoom(event_1);
                    
            		
            		
                    diff.set(event_0.touchPosition).subtract(event_1.touchPosition);

                    if (!zooming) {
                        pLength = diff.length();
                        zooming = true;
                        continue;
                    }
                    
                   

                    float t = diff.length();

                //    Log.d("update", "" + t + " k " + event_0.pointer + " " + event_1.pointer);
                    camera2D.zoom(pLength - t);

                    pLength = t;
                    
                    float tt = 2 * (Camera2D.ZOOM / Camera2D.MAX_ZOOM);
                    
                    button1.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 5  * tt));
            		button2.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 11 * tt)); 
            		button3.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 17 * tt));
            		button4.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 23 * tt));
            		button5.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 5  * tt));
            		button6.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 11 * tt));
            		button7.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 17 * tt));
            		button8.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 23 * tt));
            		button9.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 29 * tt));

                }
            }


            return;
        }

        zooming = false;

        if (max == 0) {

            for (int i = 0; i < touchEvents.size(); i ++) {
                TouchEvent event = touchEvents.get(i);
                camera2D.touchToWorld(event);
                
                Vector2D realPos = event.touchPosition.copy().add(camera2D.left, camera2D.bottom);
                
            /*    Log.d("mylog", "x = " + event.touchPosition.x);
                Log.d("mylog", "y = " + event.touchPosition.y);*/
                
                if (event.type == TouchEvent.TOUCH_DOWN) {
                    touchedDown.set(event.touchPosition);
                    
                    
                    
                    if (!UICoordinator.isInViewArea(realPos)) {
                    	Random r = new Random();
                        
                  //  	Log.d("colorr", "WE ARE HERE");
                    	
                    /*	Body circle = ShapeModeler.createBodyFromColor(new Mass(), vertexBatcher2D, new Vector3D( 0.5f + 0.5f *r.nextFloat(), 0.5f + 0.5f *r.nextFloat(), 0.5f + 0.5f *r.nextFloat()), 0.4f, 4 * r.nextFloat());
                    	circle.translate(realPos);*/
                    	
                		if (b) {
                			
                			Vector2D[] pointsTriangle = {
                    	    		new Vector2D( 0.0f,  1.0f), 
                    				new Vector2D(-1.0f, -1.0f), 
                    				new Vector2D( 1.0f, -1.0f)
                    	    };
                        	
                        	
                        	Vector2D[] pointsHexagon = {
                    				new Vector2D( 0.0f, -0.50f), 
                    				new Vector2D( 0.5f, -0.25f), 
                    				new Vector2D( 0.5f,  0.25f),
                    				new Vector2D( 0.0f,  0.50f),
                    				new Vector2D(-0.5f,  0.25f),
                    				new Vector2D(-0.5f, -0.25f)
                    				
                    		};
                    		
                    		
                    		Body hexagon = null;
                            
                    		if (r.nextDouble() > 0.5) {
                    			hexagon = ShapeModeler.createBodyFromColor(new Mass(0.2f, 1f), vertexBatcher2D, new Vector3D(r.nextFloat(), r.nextFloat(), r.nextFloat()), 0.3f, 1 + r.nextFloat(), 1 + r.nextFloat(), r.nextFloat() > 0.5f ? pointsTriangle : pointsTriangle);
                    		} else {
                    			hexagon = ShapeModeler.createBodyFromTexture(new Mass(0.2f, 3f), vertexBatcher2D, Assets.crate, new Vector2D(128, 128), 50f, 4 * r.nextFloat());
                    		}
                    		
                    		hexagon.translate(realPos);
                    		hexagon.rotate(r.nextFloat());
                    	//	hexagon.addForce(new Vector2D(0, -9.8f), -1);
                			
                			this.world.addBody(hexagon);
                		}
                		
                    }
                    
                    
                    
                    moving = true;
                //    this.button1.setPosition(new Vector2D(camera2D.left + 4, camera2D.bottom + 4));
                    
                }

                if (event.type == TouchEvent.TOUCH_UP) {
                    touchedDown.set(event.touchPosition);
                    moving = false;
                }

                if (event.type == TouchEvent.TOUCH_DRAGGED && moving) {
                    delta.set(touchedDown).subtract(event.touchPosition);
                    touchedDown.set(event.touchPosition);
                    camera2D.translateViewport(delta);
                    
                    
                }
                
                event.touchPosition.add(camera2D.left, camera2D.bottom);
          //      Log.d("tagg", "x = " + event.touchPosition.x + " y = " + event.touchPosition.y);
                
                UICoordinator.updateViews(event, realPos);
                
                

            }
        }
		
		List<KeyEvent> keyEvents = openGLWorker.getInput().getKeyEvents();
		
		int len = keyEvents.size();
		for (int i = 0; i < len; i ++) { 
			KeyEvent event = keyEvents.get(i);
		
			if (event.type == KeyEvent.KEY_UP) 
				continue;
		
			switch (event.keyCode) {
			case KeyEvent.VOLUME_UP:
				openGLWorker.getAudio().volumeUp();
				break;
			case KeyEvent.VOLUME_DOWN:
				openGLWorker.getAudio().volumeDown();
				break;
			case KeyEvent.VOLUME_MUTE: 
				openGLWorker.getAudio().volumeMute();
				break;
			default:
				break;
			}
		
		}	
		
		float tt = 2 * (Camera2D.ZOOM / Camera2D.MAX_ZOOM);
        
        button1.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 5  * tt));
		button2.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 11 * tt));  // up
		button3.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 17 * tt));  // down
		button4.setPosition(new Vector2D(camera2D.right - 10 * tt, camera2D.bottom + 23 * tt));  // down
		button5.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 5  * tt));
		button6.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 11 * tt));
		button7.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 17 * tt));
		button8.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 23 * tt));
		button9.setPosition(new Vector2D(camera2D.left  + 10 * tt, camera2D.bottom + 29 * tt));
		
	}
	
	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

	private boolean g = false;

	private Force angularWheel1Force = new AngularForce(-50f);
	private boolean f1 = true;
	
	private Force angularWheel2Force = new AngularForce(+50f);
	private boolean f2 = true;
	
	
	@Override
	public void onClick(View v) {
		
		switch (v.id) {
		case 0:
			b = !b;
			break;
		case 1:
			this.world.removeJoint(this.jointL);
			break;
		case 2:
			this.world.getBody(10).translate(-0.4f, 0);
			break;
		case 3:
			this.world.getBody(10).translate(0.4f, 0);
			break;
		case 4:
			g = !g;
			break;
			
		case 5:
			if (f1) {
			//	this.world.getBody(19).angularVelocity = 0;
				this.world.getBody(20).addForce(this.angularWheel1Force);
			} else {
				this.world.getBody(20).removeForce(this.angularWheel1Force);
			}
			
			f1 = !f1;
			
		//	this.world.getBody(this.world.getBodyCount() - 1).translate(new Vector2D(0, -0.1f));
			break;
			
		case 6:
			if (f2) {
				this.world.getBody(21).addForce(this.angularWheel2Force);
			//	this.world.getBody(18).angularVelocity = 0;
			} else {
				this.world.getBody(21).removeForce(this.angularWheel2Force);
			}
			
			f2 = !f2;
			
		//	this.world.getBody(this.world.getBodyCount() - 1).translate(new Vector2D(0, 0.1f));
			break;
			
		case 7:
			this.world.getBody(this.world.getBodyCount() - 1).translate(new Vector2D(-0.1f, 0));
			break;
		case 8:
			this.world.getBody(this.world.getBodyCount() - 1).translate(new Vector2D(0.1f, 0));
			break;
		}
		
		
		
	}

}
