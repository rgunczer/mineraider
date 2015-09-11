package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class Stats extends Overlay {

// public
    public boolean done;

// private
    private float offsetY;

    private MenuItem selectedMenuItem;    

    private final MenuItemAnim menuItemAnim;
    private final MenuItem backButton;
    private final Fade background;    
    
    private final StatSectionGemTypes statSectionGemTypes;
    private final StatSectionMatchTypes statSectionMatchTypes; 
    private final StatSectionExtras statSectionExtras;
    private final StatSectionBalance statSectionBalance;
    
    
    // ctor
    public Stats() {
        System.out.println("Stats ctor...");

        offsetY = 0f;

        // general
        backButton = new MenuItem();
        menuItemAnim = new MenuItemAnim();
        background = new Fade();

        // sections
        statSectionGemTypes = new StatSectionGemTypes();
        statSectionMatchTypes = new StatSectionMatchTypes();
        statSectionExtras = new StatSectionExtras();
        statSectionBalance = new StatSectionBalance();
    }

    public void init() {
        done = false;
        selectedMenuItem = null;
        ScoreCounter scoreCounter = Engine.getInstance().game.scoreCounter;

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
        
        // stat sections
        statSectionGemTypes.init(scoreCounter);
        statSectionMatchTypes.init(scoreCounter);
        statSectionExtras.init(scoreCounter);
        statSectionBalance.init(scoreCounter);
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
        //System.out.println("Stats draw...");

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
        offsetY = (touchDownY - normalizedY) * 2f;
    }
    
}