package com.almagems.mineraider.scenes;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glBlendFunc;

import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.EffectAnims.Fade;
import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Text;
import com.almagems.mineraider.util.Texture;

public class Menu extends Scene {
		
	private VertexArray vertexArrayBg;
	private final Quad title;
	private final Quad play;
	private final Quad options;
	private final Quad about;

    private Text text;
    private Text credits;

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
	}

    private VertexArray createVertexArray(Rectangle rc, Texture texture) {
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
                -x, -y, 0.0f, 	r, g, b, a,     tx0, ty0,
                x, -y, 0.0f,	r, g, b, a,     tx1, ty0,
                x,  y, 0.0f,	r, g, b, a,     tx1, ty1,

                -x, -y, 0.0f,	r, g, b, a,     tx0, ty0,
                x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
                -x,  y, 0.0f,	r, g, b, a,     tx0, ty1
        };

        VertexArray vertexArray = new VertexArray(vertexData);
        return vertexArray;
    }

	@Override
	public void surfaceChanged(int width, int height) {
        final float r = 1f;
        final float g = 1f;
        final float b = 1f;
        final float a = 1f;
        final float aspect = Visuals.aspectRatio;

		float[] vertexDataBg = { 
				// x, y, z, 			                u, v,
				-1.0f, -aspect, 0.0f,   r, g, b, a,     0.0f, 0.0f,
				 1.0f, -aspect, 0.0f,	r, g, b, a,     1.0f, 0.0f,
				 1.0f,  aspect, 0.0f,	r, g, b, a,     1.0f, 1.0f,
				 
				-1.0f, -aspect, 0.0f,	r, g, b, a,     0.0f, 0.0f,
			 	 1.0f,  aspect, 0.0f,	r, g, b, a,     1.0f, 1.0f,
				-1.0f,  aspect, 0.0f,	r, g, b, a,     0.0f, 1.0f
			};		
		
		vertexArrayBg = new VertexArray(vertexDataBg);

        MyColor whiteColor = new MyColor(1f, 1f, 1f, 1f);
        final boolean flipUTextureCoordinate = false;

        about.init(visuals.textureMenuItems, whiteColor, new Rectangle(0, 0+248, 460, 248), flipUTextureCoordinate);
        about.op.setPosition(0f, -1.32f, 0f);
        about.op.setRot(0f, 0f, 0f);
        about.op.setScale(0.5f * 460f * 0.0018f, 0.5f * 248f * 0.0018f, 1.0f);

        options.init(visuals.textureMenuItems, whiteColor, new Rectangle(460, 0+260, 576, 260), flipUTextureCoordinate);
        options.op.setPosition(0f, -0.67f, 0f);
        options.op.setRot(0f, 0f, 0f);
        options.op.setScale(0.5f * 576f * 0.0018f, 0.5f * 260f * 0.0018f, 1f);

        play.init(visuals.textureMenuItems, whiteColor, new Rectangle(1036, 0+248, 370, 248), flipUTextureCoordinate);
        play.op.setPosition(0f, 0.0f, 0f);
        play.op.setRot(0f, 0f, 0f);
        play.op.setScale(0.5f * 370f * 0.0018f, 0.5f * 248f * 0.0018f, 1f);

        title.init(visuals.textureMenuItems, whiteColor, new Rectangle(0, 260+346, 1080, 346), flipUTextureCoordinate);
        title.op.setPosition(0f, 1f, 0f);
        title.op.setRot(0f, 0f, 0f);
        title.op.setScale(0.5f*1080f*0.0018f, 0.5f*346*0.0018f, 1f);

        //Texture texture = Visuals.getInstance().getTextureObj(Visuals.getInstance().textureMenuItems);

//        vertexArrayAbout = createVertexArray( new Rectangle(0, 0, 460, 248), texture );
//        vertexArrayOptions = createVertexArray( new Rectangle(460, 0, 576, 260), texture) ;
//        vertexArrayPlay = createVertexArray( new Rectangle(1036, 0, 370, 248), texture);
//        vertexArrayTitle = createVertexArray( new Rectangle(0, 260, 1080, 346), texture );

        text = new Text();
        text.setSpacingScale(0.09f);
        text.init("ANDREA", new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.5f);
        text.pos.setPosition(-0.95f, -Visuals.aspectRatio, 0f);

        credits = new Text();
        credits.init("CREDITS", new MyColor(1f, 0f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.5f);
        credits.pos.setPosition(-0.85f, 1.04f, 0f);

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

        visuals.textureShader.setTexture(visuals.textureMenu);
		drawBg();

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);

        visuals.textureShader.setTexture(visuals.textureMenuItems);
/*
		drawTitle();
		drawPlay();
		drawOptions();
		drawAbout();
*/
        title.draw();
        play.draw();
        options.draw();
        about.draw();

        visuals.textureShader.setTexture(visuals.textureFonts);
        text.draw();
        credits.draw();

        visuals.bindNoTexture();
        fade.update();
        fade.draw();
	}

	/*
	private void drawAbout() {
		ObjectPosition op = new ObjectPosition();
		op.setPosition(0f, -1.32f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(1f, 1f, 1.0f);
		
		visuals.calcMatricesForObject(op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayAbout);
		glDrawArrays(GL_TRIANGLES, 0, 6);				
	}
	
	private void drawOptions() {
		ObjectPosition op = new ObjectPosition();		
		op.setPosition(0f, -0.67f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(1f, 1f, 1f);
					
		visuals.calcMatricesForObject(op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayOptions);
		glDrawArrays(GL_TRIANGLES, 0, 6);				
	}
	
	private void drawPlay() {
		_op.setPosition(0f, -0.03f, 0f);
        _op.setRot(0f, 0f, 0f);
        _op.setScale(1f, 1f, 1f);

		visuals.calcMatricesForObject(_op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayPlay);
		glDrawArrays(GL_TRIANGLES, 0, 6);		
	}
*/
	private void drawBg() {
		_op.setPosition(0f, 0f, 0f);
        _op.setRot(0f, 0f, 0f);
        _op.setScale(1.0f, 1.0f, 1.0f);
		
		visuals.calcMatricesForObject(_op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayBg);
		glDrawArrays(GL_TRIANGLES, 0, 6);			
	}
/*
    private void drawTitle() {
        _op.setPosition(0f, 1.03f, 0f);
        _op.setRot(0f, 0f, 0f);
        _op.setScale(1f, 1f, 1f);

        visuals.calcMatricesForObject(_op);
        visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayTitle);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
*/
    @Override
	public void handleTouchPress(float normalizedX, float normalizedY) {



        boolean isHit = play.isHit(normalizedX, normalizedY);

        if (isHit) {
            //System.out.println("is hit...");
            ClassicSingleton.getInstance().showScene(ScenesEnum.HelmetSelect);
        }

	}

	@Override
	public void handleTouchDrag(float normalizedX, float normalizedY) {

	}

	@Override
	public void handleTouchRelease(float normalizedX, float normalizedY) {

	}
}
