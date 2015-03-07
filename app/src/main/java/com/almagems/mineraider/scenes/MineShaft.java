package com.almagems.mineraider.scenes;


import com.almagems.mineraider.ClassicSingleton;

import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.Physics;
import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.IndexBuffer;
import com.almagems.mineraider.data.VertexBuffer;
import com.almagems.mineraider.objects.EdgeDrawer;
import com.almagems.mineraider.objects.MineCart;
import com.almagems.mineraider.util.MyColor;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawElements;

public class MineShaft extends Scene {
    private enum ElevatorPostions {
        None,
        Top,
        Middle,
        Bottom
    }

    private MineCart _mineCart;

    private boolean _fadeOutCheck;

    private ElevatorPostions _elevatorPositions = ElevatorPostions.None;

    private Body _bodyElevatorBottom;
    private Body _bodyElevatorLeftWall;

    private VertexBuffer vbBg;
    private IndexBuffer ibBg;

    private final PositionInfo _pos;

    private float _elevatorY = 0f;
    private float _elevatorYstep = 0.15f;
    private float _elevatorStopTimeout = 1f;
    private boolean _stopped = false;
    private boolean _elevatorStays = false;

    private final float _topTunnelY = 13.8f;
    private final float _middleTunnelY = -4.2f;
    private final float _bottomTunnelY = -22.0f;

    private static Visuals visuals;
    private Physics _physics;

    // ctor
    public MineShaft() {
        System.out.print("MineShaft ctor...");

        _physics = new Physics();
        _pos = new PositionInfo();
        visuals = Visuals.getInstance();

        // setup physics world and objects

        _mineCart = new MineCart(_physics, -4f, 4f);
        _mineCart.z = -0.75f;

        _bodyElevatorBottom = _physics.addBoxStatic(-4f, 0f, 0f, 10.0f, 1.0f); // elevator
        _bodyElevatorLeftWall = _physics.addBoxStatic(-9f, 0f, 0f, 0.5f, 6.0f); // elevator


        _physics.addBoxStatic(16.5f, 11.75f, 0f, 30.0f, 1.0f); // top
        _physics.addBoxStatic(16.5f, -6.4f, 0f, 30.0f, 1.0f); // middle
        _physics.addBoxStatic(16.5f, -24.4f, 0f, 30.0f, 1.0f); // bottom
    }

    @Override
    public void surfaceChanged(int width, int height) {
        final float r = 1f;
        final float g = 1f;
        final float b = 1f;
        final float a = 1f;
        final float aspect = Visuals.aspectRatio;

        final float x = 1.0f;
        final float y = aspect;

        float[] vertices = {
                // x, y, z, 			        u, v,
                -x, -y, 0.0f,   r, g, b, a,     0.0f, 0.0f, // 0
                x, -y, 0.0f,	r, g, b, a,     1.0f, 0.0f, // 1
                x,  y, 0.0f,	r, g, b, a,     1.0f, 1.0f, // 2

                -x, -y, 0.0f,	r, g, b, a,     0.0f, 0.0f, // 3
                x,  y, 0.0f,	r, g, b, a,     1.0f, 1.0f, // 4
                -x,  y, 0.0f,	r, g, b, a,     0.0f, 1.0f  // 5
        };


        short[] indices = {
                // for gl_lines
                //0, 1,
                //1, 2,
                //2, 5,
                //5, 0

                0, 1, 2,
                3, 4, 5
        };

        vbBg = new VertexBuffer(vertices);
        ibBg = new IndexBuffer(indices);

        _fade.init(new MyColor(0f, 0f, 0f, 1f), new MyColor(0f, 0f, 0f, 0f));
    }

    private void drawBg() {
        _pos.trans(0f, 0f, 0f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);

        visuals.calcMatricesForObject(_pos);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);

        vbBg.bind();
        visuals.textureShader.bindData(vbBg);

        ibBg.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
        vbBg.unbind();
        ibBg.unbind();
    }

    private void drawShaft() {
        visuals.shaft.bindData(visuals.dirLightShader);

        _pos.trans(-4f, 0.0f, 0f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.shaft.draw();
    }

    private void drawElevator() {
        visuals.elevator.bindData(visuals.dirLightShader);

        _pos.trans(-4f, _elevatorY, 0f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.elevator.draw();
    }

    void drawRailRoads() {
        float x, y, z,  tempZ;
        visuals.railroad.bindData(visuals.dirLightShader);

        // top tunnel
        x = 6.5f;
        y = 11.8f;
        z = -0.75f;
        for(int i = 0; i < 2; ++i) {
            tempZ = z;
            _pos.trans(x, y, z);
            _pos.rot(0f, 0f, 0f);
            _pos.scale(1f, 1f, 1f);

            visuals.calcMatricesForObject(_pos);
            visuals.dirLightShader.setUniforms();
            visuals.railroad.draw();

            z = tempZ;
            x += 10.0f;
        }

        // middle tunnel
        x = 6.5f;
        y = -6.5f;
        for(int i = 0; i < 2; ++i) {
            tempZ = z;
            _pos.trans(x, y, z);
            _pos.rot(0f, 0f, 0f);
            _pos.scale(1f, 1f, 1f);

            visuals.calcMatricesForObject(_pos);
            visuals.dirLightShader.setUniforms();
            visuals.railroad.draw();

            z = tempZ;
            x += 10.0f;
        }

        // bottom tunnel
        x = 6.5f;
        y = -24.3f;
        for(int i = 0; i < 2; ++i) {
            tempZ = z;
            _pos.trans(x, y, z);
            _pos.rot(0f, 0f, 0f);
            _pos.scale(1f, 1f, 1f);

            visuals.calcMatricesForObject(_pos);
            visuals.dirLightShader.setUniforms();
            visuals.railroad.draw();

            z = tempZ;
            x += 10.0f;
        }

        // railroad part in elevator
        x = -4f;
        y = _elevatorY - 2f;

        _pos.trans(x, y, z);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1f, 1f, 1f);

        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.railroad.draw();
    }

    void drawPhysics() {
        //glDisable(GL_DEPTH_TEST);
        drawPhysicsStatics(_physics);
//        drawPhysicsGemsFixtures();
//        drawPhysicsEdges();
        glEnable(GL_DEPTH_TEST);
    }

    private void drawPhysicsStatics(Physics physics) {
        visuals.colorShader.useProgram();

        float z = 0.5f;
        Body body;
        Vec2 pos;
        float angle;
        float degree;
        Fixture fixture;
        PolygonShape polygon;
        EdgeDrawer edgeDrawer = new EdgeDrawer(100);
        int size = physics.statics.size();
        for(int i = 0; i < size; ++i) {
            body = physics.statics.get(i);
            pos = body.getPosition();
            angle = body.getAngle();
            degree = (float) Math.toDegrees(angle);

            fixture = body.getFixtureList();
            while(fixture != null) {
                polygon = (PolygonShape)fixture.getShape();
                if (polygon.m_count == 4) { // box
                    edgeDrawer.begin();
                    Vec2 v0 = polygon.m_vertices[0];
                    Vec2 v1 = polygon.m_vertices[1];
                    Vec2 v2 = polygon.m_vertices[2];
                    Vec2 v3 = polygon.m_vertices[3];

                    edgeDrawer.addLine(	v0.x, v0.y, 0f,   v1.x, v1.y, 0f);
                    edgeDrawer.addLine(	v1.x, v1.y, 0f,   v2.x, v2.y, 0f);
                    edgeDrawer.addLine(	v2.x, v2.y, 0f,   v3.x, v3.y, 0f);
                    edgeDrawer.addLine(	v3.x, v3.y, 0f,   v0.x, v0.y, 0f);

                    setIdentityM(visuals.modelMatrix, 0);
                    translateM(visuals.modelMatrix, 0, pos.x, pos.y, z);
                    rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);
                    multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);

                    visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.whiteColor);
                    edgeDrawer.bindData(visuals.colorShader);
                    edgeDrawer.draw();
                }
                fixture = fixture.getNext();
            }
        }
    }

    @Override
    public void prepare() {
        super.prepare();

        _fadeOutCheck = true;

        Vec2 pos = _mineCart.cart.getPosition();
        pos.x = -5f;
        _mineCart.cart.setTransform(pos, 0f);

        _mineCart.wheel1.setTransform(pos, 0f);
        _mineCart.wheel2.setTransform(pos, 0f);

        _mineCart.stop();

        _elevatorStays = false;
        _stopped = false;
    }

    @Override
    public void update() {
        if (!_elevatorStays) {
            if (!_stopped) {
                if (Math.abs(_elevatorY - _topTunnelY) < 0.1f) {
                    _elevatorStopTimeout = 10f;
                    _elevatorPositions = ElevatorPostions.Top;
                    _stopped = true;
                } else if (Math.abs(_elevatorY - _middleTunnelY) < 0.1f) {
                    _elevatorStopTimeout = 10f;
                    _elevatorPositions = ElevatorPostions.Middle;
                    _stopped = true;
                } else if (Math.abs(_elevatorY - _bottomTunnelY) < 0.1f) {
                    _elevatorStopTimeout = 10f;
                    _elevatorPositions = ElevatorPostions.Bottom;
                    _stopped = true;
                } else {
                    _elevatorY -= _elevatorYstep;
                }
            } else {
                _elevatorStopTimeout -= 0.1f;

                if (_elevatorStopTimeout < 0f) {
                    _stopped = false;
                    _elevatorPositions = ElevatorPostions.None;
                    _elevatorY -= _elevatorYstep * 2f;
                }
            }
        } else {
            Vec2 pos = _mineCart.cart.getPosition();

            if (_fadeOutCheck && pos.x > 20f) {
                switch (_elevatorPositions) {
                    case None:
                        break;

                    case Top:
                        System.out.println("Top Tunnel");
                        super.initFadeOut();
                        break;

                    case Middle:
                        System.out.println("Middle Tunnel");
                        super.initFadeOut();
                        break;

                    case Bottom:
                        System.out.println("Bottom Tunnel");
                        super.initFadeOut();
                        break;
                }
                _fadeOutCheck = false;
            }
        }

        if (_elevatorY  > 33.0f)
            _elevatorYstep *= -1f;
        else if (_elevatorY < -37.0f)
            _elevatorYstep *= -1f;

        Vec2 pos;
        pos = _bodyElevatorBottom.getPosition();
        pos.y = _elevatorY - 2f;
        _bodyElevatorBottom.setTransform(pos, 0f);

        pos = _bodyElevatorLeftWall.getPosition();
        pos.y = _elevatorY + 1f;
        _bodyElevatorLeftWall.setTransform(pos, 0f);

        _physics.update();
    }

    @Override
    public void draw() {
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();
        visuals.textureShader.useProgram();

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

        visuals.textureShader.setTexture(visuals.textureShaftBg);
        drawBg();

        glEnable(GL_DEPTH_TEST);
        visuals.setProjectionMatrix3dForShaft();
        visuals.updateViewProjMatrix();

        visuals.dirLightShader.useProgram();
        visuals.dirLightShader.setTexture(visuals.textureShaft);
        drawShaft();

        visuals.dirLightShader.setTexture(visuals.textureElevator);
        drawElevator();

        visuals.dirLightShader.setTexture(visuals.textureRailRoad);
        drawRailRoads();

        _mineCart.draw();

        //drawPhysics();
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();

        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        super.drawFade();
    }

    @Override
    public void handleTouchPress(float normalizedX, float normalizedY) {
        if (!_elevatorStays) {
            if (_stopped) {
                _mineCart.start(-4f);
                _elevatorStays = true;
                nextSceneId = ScenesEnum.Level;
                goNextScene = true;
            }
        }

        //System.out.println("ElevatorY: " + _elevatorY);
    }

    @Override
    public void handleTouchDrag(float normalizedX, float normalizedY) {

    }

    @Override
    public void handleTouchRelease(float normalizedX, float normalizedY) {

    }
}
