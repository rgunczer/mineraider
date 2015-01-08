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
import com.almagems.mineraider.Constants;
import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.shaders.TextureShader;
import com.almagems.mineraider.util.Vector;

public class Menu extends Scene {
		
	private VertexArray vertexArrayBg;
	private VertexArray vertexArrayTitle;
	private VertexArray vertexArrayPlay;
	private VertexArray vertexArrayOptions;
	private VertexArray vertexArrayAbout;
	
	private float aspect;
	
	public Menu() {
		
	}

	@Override
	public void surfaceChanged(int width, int height) {
		// TODO Auto-generated method stub
		final float tw = 512f;
		final float th = 1024f;
		
		visuals.setProjectionMatrix2D(width, height);
		
		aspect = width > height ? (float)width / (float)height : (float)height / (float)width;
				
		float[] vertexDataBg = { 
				// x, y, z, 			s, t,			
				-1.0f, -aspect, 0.0f, 	0.0f, 0.0f,
				 1.0f, -aspect, 0.0f,	1.0f, 0.0f,
				 1.0f,  aspect, 0.0f,	1.0f, 1.0f,
				 
				-1.0f, -aspect, 0.0f,	0.0f, 0.0f,
			 	 1.0f,  aspect, 0.0f,	1.0f, 1.0f,
				-1.0f,  aspect, 0.0f,	0.0f, 1.0f
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
				// x, y, z, 	s, t,			
				-x, -y, 0.0f, 	tx0, ty0,
				 x, -y, 0.0f,	tx1, ty0,
				 x,  y, 0.0f,	tx1, ty1,
				 
				-x, -y, 0.0f,	tx0, ty0,
			 	 x,  y, 0.0f,	tx1, ty1,
				-x,  y, 0.0f,	tx0, ty1				
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
				// x, y, z, 	s, t,			
				-x, -y, 0.0f, 	tx0, ty0,
				 x, -y, 0.0f,	tx1, ty0,
				 x,  y, 0.0f,	tx1, ty1,
				 
				-x, -y, 0.0f,	tx0, ty0,
			 	 x,  y, 0.0f,	tx1, ty1,
				-x,  y, 0.0f,	tx0, ty1				
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
				// x, y, z, 	s, t,			
				-x, -y, 0.0f, 	tx0, ty0,
				 x, -y, 0.0f,	tx1, ty0,
				 x,  y, 0.0f,	tx1, ty1,
				 
				-x, -y, 0.0f,	tx0, ty0,
			 	 x,  y, 0.0f,	tx1, ty1,
				-x,  y, 0.0f,	tx0, ty1				
		};
		
		vertexArrayOptions = new VertexArray(vertexDataOptions);

		
		
//	"filename": "menu_play.png",
//	"frame": {"x":2,"y":542,"w":214,"h":121},
		tx0 = 2f / tw;
		tx1 = (2f + 214f) / tw;
		ty0 = ((th - 542f) - 121f ) / th; // 0f
		ty1 = ((th - 542f) ) / th; 	// 1f
		
		x = (214f / width) * scale;
		y = (121f / width) * scale;
		float[] vertexDataPlay = {
				// x, y, z, 	s, t,			
				-x, -y, 0.0f, 	tx0, ty0,
				 x, -y, 0.0f,	tx1, ty0,
				 x,  y, 0.0f,	tx1, ty1,
				 
				-x, -y, 0.0f,	tx0, ty0,
			 	 x,  y, 0.0f,	tx1, ty1,
				-x,  y, 0.0f,	tx0, ty1				
		};
		
		vertexArrayPlay = new VertexArray(vertexDataPlay);			
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		glDisable(GL_DEPTH_TEST);
		
		drawBg();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		
		drawTitle();
		drawPlay();
		drawOptions();
		drawAbout();
	}
	
	private void drawAbout() {
		ObjectPosition op = new ObjectPosition();
		op.setPosition(-0.5f, -1.2f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(aspect, aspect, 1.0f);				
		
		visuals.calcMatricesForObject(op);		
		visuals.textureShader.useProgram();
		visuals.textureShader.setTexture(visuals.textureMenuItems);
		visuals.textureShader.setUniforms(visuals.modelProjMatrix);
		
		bindData(visuals.textureShader, vertexArrayAbout);
		glDrawArrays(GL_TRIANGLES, 0, 6);				
	}
	
	private void drawOptions() {
		ObjectPosition op = new ObjectPosition();		
		op.setPosition(-0.4f, -0.7f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(aspect, aspect, 1.0f);
					
		visuals.calcMatricesForObject(op);		
		visuals.textureShader.useProgram();
		visuals.textureShader.setTexture(visuals.textureMenuItems);
		visuals.textureShader.setUniforms(visuals.modelProjMatrix);
		
		bindData(visuals.textureShader, vertexArrayOptions);
		glDrawArrays(GL_TRIANGLES, 0, 6);				
	}
	
	private void drawPlay() {
		ObjectPosition op = new ObjectPosition();	
		op.setPosition(-0.55f, -0.2f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(aspect, aspect, 1.0f);

		visuals.calcMatricesForObject(op);		
		visuals.textureShader.useProgram();
		visuals.textureShader.setTexture(visuals.textureMenuItems);
		visuals.textureShader.setUniforms(visuals.modelProjMatrix);
		
		bindData(visuals.textureShader, vertexArrayPlay);
		glDrawArrays(GL_TRIANGLES, 0, 6);		
	}
	
	private void drawTitle() {
		ObjectPosition op = new ObjectPosition();
		op.setPosition(0f, 0.8f, 0f);
		op.setRot(0f, 0f, 0f);
		op.setScale(aspect, aspect, 1.0f);
		
		visuals.calcMatricesForObject(op);		
		visuals.textureShader.useProgram();
		visuals.textureShader.setTexture(visuals.textureMenuItems);
		visuals.textureShader.setUniforms(visuals.modelProjMatrix);
		
		bindData(visuals.textureShader, vertexArrayTitle);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	private void drawBg() {
		ObjectPosition op = new ObjectPosition();			
		op.setPosition(0f, 0f, 0f);
		op.setScale(1.0f, 1.0f, 1.0f);		
		op.setRot(0f, 0f, 0f);
		
		visuals.calcMatricesForObject(op);		
		visuals.textureShader.useProgram();
		visuals.textureShader.setTexture(visuals.textureMenu);
		visuals.textureShader.setUniforms(visuals.projectionMatrix);
		
		bindData(visuals.textureShader, vertexArrayBg);
		glDrawArrays(GL_TRIANGLES, 0, 6);			
	}

	@Override
	public void handleTouchPress(float normalizedX, float normalizedY) {
		// TODO Auto-generated method stub
		super.handleTouchPress(normalizedX, normalizedY);
		
		ClassicSingleton singleton = ClassicSingleton.getInstance();
		singleton.showSceneLevel();		
	}

	@Override
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		// TODO Auto-generated method stub
		super.handleTouchDrag(normalizedX, normalizedY);
	}

	@Override
	public void handleTouchRelease(float normalizedX, float normalizedY) {
		// TODO Auto-generated method stub
		super.handleTouchRelease(normalizedX, normalizedY);
	}
		
	public void bindData(TextureShader textureProgram, VertexArray vertexArray) {		
		final int POSITION_COMPONENT_COUNT = 3;
		final int NORMAL_COMPONENT_COUNT = 0;
		final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;				
		final int STRIDE = (POSITION_COMPONENT_COUNT + 
							NORMAL_COMPONENT_COUNT + 
							TEXTURE_COORDINATES_COMPONENT_COUNT ) * Constants.BYTES_PER_FLOAT;		
		
		vertexArray.setVertexAttribPointer(
				0, 
				textureProgram.getPositionAttributeLocation(), 
				POSITION_COMPONENT_COUNT, 
				STRIDE);
		
		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT, 
				textureProgram.getTextureAttributeLocation(), 
				TEXTURE_COORDINATES_COMPONENT_COUNT, 
				STRIDE);				
	}
	
	
}
