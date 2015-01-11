package com.almagems.mineraider;

import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.shaders.TextureShader;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Text;
import com.almagems.mineraider.util.TexturedQuad;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static com.almagems.mineraider.Constants.BYTES_PER_FLOAT;

public class HUD {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORD_COMPONENT_COUNT = 2;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORD_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final Visuals visuals;
    private final ArrayList<TexturedQuad> scoreText = new ArrayList<TexturedQuad>(12);
    private final VertexArray vertexArray;
    private final float[] vertices;

    private int index;
    private int triangleCount;
    private int cachedScore;

    private Text text;

    private VertexArray vertexArrayIkon;


    // ctor
    public HUD() {
        System.out.println("HUD ctor...");
        visuals = Visuals.getInstance();

        final int maxFontCount = 30;
        vertices = new float[maxFontCount * 6 * TOTAL_COMPONENT_COUNT];
        vertexArray = new VertexArray(vertices);

        text = new Text();
    }

    private void begin() {
        index = 0;
        triangleCount = 0;
    }

    private void build(float fromX, float fromY) {
        TexturedQuad font;
        int start = index;
        int size = scoreText.size();
        for (int i = 0; i < size; ++i) {
            font = scoreText.get(i);
            addFont(font);
        }
        vertexArray.updateBuffer(vertices, start, size * 6 * TOTAL_COMPONENT_COUNT);
    }

    private void addFont(TexturedQuad font) {

            // position
//            vertices[index++] = from.x;
//            vertices[index++] = from.y;
            vertices[index++] = 0f;

            // textire coord
//            vertices[index++] = to.x;
//            vertices[index++] = to.y;


           // ++lineCount;


    }

    public void init() {
        scoreText.clear();

        TexturedQuad font;
        font = visuals.fonts.get("A");
        scoreText.add(font);
        font = visuals.fonts.get("B");
        scoreText.add(font);
        font = visuals.fonts.get("D");
        scoreText.add(font);

        begin();
        build(0f, 0f);

        text.init(-0.8f, -Visuals.aspectRatio, "SCORE:" + cachedScore, new MyColor(1f, 1f, 0f, 1f), 0.5f);

        float x = 0.07f;
        float y = 0.07f;
        float x1 = 0.06f;
        float y1 = 0.06f;

        // ikon quad
        float[] vertexDataIkon = {
                // x, y, z, 			                s, t,
                -x, -y, 0.0f,       0f, 0f, 0f, 1f,     0.0f, 0.0f,
                 x, -y, 0.0f,	    0f, 0f, 0f, 1f,     1.0f, 0.0f,
                 x,  y, 0.0f,	    0f, 0f, 0f, 1f,     1.0f, 1.0f,

                -x, -y, 0.0f,	    0f, 0f, 0f, 1f,     0.0f, 0.0f,
                 x,  y, 0.0f,	    0f, 0f, 0f, 1f,     1.0f, 1.0f,
                -x,  y, 0.0f,	    0f, 0f, 0f, 1f,     0.0f, 1.0f,

                // x, y, z, 			                s, t,
                -x1, -y1, 0.0f,     1f, 1f, 1f, 1f,     0.0f, 0.0f,
                 x1, -y1, 0.0f,	    1f, 1f, 1f, 1f,     1.0f, 0.0f,
                 x1,  y1, 0.0f,	    1f, 1f, 1f, 1f,     1.0f, 1.0f,

                -x1, -y1, 0.0f,	    1f, 1f, 1f, 1f,     0.0f, 0.0f,
                 x1,  y1, 0.0f,	    1f, 1f, 1f, 1f,     1.0f, 1.0f,
                -x1,  y1, 0.0f,	    1f, 1f, 1f, 1f,     0.0f, 1.0f
        };

        vertexArrayIkon = new VertexArray(vertexDataIkon);


    }

    public void updateScore(int score) {
        if (score != cachedScore) {
            String str = "SCORE:" + score;
            text.init(-0.8f, -Visuals.aspectRatio - 0.02f, str, new MyColor(1f, 1f, 0f, 1f), 0.5f);
            cachedScore = score;
        }
    }

    public void update() {
        text.update();
    }

    public void draw() {
        // TODO: goto 2d mode
        // TODO: draw score...
        // TODO: draw gem icons...
/*
        TexturedQuad font;
        int size = scoreText.size();
        for(int i = 0; i < size; ++i) {
            font = scoreText.get(i);





        }
*/
        glDisable(GL_DEPTH_TEST);

        drawIkon();
        text.draw();
    }

    private void drawIkon() {
        ObjectPosition op = new ObjectPosition();
        op.setPosition(-0.92f, -Visuals.aspectRatio + 0.075f, 0f);
        //op.setPosition(0f, 0f, 0f);
        op.setRot(0f, 0f, -30f);


        visuals.calcMatricesForObject(op);
        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureIkon);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);

        bindData(visuals.textureShader, vertexArrayIkon);
        glDrawArrays(GL_TRIANGLES, 0, 12);

/*
        ObjectPosition op = new ObjectPosition();
        op.setPosition(-0.9f, -Visuals.aspectRatio + 0.075f, 0f);
        op.setRot(0f, 0f, 0f);
        //op.setScale(0.06f, 0.06f, 1.0f);

        visuals.calcMatricesForObject(op);
        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureIkon);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);

        bindData(visuals.textureShader, vertexArrayIkon);
        glDrawArrays(GL_TRIANGLES, 0, 6);
*/
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
