package com.almagems.mineraider.EffectAnims;

import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.util.MyColor;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.util.MyUtils.LERP;

public class Fade extends EffectAnim {

    private final Visuals visuals;

    private VertexArray vertexArray;
    private MyColor colorFrom;
    private MyColor colorTo;
    public MyColor colorCurrent;
    private float t;
    private float dt = 0.05f; // speed
    public boolean done = false;

    // ctor
    public Fade() {
        visuals = Visuals.getInstance();
        createVertexArray();
    }

    public void init(MyColor from, MyColor to) { // do we need speed param!? for how fast the fading should occur
        this.colorFrom = from;
        this.colorTo = to;
        colorCurrent = new MyColor(from);
        t = 0.0f;
        done = false;
        pos = posOrigin;
    }

    private void createVertexArray() {
        // suppose fullscreen
        final float x = 1f;
        final float y = Visuals.aspectRatio;
        float[] vertexData = {
            // x, y, z,
            -x, -y, 0.0f,
             x, -y, 0.0f,
             x,  y, 0.0f,

            -x, -y, 0.0f,
             x,  y, 0.0f,
            -x,  y, 0.0f
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
        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);

        visuals.calcMatricesForObject(pos);
        visuals.colorShader.useProgram();
        visuals.colorShader.setUniforms(visuals.mvpMatrix, colorCurrent);
        visuals.colorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
        //System.out.println("Fade draw...");
    }
}
