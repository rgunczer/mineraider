package com.almagems.mineraider;

import static android.opengl.GLES20.*;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.WheelJoint;


public final class MineCart {

    private enum CartState {
        Entering,
        StoppedHitOtherCart,
        Restarting,
        WaitingForGems,
        Leaving
    }

    private final Vec2 repositionPos = new Vec2(0f, 0f);

    public CartState state;
    public int id;
    
    public Body body;
    public Body wheel1;
    public Body wheel2;
    public WheelJoint wheelJoint1;
    public WheelJoint wheelJoint2;
    
    public static Physics physics;
    public static Graphics graphics;

    private float z = 0f;
    private float speed = 0f;

    private int counter = 0;    

    private PositionInfo op = new PositionInfo();


    // ctor
    public MineCart() {
        state = CartState.Entering;
        z = 1f;                        
    }

    public void start(float xSpeed) {
        speed = xSpeed;
        wheelJoint1.setMotorSpeed(xSpeed);
        wheelJoint2.setMotorSpeed(xSpeed);
        counter = 0;
    }

    public void stop() {
        speed = 0f;
        wheelJoint1.setMotorSpeed(speed);
        wheelJoint2.setMotorSpeed(speed);
        counter = 0;
    }

    public void reposition(float x, float y) {
        repositionPos.x = x;
        repositionPos.y = y;
        body.setTransform(repositionPos, 0f);
        wheel1.setTransform(repositionPos, 0f);
        wheel2.setTransform(repositionPos, 0f);
    }

    public void restartCart() {
        if (state == CartState.StoppedHitOtherCart) {
            state = CartState.Restarting;
            counter = MyUtils.randInt(20, 60);
        }
    }

    public void onHitOtherCart() {
        if (state == CartState.Entering) {
            state = CartState.StoppedHitOtherCart;
            stop();
        }
    }

    public void update() {
        Vec2 pos = body.getPosition();

        switch (state) {
            case Entering:
                if (pos.x > -1.0f && pos.x < 0f) {
                    stop();
                    state = CartState.WaitingForGems;
                }
                break;

            case Restarting:
                --counter;
                if (counter < 0) {
                    state = CartState.Entering;
                    start(-3f);
                }
                break;

            case StoppedHitOtherCart:
                break;

            case WaitingForGems:
                ++counter;
                if (counter > 400) {
                    state = CartState.Leaving;
                    start(-3f);
                    Engine.game.notifyOtherMinecartToStart();
                }
                break;

            case Leaving:
                if (pos.x > 19.0f) {
                    state = CartState.Entering;
                    moveToStart(pos);
                    countMineCartLoad();
                    start(-3f);
                }
                break;
        }
    }

    private void moveToStart(Vec2 pos) {
        pos.x = -19.0f - (MyUtils.rand.nextFloat() * 3f);
        pos.y = -16.5f;
        reposition(pos.x, pos.y);
    }

    // now count how many gems were there (in the minecart)
    private void countMineCartLoad() {
        Body body;
        Vec2 fragmentPos;
        int size = Physics.fragments.size();
        int count = 0;
        Physics.fragmentToPool.clear();
        for(int i = 0; i < size; ++i) {
            body = Physics.fragments.get(i);
            fragmentPos = body.getPosition();

            if (fragmentPos.x > 15f) {
                ++count;
                Physics.fragmentToPool.add(body);
            }
        }

        if (count > 0) {
            Engine.game.scoreCounter.handleGemsFromCart(count);
        }

        Physics.sortFragmentsFromPool();
    }

// drawing
	private void drawCartFixture() {
		Vec2 pos = body.getPosition();
		float angle = body.getAngle();
		float degree = (float) Math.toDegrees(angle);
		
		//System.out.println("Cart: " + pos.x + ", " + pos.y);

		EdgeDrawer edgeDrawer = new EdgeDrawer(100);
		Fixture fixture = body.getFixtureList();
        PositionInfo op = new PositionInfo();
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

			op.trans(pos.x, pos.y, 1.0f);
			op.rot(0f, 0f, degree);
			op.scale(1f, 1f, 1f);
			graphics.calcMatricesForObject(op);
			
//			setIdentityM(visuals.modelMatrix, 0);							
//			translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
//			rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);													
//			multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
			
			graphics.singleColorShader.useProgram();
			graphics.singleColorShader.setUniforms(graphics.mvpMatrix, Color.WHITE);
			edgeDrawer.bindData(graphics.singleColorShader);
			edgeDrawer.draw();
			fixture = fixture.getNext();
		}
	}

    public void draw() {
        op.scale(1f, 1f, 1f);

        graphics.dirLightShader.setTexture(Graphics.textureCart);
        graphics.mineCart.bindData(graphics.dirLightShader);
        drawCart();

		graphics.dirLightShader.setTexture(Graphics.textureWheel);
        graphics.wheel.bindData(graphics.dirLightShader);
		drawWheels();

        glDisable(GL_DEPTH_TEST);
		drawCartFixture();
        glEnable(GL_DEPTH_TEST);
    }

	public void drawCart() {
        Vec2 pos = body.getPosition();
        float degree = (float) Math.toDegrees(body.getAngle());

        op.trans(pos.x, pos.y, z); //-0.75f);
        op.rot(0f, 0f, degree);
        op.scale(1f, 1f, 1f);
        graphics.calcMatricesForObject(op);
        graphics.dirLightShader.setUniforms();
        graphics.mineCart.draw();
    }

    public void drawWheels() {
        Vec2 pos;
        float degree;

		// wheel 1
		pos = wheel1.getPosition();
		degree = (float)Math.toDegrees( wheel1.getAngle() );

		op.trans(pos.x, pos.y, z); //-0.75f);// 2f);
		op.rot(0f, 0f, degree);
        op.scale(1f, 1f, 1f);

        graphics.calcMatricesForObject(op);
		graphics.dirLightShader.setUniforms();
		graphics.wheel.draw();


		// wheel 2
		pos = wheel2.getPosition();
		degree = (float)Math.toDegrees( wheel2.getAngle() );

        op.trans(pos.x, pos.y, z); // 0f-0.75f);
		op.rot(0f, 0f, degree);

        graphics.calcMatricesForObject(op);
		graphics.dirLightShader.setUniforms();
		graphics.wheel.draw();
	}
}
