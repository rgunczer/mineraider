package com.almagems.mineraider;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;

import static com.almagems.mineraider.Constants.*;

import java.util.ArrayList;


public final class Physics {

    private static int collisionGroupIndexCounter = -1;
    private static int collisionGroupIndex = -42; // negative value = don't collide!

    private static Vec2 vector = new Vec2(0f, 0f);
    private static Vec2 pos = new Vec2(0f, 0f);
    private static Vec2 posZero = new Vec2(0f, 0f); 

    private static ArrayList<ArrayList<Body>> fragmentsPool;
    public static ArrayList<Body> fragments;
    public static ArrayList<Body> statics;
    public static ArrayList<Body> edges;
    public static ArrayList<Body> fragmentToPool;

    public static Body cart1body;
    public static Body cart1wheel1;
    public static Body cart1wheel2;
    public static WheelJoint cart1wheelJoint1;
    public static WheelJoint cart1wheelJoint2;  

    public static Body cart2body;
    public static Body cart2wheel1;
    public static Body cart2wheel2;
    public static WheelJoint cart2wheelJoint1;
    public static WheelJoint cart2wheelJoint2;  

    public static World world;

    static {
        final Vec2 gravity = new Vec2(0.0f, -48.0f);
        world = new World(gravity);
        world.setSleepingAllowed(true);
        world.setContactListener(new CollisionHandler());

        final int poolSize = 20;
        fragmentsPool = new ArrayList<ArrayList<Body>>(MAX_GEM_TYPES);
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            ArrayList<Body> lst = new ArrayList<Body>(poolSize);

            switch (i) {
                case GEM_TYPE_0:
                    for (int j = 0; j < poolSize; ++j) {
                        Body body = createFragmentGem0();
                        body.setActive(false);
                        lst.add(body);
                    }
                    break;

                case GEM_TYPE_1:
                    for (int j = 0; j < poolSize; ++j) {
                        Body body = createFragmentGem1();
                        body.setActive(false);
                        lst.add(body);
                    }
                    break;

                case GEM_TYPE_2:
                    for (int j = 0; j < poolSize; ++j) {
                        Body body = createFragmentGem2();
                        body.setActive(false);
                        lst.add(body);
                    }
                    break;

                case GEM_TYPE_3:
                    for (int j = 0; j < poolSize; ++j) {
                        Body body = createFragmentGem3();
                        body.setActive(false);
                        lst.add(body);
                    }
                    break;

                case GEM_TYPE_4:
                    for (int j = 0; j < poolSize; ++j) {
                        Body body = createFragmentGem4();
                        body.setActive(false);
                        lst.add(body);
                    }
                    break;

                case GEM_TYPE_5:
                    for (int j = 0; j < poolSize; ++j) {
                        Body body = createFragmentGem5();
                        body.setActive(false);
                        lst.add(body);
                    }
                    break;

                case GEM_TYPE_6:
                    for (int j = 0; j < poolSize; ++j) {
                        Body body = createFragmentGem6();
                        body.setActive(false);
                        lst.add(body);
                    }
                    break;
            }
            fragmentsPool.add(lst);
        }

        fragments = new ArrayList<Body>(100);
        statics = new ArrayList<Body>(10);
        edges = new ArrayList<Body>(10);
        fragmentToPool = new ArrayList<Body>(100);

        // create static parts here
        //physics.addEdge(-13.5f, -21.5f,  13.5f, -21.5f); // bottom
        Physics.addEdge(-13.0f, -7.0f, -13.5f, 20.0f); // left
        Physics.addEdge( 13.0f, -7.0f, 13.5f, 20.0f); // right
        
        Physics.addBoxStatic(8.0f, -5.9f, 24.8f, 12.0f, 0.8f);
        Physics.addBoxStatic(-7.8f, -5.9f, -25.0f, 12.0f, 0.8f);
        Physics.addBoxStatic(3.1f, -8.8f, 0f, 1.4f, 1.4f);
        Physics.addBoxStatic(-3.0f, -8.8f, 0f, 1.4f, 1.4f);
        
        // sin
        Physics.addBoxStatic(0.0f, -19.7f, 0f, 70.0f, 0.5f);


        // create moving cart parts here
        float x, y;

        // cart1
        --collisionGroupIndexCounter;
        collisionGroupIndex = collisionGroupIndexCounter;
        x = -20f;
        y = -15f;        
        cart1body = CreateCart(x, y);
        cart1wheel1 = CreateWheel(x - 2.0f, y - 2.2f);
        cart1wheel2 = CreateWheel(x + 2.0f, y - 2.2f);
        cart1wheelJoint1 = CreateWheelJoint(cart1body, cart1wheel1, -1.6f);
        cart1wheelJoint2 = CreateWheelJoint(cart1body, cart1wheel2,  1.6f);

        // cart2
        --collisionGroupIndexCounter;
        collisionGroupIndex = collisionGroupIndexCounter;
        x = -30f;
        cart2body = CreateCart(x, y);
        cart2wheel1 = CreateWheel(x - 2.0f, y - 2.2f);
        cart2wheel2 = CreateWheel(x + 2.0f, y - 2.2f);
        cart2wheelJoint1 = CreateWheelJoint(cart2body, cart2wheel1, -1.6f);
        cart2wheelJoint2 = CreateWheelJoint(cart2body, cart2wheel2,  1.6f);
    }

    private static Body CreateCart(float x, float y) {
        PolygonShape shape = new PolygonShape();
        Vec2[] vertices = new Vec2[4];
        vertices[0] = new Vec2(-3.2f, -2.0f);
        vertices[1] = new Vec2(3.2f, -2.0f);
        vertices[2] = new Vec2(3.4f, -0.01f);
        vertices[3] = new Vec2(-3.4f, -0.01f);
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
        vertices1[2] = new Vec2(-4.0f, 2.2f);
        vertices1[3] = new Vec2(-3.6f, 2.0f);
        shape1.set(vertices1, vertices1.length);

        FixtureDef fixture1 = new FixtureDef();
        fixture1.shape = shape1;
        fixture1.density = 1.0f;
        fixture1.restitution = 0.1f;
        fixture1.filter.groupIndex = collisionGroupIndex;

        PolygonShape shape2 = new PolygonShape();
        Vec2[] vertices2 = new Vec2[4];
        vertices2[0] = new Vec2(3.4f, -1.0f);
        vertices2[1] = new Vec2(3.0f, -1.0f);
        vertices2[2] = new Vec2(4.0f, 2.2f);
        vertices2[3] = new Vec2(3.6f, 2.0f);
        shape2.set(vertices2, vertices2.length);

        FixtureDef fixture2 = new FixtureDef();
        fixture2.shape = shape2;
        fixture2.density = 1.0f;
        fixture2.restitution = 0.1f;
        fixture2.filter.groupIndex = collisionGroupIndex;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        // TODO: update userData later
        //body.m_userData = this;
        body.createFixture(fixture);
        body.createFixture(fixture1);
        body.createFixture(fixture2);
        return body;
    }   

    private static Body CreateWheel(float x, float y) {
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

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return body;
    }

    private static WheelJoint CreateWheelJoint(Body body, Body wheel1, float anchorX) {
        WheelJointDef wd = new WheelJointDef();
        wd.bodyA = body;
        wd.bodyB = wheel1;
        wd.localAnchorA.set(anchorX, -2.2f);
        wd.frequencyHz = 6;
        wd.dampingRatio = 0.3f;
        wd.maxMotorTorque = 1000;
        wd.motorSpeed = 0f; //-6.0f;
        wd.enableMotor = true;
        wd.localAxisA.set(0f, 1f);

        WheelJoint wheelJoint = (WheelJoint) world.createJoint(wd);
        return wheelJoint;
    }

//        wd.bodyB = wheel2;
//        wd.localAnchorA.set(1.6f, -2.2f);
//        wheelJoint2 = (WheelJoint) physics.world.createJoint(wd);

//      Vec2 axis = new Vec2(0.0f, 0.9f);

//      wd = new WheelJointDef();   
//      wd.dampingRatio = 0.9f;
//      wd.motorSpeed = 0.0f;
//      wd.maxMotorTorque = 0.0f;       
//      wd.enableMotor = false; 

//      wd.initialize(cart, wheel1, wheel1.getPosition(), axis);
//      wheelJoint1 = (WheelJoint)physics.world.createJoint(wd);        
//  
//      wd.initialize(cart, wheel2, wheel1.getPosition(), axis);
//      wheelJoint2 = (WheelJoint)physics.world.createJoint(wd);


    // ctor
    private Physics() {}

    public static void reset() {
        fragmentToPool.clear();
        Body body;
        int size = fragments.size();
        for (int i = 0; i < size; ++i) {
            body = fragments.get(i);
            fragmentToPool.add(body);
        }

        sortFragmentsFromPool();
    }

    private static Body getBodyFromPool(int gemType) {
        ArrayList<Body> pool = fragmentsPool.get(gemType);
        Body body = null;
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
            //System.out.println("Body is null in getBodyFromPool....");
        }

        return body;
    }

    public static void clear() {
        collectObjectsToPool();
        sortFragmentsFromPool();
    }

    private static void setBodyPosAndRot(Body body, float x, float y) {
        final float angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        pos.x = x;
        pos.y = y;
        final float d = MyUtils.rand.nextFloat() * 2f - 1f;
        if (MyUtils.randInt(0, 10) > 5) {
            vector.x = 3f + d * 9f;
        } else {
            vector.x = -3f + d * 9f;
        }
        vector.y = 6f + (MyUtils.rand.nextFloat() * 6f);

        body.setLinearVelocity(vector);
        body.setAngularVelocity( 30f + MyUtils.rand.nextFloat() * 60f  );
        body.setTransform(pos, angle);
        body.setActive(true);
    }

// ADD
    public static void addFragmentGem0(float x, float y) {
        final int gemType = GEM_TYPE_0;
        Body body = getBodyFromPool(gemType);
        setBodyPosAndRot(body, x, y);
        fragments.add(body);
    }

    public static void addFragmentGem1(float x, float y) {
        final int gemType = GEM_TYPE_1;
        Body body = getBodyFromPool(gemType);
        setBodyPosAndRot(body, x, y);
        fragments.add(body);
    }

    public static void addFragmentGem2(float x, float y) {
        final int gemType = GEM_TYPE_2;
        Body body = getBodyFromPool(gemType);
        setBodyPosAndRot(body, x, y);
        fragments.add(body);
    }

    public static void addFragmentGem3(float x, float y) {
        final int gemType = GEM_TYPE_3;
        Body body = getBodyFromPool(gemType);
        setBodyPosAndRot(body, x, y);
        fragments.add(body);
    }

    public static void addFragmentGem4(float x, float y) {
        final int gemType = GEM_TYPE_4;
        Body body = getBodyFromPool(gemType);
        setBodyPosAndRot(body, x, y);
        fragments.add(body);
    }

    public static void addFragmentGem5(float x, float y) {
        final int gemType = GEM_TYPE_5;
        Body body = getBodyFromPool(gemType);
        setBodyPosAndRot(body, x, y);
        fragments.add(body);
    }

    public static void addFragmentGem6(float x, float y) {
        final int gemType = GEM_TYPE_6;
        Body body = getBodyFromPool(gemType);
        setBodyPosAndRot(body, x, y);
        fragments.add(body);
    }

// CREATE
    private static Body createFragmentGem0() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0f, 0f);
        bodyDef.angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = true;

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
        fixtureDef.density = GEM_DENSITY;
        //fixture.friction = 0.75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_userData = new Integer(GEM_TYPE_0);

        return body;
    }

    private static Body createFragmentGem1() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0f, 0f);
        bodyDef.angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = true;

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
        fixtureDef.density = GEM_DENSITY;
        //fixture.friction = 0.75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_userData = new Integer(GEM_TYPE_1);

        return body;
    }

    private static Body createFragmentGem2() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0f, 0f);
        bodyDef.angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = true;

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
        fixtureDef.density = GEM_DENSITY;
        //fixture.friction = 0.75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_userData = new Integer(GEM_TYPE_2);

        return body;
    }

    private static Body createFragmentGem3() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0f, 0f);
        bodyDef.angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = true;

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
        fixtureDef.density = GEM_DENSITY;
        //fixture.friction = 0.75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_userData = new Integer(GEM_TYPE_3);

        return body;
    }

    private static Body createFragmentGem4() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0f, 0f);
        bodyDef.angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = true;

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
        fixtureDef.density = GEM_DENSITY;
        //fixture.friction = 0.75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_userData = new Integer(GEM_TYPE_4);

        return body;
    }

    private static Body createFragmentGem5() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0f, 0f);
        bodyDef.angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = true;

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
        fixtureDef.density = GEM_DENSITY;
        //fixture.friction = 0.75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_userData = new Integer(GEM_TYPE_5);

        return body;
    }

    private static Body createFragmentGem6() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0f, 0f);
        bodyDef.angle = (float)Math.toRadians( MyUtils.rand.nextFloat() * 360f );
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = true;

        final float d = GEM_FRAGMENT_SIZE;
        final float d2 = (d / 3.0f) * 2.0f;
        final int count = 8;
        final Vec2[] vertices = new Vec2[count];
        vertices[0] = new Vec2( -d,  0f);
        vertices[1] = new Vec2(-d2,  d2);
        vertices[2] = new Vec2( 0f,  d);
        vertices[3] = new Vec2( d2,  d2);
        vertices[4] = new Vec2(  d,  0f);
        vertices[5] = new Vec2( d2, -d2);
        vertices[6] = new Vec2( 0f, -d);
        vertices[7] = new Vec2(-d2, -d2);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices, count);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.restitution = 0.1f;
        fixtureDef.density = GEM_DENSITY;
        //fixture.friction = 0.75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_userData = new Integer(GEM_TYPE_6);

        return body;
    }

    public static void addEdge(float x1, float y1, float x2, float y2) {
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

    public static void addBoxStatic(float x, float y, float angle, float w, float h) {
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
    }

    public static void update() {
        final int velocityIterations = 8;
        final int positionIterations = 4;

        world.step(1.0f / 30.0f, velocityIterations, positionIterations);

        collectObjectsToPool();
        sortFragmentsFromPool();
    }

    private static void dumpPoolStat() {
        int i = 0;
        System.out.println("Physics pool stat:");
        for (ArrayList<Body> lst : fragmentsPool) {
            System.out.println("pool [" + i + "]: " + lst.size());
            ++i;
        }
    }

    private static void collectObjectsToPool() {
        fragmentToPool.clear();
        Body body;
        Vec2 pos;
        int size = fragments.size();
        int wastedCounter = 0;
        for (int i = 0; i < size; ++i) {
            body = fragments.get(i);
            pos = body.getPosition();

            //if (pos.y < -18.7f && pos.x < 15f) {
            if (pos.y < -18.9f && pos.x < 15f) {
                fragmentToPool.add(body);
                ++wastedCounter;
                //Engine.graphics.particleManager.addParticleEmitterAtWastedGems(pos.x, pos.y, 1);
            }
        }

        if (wastedCounter > 0) {
            Engine.game.scoreCounter.wastedGems += wastedCounter;
        }
    }

    public static void sortFragmentsFromPool() {
        Body body;
        final int size = fragmentToPool.size();
        for(int i = 0; i < size; ++i) {
            body = fragmentToPool.get(i);
            body.setActive(false);
            fragments.remove(body);
            //destroyObject(body);
            poolObject(body);
        }
        fragmentToPool.clear();
    }

    private static void destroyObject(Body body) {
        world.destroyBody(body);
    }

    private static void poolObject(Body body) {
        final int gemType = (Integer) body.m_userData;
        ArrayList<Body> pool = fragmentsPool.get(gemType);
        pool.add(body);
    }

}