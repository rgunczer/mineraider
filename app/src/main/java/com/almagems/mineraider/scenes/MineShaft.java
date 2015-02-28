package com.almagems.mineraider.scenes;


import com.almagems.mineraider.ClassicSingleton;

import static android.opengl.GLES20.glEnable;
import static com.almagems.mineraider.Constants.*;
import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.IndexBuffer;
import com.almagems.mineraider.data.VertexBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawElements;

public class MineShaft extends Scene {

    private VertexBuffer vbBg;
    private IndexBuffer ibBg;

    private final PositionInfo _pos;

    private float _elevatorY = 0f;
    private float _elevatorYstep = 0.15f;
    private float _elevatorStopTimeout = 1f;
    private boolean _stopped = false;

    private final float _topTunnelY = 13.8f;
    private final float _middleTunnelY = -4.4f;
    private final float _bottomTunnelY = -22.0f;

    private static Visuals visuals;

    // ctor
    public MineShaft() {
        System.out.print("MineShaft ctor...");

        _pos = new PositionInfo();
        visuals = Visuals.getInstance();
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
        visuals.shaft.draw();
    }

    void drawRailRoads() {
        float x, y, z,  tempZ;
        visuals.railroad.bindData(visuals.dirLightShader);

        // top tunnel
        x = 6.5f;
        y = 11.8f;
        z = 0.0f;
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
        z = 0.0f;
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
        z = 0.0f;
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
        z = 0f;
        _pos.trans(x, y, z);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1f, 1f, 1f);

        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.railroad.draw();
    }

    @Override
    public void update() {

        if (!_stopped) {
            if (Math.abs(_elevatorY - _topTunnelY) < 0.1f) {
                _elevatorStopTimeout = 10f;
                _stopped = true;
            } else if (Math.abs(_elevatorY - _middleTunnelY) < 0.1f) {
                _elevatorStopTimeout = 10f;
                _stopped = true;
            } else if (Math.abs(_elevatorY - _bottomTunnelY) < 0.1f) {
                _elevatorStopTimeout = 10f;
                _stopped = true;
            } else {
                _elevatorY -= _elevatorYstep;
            }
        } else {
            _elevatorStopTimeout -= 0.1f;

            if (_elevatorStopTimeout < 0f) {
                _stopped = false;
                _elevatorY -= _elevatorYstep*2f;
            }
        }

        if (_elevatorY  > 33.0f)
            _elevatorYstep *= -1f;
        else if (_elevatorY < -37.0f)
            _elevatorYstep *= -1f;
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

    }

    @Override
    public void handleTouchPress(float normalizedX, float normalizedY) {

        if (_stopped) {
            ClassicSingleton singleton = ClassicSingleton.getInstance();
            singleton.showScene(ScenesEnum.Level);
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
