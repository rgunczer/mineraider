package com.almagems.mineraider.objects;

import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class GemIkon {

    private ObjectPosition op;
    private VertexArray vertexArray;

    // ctor
    public void Quad() {

    }

    public void init() {
        float x = 0.07f;
        float y = 0.07f;
        float x1 = 0.06f;
        float y1 = 0.06f;

        float[] vertexData = {
                // x, y, z, 			            s, t,
                -x, -y, 0.0f,   0f, 0f, 0f, 1f,     0.0f, 0.0f,
                 x, -y, 0.0f,   0f, 0f, 0f, 1f,     1.0f, 0.0f,
                 x,  y, 0.0f,   0f, 0f, 0f, 1f,     1.0f, 1.0f,

                -x, -y, 0.0f,   0f, 0f, 0f, 1f,     0.0f, 0.0f,
                 x,  y, 0.0f,   0f, 0f, 0f, 1f,     1.0f, 1.0f,
                -x,  y, 0.0f,   0f, 0f, 0f, 1f,     0.0f, 1.0f,

                // x, y, z, 			            s, t,
                -x1, -y1, 0.0f, 1f, 1f, 1f, 1f,     0.0f, 0.0f,
                 x1, -y1, 0.0f, 1f, 1f, 1f, 1f,     1.0f, 0.0f,
                 x1,  y1, 0.0f, 1f, 1f, 1f, 1f,     1.0f, 1.0f,

                -x1, -y1, 0.0f, 1f, 1f, 1f, 1f,     0.0f, 0.0f,
                 x1,  y1, 0.0f, 1f, 1f, 1f, 1f,     1.0f, 1.0f,
                -x1,  y1, 0.0f, 1f, 1f, 1f, 1f,     0.0f, 1.0f
        };

        vertexArray = new VertexArray(vertexData);

        op = new ObjectPosition();
        op.setPosition(-0.92f, -Visuals.aspectRatio + 0.075f, 0f);
        op.setRot(0f, 0f, -30f);
        op.setScale(1f, 1f, 1f);
    }

    public void update() {
        // TODO: do animation update
    }

    public void draw() {
        Visuals visuals = Visuals.getInstance();
        visuals.calcMatricesForObject(op);
        visuals.textureShader.setTexture(visuals.textureIkon);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);

        visuals.textureShader.bindData(vertexArray);
        glDrawArrays(GL_TRIANGLES, 0, 12);
    }
}
