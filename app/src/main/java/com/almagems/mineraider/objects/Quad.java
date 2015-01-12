package com.almagems.mineraider.objects;

import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.shaders.TextureShader;
import com.almagems.mineraider.util.MyColor;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class Quad {
    public ObjectPosition op;
    private VertexArray vertexArray;
    private int textureId;

    // ctor
    public Quad() {
    }

    public void init(int textureId, MyColor color) {
        this.textureId = textureId;

        op = new ObjectPosition();
        op.setPosition(0f, 0f, 0f);
        op.setRot(0f, 0f, 0f);
        op.setScale(1f, 1f, 1f);

        float[] vertexData = {
                // x, y, z, 			                                  s, t,
                -1f, -1f, 0.0f,     color.r, color.g, color.b, color.a,   0.0f, 0.0f,
                 1f, -1f, 0.0f,     color.r, color.g, color.b, color.a,   1.0f, 0.0f,
                 1f,  1f, 0.0f,     color.r, color.g, color.b, color.a,   1.0f, 1.0f,

                -1f, -1f, 0.0f,     color.r, color.g, color.b, color.a,   0.0f, 0.0f,
                 1f,  1f, 0.0f,     color.r, color.g, color.b, color.a,   1.0f, 1.0f,
                -1f,  1f, 0.0f,     color.r, color.g, color.b, color.a,   0.0f, 1.0f,
        };

        vertexArray = new VertexArray(vertexData);
    }

    public void update() {
        // TODO: animator object should update the quad
    }

    public void draw() {
        Visuals visuals = Visuals.getInstance();
        visuals.calcMatricesForObject(op);
        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(textureId);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
