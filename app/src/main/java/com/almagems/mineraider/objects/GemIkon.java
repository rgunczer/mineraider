package com.almagems.mineraider.objects;

import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.visuals.Visuals;
import com.almagems.mineraider.data.VertexArray;

public class GemIkon {

    private PositionInfo pos;
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

        pos = new PositionInfo();
        pos.trans(-0.92f, -Visuals.aspectRatio + 0.075f, 0f);
        pos.rot(0f, 0f, -30f);
        pos.scale(1f, 1f, 1f);
    }

    public void update() {
        // TODO: do animation update
    }

    public void draw() {
        /*
        Visuals visuals = Visuals.getInstance();
        visuals.calcMatricesForObject(op);
        visuals.textureShader.setTexture(visuals.textureIkon);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);

        visuals.textureShader.bindData(vertexArray);
        glDrawArrays(GL_TRIANGLES, 0, 12);
        */
    }
}
