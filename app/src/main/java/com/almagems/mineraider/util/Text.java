package com.almagems.mineraider.util;

import com.almagems.mineraider.Constants;
import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.shaders.TextureShader;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class Text {

    private float dx;
    private final Visuals visuals;
    private VertexArray vertexArray;
    private Vector pos;
    private String text;
    private float fontScale;

    // ctor
    public Text() {
        visuals = Visuals.getInstance();
    }

    public void init(float x, float y, String text, MyColor color, float fontScale) {

        dx = 0f;
        this.fontScale = fontScale;
        this.text = text;
        pos = new Vector(x, y, 0f);

        int index = 0;
        float[] vertexData = new float[text.length() * 6 * 9];

        final int len = text.length();
        for (int i = 0; i < len; i++) {
            float[] array = getCharArray( Character.toString(text.charAt(i)), i, color);
            for (int j = 0; j < array.length; ++j, ++index) {
                vertexData[index] = array[j];
            }
        }

        vertexArray = new VertexArray(vertexData);
    }

    private float[] getCharArray(String ch, int charIndex, MyColor color) {
        TexturedQuad fontQuad = visuals.fonts.get(ch);

        float r = color.r;
        float g = color.g;
        float b = color.b;
        float a = color.a;

        float tx0 = fontQuad.tx_lo_left.x;
        float tx1 = fontQuad.tx_up_right.x;
        float ty0 = fontQuad.tx_lo_left.y;
        float ty1 = fontQuad.tx_up_right.y;

        float scale = Visuals.screenWidth / 1080f;



        float x = ((fontQuad.w / Visuals.screenWidth) * scale) * fontScale;
        float y = ((fontQuad.h / Visuals.screenWidth) * scale) * fontScale;
        dx = charIndex * fontScale * 0.06f;

        float[] vertexData = {
                // x, y, z, 	                        s, t,
//                -x + dx, -y, 0.0f, 	r, g, b, a,     tx0, ty1,
//                 x + dx, -y, 0.0f,	r, g, b, a,     tx1, ty1,
//                 x + dx,  y, 0.0f,	r, g, b, a,     tx1, ty0,
//
//                -x + dx, -y, 0.0f,	r, g, b, a,     tx0, ty1,
//                 x + dx,  y, 0.0f,	r, g, b, a,     tx1, ty0,
//                -x + dx,  y, 0.0f,	r, g, b, a,     tx0, ty0

                -x + dx, -0f, 0.0f, 	r, g, b, a,     tx0, ty1,
                 x + dx, -0f, 0.0f,	r, g, b, a,     tx1, ty1,
                 x + dx,  2*y, 0.0f,	r, g, b, a,     tx1, ty0,

                -x + dx, -0f, 0.0f,	r, g, b, a,     tx0, ty1,
                 x + dx,  2*y, 0.0f,	r, g, b, a,     tx1, ty0,
                -x + dx,  2*y, 0.0f,	r, g, b, a,     tx0, ty0


        };



        return vertexData;
    }

    public void update() {

    }

    public void draw() {
        int width = (int)Visuals.screenWidth;
        int height = (int)Visuals.screenHeight;
        visuals.setProjectionMatrix2D(width, height);
        float aspect = width > height ? (float)width / (float)height : (float)height / (float)width;

        ObjectPosition op = new ObjectPosition();
        op.setPosition(pos.x, pos.y, 0f);
        op.setRot(0f, 0f, 0f);
        op.setScale(aspect, aspect, 1.0f);

        visuals.calcMatricesForObject(op);
        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureFonts);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);

        bindData(visuals.textureShader, vertexArray);
        glDrawArrays(GL_TRIANGLES, 0, text.length() * 2 * 3);
    }

    public void bindData(TextureShader textureProgram, VertexArray vertexArray) {
        final int POSITION_COMPONENT_COUNT = 3;
        final int COLOR_COMPONENT_COUNT = 4;
        final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
        final int STRIDE = (POSITION_COMPONENT_COUNT +
                COLOR_COMPONENT_COUNT +
                TEXTURE_COORDINATES_COMPONENT_COUNT ) * Constants.BYTES_PER_FLOAT;

        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT,
                textureProgram.getTextureAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);

    }

}
