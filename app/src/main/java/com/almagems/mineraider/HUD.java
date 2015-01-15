package com.almagems.mineraider;

import com.almagems.mineraider.EffectAnims.EffectAnim;
import com.almagems.mineraider.EffectAnims.WahWah;
import com.almagems.mineraider.EffectAnims.ZigZag;
import com.almagems.mineraider.objects.EdgeDrawer;
import com.almagems.mineraider.objects.GemIkon;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Text;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class HUD {
    private int cachedScore;

    private final Text scoreText;
    private final Text extraText;
    private final GemIkon ikon;
    private final Quad quad;
    private final EdgeDrawer edgeDrawer;

    private float scoreX;
    private float scoreY;
    private float fontScale;
    private int extraTextCooling;
    private int comboCounter;
    private float comboScale;

    private float perfectSwapScale;

    private int scoreCooling;


    private WahWah effectWahWahScore;
    private WahWah effectWahWah;
    private ZigZag effectZigZag;

    // ctor
    public HUD() {
        scoreText = new Text();
        extraText = new Text();
        ikon = new GemIkon();
        quad = new Quad();
        edgeDrawer = new EdgeDrawer(32);
        extraTextCooling = 0;
        comboCounter = 0;
        comboScale = 1.0f;
        effectWahWah = new WahWah();
        effectZigZag = new ZigZag();
        effectWahWahScore = new WahWah();

        scoreCooling = 0;
        perfectSwapScale = 0.75f;
    }

    public void init() {
        fontScale = 0.9f;
        scoreText.setSpacingScale(0.06f);
        scoreText.init("SCORE:" + cachedScore, new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), fontScale);

        scoreX = -0.8f;
        scoreY = -Visuals.aspectRatio + (scoreText.getTextHeight() / 3f);
        scoreText.pos.setPosition(scoreX, scoreY, 0f);
        scoreText.pos.setRot(0f, 0f, 0f);
        scoreText.pos.setScale(1f, 1f, 1f);

        extraText.setSpacingScale(0.065f);
        extraText.init("WATCH FOR MINECARTS", new MyColor(0.6f, 0.6f, 0.6f, 1f), new MyColor(0f, 0f, 0f, 1f), comboScale);
        extraText.pos.setPosition(-extraText.getTextWidth() / 2f, -0.5f, 0.0f);

        ikon.init();

        float scale = Visuals.aspectRatio * 0.1f;

        quad.init(Visuals.getInstance().textureIkon, new MyColor(1f, 1f, 1f, 1f));
        quad.op.setPosition(0f, Visuals.aspectRatio - scale, 0f);
        quad.op.setRot(0f, 0f, 0f);
        quad.op.setScale(scale, scale, 1f);

        extraTextCooling = 0;
        comboCounter = 0;
        comboScale = 1.0f;
    }

    public void showCombo() {
        ++comboCounter;

        extraText.init("COMBOx" + comboCounter, new MyColor(1f, 0f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), comboScale);
        float textWidth = extraText.getTextWidth();
        extraText.pos.setPosition(-textWidth / 2f, -0.35f, 0f);
        extraText.pos.setRot(0f, 0f, 0f);
        extraText.pos.setScale(1f, 1f, 1f);

        extraTextCooling = 100;
        comboScale += 0.05f;

        //extraText.addAnimEffect(effectWahWah);
        extraText.addAnimEffect(effectZigZag);
    }

    public void showPerfectSwap() {
        extraText.init("PERFECT SWAP", new MyColor(0f, 0f, 0f, 1f), new MyColor(0f, 1f, 1f, 1f), perfectSwapScale);
        float textWidth = extraText.getTextWidth();
        extraText.pos.setPosition(-textWidth / 2f, -0.35f, 0f);
        extraText.pos.setRot(0f, 0f, 0f);
        extraText.pos.setScale(1f, 1f, 1f);

        extraTextCooling = 25;
        effectWahWah.wahScale = 0.6f;
        extraText.addAnimEffect(effectWahWah);
    }

    public void showMatch4() {

    }

    public void updateScore(int score) {
        if (score != cachedScore) {
            scoreCooling = 30;
            String str = "SCORE:" + score;
            scoreText.init(str, new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 0f, 0f, 1f), fontScale);
            cachedScore = score;
            effectWahWahScore.wahScale = 0.06f;
            scoreText.addAnimEffect(effectWahWahScore);
        }
    }

    public void update() {
        ikon.update();
        scoreText.update();
        extraText.update();
        quad.update();

        if (extraTextCooling > 0) {
            --extraTextCooling;

            if (extraTextCooling == 0) {
                comboCounter = 0;
                comboScale = 1.0f;
                extraText.removeAnimEffect();
            }
        }

        if (scoreCooling > 0) {
            --scoreCooling;

            if (scoreCooling == 0) {
                scoreText.removeAnimEffect();
            }
        }
    }

    public void draw() {
        Visuals visuals = Visuals.getInstance();
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        visuals.textureShader.useProgram();

        ikon.draw();
        visuals.textureShader.setTexture(visuals.textureFonts);
        scoreText.draw();
        if (extraTextCooling > 0) {
            extraText.draw();
        }

        quad.draw();
/*
        MyColor color = new MyColor(1f, 1f, 0f, 1f);

        edgeDrawer.begin();
        edgeDrawer.addLine(	-1f, 0f, 0f,
                             1f, 0f, 0f);

        edgeDrawer.addLine( 0f, -1f, 0f,
                            0f,  1f, 0f);

        setIdentityM(visuals.modelMatrix, 0);
        multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);

        visuals.colorShader.useProgram();
        visuals.colorShader.setUniforms(visuals.mvpMatrix, color);
        edgeDrawer.bindData(visuals.colorShader);
        edgeDrawer.draw();
*/
    }
}
