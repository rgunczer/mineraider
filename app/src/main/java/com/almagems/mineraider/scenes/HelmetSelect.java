package com.almagems.mineraider.scenes;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.EffectAnims.Fade;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Text;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static com.almagems.mineraider.Constants.*;



public class HelmetSelect extends Scene {

    private ScenesEnum nextSceneId;
    private boolean goNextScene;
    private int currentHelmetIndex;

    private Fade fade;
    private final Quad leftArrow;
    private final Quad rightArrow;
    private Quad helmetInFocus;
    private final Quad[] helmets;

    private final Text textScore;
    private final Text textTitle;
    private final Text textContinue;
    private final Text textBack;
    private final Visuals visuals;

    private boolean showNextHelmetArrow;
    private boolean showPrevHelmetArrow;


    public HelmetSelect() {
        helmets = new Quad[MAX_HELMET_TYPES];
        for (int i = 0; i < MAX_HELMET_TYPES; ++i) {
            helmets[i] = new Quad();
        }

        leftArrow = new Quad();
        rightArrow = new Quad();

        textScore = new Text();
        textTitle = new Text();
        textContinue = new Text();
        textBack = new Text();

        visuals = Visuals.getInstance();
    }

    public void init() {

    }

    @Override
    public void surfaceChanged(int width, int height) {
        showNextHelmetArrow = true;
        showPrevHelmetArrow = true;

        nextSceneId = ScenesEnum.None;
        goNextScene = false;
        fade = new Fade();
        fade.init(new MyColor(0f, 0f, 0f, 1f), new MyColor(0f, 0f, 0f, 0f));

        float textWidth;

        textTitle.init("SELECT YOUR HELMET", new MyColor(1f, 0f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.6f);
        textWidth = textTitle.getTextWidth();
        textTitle.pos.setPosition(-textWidth / 2f, Visuals.aspectRatio * 0.7f, 0f);
        textTitle.pos.setRot(0f, 0f, 0f);
        textTitle.pos.setScale(1f, 1f, 1f);

        textContinue.init("CONTINUE", new MyColor(1f, 0f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.9f);
        textWidth = textContinue.getTextWidth();
        textContinue.pos.setPosition(0.5f - (textWidth / 2f) , -Visuals.aspectRatio * 0.9f, 0f);
        textContinue.pos.setRot(0f, 0f, 0f);
        textContinue.pos.setScale(1f, 1f, 1f);

        textBack.init("BACK", new MyColor(1f, 0f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.9f);
        textWidth = textContinue.getTextWidth();
        textBack.pos.setPosition(-0.5f - (textWidth / 2f), -Visuals.aspectRatio * 0.9f, 0f);
        textBack.pos.setRot(0f, 0f, 0f);
        textBack.pos.setScale(1f, 1f, 1f);


        textScore.init("SCORE", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.0f);
        textWidth = textScore.getTextWidth();
        textScore.pos.setPosition(-textWidth / 2f, -0.4f, 0f);
        textScore.pos.setRot(0f, 0f, 0f);
        textScore.pos.setScale(1f, 1f, 1f);

        float quadScale = 0.1f;
        final boolean flipUCoordinate = false;
        MyColor colorWhite = new MyColor(1f, 1f, 1f, 1f);


        leftArrow.init(visuals.textureNextArrow, colorWhite, new Rectangle(0f, 0f, -256f, 256f), flipUCoordinate);
        leftArrow.op.setPosition(-0.9f, 0.0f, 0f);
        leftArrow.op.setRot(0f, 0f, 0f);
        leftArrow.op.setScale(quadScale, quadScale, 1f);


        rightArrow.init(visuals.textureNextArrow, colorWhite, new Rectangle(0f, 0f, 256f, 256f), flipUCoordinate);
        rightArrow.op.setPosition(0.9f, 0.0f, 0f);
        rightArrow.op.setRot(0f, 0f, 0f);
        rightArrow.op.setScale(quadScale, quadScale, 1f);


        quadScale = 0.3f;

        // red
        helmets[RED_HELMET].init(visuals.textureHelmets, colorWhite, rectRedHelmet, flipUCoordinate);
        helmets[RED_HELMET].op.setPosition(0.0f, 0.0f, 0f);
        helmets[RED_HELMET].op.setRot(0f, 0f, 0f);
        helmets[RED_HELMET].op.setScale(quadScale, quadScale, 1f);

        // green
        helmets[GREEN_HELMET].init(visuals.textureHelmets, colorWhite, rectGreenHelmet, flipUCoordinate);
        helmets[GREEN_HELMET].op.setPosition(0.0f, 0.0f, 0f);
        helmets[GREEN_HELMET].op.setRot(0f, 0f, 0f);
        helmets[GREEN_HELMET].op.setScale(quadScale, quadScale, 1f);

        // blue
        helmets[BLUE_HELMET].init(visuals.textureHelmets, colorWhite, rectBlueHelmet, flipUCoordinate);
        helmets[BLUE_HELMET].op.setPosition(0.0f, 0.0f, 0f);
        helmets[BLUE_HELMET].op.setRot(0f, 0f, 0f);
        helmets[BLUE_HELMET].op.setScale(quadScale, quadScale, 1f);

        // yellow
        helmets[YELLOW_HELMET].init(visuals.textureHelmets, colorWhite, rectYellowHelmet, flipUCoordinate);
        helmets[YELLOW_HELMET].op.setPosition(0.0f, 0.0f, 0f);
        helmets[YELLOW_HELMET].op.setRot(0f, 0f, 0f);
        helmets[YELLOW_HELMET].op.setScale(quadScale, quadScale, 1f);

        ClassicSingleton singleton = ClassicSingleton.getInstance();
        currentHelmetIndex = singleton.selectedHelmetIndex;

        setCurrentHelmet(currentHelmetIndex);
    }

    private void setCurrentHelmet(int index) {
        helmetInFocus = helmets[index];
        currentHelmetIndex = index;
        int score = ClassicSingleton.getInstance().loadPreferences(currentHelmetIndex);

        textScore.init("" + score, new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.0f);
        float textWidth = textScore.getTextWidth();
        textScore.pos.setPosition(-textWidth / 2f, -0.4f, 0f);
        textScore.pos.setRot(0f, 0f, 0f);
        textScore.pos.setScale(1f, 1f, 1f);
    }

    private void stepCurrentHelmet(int step) {
        showNextHelmetArrow = true;
        showPrevHelmetArrow = true;

        int nextIndex = currentHelmetIndex + step;
        if (step > 0) {
            if (nextIndex < MAX_HELMET_TYPES) {
                setCurrentHelmet(nextIndex);
            }
        } else {
            if (nextIndex >= 0) {
                setCurrentHelmet(nextIndex);
            }
        }

        if (currentHelmetIndex == 0) {
            showPrevHelmetArrow = false;
        }

        if (currentHelmetIndex == MAX_HELMET_TYPES - 1) {
            showNextHelmetArrow = false;
        }
    }

    @Override
    public void update() {
        textTitle.update();
    }

    @Override
    public void draw() {
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureFonts);

        textScore.draw();
        textTitle.draw();
        textContinue.draw();
        textBack.draw();

        visuals.textureShader.setTexture(visuals.textureHelmets);
        helmetInFocus.draw();

        if (showPrevHelmetArrow) {
            leftArrow.draw();
        }

        if (showNextHelmetArrow) {
            rightArrow.draw();
        }

        if (!fade.done) {
            visuals.bindNoTexture();
            fade.update();
            fade.draw();

            if (fade.done) {
                if (goNextScene) {
                    ClassicSingleton singleton = ClassicSingleton.getInstance();
                    singleton.selectedHelmetIndex = currentHelmetIndex;
                    singleton.showScene(nextSceneId);
                }
            }
        }
    }

    @Override
    public void handleTouchPress(float normalizedX, float normalizedY) {
        System.out.println("Normalized: " + normalizedX + ", " + normalizedY);

        if (normalizedY > -0.2f && normalizedY < 0.2f) {
            if (normalizedX < -0.6f) {
                //System.out.println("Helmets go left...");
                stepCurrentHelmet(-1);
            } else if (normalizedX > 0.6f) {
                //System.out.println("Helmets go right...");
                stepCurrentHelmet(1);
            }
        } else if (normalizedY < -0.75f) {
            //System.out.println(ClassicSingleton.getInstance().helmetIndexToString(currentHelmetIndex));

            if (normalizedX > 0.3f) {
                nextSceneId = ScenesEnum.Level;
            } else if (normalizedX < -0.3f) {
                nextSceneId = ScenesEnum.Menu;
            } else {
                nextSceneId = ScenesEnum.None;
            }

            if (nextSceneId != ScenesEnum.None) {
                fade.init(new MyColor(0f, 0f, 0f, 0f), new MyColor(0f, 0f, 0f, 1f));
                goNextScene = true;
            }
        }
    }

    @Override
    public void handleTouchDrag(float normalizedX, float normalizedY) {

    }

    @Override
    public void handleTouchRelease(float normalizedX, float normalizedY) {

    }
}