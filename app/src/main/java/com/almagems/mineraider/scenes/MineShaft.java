package com.almagems.mineraider.scenes;


import com.almagems.mineraider.ClassicSingleton;
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

    // ctor
    public MineShaft() {
        System.out.print("MineShaft ctor...");

        _pos = new PositionInfo();

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


    @Override
    public void update() {

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


    }

    @Override
    public void handleTouchPress(float normalizedX, float normalizedY) {
        ClassicSingleton singleton = ClassicSingleton.getInstance();
        singleton.showScene(ScenesEnum.Level);
    }

    @Override
    public void handleTouchDrag(float normalizedX, float normalizedY) {

    }

    @Override
    public void handleTouchRelease(float normalizedX, float normalizedY) {

    }


}
