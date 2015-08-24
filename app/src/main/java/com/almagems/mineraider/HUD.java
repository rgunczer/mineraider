package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;
import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.EffectAnims.WahWah;
import com.almagems.mineraider.EffectAnims.ZigZag;
import com.almagems.mineraider.objects.EdgeDrawer;
import com.almagems.mineraider.objects.GemIkon;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.scenes.Level;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Text;


public class HUD {
    private int cachedScore;

    private final Text scoreText;
    private final Text extraText;
    private final Text gemsFromCartText;
    private final GemIkon ikon;
    private final Quad quad;
    private final Quad quadPauseButton;
    private final EdgeDrawer edgeDrawer;

    private float scoreX;
    private float scoreY;
    private float fontScale;
    private int extraTextCooling;
    private int comboCounter;
    private float comboScale;

    private float perfectSwapScale;
    private float _bonusPosX;
    private float _bonusCartGemsTextWidth;
    private float _bonusDiff;
    private float _bonusTargetPosX;

    private int _centerCounter;
    private int scoreCooling;
    private int bonusFromCartCooling;

    private WahWah effectWahWahScore;
    private WahWah effectWahWah;
    private ZigZag effectZigZag;

    // ctor
    public HUD() {
        scoreText = new Text();
        extraText = new Text();
        gemsFromCartText = new Text();
        ikon = new GemIkon();
        quad = new Quad();
        edgeDrawer = new EdgeDrawer(32);

        comboCounter = 0;
        comboScale = 1.0f;
        effectWahWah = new WahWah();
        effectZigZag = new ZigZag();
        effectWahWahScore = new WahWah();

        quadPauseButton = new Quad();

        _bonusPosX = 0f;
        _bonusCartGemsTextWidth = 0f;

        // cooling
        extraTextCooling = 0;
        scoreCooling = 0;
        bonusFromCartCooling = 0;

        perfectSwapScale = 0.75f;
    }

    public void init() {
        fontScale = 0.9f;
        scoreText.setSpacingScale(0.06f);
        scoreText.init("SCORE:" + cachedScore, new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 0f, 0f, 1f), fontScale);

        scoreX = -0.96f;
        scoreY = -Visuals.aspectRatio + (scoreText.getTextHeight() / 3f);
        scoreText.pos.trans(scoreX, scoreY, 0f);
        scoreText.pos.rot(0f, 0f, 0f);
        scoreText.pos.scale(1f, 1f, 1f);

        extraText.setSpacingScale(0.065f);
        extraText.init("WATCH FOR MINECARTS", new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 0f, 0f, 1f), comboScale);
        extraText.pos.trans(-extraText.getTextWidth() / 2f, -0.5f, 0.0f);

        ikon.init();

        Visuals visuals = Visuals.getInstance();

        Rectangle rect = new Rectangle(0f, 0f, 128f, 128f);
        quadPauseButton.init(visuals.textureHudPauseButton, new MyColor(1f, 1f, 1f), rect, false);
        quadPauseButton.pos.trans(0.94f, -Visuals.aspectRatio + 0.06f, 0f);
        float sc = 0.055f;
        quadPauseButton.pos.scale(sc, sc, 1f);

        float scale = Visuals.aspectRatio * 0.1f;

/*
        final boolean flipUCoordinate = false;
        quad.init(Visuals.getInstance().textureHelmets, new MyColor(1f, 1f, 1f, 1f), rect, flipUCoordinate);
        quad.pos.trans(-0.92f, -Visuals.aspectRatio + 0.06f, 0f);
        quad.pos.rot(0f, 0f, 0f);
*/
        sc = 0.075f;
        quad.pos.scale(sc, sc, 1f);

        extraTextCooling = 0;
        comboCounter = 0;
        comboScale = 1.0f;
    }

    public void reset() {
        comboCounter = 0;
        comboScale = 1.0f;

        _bonusPosX = -10f;
        _bonusCartGemsTextWidth = 0f;
        _centerCounter = 0;

        extraTextCooling = 0;
        scoreCooling = 0;
        bonusFromCartCooling = 0;
    }

    public void setLevelNumber(int number) {
        // TODO: set level number here
    }

    public void showBonusCartGems(int numberOfGems) {
        gemsFromCartText.init("BONUS " + numberOfGems + " GEMS COLLECTED", new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.75f);
        _bonusCartGemsTextWidth = gemsFromCartText.getTextWidth();

        _bonusTargetPosX = -_bonusCartGemsTextWidth / 2f;
        _bonusPosX = 1.0f; // + _bonusCartGemsTextWidth / 2f;
        _centerCounter = 32;

        //gemsFromCartText.pos.trans(-textWidth / 2f, -0.9f, 0f);
        gemsFromCartText.pos.trans(_bonusPosX, -0.9f, 0f);
        gemsFromCartText.pos.rot(0f, 0f, 0f);
        gemsFromCartText.pos.scale(1f, 1f, 1f);

        bonusFromCartCooling = 200;

        _bonusDiff = -100f;
    }

    public void showCombo() {
        ++comboCounter;

        extraText.init("COMBOx" + comboCounter, new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 0f, 0f, 1f), comboScale);
        float textWidth = extraText.getTextWidth();
        extraText.pos.trans(-textWidth / 2f, -0.4f, 0f);
        extraText.pos.rot(0f, 0f, 0f);
        extraText.pos.scale(1f, 1f, 1f);

        extraTextCooling = 100;
        comboScale += 0.05f;

        //extraText.addAnimEffect(effectWahWah);
        extraText.addAnimEffect(effectZigZag);
    }

    public void showPerfectSwap() {
        extraText.init("PERFECT SWAP", new MyColor(1f, 1f, 0f, 1f), new MyColor(1f, 0f, 0f, 1f), perfectSwapScale);
        float textWidth = extraText.getTextWidth();
        extraText.pos.trans(-textWidth / 2f, -0.4f, 0f);
        extraText.pos.rot(0f, 0f, 0f);
        extraText.pos.scale(1f, 1f, 1f);

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
        gemsFromCartText.update();
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

        if (bonusFromCartCooling > 0) {
            --bonusFromCartCooling;

            float easing = 0.075f;
            float vx = (_bonusTargetPosX - _bonusPosX) * easing;

            //System.out.println("vx is: " + vx);
            //System.out.println("posX is: " + _bonusPosX);

            _bonusPosX += vx;

            if (_bonusPosX < -0.47) {
                _bonusTargetPosX = -1.2f - _bonusCartGemsTextWidth;
            }


/*
            if (_centerCounter != 0) {
                if (diff < _bonusDiff) {
                    --_centerCounter;
                    _bonusPosX += speed;
                }
            }
*/

            gemsFromCartText.pos.trans(_bonusPosX, -0.9f, 0f);

            if (bonusFromCartCooling == 0) {
                // TODO: remove effect if any on text obj
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

        //ikon.draw();
        visuals.textureShader.setTexture(visuals.textureFonts);
        scoreText.draw();
        if (extraTextCooling > 0) {
            extraText.draw();
        }

        if (bonusFromCartCooling > 0) {
            gemsFromCartText.draw();
        }


        Level level = (Level)ClassicSingleton.getInstance().level;
        if ( level.gameState == Level.GameState.Playing ) {
            quadPauseButton.draw();
        }
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
