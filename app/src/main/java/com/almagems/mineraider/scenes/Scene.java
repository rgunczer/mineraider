package com.almagems.mineraider.scenes;

import com.almagems.mineraider.Visuals;

public abstract class Scene {
	
	protected float touchDownX;
	protected float touchDownY;
	
	public static Visuals visuals;

    //  ctor
	public Scene() {
	}

	public abstract void surfaceChanged(int width, int height);
	public abstract void update();
	public abstract void draw();

	// input
	public abstract void handleTouchPress(float normalizedX, float normalizedY);
	public abstract void handleTouchDrag(float normalizedX, float normalizedY);
	public abstract void handleTouchRelease(float normalizedX, float normalizedY);
}
