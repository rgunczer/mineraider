package com.almagems.mineraider.GUI;

import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.visuals.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.util.MyColor;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class ProgressBarControl {

    private String name;
    private float height;
    private float border;
    private VertexArray vertexArrayBody;
    private VertexArray vertexArrayFrame;
    public PositionInfo pos;
    private MyColor colorFrame;
    private MyColor colorBody;
    public float value = 0.0f;
    private Visuals visuals;

    // ctor
    public ProgressBarControl(Visuals visuals, String name, MyColor colorFrame, MyColor colorBody, float height, float border) {
        //System.out.println("MenuVolumeControl ctor...");
        this.visuals = visuals;
        this.name = name;
        this.colorBody = colorBody;
        this.colorFrame = colorFrame;
        this.height = height;
        this.border = border;
        value = 0f;
    }

    public void init(float y) {
        createVertexArrayBody();
        createVertexArrayFrame();

        pos = new PositionInfo();
        pos.trans(0f, y, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);
    }

    private void createVertexArrayBody() {
        final float x = 0.1f;
        final float y = height;  //0.05f;
        float[] vertexData = {
            // x, y, z,
            0f, -y, 0f,
             x, -y, 0f,
             x,  y, 0f,

            0f, -y, 0f,
             x,  y, 0f,
            0f,  y, 0f
        };
        vertexArrayBody = new VertexArray(vertexData);
    }

    private void createVertexArrayFrame() {
        final float x = -0.92f;
        final float y = height + border; //0.02f; //0.07f;
        float[] vertexData = {
            // x, y, z,
            -x, -y, 0f,
             x, -y, 0f,
             x,  y, 0f,

            -x, -y, 0f,
             x,  y, 0f,
            -x,  y, 0f
        };
        vertexArrayFrame = new VertexArray(vertexData);
    }

    public void update() {

    }

    public void draw() {
        //System.out.println("draw " + name);

        // frame
        pos.trans(0f, pos.ty, 0f);
        pos.scale(1f, 1f, 1f);
        visuals.calcMatricesForObject(pos);
        visuals.colorShader.setUniforms(visuals.mvpMatrix, colorFrame);
        visuals.colorShader.bindData(vertexArrayFrame);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // body
        pos.trans(-0.9f, pos.ty, 0f);
        pos.scale(18f * value, 1f, 1f);
        visuals.calcMatricesForObject(pos);
        visuals.colorShader.setUniforms(visuals.mvpMatrix, colorBody);
        visuals.colorShader.bindData(vertexArrayBody);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

}
