package com.almagems.mineraider;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;

import static android.opengl.GLES20.GL_LESS;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.widget.Toast;

import com.almagems.mineraider.scenes.Level;
import com.almagems.mineraider.scenes.Menu;
import com.almagems.mineraider.scenes.PlayerSelect;
import com.almagems.mineraider.scenes.Scene;
import com.almagems.mineraider.shaders.ParticleShader;


public class MineRaiderRenderer implements Renderer { 

	private long frameStartTimeMS;

    private Scene playerSelect;
	private Scene menu;
	private Scene level;
	private Scene current;
	
	private final Context context;	
	private Visuals visuals;			
			
	// ctor
	public MineRaiderRenderer(Context context) {
		this.context = context;
		ClassicSingleton instance = ClassicSingleton.getInstance();
		instance.renderer = this;
	}
	
	public void showSceneLevel() {
		visuals.setProjectionMatrix3D();
		level.surfaceChanged((int)Visuals.screenWidth, (int)Visuals.screenHeight);
		current = level;
	}
	
	private void limitFrameRate(int framesPerSecond) {
		long elapsedFrameTimeMS = SystemClock.elapsedRealtime() - frameStartTimeMS;
		long expectedFrameTimeMS = 1000 / framesPerSecond;
		long timeToSleepMS = expectedFrameTimeMS - elapsedFrameTimeMS;
		
		if (timeToSleepMS > 0) {
			SystemClock.sleep(timeToSleepMS);
		}
		frameStartTimeMS = SystemClock.elapsedRealtime();
	}	
	
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		limitFrameRate(30);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
		//glClearColor(0.1f, 0.1f, 0.1f, 0.0f);		
		
		glEnable(GL_CULL_FACE);	
		glDepthFunc(GL_LESS);
		//glDepthMask(true);
					
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		//glBlendFunc(GL_ONE_MINUS_DST_ALPHA,GL_DST_ALPHA);
		//glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		glBlendFunc(GL_ONE, GL_ONE);
				
		visuals = Visuals.getInstance();
		visuals.init(context);

		try {		
			visuals.loadAssets();
		} catch (final Exception ex) {

			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, "Error loading assets! " + ex.toString() , Toast.LENGTH_LONG).show();
				}
			});
			return;
		}


					
		if (level == null) {
			level = new Level();
		}
					
		if (menu == null) {
			menu = new Menu();
		}

        if (playerSelect == null) {
            playerSelect = new PlayerSelect();
        }


		//current = level;
		current = menu;
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		glViewport(0, 0, width, height);

        Visuals.screenWidth = width;
        Visuals.screenHeight = height;
        Visuals.aspectRatio = width > height ? (float)width / (float)height : (float)height / (float)width;
        Visuals.scaleFactor = Visuals.screenWidth / Visuals.referenceScreenWidth;

        ParticleShader.pointSize = width * 0.1f;

        current.surfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		
		current.update();
		current.draw();
		
		limitFrameRate(30);		
	}
			
	public void handleTouchPress(float normalizedX, float normalizedY) {
		//System.out.println("TouchPress: " + normalizedX + " " + normalizedY);
		current.handleTouchPress(normalizedX, normalizedY);
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		//System.out.println("TouchDrag:" + normalizedX + " " + normalizedY);
		current.handleTouchDrag(normalizedX, normalizedY);
	}
	
	public void handleTouchRelease(float normalizedX, float normalizedY) {
		//System.out.println("TouchRelease:" + normalizedX + " " + normalizedY);
		current.handleTouchRelease(normalizedX, normalizedY);
	}	
}