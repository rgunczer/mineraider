package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class Stats extends Overlay {

    private final MenuItemAnim menuItemAnim;
    private final MenuItem backButton;
    private final Fade fade;

    public boolean done;
    private MenuItem selectedMenuItem;
    private ScoreCounter scoreCounter;

    private final Text[] text;

    private final ColoredQuad[] coloredQuadGemType = new ColoredQuad[MAX_GEM_TYPES];

    public Stats(){
        System.out.println("Stats ctor...");

        backButton = new MenuItem();
        menuItemAnim = new MenuItemAnim();
        fade = new Fade();
        text =  new Text[7];
        text[0] = new Text();
        text[1] = new Text();
        text[2] = new Text();
        text[3] = new Text();
        text[4] = new Text();
        text[5] = new Text();
        text[6] = new Text();


        coloredQuadGemType[0] = new ColoredQuad();
        coloredQuadGemType[0].pos.ty = 1.0f;

        coloredQuadGemType[1] = new ColoredQuad();
        coloredQuadGemType[1].pos.ty = 0.75f;

        coloredQuadGemType[2] = new ColoredQuad();
        coloredQuadGemType[2].pos.ty = 0.5f;

        coloredQuadGemType[3] = new ColoredQuad();
        coloredQuadGemType[3].pos.ty = 0.25f;

        coloredQuadGemType[4] = new ColoredQuad();
        coloredQuadGemType[4].pos.ty = 0.0f;

        coloredQuadGemType[5] = new ColoredQuad();
        coloredQuadGemType[5].pos.ty = -0.25f;

        coloredQuadGemType[6] = new ColoredQuad();
        coloredQuadGemType[6].pos.ty = -0.5f;
    }

    public void init() {
        done = false;
        selectedMenuItem = null;

        float sc = 1.76f;
        float aspect = Graphics.aspectRatio;
        boolean flipUTextureCoordinate = false;
        Texture textureObj = graphics.getTextureObj(graphics.textureMenuItems);

        Rectangle rect;
        rect = textureObj.getFrame("menu_item_back.png");
        backButton.init("Back", Menu.MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        backButton.setTrans(0f, -aspect * 0.6f, 0f);
        backButton.setRot(0f, 0f, 0f);
        backButton.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);

        fade.init(new Color(0f, 0f, 0f, 0.4f), new Color(0f, 0f, 0f, 0.4f));

        scoreCounter = Engine.getInstance().game.scoreCounter;

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int[] arr = scoreCounter.scoreByGemTypes;

        for(int i = 0; i < arr.length; ++i) {
            if (arr[i] > max) {
                max = arr[i];
            }

            if (arr[i] < min) {
                min = arr[i];
            }
        }

        System.out.println("Min : " + min + ", max: " + max);

        float wmax = 0.75f;
        float wmin = 0.2f;
        float w ;
        float h = 0.05f;
        w =  ((float)arr[0] / (float)max) * wmax;
        coloredQuadGemType[0].init(new Color(194, 150, 76, 255),  w, h);
        coloredQuadGemType[0].pos.tx = w - 0.8f;

        w =  ((float)arr[1] / (float)max) * wmax;
        coloredQuadGemType[1].init(new Color(197, 94, 124, 255),  w, h);
        coloredQuadGemType[1].pos.tx = w - 0.8f;

        w =  ((float)arr[2] / (float)max) * wmax;
        coloredQuadGemType[2].init(new Color(193, 102, 193, 255), w, h);
        coloredQuadGemType[2].pos.tx = w - 0.8f;

        w =  ((float)arr[3] / (float)max) * wmax;
        coloredQuadGemType[3].init(new Color(172, 152, 158, 255), w, h);
        coloredQuadGemType[3].pos.tx = w - 0.8f;

        w =  ((float)arr[4] / (float)max) * wmax;
        coloredQuadGemType[4].init(new Color(26, 61, 186, 255),   w, h);
        coloredQuadGemType[4].pos.tx = w - 0.8f;

        w =  ((float)arr[5] / (float)max) * wmax;
        coloredQuadGemType[5].init(new Color(196, 123, 99, 255),  w, h);
        coloredQuadGemType[5].pos.tx = w - 0.8f;

        w =  ((float)arr[6] / (float)max) * wmax;
        coloredQuadGemType[6].init(new Color(188, 38, 38, 255),   w, h);
        coloredQuadGemType[6].pos.tx = w - 0.8f;

        // setup texts
        float textWidth;
        Color textColor = new Color(1f, 1f, 1f);

        text[0].init("Gem Type 0: " + arr[0] , textColor, textColor, 0.9f);
        textWidth = text[0].getTextWidth();
        text[0].pos.trans(-textWidth / 2f, 0.8f, 0f);
        text[0].pos.rot(0f, 0f, 0f);
        text[0].pos.scale(1f, 1f, 1f);

        text[1].init("Gem Type 1: " + arr[1] , textColor, textColor, 0.9f);
        textWidth = text[1].getTextWidth();
        text[1].pos.trans(-textWidth / 2f, 0.6f, 0f);
        text[1].pos.rot(0f, 0f, 0f);
        text[1].pos.scale(1f, 1f, 1f);

        text[2].init("Gem Type 2: " + arr[2] , textColor, textColor, 0.9f);
        textWidth = text[2].getTextWidth();
        text[2].pos.trans(-textWidth / 2f, 0.4f, 0f);
        text[2].pos.rot(0f, 0f, 0f);
        text[2].pos.scale(1f, 1f, 1f);

        text[3].init("Gem Type 3: " + arr[3], textColor, textColor, 0.9f);
        textWidth = text[3].getTextWidth();
        text[3].pos.trans(-textWidth / 2f, 0.2f, 0f);
        text[3].pos.rot(0f, 0f, 0f);
        text[3].pos.scale(1f, 1f, 1f);

        text[4].init("Gem Type 4: " + arr[4] , textColor, textColor, 0.9f);
        textWidth = text[4].getTextWidth();
        text[4].pos.trans(-textWidth / 2f, 0f, 0f);
        text[4].pos.rot(0f, 0f, 0f);
        text[4].pos.scale(1f, 1f, 1f);

        text[5].init("Gem Type 5: " + arr[5] , textColor, textColor, 0.9f);
        textWidth = text[5].getTextWidth();
        text[5].pos.trans(-textWidth / 2f, -0.2f, 0f);
        text[5].pos.rot(0f, 0f, 0f);
        text[5].pos.scale(1f, 1f, 1f);

        text[6].init("Gem Type 6: " + arr[6] , textColor, textColor, 0.9f);
        textWidth = text[6].getTextWidth();
        text[6].pos.trans(-textWidth / 2f, -0.4f, 0f);
        text[6].pos.rot(0f, 0f, 0f);
        text[6].pos.scale(1f, 1f, 1f);
    }

    public void update() {
        backButton.update();
        if (selectedMenuItem != null) {
            if (backButton.getAnim() == null) {
                done = true;
            }
        }
    }

    public void draw() {
        //System.out.println("Stats draw...");

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        graphics.textureShader.setTexture(Graphics.textureFonts);
        text[0].draw();
        text[1].draw();
        text[2].draw();
        text[3].draw();
        text[4].draw();
        text[5].draw();
        text[6].draw();

        graphics.bindNoTexture();
        fade.draw();
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            coloredQuadGemType[i].draw();
        }

        graphics.textureShader.useProgram();
        graphics.textureShader.setTexture(Graphics.textureMenuItems);
        backButton.draw();
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, graphics.invertedViewProjectionMatrix);

        if (selectedMenuItem == null) {
            if (backButton.isHit(pos.x, pos.y)) {
                selectedMenuItem = backButton;
                backButton.setAnim(menuItemAnim);
            }
        }
    }
}
