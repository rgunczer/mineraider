package com.almagems.mineraider.scenes;


import com.almagems.mineraider.ClassicSingleton;

import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDrawArrays;
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
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.data.VertexBuffer;
import com.almagems.mineraider.objects.EdgeDrawer;
import com.almagems.mineraider.objects.MineCart;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.Geometry;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Ray;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Sphere;
import com.almagems.mineraider.util.Text;
import com.almagems.mineraider.util.Texture;
import com.almagems.mineraider.util.TexturedQuad;
import com.almagems.mineraider.util.Vector;

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
    private Body _bodyElevatorRightWall;

    private float _elevatorDoorYOffset = 0f;

    private final PositionInfo _pos;

    private final Text _textBack;


    private float _elevatorX = -4f;
    private float _elevatorY = 0f;

    private Quad _quadTunnelTop;
    private Quad _quadTunnelMiddle;
    private Quad _quadTunnelBottom;

    private final float elevatorSpeed = 0.15f;
    private float _elevatorYstep = elevatorSpeed;
    private boolean _stopped = false;
    private boolean _elevatorStays = false;

    private final float _topTunnelY = 13.8f;
    private final float _middleTunnelY = -4.2f;
    private final float _bottomTunnelY = -22.0f;

    private Vector _posElevatorUp = new Vector(0f, 0f, 0f);
    private Vector _posElevatorDown = new Vector(0f, 0f, 0f);
    private Vector _poselevatorEnter = new Vector(0f, 0f, 0f);

    private static Visuals visuals;
    private Physics _physics;

    // ctor
    public MineShaft() {
        System.out.print("MineShaft ctor...");

        _physics = new Physics();
        _pos = new PositionInfo();
        visuals = Visuals.getInstance();

        _textBack = new Text();

        _quadTunnelTop = new Quad();
        _quadTunnelMiddle = new Quad();
        _quadTunnelBottom = new Quad();

        // setup physics world and objects

        _mineCart = new MineCart(_physics, 0f, 0f);
        _mineCart.z = -0.75f;
        _mineCart._sceneType = ScenesEnum.Shaft;

        // elevator
        _bodyElevatorBottom = _physics.addBoxStatic(-4f, 0f, 0f, 10.0f, 1.0f);
        _bodyElevatorLeftWall = _physics.addBoxStatic(-9f, 0f, 0f, 0.5f, 9.0f);
        _bodyElevatorRightWall = _physics.addBoxStatic(1f, 0f, 0f, 0.5f, 9.0f);

        // tunnels
        _physics.addBoxStatic(16.5f, 24.5f, 0f, 30f, 8f);
        _physics.addBoxStatic(16.5f, 11.75f-4f, 0f, 30.0f, 9f); // top
        _physics.addBoxStatic(16.5f, -6.4f-4f, 0f, 30.0f, 9f); // middle
        _physics.addBoxStatic(16.5f, -24.4f-4f, 0f, 30.0f, 9f); // bottom
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
                // xyz, 	    rgba,	        uv
                -x, -y, 0f,     r, g, b, a,     0.0f, 0.0f, // 0
                 x, -y, 0f,     r, g, b, a,     1.0f, 0.0f, // 1
                 x,  y, 0f,     r, g, b, a,     1.0f, 1.0f, // 2

                -x, -y, 0f,     r, g, b, a,     0.0f, 0.0f, // 3
                 x,  y, 0f,     r, g, b, a,     1.0f, 1.0f, // 4
                -x,  y, 0f,     r, g, b, a,     0.0f, 1.0f  // 5
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

        float textWidth;

        _textBack.init("BACK", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.9f);
        textWidth = _textBack.getTextWidth();
        _textBack.pos.trans(-0.8f - (textWidth / 2f), -Visuals.aspectRatio * 0.95f, 0f);
        _textBack.pos.rot(0f, 0f, 0f);
        _textBack.pos.scale(1f, 1f, 1f);

        float start = 4f;
        Rectangle rect;

        rect = new Rectangle(0f, 32f * start, 256f, 32f);
        _quadTunnelTop.initWithNormalVectors(visuals.textureTunnels, rect, false);
        _quadTunnelTop.pos.trans(9f, 18f, -2.85f);
        _quadTunnelTop.pos.scale(6f, 1f, 1f);


        rect = new Rectangle(0f, 32f * (start + 1f), 256f, 32f);
        _quadTunnelMiddle.initWithNormalVectors(visuals.textureTunnels, rect, false);
        _quadTunnelMiddle.pos.trans(9f, 0f, -2.85f);
        _quadTunnelMiddle.pos.scale(6f, 1f, 1f);


        rect = new Rectangle(0f, 32f * (start + 2f), 256f, 32f);
        _quadTunnelBottom.initWithNormalVectors(visuals.textureTunnels, rect, false);
        _quadTunnelBottom.pos.trans(9f, -18f, -2.85f);
        _quadTunnelBottom.pos.scale(6f, 1f, 1f);


/*


        Texture texture = Visuals.getInstance().getTextureObj(visuals.textureTunnels);
        float tw = texture.width;
        float th = texture.height;

        TexturedQuad pFont = new TexturedQuad();
        // x								// y
        pFont.tx_lo_left.x = x / tw;        pFont.tx_lo_left.y = (th - (y - h)) / th;  // 0
        pFont.tx_lo_right.x = (x + w) / tw; pFont.tx_lo_right.y = (th - (y - h)) / th; // 1
        pFont.tx_up_right.x = (x + w) / tw; pFont.tx_up_right.y = (th - y) / th;       // 2
        pFont.tx_up_left.x = x / tw;        pFont.tx_up_left.y =  (th - y) / th;       // 3

        float tx0 = pFont.tx_lo_left.x;
        float tx1 = pFont.tx_up_right.x;
        float ty0 = pFont.tx_lo_left.y;
        float ty1 = pFont.tx_up_right.y;

        float[] vertexData = {
                // xyz, rgba, uv
                -1f, -1f, 0f,   color.r, color.g, color.b, color.a,     tx1, ty1,
                 1f, -1f, 0f,   color.r, color.g, color.b, color.a,     tx0, ty1,
                 1f,  1f, 0f,   color.r, color.g, color.b, color.a,     tx0, ty0,

                -1f, -1f, 0f,   color.r, color.g, color.b, color.a,     tx1, ty1,
                 1f,  1f, 0f,   color.r, color.g, color.b, color.a,     tx0, ty0,
                -1f,  1f, 0f,   color.r, color.g, color.b, color.a,     tx1, ty0
        };

        vertexArrayTunnel = new VertexArray(vertexData);
*/
    }

    private void  drawElevatorButtons() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        visuals.elevatorButton.bindData(visuals.dirLightShader);

        final float xoff = -2.5f;

        _posElevatorUp.x = _elevatorX + xoff;
        _posElevatorUp.y = _elevatorY + 8.5f;
        _posElevatorUp.z = 0f;

        _pos.trans(_posElevatorUp.x, _posElevatorUp.y, _posElevatorUp.z);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.elevatorButton.draw();


        _posElevatorDown.x = _elevatorX + xoff;
        _posElevatorDown.y = _elevatorY - 4.5f;
        _posElevatorDown.z = 0f;

        _pos.trans(_posElevatorDown.x, _posElevatorDown.y, _posElevatorDown.z);
        _pos.rot(0f, 0f, 180f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.elevatorButton.draw();
        glDisable(GL_BLEND);


        _poselevatorEnter.x = _elevatorX; // + xoff + ;
        _poselevatorEnter.y = _elevatorY + 4f;
        _poselevatorEnter.z = 0f;

        visuals.elevatorEnter.bindData(visuals.dirLightShader);
        visuals.dirLightShader.setTexture(visuals.textureElevatorEnter);
        _pos.trans(_poselevatorEnter.x, _poselevatorEnter.y, _poselevatorEnter.z);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.elevatorEnter.draw();

        glEnable(GL_DEPTH_TEST);
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

        _pos.trans(_elevatorX, _elevatorY, 0f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.elevator.draw();
    }

    void drawRailRoads() {
        float x, y, z, tempZ;
        visuals.railroad.bindData(visuals.dirLightShader);

        // top tunnel
        x = 6.5f;
        y = 11.8f;
        z = -0.75f;
        for (int i = 0; i < 2; ++i) {
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
        for (int i = 0; i < 2; ++i) {
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
        for (int i = 0; i < 2; ++i) {
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
        glDisable(GL_DEPTH_TEST);
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
        for (int i = 0; i < size; ++i) {
            body = physics.statics.get(i);
            pos = body.getPosition();
            angle = body.getAngle();
            degree = (float) Math.toDegrees(angle);

            fixture = body.getFixtureList();
            while (fixture != null) {
                polygon = (PolygonShape) fixture.getShape();
                if (polygon.m_count == 4) { // box
                    edgeDrawer.begin();
                    Vec2 v0 = polygon.m_vertices[0];
                    Vec2 v1 = polygon.m_vertices[1];
                    Vec2 v2 = polygon.m_vertices[2];
                    Vec2 v3 = polygon.m_vertices[3];

                    edgeDrawer.addLine(v0.x, v0.y, 0f, v1.x, v1.y, 0f);
                    edgeDrawer.addLine(v1.x, v1.y, 0f, v2.x, v2.y, 0f);
                    edgeDrawer.addLine(v2.x, v2.y, 0f, v3.x, v3.y, 0f);
                    edgeDrawer.addLine(v3.x, v3.y, 0f, v0.x, v0.y, 0f);

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

    private void drawTunnelLabels() {
        _quadTunnelTop.draw1();
        _quadTunnelMiddle.draw1();
        _quadTunnelBottom.draw1();
    }

    @Override
    public void prepare() {
        super.prepare();

        _fadeOutCheck = true;

        _elevatorDoorYOffset = 0f;
        _elevatorY = 30f;
        updateElevatorPhysics();

        Vec2 pos = _mineCart.cart.getPosition();
        pos.x = -5f;
        pos.y = 30f;
        _mineCart.reposition(pos.x, pos.y);

        _mineCart.stop();

        _elevatorStays = false;
        _stopped = false;

        _physics.update();
    }

    @Override
    public void update() {
        if (!_elevatorStays) {
            if (!_stopped) {
                if (Math.abs(_elevatorY - _topTunnelY) < 0.1f) {
                    _elevatorPositions = ElevatorPostions.Top;
                    _stopped = true;
                    _elevatorDoorYOffset = 10f;
                } else if (Math.abs(_elevatorY - _middleTunnelY) < 0.1f) {
                    _elevatorPositions = ElevatorPostions.Middle;
                    _stopped = true;
                    _elevatorDoorYOffset = 10f;
                } else if (Math.abs(_elevatorY - _bottomTunnelY) < 0.1f) {
                    _elevatorPositions = ElevatorPostions.Bottom;
                    _stopped = true;
                    _elevatorDoorYOffset = 10f;
                } else {
                    _elevatorY += _elevatorYstep;
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

        if (_elevatorY > 33.0f)
            _elevatorYstep *= -1f;
        else if (_elevatorY < -37.0f)
            _elevatorYstep *= -1f;

        updateElevatorPhysics();

        _physics.update();
        visuals.updateViewProjMatrix();
    }

    void updateElevatorPhysics() {
        Vec2 pos;
        pos = _bodyElevatorBottom.getPosition();
        pos.y = _elevatorY - 2f;
        _bodyElevatorBottom.setTransform(pos,0f);

        pos = _bodyElevatorLeftWall.getPosition();
        pos.y = _elevatorY + 1f;
        _bodyElevatorLeftWall.setTransform(pos,0f);

        pos = _bodyElevatorRightWall.getPosition();
        pos.y = _elevatorY + 1f + _elevatorDoorYOffset;
        _bodyElevatorRightWall.setTransform(pos, 0f);
    }

    @Override
    public void draw() {
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();
        visuals.textureShader.useProgram();

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

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

        visuals.dirLightShader.setTexture(visuals.textureTunnels);
        drawTunnelLabels();

        _mineCart.draw();

        if (_stopped) {
            visuals.dirLightShader.setTexture(visuals.textureNextArrow);
            drawElevatorButtons();
        }

        //drawPhysics();
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();

        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);



        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureFonts);
        _textBack.draw();
        //_quadTunnelTop.draw();

        super.drawFade();

        // needs to be set for picking to work correctly
        visuals.setProjectionMatrix3dForShaft();
        visuals.updateViewProjMatrix();

    }

    @Override
    public void handleTouchPress(float normalizedX, float normalizedY) {
        if (goNextScene) {
            return;
        }

        touchDownX = normalizedX;
        touchDownY = normalizedY;

        if (normalizedY < -0.75f) {
            if (normalizedX < -0.3f) {
                nextSceneId = ScenesEnum.Menu;
            }

            if (nextSceneId != ScenesEnum.None) {
                goNextScene = true;
                super.initFadeOut();
            }
        } else {
            if (!_elevatorStays) {
                if (_stopped) {


        // needs to be set for picking to work correctly
        visuals.setProjectionMatrix3dForShaft();
        visuals.updateViewProjMatrix();

        Ray ray = Geometry.convertNormalized2DPointToRay(touchDownX, touchDownY, visuals.invertedViewProjectionMatrix);

        Sphere sphere;
        sphere = new Sphere(_posElevatorUp.x, _posElevatorUp.y, _posElevatorUp.z, 2f);
        if (Geometry.intersects(sphere, ray)) {
            System.out.println("Elevator: Move Up");
            _stopped = false;
            _elevatorDoorYOffset = 0f;
            _elevatorPositions = ElevatorPostions.None;
            _elevatorYstep = elevatorSpeed;
            _elevatorY += _elevatorYstep * 2f;
        }

        sphere = new Sphere(_posElevatorDown.x, _posElevatorDown.y, _posElevatorDown.z, 2f);
        if (Geometry.intersects(sphere, ray)) {
            System.out.println("Elevator: Move Down");
            _stopped = false;
            _elevatorDoorYOffset = 0f;
            _elevatorPositions = ElevatorPostions.None;
            _elevatorYstep = -elevatorSpeed;
            _elevatorY += _elevatorYstep * 2f;
        }

                    sphere = new Sphere(_poselevatorEnter.x,  _poselevatorEnter.y, _poselevatorEnter.z, 3f);
                    if (Geometry.intersects(sphere, ray)) {
                        System.out.println("Elevator: time to play");

                        _mineCart.start(-4f);
                        _elevatorStays = true;
                        nextSceneId = ScenesEnum.Level;
                        goNextScene = true;
                    }



                }
            }
        }

/*
                    _mineCart.start(-4f);
                    _elevatorStays = true;
                    nextSceneId = ScenesEnum.Level;
                    goNextScene = true;

            _stopped = false;
            _elevatorDoorYOffset = 0f;
            _elevatorPositions = ElevatorPostions.None;
            _elevatorY -= _elevatorYstep * 2f;
*/

        //System.out.println("ElevatorY: " + _elevatorY);
    }

    @Override
    public void handleTouchDrag(float normalizedX, float normalizedY) {

    }

    @Override
    public void handleTouchRelease(float normalizedX, float normalizedY) {

    }
}
