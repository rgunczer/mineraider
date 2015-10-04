package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class Stats extends Overlay {

// public
    public boolean done;

// private
    private float offsetY;
    private float prevOffsetY;

    private MenuItem selectedMenuItem;    

    private final MenuItemAnim menuItemAnim;
    private final MenuItem backButton;

    private final Fade top;
    private final Fade bottom;
    
    private final StatSectionGemTypes statSectionGemTypes;
    private final StatSectionMatchTypes statSectionMatchTypes; 
    private final StatSectionExtras statSectionExtras;
    private final StatSectionBalance statSectionBalance;

    private final Color colorOpaque;
    private final Color colorTransparent;
    
    // ctor
    public Stats() {
        //System.out.println("Stats ctor...");

        offsetY = 0f;

        // general
        backButton = new MenuItem();
        menuItemAnim = new MenuItemAnim();

        top = new Fade();
        bottom = new Fade();

        colorOpaque =  new Color(0f, 0f, 0f, 1.0f);
        colorTransparent = new Color(0f, 0f, 0f, 0.0f);

        // sections
        statSectionGemTypes = new StatSectionGemTypes();
        statSectionMatchTypes = new StatSectionMatchTypes();
        statSectionExtras = new StatSectionExtras();
        statSectionBalance = new StatSectionBalance();
    }

    public void init() {
        done = false;
        selectedMenuItem = null;
        ScoreCounter scoreCounter = Engine.game.scoreCounter;

        final float sc = 1.76f;
        final float aspect = Graphics.aspectRatio;
        final boolean flipUTextureCoordinate = false;
        Texture textureObj = graphics.getTextureObj(Graphics.textureMenuItems);

        Rectangle rect = textureObj.getFrame("menu_item_back.png");
        backButton.init("Back", Menu.MenuOptions.Back, Graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        backButton.setTrans(0f, -aspect * 0.8f, 0f);
        backButton.setRot(0f, 0f, 0f);
        backButton.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);

        float h = 0.6f;

        top.init(colorOpaque, colorTransparent, h);
        top.coloredQuad.pos.ty = Graphics.aspectRatio - h;

        bottom.init(colorTransparent, colorOpaque, h);
        bottom.coloredQuad.pos.ty = -Graphics.aspectRatio + h;

        // stat sections
        StatSectionBase.scoreCounter = scoreCounter;

        statSectionGemTypes.init();
        statSectionMatchTypes.init();
        statSectionExtras.init();
        statSectionBalance.init();

        background.init(colorBackground, colorBackground);
    }

    public void update() {
        background.update();
        backButton.update();
        if (selectedMenuItem != null) {
            if (backButton.getAnim() == null) {
                done = true;
            }
        }
        
        statSectionGemTypes.update(offsetY);
        statSectionMatchTypes.update(offsetY);
        statSectionExtras.update(offsetY);
        statSectionBalance.update(offsetY);
    }

    public void draw() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

// draw background
        graphics.bindNoTexture();
        background.draw();

// draw sections
        statSectionGemTypes.draw();
        statSectionMatchTypes.draw();
        statSectionExtras.draw();
        statSectionBalance.draw();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        graphics.bindNoTexture();
        top.draw();
        bottom.draw();

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
                Engine.audio.playSound();
                backButton.setAnim(menuItemAnim);
            }
        }
    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {
        offsetY = prevOffsetY + (touchDownY - normalizedY) * -2f;
        keepOffsetInRange();
    }

    public void handleTouchRelease(float normalizedX, float normalizedY) {
        prevOffsetY = offsetY;
    }

    private void keepOffsetInRange() {
        //System.out.println("Prev offset: " + offsetY);

        if (offsetY < -0.25f) {
            offsetY = -0.25f;
        }

        if (offsetY > 4.6f) {
            offsetY = 4.6f;
        }
    }

}