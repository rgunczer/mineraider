package com.almagems.mineraider;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.widget.Toast;


public final class MineRaiderRenderer implements Renderer {

	private long frameStartTimeMS;
	private final Context context;
    private Engine engine;

	// ctor
	public MineRaiderRenderer(Context context) {
		this.context = context;
		engine = Engine.getInstance();

		engine.activity = (MineRaiderActivity)context;
		engine.renderer = this;
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

        engine.createGraphicsObject();
        engine.initGraphicsObject();

		try {
            engine.graphics.loadStartupAssets();
		} catch (final Exception ex) {
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, "Error loading assets. " + ex.toString() , Toast.LENGTH_LONG).show();
				}
			});
			return;
		}

        engine.createGameObject();
        engine.initGameObject();
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		engine.onSurfaceChanged(width, height);
    }

	@Override
	public void onDrawFrame(GL10 glUnused) {
		engine.update();
		engine.draw();
		limitFrameRate(30);		
	}
			
	public void handleTouchPress(float normalizedX, float normalizedY) {
		//System.out.println("TouchPress: " + normalizedX + " " + normalizedY);
		engine.handleTouchPress(normalizedX, normalizedY);
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		//System.out.println("TouchDrag:" + normalizedX + " " + normalizedY);
		engine.handleTouchDrag(normalizedX, normalizedY);
	}
	
	public void handleTouchRelease(float normalizedX, float normalizedY) {
		//System.out.println("TouchRelease:" + normalizedX + " " + normalizedY);
		engine.handleTouchRelease(normalizedX, normalizedY);
	}	
}