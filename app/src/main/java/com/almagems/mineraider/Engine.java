package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;

import android.content.Context;
import android.content.SharedPreferences;


public final class Engine {

    private static Engine instance;
    public static float adHeight;

    // android
    public static MineRaiderRenderer renderer;
    public static MineRaiderActivity activity;

    //
    public static Graphics graphics;
    public static Audio audio;
    public static Game game;


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
        game.init();
    }

    public void savePreferences() {
        System.out.println("Save Preferences...");

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // save score
        int score = game.scoreCounter.getScore();
        editor.putInt("SCORE", score);

        // save score by gem types
        ScoreByGemType scoreByGemType;
        int len = game.scoreCounter.scoreByGemTypes.size();
        for(int i = 0; i < len; ++i) {
            scoreByGemType = game.scoreCounter.getScoreByGemType(i);
            editor.putInt("GEM" + scoreByGemType.type, scoreByGemType.value);
        }

        // save match types
        editor.putInt("MATCH3H", game.scoreCounter.match3CountHorizontal);
        editor.putInt("MATCH3V", game.scoreCounter.match3CountVertical);

        editor.putInt("MATCH4H", game.scoreCounter.match4CountHorizontal);
        editor.putInt("MATCH4V", game.scoreCounter.match4CountVertical);

        editor.putInt("MATCH5H", game.scoreCounter.match5CountHorizontal);
        editor.putInt("MATCH5V", game.scoreCounter.match5CountVertical);

        // extras
        editor.putInt("HINTS", game.scoreCounter.hintCounter);
        editor.putInt("SWAPS", game.scoreCounter.perfectSwapCounter);
        editor.putInt("COMBOS", game.scoreCounter.highestComboCounter);
        editor.putInt("MATCHES", game.scoreCounter.sharedMatchesCounter);

        // collected vs wasted
        editor.putInt("COLLECTED", game.scoreCounter.collectedGems);
        editor.putInt("WASTED", game.scoreCounter.wastedGems);

        // save music and sound volume
        editor.putFloat("MUSIC", audio.musicVolume);
        editor.putFloat("SOUND", audio.soundVolume);

        editor.commit();
    }

    public int loadPreferences() {
        System.out.println("Load preferences...");
        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);

        // score
        int score = sharedPrefs.getInt("SCORE", 0);
        game.scoreCounter.setScore(score);
        game.scoreCounter.dumpScore();

        // gem types
        int[] arr = new int[MAX_GEM_TYPES];
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            arr[i] = sharedPrefs.getInt("GEM" + i, 0);
        }
        game.scoreCounter.setScoreByGemTypes(arr);
        game.scoreCounter.dumpScoreByGemTypes();

        // match types
        game.scoreCounter.match3CountHorizontal = sharedPrefs.getInt("MATCH3H", 0);
        game.scoreCounter.match3CountVertical =  sharedPrefs.getInt("MATCH3V", 0);

        game.scoreCounter.match4CountHorizontal = sharedPrefs.getInt("MATCH4H", 0);
        game.scoreCounter.match4CountVertical = sharedPrefs.getInt("MATCH4V", 0);

        game.scoreCounter.match5CountHorizontal = sharedPrefs.getInt("MATCH5H", 0);
        game.scoreCounter.match5CountVertical = sharedPrefs.getInt("MATCH5V", 0);

        // extras
        game.scoreCounter.hintCounter = sharedPrefs.getInt("HINTS", 0);
        game.scoreCounter.perfectSwapCounter = sharedPrefs.getInt("SWAPS", 0);
        game.scoreCounter.highestComboCounter = sharedPrefs.getInt("COMBOS", 0);
        game.scoreCounter.sharedMatchesCounter = sharedPrefs.getInt("MATCHES", 0);

        // collected vs wasted
        game.scoreCounter.collectedGems = sharedPrefs.getInt("COLLECTED", 0);
        game.scoreCounter.wastedGems = sharedPrefs.getInt("WASTED", 0);

        // audio
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
