package com.almagems.mineraider;

import com.almagems.mineraider.objects.MineCart;

public class ClassicSingleton {
	private static ClassicSingleton instance = null;
	public MineRaiderRenderer renderer = null;
	public MineCart cart1 = null;
	public MineCart cart2 = null;
    public ScoreCounter scoreCounter = null;
    public HUD hud;

	protected ClassicSingleton() {
        scoreCounter = new ScoreCounter();
        hud = new HUD();
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

	public void showSceneLevel() {
		renderer.showSceneLevel();
	}

    public int getScore() {
        return scoreCounter.getScore();
    }

    public void setScore(int score) {
        scoreCounter.setScore(score);
    }
	
//	public void spawnParticleEmitterAt(float x, float y, int gemType) {
//		//System.out.println("Spawn particle emitter at: " + x + ", " + y);
//		particleManager.addParticleEmitterAt(x, y, gemType);
//	}
	
}
