package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class Loading extends Overlay {

    private int loadingStepCounter = 0;
    private int maxLoadingStep = 15;
    public boolean done = false;
    private final Quad quad;
    private final ProgressBarControl progress;

    // ctor
    public Loading() {
        Color colorBody = new Color(0.9f, 0.0f, 0.0f);
        Color colorFrame = new Color(0.1f, 0.1f, 0.1f);
        float width = 1.0f;
        float height = 0.02f;
        float border = 0.01f;

        progress = new ProgressBarControl("Loading", colorFrame, colorBody, width, height, border);
        quad = new Quad();
    }

    public void init() {
        // quad (Logo)
        Rectangle rect = new Rectangle(0f, 0f, 512f, 256f);
        quad.init(Graphics.textureLoading, Color.WHITE, rect, false);

        float sc = 0.75f;
        quad.pos.trans(0f, 0f, 0f);
        quad.pos.rot(0f, 0f, 0f);
        quad.pos.scale(1f * sc, 0.5f * sc, 1f);

        // progress
        float aspect = Graphics.aspectRatio;
        progress.init(-aspect * 0.8f);
        progress.value = 0.25f;
    }

    public void update() {

        switch (loadingStepCounter) {
            case 0:  Engine.audio = new Audio(); break;
            case 1:  graphics.loadTexturesPart01(); break;
            case 2:  graphics.loadTexturesPart02(); break;
            case 3:  graphics.loadTexturesPart03(); break;
            case 4:  graphics.loadShaders(); break;
            case 5:  graphics.loadFonts(); break;
            case 6:  graphics.loadModelsPart01(); break;
            case 7:  graphics.loadModelsPart02(); break;
            case 8:  graphics.loadModelsPart03(); break;
            case 9:  Engine.game.createObjects(); break;
            case 10: Engine.game.renderToFBO(); break;
            case 11: graphics.releaseModelLoader(); break;
            case 12: graphics.particleManager.init(); break;
            case 13: Engine.loadPreferences(); break;
            case 14: Engine.audio.init(Engine.activity); break;
            case 15: graphics.releaseUnusedAssets(); break;
        }

        // update loading progress based on loadingStepCounter
        progress.value = (float)loadingStepCounter / (float)maxLoadingStep;
        //System.out.println("loadingStepCounter: " + loadingStepCounter + ", progress value: " + progress.value);

        if (progress.value > 1.0f) {
            progress.value = 1f;
            done = true;
        }

        ++loadingStepCounter;
    }

    public void draw() {
        graphics.setProjectionMatrix2D();
        graphics.updateViewProjMatrix();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        quad.draw();
        glDisable(GL_BLEND);

        graphics.bindNoTexture();
        graphics.singleColorShader.useProgram();
        progress.draw();
    }



}