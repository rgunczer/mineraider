package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class Loading extends Overlay {

    private int loadingStepCounter = 0;
    private static final int maxLoadingStep = 15;
    public boolean done = false;
    private final Quad quadLogo;
    private final ProgressBarControl progress;

    // ctor
    public Loading() {
        Color colorBody = new Color(0.9f, 0.0f, 0.0f);
        Color colorFrame = new Color(0.1f, 0.1f, 0.1f);
        final float width = 1.0f;
        final float height = 0.02f;
        final float border = 0.01f;

        progress = new ProgressBarControl("Loading", colorFrame, colorBody, width, height, border);
        quadLogo = new Quad();
    }

    public void init() {
        final boolean flipUTextureCoordinate = false;
        final float sc = 0.6f;
        quadLogo.init(Graphics.textureLoading, Color.WHITE, new Rectangle(0f, 0f, 512f, 256f), flipUTextureCoordinate);
        quadLogo.pos.trans(0f, -0.1f, 0f);
        quadLogo.pos.rot(0f, 0f, 0f);
        quadLogo.pos.scale(1f * sc, 0.5f * sc, 1f);

        progress.init(-Graphics.aspectRatio * 0.8f);
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
            case 15:
                graphics.releaseUnusedAssets();
                Engine.audio.playMusic();
                Engine.showInterstitialAd();
                break;
        }

        progress.value = (float)loadingStepCounter / (float)maxLoadingStep;
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
        quadLogo.draw();

        graphics.bindNoTexture();
        graphics.singleColorShader.useProgram();
        progress.draw();
    }



}