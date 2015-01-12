package com.almagems.mineraider.objects;

import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;

import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Physics;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Vector;


public class MineCart {
	public Body cart;
	Body wheel1;
	Body wheel2;
	
	WheelJoint wheelJoint1;
	WheelJoint wheelJoint2;
	
	private static int collisionGroupIndexCounter = -1;
	
	private int collisionGroupIndex = -42; // negative value = don't collide!
	
	private boolean Stopped = false;
	private boolean check = true;
	private boolean collisionStop = false;
	
	float r = 0.0f;
	int stopTimeout = 0;
	int collisionStopTimer = 0;
	
	private ObjectPosition _op = new ObjectPosition();
	
	public MineCart(float x, float y) {
		
		--collisionGroupIndexCounter;
		collisionGroupIndex = collisionGroupIndexCounter;
		
		System.out.println("Collision Group Index: " + collisionGroupIndex);
				
		CreateCart(x, y);
		wheel1 = CreateWheel(x - 2.0f, y - 2.2f);
		wheel2 = CreateWheel(x + 2.0f, y - 2.2f);
		CreateWheelJoint();		
	}
	
	private void CreateWheelJoint() {
		Physics physics = Physics.getInstance();
		
		WheelJointDef wd =  new WheelJointDef();
		wd.bodyA = cart;
		wd.bodyB = wheel1;
		wd.localAnchorA.set(-1.6f, -2.2f);
		wd.frequencyHz = 6;
		wd.dampingRatio = 0.3f;
		wd.maxMotorTorque = 1000;
		wd.motorSpeed = -6.0f;
		wd.enableMotor = true;
		wd.localAxisA.set(0f, 1f);
		
		wheelJoint1 = (WheelJoint)physics.world.createJoint(wd);
		
		wd.bodyB = wheel2;
		wd.localAnchorA.set(1.6f, -2.2f);		
		wheelJoint2 = (WheelJoint)physics.world.createJoint(wd);
		
//		Vec2 axis = new Vec2(0.0f, 0.9f);
		
//		wd = new WheelJointDef();	
//		wd.dampingRatio = 0.9f;
//		wd.motorSpeed = 0.0f;
//		wd.maxMotorTorque = 0.0f;		
//		wd.enableMotor = false;	
				
//		wd.initialize(cart, wheel1, wheel1.getPosition(), axis);
//		wheelJoint1 = (WheelJoint)physics.world.createJoint(wd);		
//	
//		wd.initialize(cart, wheel2, wheel1.getPosition(), axis);
//		wheelJoint2 = (WheelJoint)physics.world.createJoint(wd);
	}
	
	private Body CreateWheel(float x, float y) {
		CircleShape shape = new CircleShape();
		shape.m_radius = 0.8f;
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.restitution = 0.2f;
		fixtureDef.filter.groupIndex = collisionGroupIndex;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		Physics physics = Physics.getInstance();
		Body body = physics.world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		return body;
	}
	
	private void CreateCart(float x, float y) {		
		PolygonShape shape = new PolygonShape();
		Vec2[] vertices = new Vec2[4];
		vertices[0] = new Vec2(-3.2f, -2.0f);
		vertices[1] = new Vec2( 3.2f, -2.0f);
		vertices[2] = new Vec2( 3.4f, -1.0f);
		vertices[3] = new Vec2(-3.4f, -1.0f);
		shape.set(vertices, vertices.length);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1.0f;
		fixture.restitution = 0.1f;
		fixture.filter.groupIndex = collisionGroupIndex;

		
		PolygonShape shape1 = new PolygonShape();
		Vec2[] vertices1 = new Vec2[4];
		vertices1[0] = new Vec2(-3.4f, -1.0f);
		vertices1[1] = new Vec2(-3.0f, -1.0f);
		vertices1[2] = new Vec2(-4.0f,  2.2f);
		vertices1[3] = new Vec2(-3.6f,  2.2f);
		shape1.set(vertices1, vertices1.length);
		
		FixtureDef fixture1 = new FixtureDef();
		fixture1.shape = shape1;
		fixture1.density = 1.0f;
		fixture1.restitution = 0.1f;
		fixture1.filter.groupIndex = collisionGroupIndex;

		
		PolygonShape shape2 = new PolygonShape();
		Vec2[] vertices2 = new Vec2[4];
		vertices2[0] = new Vec2( 3.4f, -1.0f);
		vertices2[1] = new Vec2( 3.0f, -1.0f);
		vertices2[2] = new Vec2( 4.0f,  2.2f);
		vertices2[3] = new Vec2( 3.6f,  2.2f);
		shape2.set(vertices2, vertices2.length);
		
		FixtureDef fixture2 = new FixtureDef();
		fixture2.shape = shape2;
		fixture2.density = 1.0f;
		fixture2.restitution = 0.1f;
		fixture2.filter.groupIndex = collisionGroupIndex;
		
		
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);		
		
		Physics physics = Physics.getInstance();
		cart = physics.world.createBody(bodyDef);
		cart.m_userData = this;
		cart.createFixture(fixture);
		cart.createFixture(fixture1);
		cart.createFixture(fixture2);		
	}
	
	public void stop() {
		wheelJoint1.setMotorSpeed(0.0f);
		wheelJoint2.setMotorSpeed(0.0f);
		collisionStop = true;
		collisionStopTimer = 0;
	}
	
	public void update() {		
		Vec2 pos = cart.getPosition();
		
		if (collisionStop) {
			++collisionStopTimer; 
			
			if (collisionStopTimer > 100) {
				wheelJoint1.setMotorSpeed(-3.0f);
				wheelJoint2.setMotorSpeed(-3.0f);
				collisionStop = false;
			}
		}		
		
		if (check) {		
			if (!Stopped) {		
				if (pos.x > -0.25f && pos.x < 0.25f) {
					wheelJoint1.setMotorSpeed(0.0f);
					wheelJoint2.setMotorSpeed(0.0f);
					Stopped = true;
				}
			} else {
				++stopTimeout;
				
				if (stopTimeout > 300) {
					wheelJoint1.setMotorSpeed(-3.0f);
					wheelJoint2.setMotorSpeed(-3.0f);
					check = false;
				}
			}
		} else {	
			if (pos.x > 19.0f) {
				Random rand = new Random();
				pos.x = -19.0f - (rand.nextFloat() * 3f);
				pos.y = -16.5f;
				cart.setTransform(pos, 0f);
				wheel1.setTransform(pos, 0f);
				wheel2.setTransform(pos, 0f);
				check = true;
				stopTimeout = 0;
				Stopped = false;
			}
		}
		/*
		if (pos.x > 8.0f) {
			wheelJoint1.setMotorSpeed(1.0f);
			wheelJoint2.setMotorSpeed(1.0f);
		}
		
		if (pos.x < -8.0f) {
			wheelJoint1.setMotorSpeed(-1.0f);
			wheelJoint2.setMotorSpeed(-1.0f);
		}
		*/
	}
	
	private void drawCartFixture() {
		Visuals visuals = Visuals.getInstance();
		
		Vec2 pos = cart.getPosition();
		float angle = cart.getAngle();
		float degree = (float) Math.toDegrees(angle);
		
		//System.out.println("Cart: " + pos.x + ", " + pos.y);
		
		
		EdgeDrawer edgeDrawer = new EdgeDrawer(100);
		Fixture fixture = cart.getFixtureList();
        ObjectPosition op = new ObjectPosition();
		while(fixture != null) {
			PolygonShape shape = (PolygonShape)fixture.getShape();
			edgeDrawer.begin();
			Vec2 v0 = shape.m_vertices[0];
			Vec2 v1 = shape.m_vertices[1];
			Vec2 v2 = shape.m_vertices[2];
			Vec2 v3 = shape.m_vertices[3];
			
			edgeDrawer.addLine(	v0.x, v0.y, 0.0f,
								v1.x, v1.y, 0.0f );

			edgeDrawer.addLine(	v1.x, v1.y, 0.0f,
								v2.x, v2.y, 0.0f );

			edgeDrawer.addLine(	v2.x, v2.y, 0.0f,
								v3.x, v3.y, 0.0f );
			
			edgeDrawer.addLine(	v3.x, v3.y, 0.0f,
								v0.x, v0.y, 0.0f );

			op.setPosition(pos.x, pos.y, 1.0f);
			op.setRot(0f, 0f, degree);
			op.setScale(1f, 1f, 1f);
			visuals.calcMatricesForObject(op);
			
//			setIdentityM(visuals.modelMatrix, 0);							
//			translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
//			rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);													
//			multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
			
			visuals.colorShader.useProgram();
			visuals.colorShader.setUniforms(visuals.mvpMatrix, new MyColor(1f, 1f, 1f) );
			edgeDrawer.bindData(visuals.colorShader);
			edgeDrawer.draw();
			fixture = fixture.getNext();
		}
	}
	
	public void draw() {
		Visuals visuals = Visuals.getInstance();
								
		//Vec2 pos = cart.getLocalCenter();
		Vec2 pos = cart.getPosition();
		float angle = cart.getAngle();
		float degree = (float)Math.toDegrees(angle);
		
		// cart					
		_op.setScale(1f, 1f, 1f);				
		_op.setRot(0f, 0f, degree);		
		_op.setPosition(pos.x, pos.y, 1f); //position;
		visuals.calcMatricesForObject(_op);	
		
		MyColor color = new MyColor(1f, 1f, 1f);
		
		visuals.dirLightShader.setTexture(visuals.textureCart);
		visuals.dirLightShader.setUniforms(color, visuals.lightColor, visuals.lightNorm);		
		visuals.mineCart.bindData(visuals.dirLightShader);
		visuals.mineCart.draw();
//		visuals.rampa.bindData(visuals.dirLightShader);
//		visuals.rampa.draw();
//		visuals.mineCartWheel.bindData(visuals.dirLightShader);
//		visuals.mineCartWheel.draw();
		
		//r+=1.0f;
		
		
		
		// wheels
		visuals.dirLightShader.setTexture(visuals.textureWheel);
		
		// wheel 1
		//op.position = new Vector(pos.x - 1.5f, pos.y - 1.5f, 2f);
		pos = wheel1.getPosition();
		angle = wheel1.getAngle();
		degree = (float)Math.toDegrees(angle);
		_op.setPosition(pos.x, pos.y, 2f);		
		_op.setRot(0f, 0f, degree);
		
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.setUniforms(color, visuals.lightColor, visuals.lightNorm);		
		visuals.wheel.bindData(visuals.dirLightShader);
		visuals.wheel.draw();
			
		// wheel 2
		//op.position = new Vector(pos.x + 1.5f, pos.y - 1.5f, 2f);
		pos = wheel2.getPosition();
		angle = wheel2.getAngle();
		degree = (float)Math.toDegrees(angle); 

		_op.setPosition(pos.x, pos.y, 2f);
		_op.setRot(0f, 0f, degree);
		
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.setUniforms(color, visuals.lightColor, visuals.lightNorm);		
		visuals.wheel.bindData(visuals.dirLightShader);
		visuals.wheel.draw();
		
		//glDisable(GL_DEPTH_TEST);
		//drawCartFixture();
		//glEnable(GL_DEPTH_TEST);
	}
}
