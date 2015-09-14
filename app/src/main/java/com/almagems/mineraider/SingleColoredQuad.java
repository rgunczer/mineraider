package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class SingleColoredQuad {
    public static Graphics graphics;
    private VertexArray vertexArray;
    public final Color color = new Color();
    public final PositionInfo pos = new PositionInfo();

    public float posYorigin;

    // ctor
    public SingleColoredQuad() {
        this(Color.GRAY);
        createVertexArray(1f, 1f);
    }

    public SingleColoredQuad(Color color) {
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
        graphics.singleColorShader.useProgram();
        graphics.singleColorShader.setUniforms(graphics.mvpMatrix, color);
        graphics.singleColorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public void drawBatch() {
        graphics.calcMatricesForObject(pos);
        graphics.singleColorShader.setUniforms(graphics.mvpMatrix, color);
        graphics.singleColorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

}
