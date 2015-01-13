package com.almagems.mineraider.util;

import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;


public class Text {

    private final Visuals visuals;
    private VertexArray vertexArray;
    public Vector pos = new Vector(0f, 0f, 0f);
    private String text;
    private float fontScale;
    private float fontSpacingScale = 0.06f;
    private final ObjectPosition op = new ObjectPosition();

    // ctor
    public Text() {
        visuals = Visuals.getInstance();
    }

    public void setSpacingScale(float scale) {
        fontSpacingScale = scale;
    }

    public void init(String text, MyColor color, float fontScale) {
        this.fontScale = fontScale;
        this.text = text;

        final int numOfTriangles = 2;
        final int verticesPerTriangle = 3;
        final int numOfComponentsPerVertex = 9; // x, y, z, r, g, b, a, s, t
        int index = 0;
        float[] vertexData = new float[text.length() * numOfTriangles * verticesPerTriangle * numOfComponentsPerVertex];

        final int len = text.length();
        for (int i = 0; i < len; i++) {
            float[] array = getCharArray( Character.toString(text.charAt(i)), i, color);
            for (int j = 0; j < array.length; ++j, ++index) {
                vertexData[index] = array[j];
            }
        }

        vertexArray = new VertexArray(vertexData);
    }

    public float getTextWidth() {
        return (text.length() - 1) * fontScale * fontSpacingScale;
    }

    public float getTextHeight() {
        TexturedQuad fontQuad = visuals.fonts.get("W");
        float y = ((fontQuad.h / Visuals.screenWidth) * Visuals.scaleFactor) * fontScale;
        return y * 2f;
    }

    private float[] getCharArray(String ch, int charIndex, MyColor color) {
        TexturedQuad fontQuad = visuals.fonts.get(ch);
        float x, y;
        float tx0 = fontQuad.tx_lo_left.x;
        float tx1 = fontQuad.tx_up_right.x;
        float ty0 = fontQuad.tx_lo_left.y;
        float ty1 = fontQuad.tx_up_right.y;

//        x = 0.025f;
//        y = 0.025f;
        x = ((fontQuad.w / Visuals.screenWidth) * Visuals.scaleFactor) * fontScale;
        y = ((fontQuad.h / Visuals.screenWidth) * Visuals.scaleFactor) * fontScale;
        float space = charIndex * fontScale * fontSpacingScale;

        float[] vertexData = {
                // x, y, z, 	                                                    s, t,
                -x + space,  -y, 0.0f, 	color.r, color.g, color.b, color.a,     tx0, ty1,
                 x + space,  -y, 0.0f,	color.r, color.g, color.b, color.a,     tx1, ty1,
                 x + space,   y, 0.0f,	color.r, color.g, color.b, color.a,     tx1, ty0,

                -x + space,  -y, 0.0f, 	color.r, color.g, color.b, color.a,     tx0, ty1,
                 x + space,   y, 0.0f, 	color.r, color.g, color.b, color.a,     tx1, ty0,
                -x + space,   y, 0.0f, 	color.r, color.g, color.b, color.a,     tx0, ty0
        };

        return vertexData;
    }

    public void update() {

    }

    public void draw() { // suppose we are in 2D projection mode
        op.setPosition(pos.x, pos.y, 0f);
        op.setRot(0f, 0f, 0f);
        op.setScale(1f, 1f, 1f);

        visuals.calcMatricesForObject(op);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);

        visuals.textureShader.bindData(vertexArray);
        final int numOfTriangles = 2;
        final int numOfVerticesPerTriangle = 3;
        glDrawArrays(GL_TRIANGLES, 0, text.length() * numOfTriangles * numOfVerticesPerTriangle);
    }
}
