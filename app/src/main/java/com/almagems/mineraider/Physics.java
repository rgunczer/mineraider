package com.almagems.mineraider;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.FixtureDef;

import com.almagems.mineraider.Constants;

import java.util.ArrayList;

public class Physics {
	private static Physics instance = null;
	
	public static int velIterations = 10; //4;
	public static int posIterations = 10; //8;
	
	public ArrayList<Body> balls = new ArrayList<Body>();
	public ArrayList<Body> polygons = new ArrayList<Body>();
	public ArrayList<Body> boxes = new ArrayList<Body>();
	public ArrayList<Body> edges = new ArrayList<Body>();
	
	public CollisionHandler collisionHandler = new CollisionHandler();
	
	public World world;
		
	public static Physics getInstance() {
		if (instance == null) {
			instance = new Physics(); 
		}
		return instance;
	}

	private Physics() {
		Vec2 gravity = new Vec2(0.0f, -32.0f);
		world = new World(gravity);
		world.setSleepingAllowed(true);
		world.setContactListener(collisionHandler);
	}

	
	// Fragments	
	
	public void addFragmentGem0(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		float d = Constants.GEM_FRAGMENT_SIZE;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(d, d);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Constants.GEM_DENSITY;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		body.m_userData = new Integer(Constants.GEM_TYPE_0);
		
		boxes.add(body);		
	}
	
	public void addFragmentGem1(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		float d = Constants.GEM_FRAGMENT_SIZE;
		Vec2[] vertices = new Vec2[4];
		vertices[0] = new Vec2( 0, d);
		vertices[1] = new Vec2( d, 0);
		vertices[2] = new Vec2( 0, -d);
		vertices[3] = new Vec2( -d, 0);
		int count = 4;		

		PolygonShape shape = new PolygonShape();
		shape.set(vertices, count);		
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Constants.GEM_DENSITY;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		body.m_userData = new Integer(Constants.GEM_TYPE_1);
		
		boxes.add(body);				
	}

	public void addFragmentGem2(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);

		float d = Constants.GEM_FRAGMENT_SIZE;
		float d3 = (d / 3.0f) * 2.0f;
		int count = 5;
		Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2( -d3, -d);
		vertices[1] = new Vec2( -d, d/3);
		vertices[2] = new Vec2(  0, d);
		vertices[3] = new Vec2(  d, d/3);
		vertices[4] = new Vec2(  d3, -d);		
		
		PolygonShape shape = new PolygonShape();
		shape.set(vertices, count);		
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Constants.GEM_DENSITY;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		body.m_userData = new Integer(Constants.GEM_TYPE_2);
		
		boxes.add(body);		
	}
	
	public void addFragmentGem3(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);

		float d = Constants.GEM_FRAGMENT_SIZE;
		int count = 6;
		Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2(-d, -d/2);
		vertices[1] = new Vec2(-d,  d/2);
		vertices[2] = new Vec2( 0,  d);
		vertices[3] = new Vec2( d,  d/2);
		vertices[4] = new Vec2( d, -d/2);
		vertices[5] = new Vec2( 0, -d);
		
		PolygonShape shape = new PolygonShape();
		shape.set(vertices, count);		
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Constants.GEM_DENSITY;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		body.m_userData = new Integer(Constants.GEM_TYPE_3);
		
		boxes.add(body);		
	}

	public void addFragmentGem4(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		float d = Constants.GEM_FRAGMENT_SIZE;
		
		Vec2[] vertices = new Vec2[3];
		vertices[0] = new Vec2(-d, -d);
		vertices[1] = new Vec2( d, -d);
		vertices[2] = new Vec2( 0f, d);
		int count = 3;
		
		PolygonShape shape = new PolygonShape();
		shape.set(vertices, count);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Constants.GEM_DENSITY;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		body.m_userData = new Integer(Constants.GEM_TYPE_4);
		
		boxes.add(body);
	}
	
	public void addFragmentGem5(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);

		float d = Constants.GEM_FRAGMENT_SIZE;
		int count = 8;
		Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2(-d, -d/2);
		vertices[1] = new Vec2(-d,  d/2);
		vertices[2] = new Vec2(-d/2, d);
		vertices[3] = new Vec2( d/2, d);
		vertices[4] = new Vec2( d, d/2);
		vertices[5] = new Vec2( d, -d/2);
		vertices[6] = new Vec2( d/2, -d);
		vertices[7] = new Vec2(-d/2, -d);
		
		PolygonShape shape = new PolygonShape();
		shape.set(vertices, count);		
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Constants.GEM_DENSITY;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		body.m_userData = new Integer(Constants.GEM_TYPE_5);
		
		boxes.add(body);
	}
	
	public void addFragmentGem6(float x, float y) {		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);

		float d = Constants.GEM_FRAGMENT_SIZE;	
		float d2 = (d / 3.0f) * 2.0f;
		int count = 8;
		Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2(-d, 0f);
		vertices[1] = new Vec2(-d2, d2);
		vertices[2] = new Vec2( 0f, d);
		vertices[3] = new Vec2( d2, d2);
		vertices[4] = new Vec2( d, 0f);		
		vertices[5] = new Vec2( d2, -d2);
		vertices[6] = new Vec2( 0f, -d);
		vertices[7] = new Vec2(-d2, -d2);
		
		PolygonShape shape = new PolygonShape();
		shape.set(vertices, count);		
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Constants.GEM_DENSITY;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		body.m_userData = new Integer(Constants.GEM_TYPE_6);
		
		boxes.add(body);				
	}
		
	// end of fragments
	
	
	public void addBall(float x, float y, float radius) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		CircleShape shape = new CircleShape();
		shape.m_radius = radius;
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1.0f;
		//fixture.restitution = 0.1f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		balls.add(body);		
	}
	
	public void addPolygon(float x, float y, float[] verts) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		Vec2[] vertices = new Vec2[ verts.length ];
		for (int i = 0; i < verts.length; i+=2) {
			vertices[i] = new Vec2(verts[i], verts[i+1]);
		}
		
		PolygonShape shape = new PolygonShape();
		shape.set(vertices, verts.length);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1.0f;
		fixture.restitution = 0.8f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		polygons.add(body);
	}
	
	public void addEdge(float x1, float y1, float x2, float y2) {
		BodyDef bodyDef = new BodyDef();
		
		EdgeShape shape = new EdgeShape();
		shape.set(new Vec2(x1, y1), new Vec2(x2, y2));
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.filter.groupIndex = -42;
		
		Body body = world.createBody(bodyDef);
		//body.createFixture(shape, 0);
		body.createFixture(fixture);
		
		edges.add(body);
	}
	
	
	
	
		
	
	
	
	public void addBox1(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1.0f, 1.0f);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1.0f;
		fixture.restitution = 0.1f;
		//fixture.friction = 0.75f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		
		boxes.add(body);
	}
		
	public void addBoxStatic(float x, float y, float angle, float w, float h) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(x, y);
		bodyDef.angle = (float)Math.toRadians(angle);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w/2.0f, h/2.0f);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1.0f;
		fixture.restitution = 0.1f;
		fixture.friction = 0.5f;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		
		boxes.add(body);
	}
	
	public void update() {
		world.step(1.0f/30.0f, velIterations, posIterations);
		Body body = null;
		int size = boxes.size();
		for(int i = 0; i < size; ++i) {
			body = boxes.get(i);
			if (body.m_type == BodyType.DYNAMIC && body.m_userData != null) {			
				Vec2 pos = body.getPosition();
				
				if (pos.y < -18.7f) {
					world.destroyBody(body);
					boxes.remove(body);
					break;
				}
				
				if (pos.x > 20.0f) {
					world.destroyBody(body);
					boxes.remove(body);
					break;
				}				
			}
			
			//float angle = body.getAngle();
			//System.out.println("box2d x: " + pos.x + ", y: " + pos.y + ", angle: " + angle);
		}

	}
}
