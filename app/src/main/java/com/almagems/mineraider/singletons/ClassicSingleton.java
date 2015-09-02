package com.almagems.mineraider.singletons;

import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.BatchDrawer;
import com.almagems.mineraider.HUD;
import com.almagems.mineraider.MineRaiderActivity;
import com.almagems.mineraider.MineRaiderRenderer;
import com.almagems.mineraider.ScoreCounter;
import com.almagems.mineraider.audio.Audio;
import com.almagems.mineraider.objects.MineCart;
import com.almagems.mineraider.particlesystem.ParticleManager;
import com.almagems.mineraider.scenes.Level;
import com.almagems.mineraider.visuals.Visuals;

import android.content.Context;
import android.content.SharedPreferences;

import org.jbox2d.common.Vec2;


public class ClassicSingleton {

    private static ClassicSingleton instance = null;

    public static float adHeight;

    // android
    public MineRaiderRenderer renderer = null;
    public MineRaiderActivity activity = null;

    public ParticleManager particleManager;
    public Visuals visuals = null;
    public Audio audio = null;
	public MineCart cart1 = null;
	public MineCart cart2 = null;
    public ScoreCounter scoreCounter = null;
    public HUD hud;
    public BatchDrawer batchDrawer = null;
    public Level level = null;


    // ctor
	private ClassicSingleton() {
        System.out.println("ClassicSingleton ctor...");
    }

	public static ClassicSingleton getInstance() {
		if (instance == null) {
			instance = new ClassicSingleton(); 
		}
		return instance;
	}

    public void createVisuals() {
        System.out.println("ClassicSingleton createVisuals...");
        visuals = new Visuals(activity);
        particleManager = new ParticleManager(visuals);
    }

    public void init() {
        System.out.println("ClassicSingleton init...");
        scoreCounter = new ScoreCounter();
        hud = new HUD(visuals);
        batchDrawer = new BatchDrawer(visuals);
        level = new Level();

        level.init(this);
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
        int score = getScore();
        editor.putInt("SCORE", score);

        // save score by gem types
        int[] arr = getScoreByGemTypes();
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
        setScore(score);
        scoreCounter.dumpScore();

        // load score by gem types
        int[] arr = new int[MAX_GEM_TYPES];
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            arr[i] = sharedPrefs.getInt("GEM" + i, 0);
        }
        setScoreByGemTypes(arr);
        scoreCounter.dumpScoreByGemTypes();

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
        level.update();
    }
    public void draw() {
        level.draw();
    }

    public void onSurfaceChanged(int width, int height) {
        level.surfaceChanged(width, height);
    }

    // input
    public void handleTouchPress(float normalizedX, float normalizedY) {
        level.handleTouchPress(normalizedX, normalizedY);
    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {
        level.handleTouchDrag(normalizedX, normalizedY);
    }

    public void handleTouchRelease(float normalizedX, float normalizedY) {
        level.handleTouchRelease(normalizedX, normalizedY);
    }

    public void notifyOtherMinecartToStart() {
        Vec2 pos1 = cart1.cart.getPosition();
        Vec2 pos2 = cart2.cart.getPosition();

        cart1.restartCart();
        cart2.restartCart();

        /*
        if (pos1.x < pos2.x) {
            cart1.restartCart();
        } else {
            cart2.restartCart();
        }
*/
    }

}
