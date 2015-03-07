package com.almagems.mineraider.scenes;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.Constants;
import com.almagems.mineraider.EffectAnims.Fade;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.MyColor;

public abstract class Scene {
	
	protected float touchDownX;
	protected float touchDownY;

    protected Constants.ScenesEnum nextSceneId;
    protected boolean goNextScene;

    protected Fade _fade = new Fade();

	public static Visuals visuals;
    protected boolean initialized = false;

    //  ctor
	public Scene() {
	}

    protected void initFadeOut() {
        _fade.init(new MyColor(0f, 0f, 0f, 0f), new MyColor(0f, 0f, 0f, 1f));
    }

    protected void initFadeIn() {
        _fade.init(new MyColor(0f, 0f, 0f, 1f), new MyColor(0f, 0f, 0f, 0f));
    }

	protected abstract void surfaceChanged(int width, int height);
	public abstract void update();
	public abstract void draw();
    public void prepare() {
        if (!initialized) {
            surfaceChanged((int)Visuals.screenWidth, (int)Visuals.screenHeight);
            initialized = true;
        } else {
            System.out.println("Prepare doing nothing already Initialized");
        }

        goNextScene = false;
        nextSceneId = Constants.ScenesEnum.None;

        initFadeIn();
    }

    protected void drawFade() {
        if (!_fade.done) {
            visuals.bindNoTexture();
            _fade.update();
            _fade.draw();

            if (_fade.done && goNextScene) {
                ClassicSingleton singleton = ClassicSingleton.getInstance();
                singleton.showScene(nextSceneId);
            }
        }
    }

    // input
	public abstract void handleTouchPress(float normalizedX, float normalizedY);
	public abstract void handleTouchDrag(float normalizedX, float normalizedY);
	public abstract void handleTouchRelease(float normalizedX, float normalizedY);
}
