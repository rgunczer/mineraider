package com.almagems.mineraider.loading;

import android.os.SystemClock;

import com.almagems.mineraider.singletons.ClassicSingleton;
import com.almagems.mineraider.GUI.ProgressBarControl;
import com.almagems.mineraider.visuals.Visuals;
import com.almagems.mineraider.audio.Audio;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;


public class Loading {

    private int loadingStepCounter = 0;
    private int maxLoadingStep = 10;
    public boolean done = false;
    private final Quad quad;
    private final ProgressBarControl progress;
    private Visuals visuals;

    // ctor
    public Loading(Visuals visuals) {
        this.visuals = visuals;

        MyColor colorBody = new MyColor(0.8f, 0.8f, 0.8f);
        MyColor colorFrame = new MyColor(0.2f, 0.2f, 0.2f);
        float height = 0.03f;
        float border = 0.01f;

        progress = new ProgressBarControl(visuals, "Loading", colorFrame, colorBody, height, border);
        quad = new Quad(visuals);
    }

    public void init() {
        // quad (Logo)
        Rectangle rect = new Rectangle(0f, 0f, 512f, 256f);
        quad.init(visuals.textureLoading, visuals.whiteColor, rect, false);

        float sc = 0.75f;
        quad.pos.trans(0f, 0f, 0f);
        quad.pos.rot(0f, 0f, 0f);
        quad.pos.scale(1f * sc, 0.5f * sc, 1f);

        // progress
        float aspect = Visuals.aspectRatio;
        progress.init(-aspect * 0.8f);
        progress.value = 0.25f;
    }

    public void update() {
        // step by step loading logic
        ClassicSingleton singleton = ClassicSingleton.getInstance();

        traceLoadingStep();

        switch (loadingStepCounter) {
            case 0:
                singleton.audio = new Audio();
                singleton.audio.init(singleton.activity);
                break;

            case 1:
                singleton.loadPreferences();
                break;

            case 2:

                break;

            case 3:

                break;

            case 4:

                break;

            case 5:

                break;

            case 6:

                break;

            case 7:

                break;

            case 8:

                break;

            case 9:

                break;

            case 10:

                break;

        }

        SystemClock.sleep(500);

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
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();

        quad.draw();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        visuals.bindNoTexture();
        visuals.colorShader.useProgram();

        progress.draw();
    }

    private void traceLoadingStep() {
        System.out.println("Loading step: " + loadingStepCounter);
    }

}