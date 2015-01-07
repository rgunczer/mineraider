package com.almagems.mineraider;

import com.almagems.mineraider.objects.MineCart;

public class ClassicSingleton {
	private static ClassicSingleton instance = null;
	
	public MineRaiderRenderer renderer = null;
	public MineCart cart1 = null;
	public MineCart cart2 = null;
	
	protected ClassicSingleton() {
		
	}
	
	public static ClassicSingleton getInstance() {
		if (instance == null) {
			instance = new ClassicSingleton(); 
		}
		return instance;
	}
	
	public void showSceneLevel() {
		renderer.showSceneLevel();
	}
	
//	public void spawnParticleEmitterAt(float x, float y, int gemType) {
//		//System.out.println("Spawn particle emitter at: " + x + ", " + y);
//		particleManager.addParticleEmitterAt(x, y, gemType);
//	}
	
}
