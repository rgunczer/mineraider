package com.almagems.mineraider;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


import static com.almagems.mineraider.Constants.*;


public class MineRaiderActivity extends Activity {
	private AdView adView;
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		glSurfaceView = new GLSurfaceView(this);
		setContentView(glSurfaceView);
		//setContentView(R.layout.activity_hello_world);
		//glSurfaceView = (GLSurfaceView)findViewById(R.id.glSurfaceView);
		glSurfaceView.setPreserveEGLContextOnPause(true);
				
		final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);		
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();		
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;		
		final MineRaiderRenderer renderer = new MineRaiderRenderer(this);
		
		if (supportsEs2) {
			glSurfaceView.setEGLContextClientVersion(2);
			glSurfaceView.setRenderer(renderer);
			rendererSet = true;
		} else {
			Toast.makeText(this, "Unable to continue. This device does not support OpenGL ES 2.0", Toast.LENGTH_LONG).show();
			return;
		}

		glSurfaceView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event != null) {
                    System.out.println("Touch Event: " + event.getX() + ", " + event.getY() );

					// convert touch coordinates into normalized device
					// coordinates, keeping in mind that Android's Y coordinates are inverted
					final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
					final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						System.out.println("Action Down... " + normalizedX + ", " + normalizedY);
						glSurfaceView.queueEvent(new Runnable() {
							
							@Override
							public void run() {
								renderer.handleTouchPress(normalizedX, normalizedY);								
							}
						});
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						//System.out.println("Action Move...");
						glSurfaceView.queueEvent(new Runnable() {
							
							@Override
							public void run() {
								renderer.handleTouchDrag(normalizedX, normalizedY);								
							}
						});
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						//System.out.println("Action Up...");						
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

        //initAds();
	}
	
	private void initAds() {
		// get deviceId
		String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5.md5(android_id).toUpperCase();

//        if (deviceId.equals("5FD98F5344B72D203C178E0B2095F330")) {
//            System.out.print("They are equal....");
//        }


        //System.out.println(deviceId);
        //5FD98F5344B72D203C178E0B2095F330
        // note3 device id: 8EEDF3F54B832F9C4EBFB9B05391CF11
        		
		//AdView adView = (AdView)findViewById(R.id.adView);
		
		AdRequest adRequest = new AdRequest.Builder()
		// to show real ads comment out the next two lines!
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		//.addTestDevice("F689981D411369408E68481517CDBDF7")
		.addTestDevice(deviceId)
        .addTestDevice("6503CEAA4CF8DC33F5F88E5984BA09C7") // LG LG Optimus L3 || - 4.1.1 API 16 - 240x320
        .addTestDevice("5FD98F5344B72D203C178E0B2095F330") // Google Nexus 6 - 5.0.0 API 21 1440x2560
        .addTestDevice("D4029AFBC2B78394E46D8777362D7881") // Google Nexus S - 4.1.1 - API 16 - 480x800
		.build();
		
		//adView.loadAd(adRequest);
					
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-1002179312870743/4200681514");
		adView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
		RelativeLayout rl = new RelativeLayout(this);

		//LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		final FrameLayout.LayoutParams params =
		        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
		                FrameLayout.LayoutParams.WRAP_CONTENT,
		                                     Gravity.RIGHT|Gravity.TOP); 
		this.addContentView(rl, params);
		
		rl.addView(adView);
		rl.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		rl.bringToFront();

        adView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = adView.getHeight();
                if (height > 0) {
                    // now the height is gotten, you can do things you want
                    System.out.println("Here the ad size is: " + height);
                    ClassicSingleton.adHeight = height;
                }
            }
        });

		adView.loadAd(adRequest);
	}
	
	@Override
	protected void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();		
		if (rendererSet) {
            glSurfaceView.onPause();
        }

        ClassicSingleton.getInstance().savePreferences();
	}
	
	@Override
	protected void onResume() {
		if (adView != null) {
			adView.resume();
		}
		super.onResume();		
		if (rendererSet) {
			glSurfaceView.onResume();
		}
	}


}
