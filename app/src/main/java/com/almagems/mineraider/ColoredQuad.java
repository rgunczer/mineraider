package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class ColoredQuad {

    public static Graphics graphics;
    private VertexArray vertexArray;
    public final PositionInfo pos = new PositionInfo();

    // ctor
    public ColoredQuad() {
        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);
    }

    public void init(float w, float h, Color colorTop, Color colorBottom) {
        createVertexArray(w, h, colorTop, colorBottom);
    }

    private void createVertexArray(float x, float y, Color colorT, Color colorB) {
        float[] vertexData = {
            -x, -y, 0f, colorB.r, colorB.g, colorB.b, colorB.a,
             x, -y, 0f, colorB.r, colorB.g, colorB.b, colorB.a,
             x,  y, 0f, colorT.r, colorT.g, colorT.b, colorT.a,

            -x, -y, 0f, colorB.r, colorB.g, colorB.b, colorB.a,
             x,  y, 0f, colorT.r, colorT.g, colorT.b, colorT.a,
            -x,  y, 0f, colorT.r, colorT.g, colorT.b, colorT.a,
        };
        vertexArray = new VertexArray(vertexData);
    }

    public void draw() {
        graphics.calcMatricesForObject(pos);
        graphics.colorShader.useProgram();
        graphics.colorShader.setUniforms(graphics.mvpMatrix);
        graphics.colorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public void drawBatch() {
        graphics.calcMatricesForObject(pos);
        graphics.colorShader.setUniforms(graphics.mvpMatrix);
        graphics.colorShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

}
