package com.almagems.mineraider;


public final class HUD {
    public static Graphics graphics;

    private int cachedScore;

    private final Text scoreText;
    private final Text extraText;
    private final Text gemsFromCartText;
    private final Quad quad;
    private final Quad quadPauseButton;

    private int _centerCounter;

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

        quad = new Quad();

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

        perfectSwapScale = 1.2f;
    }

    public void init() {
        fontScale = 0.9f;
        scoreText.setSpacingScale(0.06f);
        scoreText.init("SCORE:" + cachedScore, new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), fontScale);

        scoreX = -0.96f;
        scoreY = -Graphics.aspectRatio + (scoreText.getTextHeight() / 3f);
        scoreText.pos.trans(scoreX, scoreY, 0f);
        scoreText.pos.rot(0f, 0f, 0f);
        scoreText.pos.scale(1f, 1f, 1f);

        extraText.setSpacingScale(0.065f);
        extraText.init("WATCH FOR MINECARTS", new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), comboScale);
        extraText.pos.trans(-extraText.getTextWidth() / 2f, -0.5f, 0.0f);

        Rectangle rect = new Rectangle(0f, 0f, 128f, 128f);
        quadPauseButton.init(graphics.textureHudPauseButton, new Color(1f, 1f, 1f), rect, false);
        quadPauseButton.pos.trans(0.94f, -Graphics.aspectRatio + 0.06f, 0f);
        float sc = 0.055f;
        quadPauseButton.pos.scale(sc, sc, 1f);

        float scale = Graphics.aspectRatio * 0.1f;

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

    public void showMessage(String message) {
        extraText.init(message, new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), comboScale);
        float textWidth = extraText.getTextWidth();
        extraText.pos.trans(-textWidth / 2f, -0.4f, 0f);
        extraText.pos.rot(0f, 0f, 0f);
        extraText.pos.scale(1f, 1f, 1f);

        extraTextCooling = 125;
        comboScale += 0.05f;

        //extraText.addAnimEffect(effectWahWah);
        extraText.addAnimEffect(effectZigZag);
    }

    public void showBonusCartGems(int numberOfGems) {
        gemsFromCartText.init("BONUS " + numberOfGems + " GEMS COLLECTED", new Color(1f, 1f, 0f, 1f), new Color(1f, 1f, 1f, 1f), 0.75f);
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

    public int showCombo() {
        ++comboCounter;

        extraText.init("COMBOx" + comboCounter, new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), comboScale);
        float textWidth = extraText.getTextWidth();
        extraText.pos.trans(-textWidth / 2f, -0.4f, 0f);
        extraText.pos.rot(0f, 0f, 0f);
        extraText.pos.scale(1f, 1f, 1f);

        extraTextCooling = 100;
        comboScale += 0.05f;

        //extraText.addAnimEffect(effectWahWah);
        extraText.addAnimEffect(effectZigZag);

        return comboCounter;
    }

    public void showPerfectSwap() {
        extraText.init("PERFECT SWAP", new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), perfectSwapScale);
        float textWidth = extraText.getTextWidth();
        extraText.pos.trans(-textWidth / 2f, -0.4f, 0f);
        extraText.pos.rot(0f, 0f, 0f);
        extraText.pos.scale(2f, 2f, 1f);

        extraTextCooling = 30;
        effectWahWah.wahScale = 1.0f;
        extraText.addAnimEffect(effectWahWah);
    }

    public void showMatch4InARowBonus() {
        extraText.init("FOUR IN A ROW", new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), perfectSwapScale);
        float textWidth = extraText.getTextWidth() * 1.75f;
        extraText.pos.trans(-textWidth / 2f, -0.4f, 0f);
        extraText.pos.rot(0f, 0f, 0f);
        extraText.pos.scale(1.75f, 2f, 1f);

        extraTextCooling = 45;
        effectWahWah.wahScale = 0.9f;
        extraText.addAnimEffect(effectZigZag);
    }

    public void showMatch4InAColBonus() {
        extraText.init("FOUR IN A COLUMN", new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), perfectSwapScale);
        float textWidth = extraText.getTextWidth() * 1.75f;
        extraText.pos.trans(-textWidth / 2f, -0.4f, 0f);
        extraText.pos.rot(0f, 0f, 0f);
        extraText.pos.scale(1.75f, 2f, 1f);

        extraTextCooling = 45;
        effectWahWah.wahScale = 0.9f;
        extraText.addAnimEffect(effectZigZag);
    }

    public void updateScore(int score) {
        if (score != cachedScore) {
            scoreCooling = 30;
            String str = "SCORE:" + score;
            scoreText.init(str, new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), fontScale);
            cachedScore = score;
            effectWahWahScore.wahScale = 0.2f;
            scoreText.addAnimEffect(effectWahWahScore);
        }
    }

    public void update() {
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

        //System.out.println("Score cooling: " + scoreCooling);

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
        graphics.textureShader.useProgram();

        graphics.textureShader.setTexture(Graphics.textureFonts);
        scoreText.draw();
        if (extraTextCooling > 0) {
            extraText.draw();
        }

        if (bonusFromCartCooling > 0) {
            gemsFromCartText.draw();
        }


        Game game = Engine.getInstance().game;
        if ( game.gameState == Game.GameState.Playing ) {
            quadPauseButton.draw();
        }

        //visuals.drawAxes();
    }
}
