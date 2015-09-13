package com.almagems.mineraider;

import static android.opengl.GLES20.*;

public final class Text {

    public float posYorigin;
    public static Graphics graphics;
    private VertexArray vertexArray;
    private String text;
    private float fontScale;
    private float fontSpacingScale = 0.06f;
    public PositionInfo pos = new PositionInfo();
    private EffectAnim anim;

    public void setSpacingScale(float scale) {
        fontSpacingScale = scale;
    }

    public void init(String text, Color colorUp, Color colorDown, float fontScale) {
        this.fontScale = fontScale;
        this.text = text;

        final int numOfTriangles = 2;
        final int verticesPerTriangle = 3;
        final int numOfComponentsPerVertex = 9; // x, y, z,     r, g, b, a,     u, v
        int index = 0;
        float[] vertexData = new float[text.length() * numOfTriangles * verticesPerTriangle * numOfComponentsPerVertex];

        final int len = text.length();
        for (int i = 0; i < len; i++) {
            float[] array = getCharArray( Character.toString(text.charAt(i)), i, colorUp, colorDown);
            for (int j = 0; j < array.length; ++j, ++index) {
                vertexData[index] = array[j]; // fill
            }
        }
        vertexArray = new VertexArray(vertexData);
    }

    public float getTextWidth() {
        return (text.length() - 1) * fontScale * fontSpacingScale;
    }

    public float getTextHeight() {
        TexturedQuad fontQuad = graphics.fonts.get("W");
        float y = ((fontQuad.h / Graphics.screenWidth) * Graphics.scaleFactor) * fontScale;
        return y * 2f;
    }

    private float[] getCharArray(String ch, int charIndex, Color colorUp, Color colorDown) {
        TexturedQuad fontQuad = graphics.fonts.get(ch);
        float x, y;
        float tx0 = fontQuad.tx_lo_left.x;
        float tx1 = fontQuad.tx_up_right.x;
        float ty0 = fontQuad.tx_lo_left.y;
        float ty1 = fontQuad.tx_up_right.y;

        x = ((fontQuad.w / Graphics.screenWidth) * Graphics.scaleFactor) * fontScale;
        y = ((fontQuad.h / Graphics.screenWidth) * Graphics.scaleFactor) * fontScale;
        float space = charIndex * fontScale * fontSpacingScale;

        float[] vertexData = {
                // x, y, z, 	                                                    s, t,
                -x + space,  -y, 0.0f, 	colorDown.r, colorDown.g, colorDown.b, colorDown.a,     tx0, ty1,
                 x + space,  -y, 0.0f,	colorDown.r, colorDown.g, colorDown.b, colorDown.a,     tx1, ty1,
                 x + space,   y, 0.0f,	colorUp.r,   colorUp.g,   colorUp.b,   colorUp.a,       tx1, ty0,

                -x + space,  -y, 0.0f, 	colorDown.r, colorDown.g, colorDown.b, colorDown.a,     tx0, ty1,
                 x + space,   y, 0.0f, 	colorUp.r,   colorUp.g,   colorUp.b,   colorUp.a,       tx1, ty0,
                -x + space,   y, 0.0f, 	colorUp.r,   colorUp.g,   colorUp.b,   colorUp.a,       tx0, ty0
        };

        return vertexData;
    }

    public void addAnimEffect(EffectAnim anim) {
        this.anim = anim;
        this.anim.init(this.pos);
    }

    public void removeAnimEffect() {
        if (this.anim != null) {
            this.pos.init(this.anim.posOrigin);
            this.anim = null;
        }
    }

    public void update() {
        if (anim != null) {
            anim.update();
        }
    }

    public void draw() {
        graphics.calcMatricesForObject(pos);
        graphics.textureShader.setUniforms(graphics.mvpMatrix);
        graphics.textureShader.bindData(vertexArray);

        final int numOfTriangles = 2;
        final int numOfVerticesPerTriangle = 3;
        glDrawArrays(GL_TRIANGLES, 0, text.length() * numOfTriangles * numOfVerticesPerTriangle);
    }
}
