package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class Stats extends Overlay {

    private float deltaY;

    private float touchDownX;
    private float touchDownY;

    private final MenuItemAnim menuItemAnim;
    private final MenuItem backButton;
    private final Fade background;

    public boolean done;
    private MenuItem selectedMenuItem;
    private ScoreCounter scoreCounter;

    // gem types
    private final Text[] textsGemTypes;
    private final ColoredQuad[] barsGemTypes;
    private final float[] positionsY;
    
    // match types
    private final Text[] textsMatchTypesRows;
    private final Text[] textsMatchTypesCols;
    private final Text[][] textsMatchTypesValues;

    private final int matchTypesMaxRows;
    private final int matchTypesMaxCols;

    // extras section
    private final Text textMaxComboCount;
    private final Text textHintShownCount;
    private final Text textLongestComboCount;

    // balance section
    private final Text textBalanceCollected;
    private final Text textBalanceWasted;


    // ctor
    public Stats() {
        System.out.println("Stats ctor...");

        deltaY = 0f;

        matchTypesMaxRows = 6;
        matchTypesMaxCols = 3;

        // general
        backButton = new MenuItem();
        menuItemAnim = new MenuItemAnim();
        background = new Fade();

        // gem types section
        positionsY = new float[MAX_GEM_TYPES];
        textsGemTypes = new Text[MAX_GEM_TYPES];
        barsGemTypes = new ColoredQuad[MAX_GEM_TYPES];

        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i] = new ColoredQuad();
            textsGemTypes[i] = new Text();
        }

        float y = 1.0f;
        float step = -0.25f;
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            positionsY[i] = y;
            y += step;
        }
        
        barsGemTypes[0].pos.ty = positionsY[0];
        textsGemTypes[0].pos.ty = positionsY[0];
        
        barsGemTypes[1].pos.ty = positionsY[1];
        textsGemTypes[1].pos.ty = positionsY[1];

        barsGemTypes[2].pos.ty = positionsY[2];
        textsGemTypes[2].pos.ty = positionsY[2];

        barsGemTypes[3].pos.ty = positionsY[3];
        textsGemTypes[3].pos.ty = positionsY[3];

        barsGemTypes[4].pos.ty = positionsY[4];
        textsGemTypes[4].pos.ty = positionsY[4];

        barsGemTypes[5].pos.ty = positionsY[5];
        textsGemTypes[5].pos.ty = positionsY[5];

        barsGemTypes[6].pos.ty = positionsY[6];
        textsGemTypes[6].pos.ty = positionsY[6];

        // match types section
        textsMatchTypesCols = new Text[matchTypesMaxCols]; // type, horizontal, vertical
        textsMatchTypesRows = new Text[matchTypesMaxRows]; // 3, 4, 5, 6, 7, 8    
        textsMatchTypesValues = new Text[matchTypesMaxRows][matchTypesMaxCols];

        for (int i = 0; i < matchTypesMaxCols; ++i) {
            textsMatchTypesCols[i] = new Text();
        }

        for (int i = 0; i < matchTypesMaxRows; ++i) {
            textsMatchTypesRows[i] = new Text();
        }

        for (int row = 0; row < matchTypesMaxRows; ++row) {
            for (int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col] = new Text();
            }
        }

        // extras section
        textMaxComboCount = new Text();
        textHintShownCount = new Text();
        textLongestComboCount = new Text();

        // collected wasted section
        textBalanceCollected = new Text();
        textBalanceWasted = new Text();
    }

    public void init() {        
        done = false;
        selectedMenuItem = null;
        scoreCounter = Engine.getInstance().game.scoreCounter;

        float sc = 1.76f;
        float aspect = Graphics.aspectRatio;
        boolean flipUTextureCoordinate = false;
        Texture textureObj = graphics.getTextureObj(graphics.textureMenuItems);

        Rectangle rect = textureObj.getFrame("menu_item_back.png");
        backButton.init("Back", Menu.MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        backButton.setTrans(0f, -aspect * 0.6f, 0f);
        backButton.setRot(0f, 0f, 0f);
        backButton.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);

        background.init(new Color(0f, 0f, 0f, 0.4f), new Color(0f, 0f, 0f, 0.6f));
        
        /// stat sections
        initGemByTypesSection();
        initMatchTypesSection();
        initExtrasSection();
        initBalanceSection();
    }

    private void initGemByTypesSection() {
        System.out.println("initGemByTypesSection...");

        // calc min - max among collected gems        
        int[] realGems = scoreCounter.getScoreByGemTypesAsIntArray();
        int min = calcMin(realGems);
        int max = calcMax(realGems);
        System.out.println("Min : " + min + ", max: " + max);

        int[] altGems = realGems.clone();

        // adjust
        if (min != max) {
            int range = max - min;
            int delta = min - (range / 2);
            int len = altGems.length;
            for (int i = 0; i < len; ++i) {
                altGems[i] -= delta;
            }
        }

        // at this point values in array are between 0 and (max-min) value
        // recalc max and min values
        min = calcMin(altGems);
        max = calcMax(altGems);

        if (min == 0 && max == 0) {
            max = 10;
            int len = altGems.length;
            for (int i = 0; i < len; ++i) {
                altGems[i] = 1;
            }
        }

        // calc bars width
        final float wmax = 0.75f;
        final float h = 0.05f;
        final float xminus = 0.8f;
        float w;
        w = ((float)altGems[0] / (float)max) * wmax;
        barsGemTypes[0].init(scoreCounter.scoreByGemTypes.get(0).color,  w, h);
        barsGemTypes[0].pos.tx = w - xminus;

        w = ((float)altGems[1] / (float)max) * wmax;
        barsGemTypes[1].init(scoreCounter.scoreByGemTypes.get(1).color,  w, h);
        barsGemTypes[1].pos.tx = w - xminus;

        w = ((float)altGems[2] / (float)max) * wmax;
        barsGemTypes[2].init(scoreCounter.scoreByGemTypes.get(2).color, w, h);
        barsGemTypes[2].pos.tx = w - xminus;

        w = ((float)altGems[3] / (float)max) * wmax;
        barsGemTypes[3].init(scoreCounter.scoreByGemTypes.get(3).color, w, h);
        barsGemTypes[3].pos.tx = w - xminus;

        w = ((float)altGems[4] / (float)max) * wmax;
        barsGemTypes[4].init(scoreCounter.scoreByGemTypes.get(4).color,   w, h);
        barsGemTypes[4].pos.tx = w - xminus;

        w = ((float)altGems[5] / (float)max) * wmax;
        barsGemTypes[5].init(scoreCounter.scoreByGemTypes.get(5).color,  w, h);
        barsGemTypes[5].pos.tx = w - xminus;

        w = ((float)altGems[6] / (float)max) * wmax;
        barsGemTypes[6].init(scoreCounter.scoreByGemTypes.get(6).color,   w, h);
        barsGemTypes[6].pos.tx = w - xminus;

        // setup texts
        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);        

        textsGemTypes[0].init("" + realGems[0], textColor, textColor, fontScale);
        textsGemTypes[0].pos.tx = barsGemTypes[0].pos.tx - textsGemTypes[0].getTextWidth() / 2f;

        textsGemTypes[1].init("" + realGems[1], textColor, textColor, fontScale);
        textsGemTypes[1].pos.tx = barsGemTypes[1].pos.tx - textsGemTypes[1].getTextWidth() / 2f;

        textsGemTypes[2].init("" + realGems[2], textColor, textColor, fontScale);
        textsGemTypes[2].pos.tx = barsGemTypes[2].pos.tx - textsGemTypes[2].getTextWidth() / 2f;

        textsGemTypes[3].init("" + realGems[3], textColor, textColor, fontScale);
        textsGemTypes[3].pos.tx = barsGemTypes[3].pos.tx - textsGemTypes[3].getTextWidth() / 2f;

        textsGemTypes[4].init("" + realGems[4], textColor, textColor, fontScale);
        textsGemTypes[4].pos.tx = barsGemTypes[4].pos.tx - textsGemTypes[4].getTextWidth() / 2f;

        textsGemTypes[5].init("" + realGems[5], textColor, textColor, fontScale);
        textsGemTypes[5].pos.tx = barsGemTypes[5].pos.tx - textsGemTypes[5].getTextWidth() / 2f;

        textsGemTypes[6].init("" + realGems[6], textColor, textColor, fontScale);
        textsGemTypes[6].pos.tx = barsGemTypes[6].pos.tx - textsGemTypes[6].getTextWidth() / 2f;
    }

    private void initMatchTypesSection() {
        System.out.println("createAndInitMatchTypesSection...");

        // 3 columns: type | horizontal | vertical
        // rows 3, 4, 5, 6, 7, 8

        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);

        textsMatchTypesCols[0].init("TYPE", textColor, textColor, fontScale);
        textsMatchTypesCols[1].init("HORIZONTAL", textColor, textColor, fontScale);
        textsMatchTypesCols[2].init("VERTICAL", textColor, textColor, fontScale);

        textsMatchTypesRows[0].init("3", textColor, textColor, fontScale);
        textsMatchTypesRows[1].init("4", textColor, textColor, fontScale);
        textsMatchTypesRows[2].init("5", textColor, textColor, fontScale);
        textsMatchTypesRows[3].init("6", textColor, textColor, fontScale);
        textsMatchTypesRows[4].init("7", textColor, textColor, fontScale);
        textsMatchTypesRows[5].init("8", textColor, textColor, fontScale);

        for(int row = 0; row < matchTypesMaxRows; ++row) {
            for(int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col].init("" + MyUtils.randInt(100, 1000), textColor, textColor, fontScale );
            }
        }

        // setup positions
        float x[] = new float[] { -0.8f, -0.4f, 0.0f, 0.4f };
        final float yStart = 1f;
        final float yStep = -0.25f;
        float y = yStart;

        textsMatchTypesCols[0].pos.tx = x[1];
        textsMatchTypesCols[0].pos.ty = y;

        textsMatchTypesCols[1].pos.tx = x[2];
        textsMatchTypesCols[1].pos.ty = y;

        textsMatchTypesCols[2].pos.tx = x[3];
        textsMatchTypesCols[2].pos.ty = y;

        y = yStart;
        textsMatchTypesRows[0].pos.tx = x[0];
        textsMatchTypesRows[0].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[1].pos.tx = x[0];
        textsMatchTypesRows[1].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[2].pos.tx = x[0];
        textsMatchTypesRows[2].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[3].pos.tx = x[0];
        textsMatchTypesRows[3].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[4].pos.tx = x[0];
        textsMatchTypesRows[4].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[5].pos.tx = x[0];
        textsMatchTypesRows[5].pos.ty = y;

        float xpos;
        float ypos = yStart + yStep;

        for(int row = 0; row < matchTypesMaxRows; ++row) {
            xpos = x[1];
            for(int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col].pos.tx = xpos;
                textsMatchTypesValues[row][col].pos.ty = ypos;
            }
            ypos += yStep;
        }
    }

    private void initExtrasSection() {
        System.out.println("initExtrasSection...");

        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);

        // plain text and a number at the end (simple)

        textMaxComboCount.init("Combo Count: " + scoreCounter.getComboCount(), textColor, textColor, fontScale);
        textHintShownCount.init("Hint Shown: " + scoreCounter.getHintsShownCount(), textColor, textColor, fontScale);
        textLongestComboCount.init("Longest Combo: " + scoreCounter.getLongestComboNumber(), textColor, textColor, fontScale);

    }
        
    private void initBalanceSection() {
        System.out.println("initBalanceSection...");

        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);

        // two numbers text
        // and images to depict a libra

        textBalanceCollected.init("" + scoreCounter.getCollectedCount(), textColor, textColor, fontScale);
        textBalanceWasted.init("" + scoreCounter.getWastedCount(), textColor, textColor, fontScale);
    }

    public void update() {
        background.update();
        backButton.update();
        if (selectedMenuItem != null) {
            if (backButton.getAnim() == null) {
                done = true;
            }
        }

        // TODO: update positions based on deltaY

    }

    public void draw() {
        //System.out.println("Stats draw...");

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

// draw background
        graphics.bindNoTexture();
        background.draw();

// draw sections
        drawGemTypesSection();
        //drawMatchTypesSection();
        //drawExtrasSection();
        //drawBalanceSection();

// draw back button
        graphics.textureShader.useProgram();
        graphics.textureShader.setTexture(Graphics.textureMenuItems);
        backButton.draw();
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, graphics.invertedViewProjectionMatrix);
        touchDownX = normalizedX;
        touchDownY = normalizedY;

        if (selectedMenuItem == null) {
            if (backButton.isHit(pos.x, pos.y)) {
                selectedMenuItem = backButton;
                backButton.setAnim(menuItemAnim);
            }
        }
    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {
        // update deltaY position;
        deltaY = normalizedY * 2f;
    }

    private void drawGemTypesSection() {

        // draw bars
        graphics.colorShader.useProgram();
        glDisable(GL_BLEND);
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i].drawBatch();
        }

        // draw texts
        glEnable(GL_BLEND);
        graphics.textureShader.useProgram();
        graphics.textureShader.setTexture(Graphics.textureFonts);
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            textsGemTypes[i].draw();
        }
    }

    private void drawMatchTypesSection() {
        glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);

        // draw cols
        for (int i = 0; i < matchTypesMaxCols; ++i) {
            textsMatchTypesCols[i].draw();
        }
        
        // draw rows
        for (int i = 0; i < matchTypesMaxRows; ++i) {
            textsMatchTypesRows[0].draw();        
        }

        // draw data
        for(int row = 0; row < matchTypesMaxRows; ++row) {
            for(int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col].draw();
            }
        }
    }

    private void drawExtrasSection() {
        glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);


    }

    private void drawBalanceSection() {
        glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);


    }

    private int calcMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        int len = arr.length;
        
        for(int i = 0; i < len; ++i) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    private int calcMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        int len = arr.length;

        for(int i = 0; i < len; ++i) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

}
