package com.almagems.mineraider.scenes;


import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.EffectAnims.Fade;
import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.IndexBuffer;
import com.almagems.mineraider.data.VertexBuffer;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Text;
import com.almagems.mineraider.util.Texture;

public class Menu extends Scene {

    private VertexBuffer vbBg;
    private IndexBuffer ibBg;

    private final Quad title;
    private final Quad play;
    private final Quad options;
    private final Quad about;
    private final Quad minecart;
    private final Quad gemTypes;
    private final Quad minerSign;

    private Fade fade;
    private final Visuals visuals;
    private ObjectPosition _op = new ObjectPosition();

    // ctor
    public Menu() {
        visuals = Visuals.getInstance();
        title = new Quad();
        play = new Quad();
        options = new Quad();
        about = new Quad();
        minecart = new Quad();
        gemTypes = new Quad();
        minerSign = new Quad();
    }

    private VertexBuffer createVertexBuffer(Rectangle rc, Texture texture) {
        float width = Visuals.screenWidth;
        float scale = width / 1080f;

        float tw = texture.width;
        float th = texture.height;

        float r = 1f;
        float g = 1f;
        float b = 1f;
        float a = 1f;

        float tx0 = rc.x / tw;
        float tx1 = (rc.x + rc.w) / tw;
        float ty0 = ((th - rc.y) - rc.h) / th;
        float ty1 = ((th - rc.y)) / th;

        float x = (rc.w / width) * scale;
        float y = (rc.h / width) * scale;
        float[] vertexData = {
                // x, y, z, 	                u, v,
                -x, -y, 0.0f, r, g, b, a, tx0, ty0,
                x, -y, 0.0f, r, g, b, a, tx1, ty0,
                x, y, 0.0f, r, g, b, a, tx1, ty1,

                -x, -y, 0.0f, r, g, b, a, tx0, ty0,
                x, y, 0.0f, r, g, b, a, tx1, ty1,
                -x, y, 0.0f, r, g, b, a, tx0, ty1
        };

        VertexBuffer vb = new VertexBuffer(vertexData);
        return vb;
    }

    @Override
	public void surfaceChanged(int width, int height) {
        final float r = 1f;
        final float g = 1f;
        final float b = 1f;
        final float a = 1f;
        final float aspect = Visuals.aspectRatio;

        final float x = 1.0f;
        final float y = aspect;

		float[] vertices = {
				// x, y, z, 			                u, v,
				-x, -y, 0.0f,   r, g, b, a,     0.0f, 0.0f, // 0
				 x, -y, 0.0f,	r, g, b, a,     1.0f, 0.0f, // 1
				 x,  y, 0.0f,	r, g, b, a,     1.0f, 1.0f, // 2
				 
				-x, -y, 0.0f,	r, g, b, a,     0.0f, 0.0f, // 3
			 	 x,  y, 0.0f,	r, g, b, a,     1.0f, 1.0f, // 4
				-x,  y, 0.0f,	r, g, b, a,     0.0f, 1.0f  // 5
			};		


        short[] indices = {
            // for gl_lines
            //0, 1,
            //1, 2,
            //2, 5,
            //5, 0

            0, 1, 2,
            3, 4, 5
        };

		vbBg = new VertexBuffer(vertices);
        ibBg = new IndexBuffer(indices);

        Rectangle rect;
        MyColor whiteColor = new MyColor(1f, 1f, 1f, 1f);
        final boolean flipUTextureCoordinate = false;

        rect = new Rectangle(0, 0+259, 496, 259);
        about.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        about.op.setPosition(0.48f, -aspect * 0.8f, 0f);
        about.op.setRot(0f, 0f, 0f);
        about.op.setScale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(817, 259+279, 638, 279);
        options.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        options.op.setPosition(0.14f, -aspect * 0.5f, 0f);
        options.op.setRot(0f, 0f, 0f);
        options.op.setScale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(1455, 259+288, 394, 288);
        play.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        play.op.setPosition(-0.35f, -0.2f, 0f);
        play.op.setRot(0f, 0f, 0f);
        play.op.setScale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(0, 684+286, 1080, 286);
        title.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        title.op.setPosition(0f, aspect*0.85f, 0f);
        title.op.setRot(0f, 0f, 0f);
        title.op.setScale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);


        rect = new Rectangle(496, 0+211, 1080, 211);
        gemTypes.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        gemTypes.op.setPosition(0f,  aspect*0.62f, 0f);
        gemTypes.op.setRot(0f, 0f, 0f);
        gemTypes.op.setScale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(0, 259+425, 489, 425);
        minerSign.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        minerSign.op.setPosition(0f, aspect * 0.24f, 0f);
        minerSign.op.setRot(0f, 0f, 0f);
        minerSign.op.setScale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(489, 259+254, 328, 254);
        minecart.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        minecart.op.setPosition(-0.68f, -aspect * 0.8f, 0f);
        minecart.op.setRot(0f, 0f, 0f);
        minecart.op.setScale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);




        fade = new Fade();
        fade.init(new MyColor(0f, 0f, 0f, 1f), new MyColor(0f, 0f, 0f, 0f));
	}

	@Override
	public void update() {
        visuals.updateViewProjMatrix();
	}

	@Override
	public void draw() {

        //play.op.tx += 0.01f;

        visuals.setProjectionMatrix2D();
        visuals.textureShader.useProgram();

		glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

        visuals.textureShader.setTexture(visuals.textureMenu);
		drawBg();

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);

        //visuals.textureShader.setTexture(visuals.textureMenuItems);

        title.draw();
        play.draw();
        options.draw();
        about.draw();
        gemTypes.draw();
        minerSign.draw();
        minecart.draw();

        //visuals.textureShader.setTexture(visuals.textureFonts);
        //text.draw();
       // credits.draw();

        visuals.bindNoTexture();
        fade.update();
        fade.draw();
	}

	private void drawBg() {
/*
        _op.setPosition(0f, 0f, 0f);
        _op.setRot(0f, 0f, 0f);
        _op.setScale(1.0f, 1.0f, 1.0f);

		visuals.calcMatricesForObject(_op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);


        vbBg.bind();
        visuals.textureShader.bindData(vbBg);

        ibBg.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
        vbBg.unbind();
        ibBg.unbind();
*/
	}

    @Override
	public void handleTouchPress(float normalizedX, float normalizedY) {

        if ( play.isHit(normalizedX, normalizedY) ) {
            System.out.println("play is hit...");
            //ClassicSingleton.getInstance().showScene(ScenesEnum.HelmetSelect);
        }

        if  (options.isHit(normalizedX, normalizedY) ) {
            System.out.println("options is hit...");
        }

        if ( about.isHit(normalizedX, normalizedY) ) {
            System.out.println("about is hit...");
        }
	}

	@Override
	public void handleTouchDrag(float normalizedX, float normalizedY) {

	}

	@Override
	public void handleTouchRelease(float normalizedX, float normalizedY) {

	}
}
