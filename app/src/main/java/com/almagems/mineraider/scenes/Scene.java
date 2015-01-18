package com.almagems.mineraider.scenes;

import com.almagems.mineraider.Visuals;

public abstract class Scene {
	
	protected float touchDownX;
	protected float touchDownY;
	
	protected Visuals visuals;
	
	public Scene() {
		visuals = Visuals.getInstance();
	}

	public abstract void surfaceChanged(int width, int height);
	public abstract void update();
	public abstract void draw();

	// input
	public abstract void handleTouchPress(float normalizedX, float normalizedY);
	public abstract void handleTouchDrag(float normalizedX, float normalizedY);
	public abstract void handleTouchRelease(float normalizedX, float normalizedY);
}
