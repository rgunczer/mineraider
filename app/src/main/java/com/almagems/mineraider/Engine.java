package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;

import android.content.Context;
import android.content.SharedPreferences;


public class Engine {

    private static Engine instance;
    public static float adHeight;

    // android
    public MineRaiderRenderer renderer;
    public MineRaiderActivity activity;

    //
    public Graphics graphics;
    public Audio audio;
    public Game game;


    // ctor
	private Engine() {
        System.out.println("Engine ctor...");
    }

	public static Engine getInstance() {
		if (instance == null) {
			instance = new Engine();
		}
		return instance;
	}

    public void createGraphicsObject() {
        System.out.println("Engine createGraphicsObject...");
        graphics = new Graphics(activity);
    }

    public void initGraphicsObject() {
        System.out.println("Engine initGraphicsObject...");
        graphics.initialSetup();
    }

    public void createGameObject() {
        System.out.println("Engine createGameObject...");
        game = new Game();
    }

    public void initGameObject() {
        game.init(this);
    }

    public void savePreferences() {
        System.out.println("Save Preferences...");

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // save score
        int score = game.scoreCounter.getScore();
        editor.putInt("SCORE", score);

        // save score by gem types
        int[] arr = game.scoreCounter.scoreByGemTypes;
        for(int i = 0; i < arr.length; ++i) {
            editor.putInt("GEM" + i, arr[i]);
        }

        // save music and sound volume
        editor.putFloat("MUSIC", audio.musicVolume);
        editor.putFloat("SOUND", audio.soundVolume);

        editor.commit();
    }

    public int loadPreferences() {
        System.out.println("Load preferences...");

        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);

        // load score
        int score = sharedPrefs.getInt("SCORE", 0);
        game.scoreCounter.setScore(score);
        game.scoreCounter.dumpScore();

        // load score by gem types
        int[] arr = new int[MAX_GEM_TYPES];
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            arr[i] = sharedPrefs.getInt("GEM" + i, 0);
        }
        game.scoreCounter.setScoreByGemTypes(arr);
        game.scoreCounter.dumpScoreByGemTypes();

        audio.musicVolume = sharedPrefs.getFloat("MUSIC", 0.5f);
        audio.soundVolume = sharedPrefs.getFloat("SOUND", 0.5f);

        return score;
    }

    public void pauseAudio() {
        if (audio != null) {
            audio.pause();
        }
    }

    public void resumeAudio() {
        if (audio != null) {
            audio.resume();
        }
    }

    public void update() {
        game.update();
    }
    public void draw() {
        graphics.prepareFrame();
        game.draw();
    }

    public void onSurfaceChanged(int width, int height) {
        graphics.onSurfaceChanged(width, height);
        game.onSurfaceChanged(width, height);
    }

    // input
    public void handleTouchPress(float normalizedX, float normalizedY) {
        game.handleTouchPress(normalizedX, normalizedY);
    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {
        game.handleTouchDrag(normalizedX, normalizedY);
    }

    public void handleTouchRelease(float normalizedX, float normalizedY) {
        game.handleTouchRelease(normalizedX, normalizedY);
    }

}
