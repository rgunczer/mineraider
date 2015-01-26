package com.almagems.mineraider.objects;

import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Texture;
import com.almagems.mineraider.util.TexturedQuad;

import static android.opengl.GLES20.*;

public class Quad {
    public final PositionInfo pos;
    private VertexArray vertexArray;
    private int textureId;

    // ctor
    public Quad() {
        pos = new PositionInfo();
    }

    public boolean isHit(float x, float y) {
        final float h = pos.sy;
        final float w = pos.sx;

        if ( y > (pos.ty - h) && y < (pos.ty + h) &&
             x > (pos.tx - w) && x < (pos.tx + w) ) {
            System.out.println("Here....");
            return true;
        }

        return false;
    }

    public void init(int textureId, MyColor color, Rectangle rect, boolean flipUTextureCoordinate) {
        this.textureId = textureId;

        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);

        float x = rect.x;
        float y = rect.y;
        float w = rect.w;
        float h = rect.h;

        Texture texture = Visuals.getInstance().getTextureObj(textureId);
        float tw = texture.width;
        float th = texture.height;

        TexturedQuad pFont = new TexturedQuad();
        // x								// y
        pFont.tx_lo_left.x = x / tw;        pFont.tx_lo_left.y = (th - (y - h)) / th;  // 0
        pFont.tx_lo_right.x = (x + w) / tw; pFont.tx_lo_right.y = (th - (y - h)) / th; // 1
        pFont.tx_up_right.x = (x + w) / tw; pFont.tx_up_right.y = (th - y) / th;       // 2
        pFont.tx_up_left.x = x / tw;        pFont.tx_up_left.y =  (th - y) / th;       // 3

        float tx0 = pFont.tx_lo_left.x;
        float tx1 = pFont.tx_up_right.x;
        float ty0 = pFont.tx_lo_left.y;
        float ty1 = pFont.tx_up_right.y;

        if (!flipUTextureCoordinate) {
            float[] vertexData = {
                    // x, y, z, 		// r g b a                              u, v,
                    -1f, -1f, 0.0f,     color.r, color.g, color.b, color.a,     tx0, ty1,
                     1f, -1f, 0.0f,     color.r, color.g, color.b, color.a,     tx1, ty1,
                     1f,  1f, 0.0f,     color.r, color.g, color.b, color.a,     tx1, ty0,

                    -1f, -1f, 0.0f,     color.r, color.g, color.b, color.a,     tx0, ty1,
                     1f,  1f, 0.0f,     color.r, color.g, color.b, color.a,     tx1, ty0,
                    -1f,  1f, 0.0f,     color.r, color.g, color.b, color.a,     tx0, ty0
            };
            vertexArray = new VertexArray(vertexData);
        } else {
            float[] vertexData = {
                    // x, y, z, 		// r g b a                            u, v,
                    -1f, -1f, 0.0f, color.r, color.g, color.b, color.a, tx1, ty1,
                    1f, -1f, 0.0f, color.r, color.g, color.b, color.a, tx0, ty1,
                    1f, 1f, 0.0f, color.r, color.g, color.b, color.a, tx0, ty0,

                    -1f, -1f, 0.0f, color.r, color.g, color.b, color.a, tx1, ty1,
                    1f, 1f, 0.0f, color.r, color.g, color.b, color.a, tx0, ty0,
                    -1f, 1f, 0.0f, color.r, color.g, color.b, color.a, tx1, ty0
            };
            vertexArray = new VertexArray(vertexData);
        }
    }

    public void update() {
        // TODO: animator object should update the quad
    }

    public void draw() {
        Visuals visuals = Visuals.getInstance();
        visuals.calcMatricesForObject(pos);
        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(textureId);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArray);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
