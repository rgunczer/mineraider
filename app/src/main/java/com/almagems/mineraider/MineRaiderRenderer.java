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

	// ctor
	public MineRaiderRenderer(Context context) {
		this.context = context;

		Engine.activity = (MineRaiderActivity)context;
		Engine.renderer = this;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        System.out.println("onSurfaceCreated...");
        frameStartTimeMS = SystemClock.elapsedRealtime();

        Engine.createGraphicsObject();
        Engine.initGraphicsObject();

        try {
            Engine.graphics.loadStartupAssets();
        } catch (final Exception ex) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Error loading assets. " + ex.toString(), Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        Engine.createGameObject();
        Engine.initGameObject();
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		Engine.onSurfaceChanged(width, height);
    }

	@Override
	public void onDrawFrame(GL10 glUnused) {
		Engine.update();
		Engine.draw();

        // limit frame rate
        final int framesPerSecond = 30;
        long elapsedFrameTimeMS = SystemClock.elapsedRealtime() - frameStartTimeMS;
        long expectedFrameTimeMS = 1000 / framesPerSecond;
        long timeToSleepMS = expectedFrameTimeMS - elapsedFrameTimeMS;

        if (timeToSleepMS > 0) {
            SystemClock.sleep(timeToSleepMS);
        }
        frameStartTimeMS = SystemClock.elapsedRealtime();
    }
			
	public void handleTouchPress(float normalizedX, float normalizedY) {
		Engine.handleTouchPress(normalizedX, normalizedY);
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		Engine.handleTouchDrag(normalizedX, normalizedY);
	}
	
	public void handleTouchRelease(float normalizedX, float normalizedY) {
		Engine.handleTouchRelease(normalizedX, normalizedY);
	}	
}