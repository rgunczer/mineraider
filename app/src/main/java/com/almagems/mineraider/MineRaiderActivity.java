package com.almagems.mineraider;

import android.app.Activity;
import android.graphics.*;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;




public final class MineRaiderActivity extends Activity {
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
    private MineRaiderRenderer renderer;

    @Override
    public void onBackPressed() {
        //System.out.println("Back button pressed!");
        glSurfaceView.queueEvent(new Runnable() {

            @Override
            public void run() {
                renderer.handleBackButtonPress();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU ) {
            glSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    renderer.handleMenuButtonPress();
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		glSurfaceView = new GLSurfaceView(this);
		glSurfaceView.getHolder().setFormat(PixelFormat.RGB_565);
		//glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
		setContentView(glSurfaceView);
		//setContentView(R.layout.activity_hello_world);
		//glSurfaceView = (GLSurfaceView)findViewById(R.id.glSurfaceView);
		glSurfaceView.setPreserveEGLContextOnPause(true);
				
		//final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		//final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		renderer = new MineRaiderRenderer(this);
		
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(renderer);
		rendererSet = true;

		glSurfaceView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    //System.out.println("Touch Event: " + event.getX() + ", " + event.getY() );
                    // convert touch coordinates into normalized device
                    // coordinates, keeping in mind that Android's Y coordinates are inverted
                    final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
                    final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchPress(normalizedX, normalizedY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchRelease(normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (rendererSet) {
            glSurfaceView.onPause();
        }

        Engine.savePreferences();
        Engine.releaseAudio();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (rendererSet) {
			glSurfaceView.onResume();
		}

        Engine.createAudio();
	}

}
