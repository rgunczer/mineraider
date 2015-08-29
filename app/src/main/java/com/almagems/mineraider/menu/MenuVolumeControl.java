package com.almagems.mineraider.menu;

import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.util.MyColor;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class MenuVolumeControl {

    private String name;
    private VertexArray vertexArray;
    public PositionInfo pos;
    private MyColor color;
    public float volumeValue = 0.0f;

    // ctor
    public MenuVolumeControl(String name) {
        this.name = name;
        System.out.println("MenuVolumeControl ctor...");
    }

    public void init(float y) {
        createVertexArray();
        pos = new PositionInfo();

        pos.tx = 0f;
        pos.ty = y;
        pos.tz = 0f;

        pos.rx = 0f;
        pos.ry = 0f;
        pos.rz = 0f;

        pos.sx = 1.0f;
        pos.sy = 1.0f;
        pos.sz = 1.0f;

        //color = new MyColor(1f, 0f, 0f, 1f);
        color = new MyColor(143f/255f, 127f/255f, 96f/255f, 1f);
    }

    private void createVertexArray() {
        // suppose fullscreen
        final float x = 0.1f;
        final float y = 0.06f;
        float[] vertexData = {
                // x, y, z,
                0f, -y, 0f,
                 x, -y, 0f,
                 x,  y, 0f,

                0f, -y, 0f,
                 x,  y, 0f,
                0f,  y, 0f
        };
        vertexArray = new VertexArray(vertexData);
    }


    public void update() {

    }

    public void draw() {
        //System.out.println("draw " + name);

        pos.trans(-0.9f, pos.ty, 0f);
        //pos.rot(0f, 0f, 0f);
        pos.scale(18f * volumeValue, 1f, 1f);

        //System.out.println("Volume Value: " + volumeValue);

        // -0.9f - 0.9f = 18f

        Visuals visuals = Visuals.getInstance();

        visuals.calcMatricesForObject(pos);
        visuals.colorShader.useProgram();
        visuals.colorShader.setUniforms(visuals.mvpMatrix, color);
        visuals.colorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

}
