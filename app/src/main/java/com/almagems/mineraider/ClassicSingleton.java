package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;
import com.almagems.mineraider.objects.MineCart;
import com.almagems.mineraider.scenes.Level;
import com.almagems.mineraider.scenes.Scene;

import android.content.Context;
import android.content.SharedPreferences;

public class ClassicSingleton {
    private static ClassicSingleton instance = null;
    public static float adHeight;
	public MineRaiderRenderer renderer = null;
    public MineRaiderActivity activity = null;
	public MineCart cart1 = null;
	public MineCart cart2 = null;
    public ScoreCounter scoreCounter = null;
    public HUD hud;
    public BatchDrawer batchDrawer = null;

    // scenes
    public Scene loading = null;
    public Scene level = null;

    public Scene currentScene = null;
    public Scene previousScene = null;

    public int levelNumber = 0;

	protected ClassicSingleton() {
        scoreCounter = new ScoreCounter();
        hud = new HUD();
        batchDrawer = new BatchDrawer();
	}
	
	public static ClassicSingleton getInstance() {
		if (instance == null) {
			instance = new ClassicSingleton(); 
		}
		return instance;
	}

    public void setCurrentScene(Scene scene) {
        previousScene = currentScene;
        currentScene = scene;

        currentScene.prepare();
    }

    public void showScene() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // ???
        setCurrentScene(level);
    }

    public void createScenes() {
        if (level == null) {
            level = new Level();
        }
    }

    public void sendComboNotification() {
        hud.showCombo();
    }

    public void sendPerfectSwapNotification() { hud.showPerfectSwap(); }

    public int getScore() {
        return scoreCounter.getScore();
    }
    public void setScore(int score) {
        scoreCounter.setScore(score);
    }

    public int[] getScoreByGemTypes() { return scoreCounter.scoreByGemTypes; }

    public void setScoreByGemTypes(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            scoreCounter.scoreByGemTypes[i] = arr[i];
        }
    }

    public void handleGemsFromCart(int[] gemTypesArray) {
        scoreCounter.handleGemsFromCart(gemTypesArray);
    }

    public void sendGemsFromCartNotification(int numberOfGems) {
        hud.showBonusCartGems(numberOfGems);
    }

//	public void spawnParticleEmitterAt(float x, float y, int gemType) {
//		//System.out.println("Spawn particle emitter at: " + x + ", " + y);
//		particleManager.addParticleEmitterAt(x, y, gemType);
//	}

    public void savePreferences() {
        System.out.println("Save Preferences...");

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // save score
        editor.putInt("SCORE", getScore());

        // save score by gem types
        int[] arr = getScoreByGemTypes();
        for(int i = 0; i < arr.length; ++i) {
            editor.putInt("GEM" + i, arr[i]);
        }

        editor.commit();
    }

    public int loadPreferences(int helmetIndex) {
        System.out.println("Load Preferences... helmetIndex: " + helmetIndex);

        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);

        // load score
        int score = sharedPrefs.getInt("SCORE", 0);
        setScore(score);
        scoreCounter.dumpScore();

        // load score by gem types
        int[] arr = new int[MAX_GEM_TYPES];
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            arr[i] = sharedPrefs.getInt("GEM" + i, 0);
        }
        setScoreByGemTypes(arr);
        scoreCounter.dumpScoreByGemTypes();

        return score;
    }
}
