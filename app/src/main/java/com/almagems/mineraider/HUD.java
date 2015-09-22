package com.almagems.mineraider;


public final class HUD {

    public static Graphics graphics;

    private static final float startingComboFontScale = 1.2f;

    private int cachedScore;
    private boolean showCombo;

    private final Text textPerfectSwap;
    private final Text textScore;
    private final Text textBonusForCollected;
    private final Text textCombo;
    private final Text textFourInARow;
    private final Text textFourInACol;

    private final Quad quadMenuButton;

    private final float fontScale = 0.9f;
    private float comboScale;
    private final float collectedScale = 0.8f;

    private float bonusPosX;
    private float bonusCartGemsTextWidth;
    private float bonusTargetPosX;

    private int coolingScore;
    private int coolingPerfectSwap;
    private int coolingBonusForCollected;
    private int coolingFourInARow;
    private int coolingFourInACol;

    private final WahWah effectWahWahScore;
    private final WahWah effectWahWah;
    private final ZigZag effectZigZag;
    private final UpDown effectUpDown4InCol;
    private final LeftRight effectLeftRight4InRow;

    private static final float yCombo = -0.1f;
    private static final float yFourInARow = -0.3f;
    private static final float yFourInACol = -0.5f;
    private static final float yPerfectSwap = -0.7f;
    private static final float yBonusCollected = -0.9f;


    // ctor
    public HUD() {
        textScore = new Text();
        textPerfectSwap = new Text();
        textBonusForCollected = new Text();
        textCombo = new Text();
        textFourInARow = new Text();
        textFourInACol = new Text();

        quadMenuButton = new Quad();

        effectWahWah = new WahWah();
        effectZigZag = new ZigZag();
        effectWahWahScore = new WahWah();
        effectUpDown4InCol = new UpDown();
        effectLeftRight4InRow = new LeftRight();
    }

    public void init() {
        // score
        textScore.setSpacingScale(0.052f);
        textScore.init("SCORE: 0000000000", Color.YELLOW, Color.RED, fontScale);
        final float x = -0.96f;
        final float y = -Graphics.aspectRatio + (textScore.getTextHeight() / 3f);
        textScore.pos.trans(x, y, 0f);
        textScore.pos.rot(0f, 0f, 0f);
        textScore.pos.scale(1.1f, 1f, 1f);

        // collected
        textBonusForCollected.init("BONUS 000 GEMS COLLECTED", Color.YELLOW, Color.WHITE, collectedScale);

        // combo
        textCombo.init("COMBOx00", Color.YELLOW, Color.WHITE, comboScale);

        // perfect swap
        final float perfectSwapScale = 1.1f;
        textPerfectSwap.init("PERFECT SWAP", Color.YELLOW, Color.WHITE, perfectSwapScale);
        textPerfectSwap.pos.trans(-textPerfectSwap.getTextWidth() / 2f, yPerfectSwap, 0f);
        textPerfectSwap.pos.rot(0f, 0f, 0f);
        textPerfectSwap.pos.scale(1.1f, 1.1f, 1f);

        // four in a row
        textFourInARow.init("FOUR IN A ROW", Color.YELLOW, Color.RED, 1.2f);
        textFourInARow.pos.trans(-textFourInARow.getTextWidth() / 2f, yFourInARow, 0f);
        textFourInARow.pos.rot(0f, 0f, 0f);
        textFourInARow.pos.scale(1f, 1f, 1f);

        // four in a col
        textFourInACol.init("FOUR IN A COLUMN", Color.YELLOW, Color.RED, 1.2f);
        textFourInACol.pos.trans(-textFourInACol.getTextWidth() / 2f, yFourInACol, 0f);
        textFourInACol.pos.rot(0f, 0f, 0f);
        textFourInACol.pos.scale(1f, 1f, 1f);

        // menu button
        quadMenuButton.init(Graphics.textureHudPauseButton, Color.WHITE, new Rectangle(0f, 0f, 128f, 128f), false);
        quadMenuButton.pos.trans(0.94f, -Graphics.aspectRatio + 0.06f, 0f);
        quadMenuButton.pos.scale(0.055f, 0.055f, 1f);

        reset();
    }

    public void reset() {
        // cooling
        coolingScore = 0;
        coolingPerfectSwap = 0;
        coolingBonusForCollected = 0;
        coolingFourInARow = 0;
        coolingFourInACol = 0;

        // other
        showCombo = false;
        comboScale = startingComboFontScale;

        bonusPosX = 0f;
        bonusCartGemsTextWidth = 0f;
    }

    public void showFourInARow() {
        coolingFourInARow = 50;
        textFourInARow.addAnimEffect(effectLeftRight4InRow);
    }

    public void showFourInACol() {
        coolingFourInACol = 50;
        textFourInACol.addAnimEffect(effectUpDown4InCol);
    }

    public void showBonusCartGems(int numberOfGems) {
        textBonusForCollected.updateText("BONUS " + numberOfGems + " GEMS COLLECTED", Color.YELLOW, Color.WHITE, collectedScale);
        bonusCartGemsTextWidth = textBonusForCollected.getTextWidth();

        bonusTargetPosX = -bonusCartGemsTextWidth / 2f;
        bonusPosX = 1.0f;

        textBonusForCollected.pos.trans(bonusPosX, yBonusCollected, 0f);
        textBonusForCollected.pos.rot(0f, 0f, 0f);
        textBonusForCollected.pos.scale(1.1f, 1f, 1f);

        coolingBonusForCollected = 200;
    }

    public void showCombo(int count) {
        if (MyUtils.rand.nextBoolean()) {
            textCombo.updateText("COMBOx" + count, Color.YELLOW, Color.WHITE, comboScale);
        } else {
            textCombo.updateText("COMBOx" + count, Color.WHITE, Color.YELLOW, comboScale);
        }
        textCombo.pos.trans(-textCombo.getTextWidth() / 2f, yCombo, 0f);
        textCombo.pos.rot(0f, 0f, 0f);
        textCombo.pos.scale(1f, 1f, 1f);

        textCombo.addAnimEffect(effectZigZag);
        showCombo = true;
        comboScale += 0.1f;
    }

    public void hideCombo() {
        comboScale = startingComboFontScale;
        showCombo = false;
        textCombo.removeAnimEffect();
    }

    public void showPerfectSwap() {
        coolingPerfectSwap = 30;
        effectWahWah.wahScale = 0.3f;
        textPerfectSwap.addAnimEffect(effectWahWah);
    }

    public void updateScore(int score) {
        if (score != cachedScore) {
            if (coolingScore > 0) {
                textScore.removeAnimEffect();
            }
            coolingScore = 30;
            textScore.updateText("SCORE: " + score, Color.YELLOW, Color.RED, fontScale);
            cachedScore = score;
            effectWahWahScore.wahScale = 0.2f;
            textScore.addAnimEffect(effectWahWahScore);
        }
    }

    public void update() {
        textScore.update();
        textPerfectSwap.update();
        textBonusForCollected.update();
        textFourInARow.update();
        textFourInACol.update();

        if (showCombo) {
            textCombo.update();
        }

        if (coolingScore > 0) {
            --coolingScore;
            if (coolingScore == 0) {
                textScore.removeAnimEffect();
            }
        }

        if (coolingPerfectSwap > 0) {
            --coolingPerfectSwap;
            if (coolingPerfectSwap == 0) {
                textPerfectSwap.removeAnimEffect();
            }
        }

        if (coolingFourInARow > 0) {
            --coolingFourInARow;
            if (coolingFourInARow == 0) {
                textFourInARow.removeAnimEffect();
            }
        }

        if (coolingFourInACol > 0) {
            --coolingFourInACol;
            if (coolingFourInACol == 0) {
                textFourInACol.removeAnimEffect();
            }
        }

        if (coolingBonusForCollected > 0) {
            --coolingBonusForCollected;

            final float easing = 0.075f;
            final float vx = (bonusTargetPosX - bonusPosX) * easing;

            bonusPosX += vx;
            if (bonusPosX < -0.47) {
                bonusTargetPosX = -1.2f - bonusCartGemsTextWidth;
            }

            textBonusForCollected.pos.trans(bonusPosX, -0.9f, 0f);
        }
    }

    public void draw() {
        graphics.textureShader.useProgram();
        graphics.textureShader.setTexture(Graphics.textureFonts);
        textScore.draw();

        if (showCombo) {
            textCombo.draw();
        }

        if (coolingPerfectSwap > 0) {
            textPerfectSwap.draw();
        }

        if (coolingBonusForCollected > 0) {
            textBonusForCollected.draw();
        }

        if (coolingFourInARow > 0) {
            textFourInARow.draw();
        }

        if (coolingFourInACol > 0) {
            textFourInACol.draw();
        }

        if ( Engine.game.gameState == Game.GameState.Playing ) {
            quadMenuButton.draw();
        }

        //graphics.drawAxes();
    }
}
