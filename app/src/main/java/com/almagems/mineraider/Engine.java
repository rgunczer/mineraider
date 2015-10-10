package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;

import android.content.Context;
import android.content.SharedPreferences;


public final class Engine {

    // android
    public static MineRaiderRenderer renderer;
    public static MineRaiderActivity activity;

    // basic
    public static Graphics graphics;
    public static Audio audio;
    public static Game game;


    // ctor
	private Engine() {
        //System.out.println("Engine ctor...");
    }

    public static void createGraphicsObject() {        
        //System.out.println("Engine createGraphicsObject...");
        graphics = new Graphics(activity);                
    }

    public static void initGraphicsObject() {
        //System.out.println("Engine initGraphicsObject...");
        graphics.initialSetup();
    }

    public static void createGameObject() {        
        //System.out.println("Engine createGameObject...");
        game = new Game();                    
    }

    public static void initGameObject() {
        Physics.reset();
        game.init();
    }

    public static void savePreferences() {
        //System.out.println("Save Preferences...");
        innerSavePreferences();
    }

    public static void loadPreferences() {
        //System.out.println("Load preferences...");
        innerLoadPreferences();
    }

    private static SecurePreferences CreateSecurePreferencesObj() {
        String name = "raiders0";
        String key = "jKDz2fJhKE33cBQlDAkRM9axAnrBmUxs9dp11AqoogfulCL2DvNsexBuL2qeEstAIc27bqiMZbOOxZbZ";
        key = key.substring(10, 20) + key.substring(21, 26);

        return new SecurePreferences(renderer.context, name, key, true);
    }

    private static void innerSavePreferences() {
        if (game == null) {
            return;
        }

        if (game.scoreCounter == null) {
            return;
        }

        SecurePreferences preferences = CreateSecurePreferencesObj();

        // Put (all puts are automatically committed)
        //preferences.put("userId", "User1234");

        preferences.put("SCORE", "" + game.scoreCounter.score);

        // save score by gem types
        ScoreByGemType scoreObj;
        int len = game.scoreCounter.scoreByGemTypes.size();
        for (int i = 0; i < len; ++i) {
            scoreObj = game.scoreCounter.getScoreObjForGemType(i);
            preferences.put("GEM" + scoreObj.type, "" + scoreObj.value);
        }

        // save match types
        preferences.put("MATCH3H", "" + game.scoreCounter.match3CountHorizontal);
        preferences.put("MATCH3V", "" + game.scoreCounter.match3CountVertical);

        preferences.put("MATCH4H", "" + game.scoreCounter.match4CountHorizontal);
        preferences.put("MATCH4V", "" + game.scoreCounter.match4CountVertical);

        preferences.put("MATCH5H", "" + game.scoreCounter.match5CountHorizontal);
        preferences.put("MATCH5V", "" + game.scoreCounter.match5CountVertical);

        // extras
        preferences.put("HINTS", "" + game.scoreCounter.hintCounter);
        preferences.put("SWAPS", "" + game.scoreCounter.perfectSwapCounter);
        preferences.put("COMBOS", "" + game.scoreCounter.highestComboCounter);
        preferences.put("MATCHES", "" + game.scoreCounter.sharedMatchesCounter);

        // collected vs wasted
        preferences.put("COLLECTED", "" + game.scoreCounter.collectedGems);
        preferences.put("WASTED", "" + game.scoreCounter.wastedGems);

        // save music and sound volume
        preferences.put("MUSIC", "" + audio.musicVolume);
        preferences.put("SOUND", "" + audio.soundVolume);
    }

    private static int GetPref(SecurePreferences preferences, String key, int defaultValue) {
        String valueString = preferences.getString(key);
        if (valueString != null) {
            return Integer.parseInt(valueString);
        }
        return defaultValue;
    }

    private static float GetPref(SecurePreferences preferences, String key, float defaultValue) {
        String valueString = preferences.getString(key);
        if (valueString != null) {
            return Float.parseFloat(valueString);
        }
        return defaultValue;
    }

    private static void innerLoadPreferences() {
        SecurePreferences preferences = CreateSecurePreferencesObj();

        // score
        game.scoreCounter.score = GetPref(preferences, "SCORE", 0);

        // gem types
        int[] arr = new int[MAX_GEM_TYPES];
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            arr[i] = GetPref(preferences, "GEM" + i, 0);
        }
        game.scoreCounter.setScoreByGemTypes(arr);

        // match types
        game.scoreCounter.match3CountHorizontal = GetPref(preferences, "MATCH3H", 0);
        game.scoreCounter.match3CountVertical =  GetPref(preferences, "MATCH3V", 0);

        game.scoreCounter.match4CountHorizontal = GetPref(preferences, "MATCH4H", 0);
        game.scoreCounter.match4CountVertical = GetPref(preferences, "MATCH4V", 0);

        game.scoreCounter.match5CountHorizontal = GetPref(preferences, "MATCH5H", 0);
        game.scoreCounter.match5CountVertical = GetPref(preferences, "MATCH5V", 0);

        // extras
        game.scoreCounter.hintCounter = GetPref(preferences, "HINTS", 0);
        game.scoreCounter.perfectSwapCounter = GetPref(preferences, "SWAPS", 0);
        game.scoreCounter.highestComboCounter = GetPref(preferences, "COMBOS", 0);
        game.scoreCounter.sharedMatchesCounter = GetPref(preferences, "MATCHES", 0);

        // collected vs wasted
        game.scoreCounter.collectedGems = GetPref(preferences, "COLLECTED", 0);
        game.scoreCounter.wastedGems = GetPref(preferences, "WASTED", 0);

        // audio
        audio.musicVolume = GetPref(preferences, "MUSIC", 0.5f);
        audio.soundVolume = GetPref(preferences, "SOUND", 0.5f);

        game.scoreCounter.dump();
    }

    private static void innerSavePreferencesOld() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // save score
        editor.putInt("SCORE", game.scoreCounter.score);

        // save score by gem types
        ScoreByGemType scoreObj;
        int len = game.scoreCounter.scoreByGemTypes.size();
        for(int i = 0; i < len; ++i) {
            scoreObj = game.scoreCounter.getScoreObjForGemType(i);
            editor.putInt("GEM" + scoreObj.type, scoreObj.value);
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

    private static void innerLoadPreferencesOld() {
        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);

        // score
        game.scoreCounter.score = sharedPrefs.getInt("SCORE", 0);

        // gem types
        int[] arr = new int[MAX_GEM_TYPES];
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            arr[i] = sharedPrefs.getInt("GEM" + i, 0);
        }
        game.scoreCounter.setScoreByGemTypes(arr);

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

        game.scoreCounter.dump();
    }

    public static void pause() {
        if (audio != null) {
            audio.pause();
        }
    }

    public static void resume() {
        if (audio != null) {
            audio.resume();
        }
    }

    public static void releaseAudio() {
        if (audio != null) {
            audio.release();
        }
    }

    public static void createAudio() {
        if (audio != null) {
            audio.create();
        }
    }

    public static void update() {
        game.update();
    }

    public static void draw() {
        graphics.prepareFrame();
        game.draw();
    }

    public static void onSurfaceChanged(int width, int height) {
        graphics.onSurfaceChanged(width, height);
        game.onSurfaceChanged(width, height);
    }

    public static void showInterstitialAd() {
        activity.requestNewInterstitial();
    }

    // input
    public static void handleTouchPress(float normalizedX, float normalizedY) {
        game.handleTouchPress(normalizedX, normalizedY);
    }

    public static void handleTouchDrag(float normalizedX, float normalizedY) {
        game.handleTouchDrag(normalizedX, normalizedY);
    }

    public static void handleTouchRelease(float normalizedX, float normalizedY) {
        game.handleTouchRelease(normalizedX, normalizedY);
    }

}
