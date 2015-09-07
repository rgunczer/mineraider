package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.MyUtils.LERP;


public final class Fade extends EffectAnim {

    private VertexArray vertexArray;
    private MyColor colorFrom;
    private MyColor colorTo;
    public MyColor colorCurrent;
    private float t;
    private float dt = 0.05f; // speed
    public boolean done = false;
    public Rectangle rect;

    // ctor
    public Fade() {
        System.out.println("Fade ctor...");
    }

    public void init(MyColor from, MyColor to) { // need of a speed param!? for how fast the fading should occur
        this.colorFrom = from;
        this.colorTo = to;
        colorCurrent = new MyColor(from);
        t = 0.0f;
        done = false;
        pos = posOrigin;

        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);

        createVertexArray();
    }

    public void init(MyColor from, MyColor to, Rectangle rect) {
        this.colorFrom = from;
        this.colorTo = to;
        colorCurrent = new MyColor(from);
        t = 0.0f;
        done = false;
        pos = posOrigin;
        this.rect = rect;

        createVertexArray(rect);
    }

    private void createVertexArray(Rectangle rect) {
        // suppose fullscreen
        final float x = rect.x;
        final float y = rect.y;
        final float w = rect.w;
        final float h = rect.h;
        float[] vertexData = {
                // x, y, z,
                -w+x, -h+y, 0f,
                 w+x, -h+y, 0f,
                 w+x,  h+y, 0f,

                -w+x, -h+y, 0f,
                 w+x,  h+y, 0f,
                -w+x,  h+y, 0f
        };
        vertexArray = new VertexArray(vertexData);
    }

    private void createVertexArray() {
        // suppose fullscreen
        final float x = 1f;
        final float y = Graphics.aspectRatio;
        float[] vertexData = {
            // x, y, z,
            -x, -y, 0f,
             x, -y, 0f,
             x,  y, 0f,

            -x, -y, 0f,
             x,  y, 0f,
            -x,  y, 0f
        };
        vertexArray = new VertexArray(vertexData);
    }

    @Override
    public void init(PositionInfo pos) {
    }

    @Override
    public void update() {
        if (!done) {
            t += dt;
            if (t > 1f) {
                t = 1f;
                done = true;
            }
            colorCurrent.r = LERP(colorFrom.r, colorTo.r, t);
            colorCurrent.g = LERP(colorFrom.g, colorTo.g, t);
            colorCurrent.b = LERP(colorFrom.b, colorTo.b, t);
            colorCurrent.a = LERP(colorFrom.a, colorTo.a, t);
            //System.out.println("Fade update... " + t + ", " + colorCurrent.toString());
        }
    }

    public void draw() {
        // blending must be enabled!
        // use color shader!? (no textures)
        graphics.calcMatricesForObject(pos);
        graphics.colorShader.useProgram();
        graphics.colorShader.setUniforms(graphics.mvpMatrix, colorCurrent);
        graphics.colorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
        //System.out.println("Fade draw...");
    }
}
