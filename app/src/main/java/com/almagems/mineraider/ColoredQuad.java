package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class ColoredQuad {
    public static Graphics graphics;
    private VertexArray vertexArray;
    public final Color color = new Color();
    public final PositionInfo pos = new PositionInfo();


    // ctor
    public ColoredQuad() {
        this(Color.GRAY);
        createVertexArray(1f, 1f);
    }

    public ColoredQuad(Color color) {
        this.color.init(color);

        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);
    }

    public void init(Color color, float w, float h) {
        this.color.init(color);
        pos.sx = w;
        pos.sy = h;
    }

    private void createVertexArray(float x, float y) {
        float[] vertexData = {
                -x, -y, 0f,
                 x, -y, 0f,
                 x,  y, 0f,

                -x, -y, 0f,
                 x,  y, 0f,
                -x,  y, 0f
        };
        vertexArray = new VertexArray(vertexData);
    }

    public void draw() {
        graphics.calcMatricesForObject(pos);
        graphics.colorShader.useProgram();
        graphics.colorShader.setUniforms(graphics.mvpMatrix, color);
        graphics.colorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

}
