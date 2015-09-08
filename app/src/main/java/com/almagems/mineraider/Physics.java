package com.almagems.mineraider;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.dynamics.FixtureDef;

import static com.almagems.mineraider.Constants.*;

import java.util.ArrayList;
import java.util.Random;


public final class Physics {

	public final CollisionHandler collisionHandler;
	
	private final Random random = new Random();
		
	public final static int velIterations = 4;
	public final static int posIterations = 8;

	// pool of fragments
	private final ArrayList<ArrayList<Body>> fragmentsPool;
	public final ArrayList<Body> fragments;
	public final ArrayList<Body> statics;
	public final ArrayList<Body> edges;
	public final ArrayList<Body> fragmentToPool;

	private final World world;

	// ctor
	public Physics(Game game, int poolSize) {
		collisionHandler = new CollisionHandler(game);

		final Vec2 gravity = new Vec2(0.0f, -48.0f);
		world = new World(gravity);
		world.setSleepingAllowed(true);
		world.setContactListener(collisionHandler);

		gemBodyDef.type = BodyType.DYNAMIC;
		gemBodyDef.allowSleep = true;

		gemFixtureDef.density = GEM_DENSITY;

		fragmentsPool = new ArrayList<ArrayList<Body>>(MAX_GEM_TYPES);
		for(int i = 0; i < MAX_GEM_TYPES; ++i) {
			fragmentsPool.add(new ArrayList<Body>(poolSize));
		}

		fragments = new ArrayList<Body>();
		statics = new ArrayList<Body>();
		edges = new ArrayList<Body>();
		fragmentToPool = new ArrayList<Body>(100);
	}

	private Body getBodyFromPool(int gemType) {
		ArrayList<Body> pool = fragmentsPool.getAt(gemType);
		Body body;
		int size = pool.size();
		if (size > 0) {
			body = pool.get( size - 1 );
			pool.remove( size - 1 );
		} else {
			switch (gemType) {
				case 0: body = createFragmentGem0(); break;
				case 1: body = createFragmentGem1(); break;
				case 2: body = createFragmentGem2(); break;
				case 3: body = createFragmentGem3(); break;
				case 4: body = createFragmentGem4(); break;
				case 5: body = createFragmentGem5(); break;
				case 6: body = createFragmentGem6(); break;
			}
		}

		if (body == null) {
			// TODO: place a breakpoint here
			System.out.println("Body is null in getBodyFromPool....");
		}

		return body;
	}

	public void clear() {
		collectObjectsToPool();
		sortFragmentsFromPool();
	}

	private void setBodyPosAndRot(Body body, float x, float y) {
		float angle = (float)Math.toRadians( random.nextFloat() * 360f );
		Vec2 pos = new Vec2(x, y)
		body.setTransform(pos, angle);
		body.setActive(true);
	}

// ADD
	public void addFragmentGem0(float x, float y) {
		final int gemType = GEM_TYPE_0;
		Body body = getBodyFromPool(gemType);
		setBodyPosAndRot(body, x, y);
		fragments.add(body);
	}

	public void addFragmentGem1(float x, float y) {
		final int gemType = GEM_TYPE_1;
		Body body = getBodyFromPool(gemType);
		setBodyPosAndRot(body, x, y);
		fragments.add(body);
	}

	public void addFragmentGem2(float x, float y) {
		final int gemType = GEM_TYPE_2;
		Body body = getBodyFromPool(gemType);
		setBodyPosAndRot(body, x, y);
		fragments.add(body);
	}

	public void addFragmentGem3(float x, float y) {
		final int gemType = GEM_TYPE_3;
		Body body = getBodyFromPool(gemType);
		setBodyPosAndRot(body, x, y);
		fragments.add(body);
	}

	public void addFragmentGem4(float x, float y) {
		final int gemType = GEM_TYPE_4;
		Body body = getBodyFromPool(gemType);
		setBodyPosAndRot(body, x, y);
		fragments.add(body);
	}

	public void addFragmentGem5(float x, float y) {
		final int gemType = GEM_TYPE_5;
		Body body = getBodyFromPool(gemType);
		setBodyPosAndRot(body, x, y);
		fragments.add(body);
	}

	public void addFragmentGem6(float x, float y) {
		final int gemType = GEM_TYPE_6;
		Body body = getBodyFromPool(gemType);
		setBodyPosAndRot(body, x, y);
		fragments.add(body);
	}

// CREATE
	private Body createFragmentGem0() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.angle = (float)Math.toRadians( random.nextFloat() * 360f );

		final float d = GEM_FRAGMENT_SIZE;
		final int count = 4;
		final Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2( 0, d);
		vertices[1] = new Vec2( d, 0);
		vertices[2] = new Vec2( 0, -d);
		vertices[3] = new Vec2( -d, 0);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices, count);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0.1f;
		//fixture.friction = 0.75f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.m_userData = new Integer(GEM_TYPE_0);

		return body;
	}

	private Body createFragmentGem1() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.angle = (float)Math.toRadians( random.nextFloat() * 360f );

		final float d = GEM_FRAGMENT_SIZE;
		final int count = 8;
		final Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2( -d*0.8f,  d*0.5f);
		vertices[1] = new Vec2( -d*0.3f,  d);
		vertices[2] = new Vec2(  d*0.3f,  d);
		vertices[3] = new Vec2(  d*0.8f,  d*0.5f);
		vertices[4] = new Vec2(  d*0.8f, -d*0.5f);
		vertices[5] = new Vec2( -d*0.3f, -d);
		vertices[6] = new Vec2(  d*0.3f, -d);
		vertices[7] = new Vec2( -d*0.8f, -d*0.5f);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices, count);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0.1f;
		//fixture.friction = 0.75f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.m_userData = new Integer(GEM_TYPE_1);

		return body;
	}

	private Body createFragmentGem2() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.angle = (float)Math.toRadians( random.nextFloat() * 360f );

		final float d = GEM_FRAGMENT_SIZE;
		final float d3 = (d / 3.0f) * 2.0f;
		final int count = 5;
		final Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2( -d3, -d);
		vertices[1] = new Vec2( -d, d/3);
		vertices[2] = new Vec2(  0, d);
		vertices[3] = new Vec2(  d, d/3);
		vertices[4] = new Vec2(  d3, -d);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices, count);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0.1f;
		//fixture.friction = 0.75f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.m_userData = new Integer(GEM_TYPE_2);

		return body;
	}

	private Body createFragmentGem3() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.angle = (float)Math.toRadians( random.nextFloat() * 360f );

		final float d = GEM_FRAGMENT_SIZE;
		final int count = 6;
		final Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2(-d*0.8f, -d/2);
		vertices[1] = new Vec2(-d*0.8f,  d/2);
		vertices[2] = new Vec2( 0,  d);
		vertices[3] = new Vec2( d*0.8f,  d/2);
		vertices[4] = new Vec2( d*0.8f, -d/2);
		vertices[5] = new Vec2( 0, -d);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices, count);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0.1f;
		//fixture.friction = 0.75f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.m_userData = new Integer(GEM_TYPE_3);

		return body;
	}

	private Body createFragmentGem4() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.angle = (float)Math.toRadians( random.nextFloat() * 360f );
		
		final float d = GEM_FRAGMENT_SIZE;
		final int count = 8;
		final Vec2[] vertices = new Vec2[ count ];
		vertices[0] = new Vec2(0f, -d);
		vertices[1] = new Vec2(-d*0.6f, -d*0.9f);
		vertices[2] = new Vec2(-d*0.9f, -d*0.3f);
		vertices[3] = new Vec2(-d*0.8f, 0f);
		vertices[4] = new Vec2( 0f, d);
		vertices[5] = new Vec2( d*0.8f, 0f);
		vertices[6] = new Vec2( d*0.9f, -d*0.3f);
		vertices[7] = new Vec2( d*0.6f, -d*0.9f);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices, count);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0.1f;
		//fixture.friction = 0.75f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.m_userData = new Integer(GEM_TYPE_4);

		return body;
	}

	private Body addFragmentGem5() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.angle = (float)Math.toRadians( random.nextFloat() * 360f );

		final float d = GEM_FRAGMENT_SIZE;
		final int count = 7;
		final Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2(0f, -d);
		vertices[1] = new Vec2(-d*0.9f, 0f);
		vertices[2] = new Vec2(-d, d*0.5f);
		vertices[3] = new Vec2(-d*0.5f, d);
		vertices[4] = new Vec2(d*0.5f, d);
		vertices[5] = new Vec2(d, d*0.5f);
		vertices[6] = new Vec2(d*0.9f, 0f);
		//vertices[7] = new Vec2(d*0.9f, 0f);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices, count);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0.1f;
		//fixture.friction = 0.75f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.m_userData = new Integer(GEM_TYPE_5);

		return body;
	}

	private Body createFragmentGem6() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.angle = (float)Math.toRadians( random.nextFloat() * 360f );

		final float d = GEM_FRAGMENT_SIZE;
		final float d2 = (d / 3.0f) * 2.0f;
		final int count = 8;
		final Vec2[] vertices = new Vec2[count];
		vertices[0] = new Vec2(-d,   0f);
		vertices[1] = new Vec2(-d2,  d2);
		vertices[2] = new Vec2( 0f,  d);
		vertices[3] = new Vec2( d2,  d2);
		vertices[4] = new Vec2( d,   0f);
		vertices[5] = new Vec2( d2, -d2);
		vertices[6] = new Vec2( 0f, -d);
		vertices[7] = new Vec2(-d2, -d2);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices, count);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0.1f;
		//fixture.friction = 0.75f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.m_userData = new Integer(GEM_TYPE_6);

		return body;
	}

	public void addEdge(float x1, float y1, float x2, float y2) {
		BodyDef bodyDef = new BodyDef();

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(new Vec2(x1, y1), new Vec2(x2, y2));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = edgeShape;
		fixtureDef.filter.groupIndex = -42;

		Body body = world.createBody(bodyDef);		
		body.createFixture(fixtureDef);

		edges.add(body);
	}

	public Body addBoxStatic(float x, float y, float angle, float w, float h) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(x, y);
		bodyDef.angle = (float)Math.toRadians(angle);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(w/2.0f, h/2.0f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 1.0f;
		fixtureDef.restitution = 0.1f;
		fixtureDef.friction = 0.5f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);

		statics.add(body);

		return  body;
	}

	public void update() {
		world.step(1.0f / 30.0f, velIterations, posIterations);

		collectObjectsToPool();
		sortFragmentsFromPool();
	}

	private void collectObjectsToPool() {
		fragmentToPool.clear();
		Body body;
		Vec2 pos;
		int size = fragments.size();
		for (int i = 0; i < size; ++i) {
			body = fragments.get(i);
			pos = body.getPosition();

			if (pos.y < -18.7f && pos.x < 15f) {
				fragmentToPool.add(body);
			}
		}
	}

	public void sortFragmentsFromPool() {		
		Body body;
		int size = fragmentToPool.size();
		for(int i = 0; i < size; ++i) {
			body = fragmentToPool.get(i);
			body.setActive(false);
			fragments.remove(body);
			//destroyObject(body);
			poolObject(body);
		}
		fragmentToPool.clear();
	}

	private void destroyObject(Body body) {
		world.destroyBody(body);
	}

	private void poolObject(Body body) {
		body.setActive(false);
		int gemType = (Integer)body.m_userData;
		ArrayList<Body> pool = fragmentsPool.getAt(gemType);
		pool.add(body);
	}

}