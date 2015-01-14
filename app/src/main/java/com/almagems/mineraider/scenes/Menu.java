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

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Text;

public class Menu extends Scene {
		
	private VertexArray vertexArrayBg;
	private VertexArray vertexArrayTitle;
	private VertexArray vertexArrayPlay;
	private VertexArray vertexArrayOptions;
	private VertexArray vertexArrayAbout;

    private Text text;
    private Text credits;

    // ctor
	public Menu() {
		
	}

	@Override
	public void surfaceChanged(int width, int height) {
		final float tw = 512f;
		final float th = 1024f;

        float r = 1f;
        float g = 1f;
        float b = 1f;
        float a = 1f;

        float aspect = Visuals.aspectRatio;
				
		float[] vertexDataBg = { 
				// x, y, z, 			                s, t,
				-1.0f, -aspect, 0.0f,   r, g, b, a,     0.0f, 0.0f,
				 1.0f, -aspect, 0.0f,	r, g, b, a,     1.0f, 0.0f,
				 1.0f,  aspect, 0.0f,	r, g, b, a,     1.0f, 1.0f,
				 
				-1.0f, -aspect, 0.0f,	r, g, b, a,     0.0f, 0.0f,
			 	 1.0f,  aspect, 0.0f,	r, g, b, a,     1.0f, 1.0f,
				-1.0f,  aspect, 0.0f,	r, g, b, a,     0.0f, 1.0f
			};		
		
		vertexArrayBg = new VertexArray(vertexDataBg);

        float scale = width / 1080f;
		float tx0, tx1, ty0, ty1;
		float x, y;
		
//		"filename": "menu_title.png",
//		"frame": {"x":2,"y":2,"w":350,"h":286},
		tx0 = 2.0f / tw;
		tx1 = (2.0f + 350.0f) / tw;
		ty0 = ((th - 2.0f) - 286f) / th;
		ty1 = ((th - 2.0f)) / th; 

		x = (350f / width) * scale;
		y = (286f / width) * scale;
		float[] vertexDataTitle = {
				// x, y, z, 	                s, t,
				-x, -y, 0.0f, 	r, g, b, a,     tx0, ty0,
				 x, -y, 0.0f,	r, g, b, a,     tx1, ty0,
				 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				 
				-x, -y, 0.0f,	r, g, b, a,     tx0, ty0,
			 	 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				-x,  y, 0.0f,	r, g, b, a,     tx0, ty1
		};
		
		vertexArrayTitle = new VertexArray(vertexDataTitle);
		
//	"filename": "menu_about.png",
//	"frame": {"x":2,"y":415,"w":241,"h":125},
		tx0 = 2f / tw;
		tx1 = (2f + 241f) / tw;
		ty0 = ((th - 415f) - 125f)  / th;
		ty1 = ((th - 415f)) / th; 
				
		x = (241f / width) * scale;
		y = (125f / width) * scale;
		float[] vertexDataAbout = {
				// x, y, z, 	                s, t,
				-x, -y, 0.0f, 	r, g, b, a,     tx0, ty0,
				 x, -y, 0.0f,	r, g, b, a,     tx1, ty0,
				 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				 
				-x, -y, 0.0f,	r, g, b, a,     tx0, ty0,
			 	 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				-x,  y, 0.0f,	r, g, b, a,     tx0, ty1
		};
		
		vertexArrayAbout = new VertexArray(vertexDataAbout);

		
//	"filename": "menu_options.png",
//	"frame": {"x":2,"y":290,"w":291,"h":123},
		tx0 = 2f / tw;
		tx1 = (2f + 291f) / tw;
		ty0 = ((th - 290f) - 123f) / th;
		ty1 = ((th - 290f)) / th; 
				
		x = (291f / width) * scale;
		y = (123f / width) * scale;
		float[] vertexDataOptions = {
				// x, y, z, 	                s, t,
				-x, -y, 0.0f, 	r, g, b, a,     tx0, ty0,
				 x, -y, 0.0f,	r, g, b, a,     tx1, ty0,
				 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				 
				-x, -y, 0.0f,	r, g, b, a,     tx0, ty0,
			 	 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				-x,  y, 0.0f,	r, g, b, a,     tx0, ty1
		};
		
		vertexArrayOptions = new VertexArray(vertexDataOptions);

		
		g = 0f;
        b = 0f;

//	"filename": "menu_play.png",
//	"frame": {"x":2,"y":542,"w":214,"h":121},
		tx0 = 2f / tw;
		tx1 = (2f + 214f) / tw;
		ty0 = ((th - 542f) - 121f ) / th; // 0f
		ty1 = ((th - 542f) ) / th; 	// 1f
		
		x = (214f / width) * scale;
		y = (121f / width) * scale;
		float[] vertexDataPlay = {
				// x, y, z, 	                s, t,
				-x, -y, 0.0f, 	r, g, b, a,     tx0, ty0,
				 x, -y, 0.0f,	r, g, b, a,     tx1, ty0,
				 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				 
				-x, -y, 0.0f,	r, g, b, a,     tx0, ty0,
			 	 x,  y, 0.0f,	r, g, b, a,     tx1, ty1,
				-x,  y, 0.0f,	r, g, b, a,     tx0, ty1
		};
		
		vertexArrayPlay = new VertexArray(vertexDataPlay);

        text = new Text();
        text.setSpacingScale(0.09f);
        text.init("ANDREA", new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.5f);
        text.pos.setPosition(-0.95f, -Visuals.aspectRatio, 0f);

        credits = new Text();
        credits.init("CREDITS", new MyColor(1f, 0f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.5f);
        credits.pos.setPosition(-0.85f, 1.0f, 0f);
	}

	@Override
	public void update() {
		super.update();
        visuals.updateViewProjMatrix();
	}

	@Override
	public void draw() {
        visuals.setProjectionMatrix2D();
        visuals.textureShader.useProgram();

		glDisable(GL_DEPTH_TEST);

        visuals.textureShader.setTexture(visuals.textureMenu);
		drawBg();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);

        visuals.textureShader.setTexture(visuals.textureMenuItems);
		drawTitle();
		drawPlay();
		drawOptions();
		drawAbout();

        visuals.textureShader.setTexture(visuals.textureFonts);
        text.draw();
        credits.draw();
	}
	
	private void drawAbout() {
		ObjectPosition op = new ObjectPosition();
		op.setPosition(-0.5f, -1.2f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(Visuals.aspectRatio, Visuals.aspectRatio, 1.0f);
		
		visuals.calcMatricesForObject(op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayAbout);
		glDrawArrays(GL_TRIANGLES, 0, 6);				
	}
	
	private void drawOptions() {
		ObjectPosition op = new ObjectPosition();		
		op.setPosition(-0.4f, -0.7f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(Visuals.aspectRatio, Visuals.aspectRatio, 1.0f);
					
		visuals.calcMatricesForObject(op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayOptions);
		glDrawArrays(GL_TRIANGLES, 0, 6);				
	}
	
	private void drawPlay() {
		ObjectPosition op = new ObjectPosition();	
		op.setPosition(-0.55f, -0.21f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(Visuals.aspectRatio, Visuals.aspectRatio, 1.0f);

		visuals.calcMatricesForObject(op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayPlay);
		glDrawArrays(GL_TRIANGLES, 0, 6);		
	}
	
	private void drawTitle() {
		ObjectPosition op = new ObjectPosition();
		op.setPosition(0f, 0.8f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(Visuals.aspectRatio, Visuals.aspectRatio, 1.0f);
		
		visuals.calcMatricesForObject(op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayTitle);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	private void drawBg() {
		ObjectPosition op = new ObjectPosition();			
		op.setPosition(0f, 0f, 0f);
		op.setScale(1.0f, 1.0f, 1.0f);		
		op.setRot(0f, 0f, 0f);
		
		visuals.calcMatricesForObject(op);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);
        visuals.textureShader.bindData(vertexArrayBg);
		glDrawArrays(GL_TRIANGLES, 0, 6);			
	}

	@Override
	public void handleTouchPress(float normalizedX, float normalizedY) {
		super.handleTouchPress(normalizedX, normalizedY);
		
		ClassicSingleton singleton = ClassicSingleton.getInstance();
		singleton.showSceneLevel();		
	}

	@Override
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		super.handleTouchDrag(normalizedX, normalizedY);
	}

	@Override
	public void handleTouchRelease(float normalizedX, float normalizedY) {
		super.handleTouchRelease(normalizedX, normalizedY);
	}
}
