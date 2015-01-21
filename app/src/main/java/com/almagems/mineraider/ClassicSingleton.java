package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;
import com.almagems.mineraider.objects.MineCart;

import android.content.Context;
import android.content.SharedPreferences;

public class ClassicSingleton {
    private static ClassicSingleton instance = null;
	public MineRaiderRenderer renderer = null;
    public MineRaiderActivity activity = null;
	public MineCart cart1 = null;
	public MineCart cart2 = null;
    public ScoreCounter scoreCounter = null;
    public HUD hud;
    public int selectedHelmetIndex = BLUE_HELMET;
    public BatchDrawer batchDrawer = null;

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

    public void sendComboNotification() {
        hud.showCombo();
    }

    public void sendPerfectSwapNotification() { hud.showPerfectSwap(); }

    public void showScene(ScenesEnum sceneId) {
        switch (sceneId) {
            case Level:
                renderer.showSceneLevel();
                break;

            case HelmetSelect:
                renderer.showSceneHelmetSelect();
                break;

            case Menu:
                renderer.showSceneMenu();
                break;
        }
    }

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

    public String helmetIndexToString(int index) {
        switch (index) {
            case RED_HELMET: return "RED";
            case BLUE_HELMET: return "BLUE";
            case GREEN_HELMET: return "GREEN";
            case YELLOW_HELMET: return "YELLOW";
        }
        return "UNKNOWN";
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

        String helmetColorName = helmetIndexToString(selectedHelmetIndex);

        // save score
        editor.putInt(helmetColorName + "-SCORE", getScore());

        // save score by gem types
        int[] arr = getScoreByGemTypes();
        for(int i = 0; i < arr.length; ++i) {
            editor.putInt(helmetColorName + "-GEM" + i, arr[i]);
        }

        editor.commit();
    }

    public int loadPreferences(int helmetIndex) {
        System.out.println("Load Preferences... helmetIndex: " + helmetIndex);

        String helmetColorName = helmetIndexToString(helmetIndex);
        System.out.println("Helmet Color Name: " + helmetColorName);
        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);

        // load score
        int score = sharedPrefs.getInt(helmetColorName + "-SCORE", 0);
        setScore(score);
        scoreCounter.dumpScore();

        // load score by gem types
        int[] arr = new int[MAX_GEM_TYPES];
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            arr[i] = sharedPrefs.getInt(helmetColorName + "-GEM" + i, 0);
        }
        setScoreByGemTypes(arr);
        scoreCounter.dumpScoreByGemTypes();

        return score;
    }
}
