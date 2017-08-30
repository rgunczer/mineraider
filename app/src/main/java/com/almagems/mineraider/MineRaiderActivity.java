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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public final class MineRaiderActivity extends Activity {
    private InterstitialAd interstitialAd;
	private AdView adView;
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

        //initAds();
	}

    private AdRequest createAdRequest() {
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5.md5(android_id).toUpperCase();
        //System.out.println(deviceId);

        AdRequest adRequest = new AdRequest.Builder()
                // to show real ads comment out the next few lines
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("F689981D411369408E68481517CDBDF7")
//                .addTestDevice(deviceId)
//                .addTestDevice("6503CEAA4CF8DC33F5F88E5984BA09C7") // LG LG Optimus L3 || - 4.1.1 API 16 - 240x320
//                .addTestDevice("5FD98F5344B72D203C178E0B2095F330") // Google Nexus 6 - 5.0.0 API 21 1440x2560
//                .addTestDevice("D4029AFBC2B78394E46D8777362D7881") // Google Nexus S - 4.1.1 - API 16 - 480x800
//                .addTestDevice("F3390873D15CC25BE479A1667DC09EB3") // Google Nexus 7
//                .addTestDevice("8A424B010E21ED3F9B72CF51A03E6D00") // Note 3 emulator genymotion
                .build();

        return adRequest;
    }
	
	private void initAds() {
        //AdView adView = (AdView)findViewById(R.id.adView);
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-1002179312870743/4200681514");
		adView.setBackgroundColor(Color.BLACK); // TRANSPARENT);
		RelativeLayout rl = new RelativeLayout(this);

		//LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT|Gravity.TOP);
		this.addContentView(rl, params);
		
		rl.addView(adView);
		rl.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		rl.bringToFront();

//        adView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int height = adView.getHeight();
//                if (height > 0) {
//                    // now the height is gotten, you can do things you want
//                    System.out.println("Here the ad size is: " + height);
//                }
//            }
//        });

		adView.loadAd( createAdRequest() );

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1002179312870743/2692696712");

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                innerRequestNewInterstitial();
            }
        });

        innerRequestNewInterstitial();
	}

    public void requestNewInterstitial() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null && interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }

    private void innerRequestNewInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.loadAd(createAdRequest());
        }
    }

	@Override
	protected void onPause() {
		super.onPause();

		if (adView != null) {
			adView.pause();
		}

		if (rendererSet) {
            glSurfaceView.onPause();
        }

        Engine.savePreferences();
        Engine.releaseAudio();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (adView != null) {
			adView.resume();
		}

		if (rendererSet) {
			glSurfaceView.onResume();
		}

        Engine.createAudio();
	}

}
