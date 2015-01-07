package com.almagems.mineraider.scenes;

import com.almagems.mineraider.Visuals;

public class Scene {
	
	protected float touchDownX;
	protected float touchDownY;
	
	protected Visuals visuals;
	
	public Scene() {
		visuals = Visuals.getInstance();
	}

	public void surfaceChanged(int width, int height) {
		
	}
	
	public void update() {
		
	}
	
	public void draw() {
		
	}
	
	// input
	public void handleTouchPress(float normalizedX, float normalizedY) {
	
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY) {
	
	}
	
	public void handleTouchRelease(float normalizedX, float normalizedY) {
		
	}
}
