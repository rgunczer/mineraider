package com.almagems.mineraider;


public final class MenuGameTitleAnim {

    private final Quad imageMine;
    private final Quad imageRaider;

    private final float startZZ = 20f;
    private final float step = 1.25f;
    private float zz = startZZ;
    public static Graphics graphics;

    // ctor
    public MenuGameTitleAnim() {
        imageMine = new Quad();
        imageRaider = new Quad();
        reset();
    }

    public void reset() {
        zz = startZZ;
    }

    public void init(float sc, Texture textureObj) {
        final boolean flipUTextureCoordinate = false;
        final float aspect = Graphics.aspectRatio;
        Rectangle rect;

        rect = textureObj.getFrame("menu_game_title_mine.png");
        imageMine.init(Graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        imageMine.pos.trans(0f, aspect * 0.56f, 0f);
        imageMine.pos.rot(0f, 0f, 0f);
        imageMine.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);

        rect = textureObj.getFrame("menu_game_title_raider.png");
        imageRaider.init(Graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        imageRaider.pos.trans(0f, aspect * 0.56f, 0f);
        imageRaider.pos.rot(0f, 0f, 0f);
        imageRaider.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
    }

    public void update() {
        imageMine.pos.rot(0f, 0f, zz);
        imageRaider.pos.rot(0f, 0f, -zz);

        zz -= step;

        if ( Math.abs(zz) <  step + 0.1f ) {
            zz = 0f;
        }
    }

    public void draw() {
        imageMine.draw2();
        imageRaider.draw2();
    }

}
