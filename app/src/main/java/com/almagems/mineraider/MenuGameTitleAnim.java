package com.almagems.mineraider;

public class MenuGameTitleAnim {

    private final Quad imageMine;
    private final Quad imageRaider;

    private final float startZZ = 20f;
    private final float step = 1.25f;
    private float zz = startZZ;
    public static Graphics graphics;

    public MenuGameTitleAnim() {
        reset();
        imageMine = new Quad();
        imageRaider = new Quad();
    }

    public void reset() {
        zz = startZZ;
    }

    public void init(float sc) {
        boolean flipUTextureCoordinate = false;
        float aspect = Graphics.aspectRatio;
        Rectangle rect;
        rect = new Rectangle(1097, 0+243, 591, 243);
        imageMine.init(graphics.textureMenuItems, graphics.whiteColor, rect, flipUTextureCoordinate);
        imageMine.pos.trans(0f, aspect * 0.6f, 0f);
        imageMine.pos.rot(0f, 0f, 0f);
        imageMine.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);


        rect = new Rectangle(0, 520+243, 591, 243);
        imageRaider.init(graphics.textureMenuItems, graphics.whiteColor, rect, flipUTextureCoordinate);
        imageRaider.pos.trans(0f, aspect * 0.6f, 0f);
        imageRaider.pos.rot(0f, 0f, 0f);
        imageRaider.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
    }

    public void update() {
        imageMine.pos.rot(0f, 0f, zz);
        imageRaider.pos.rot(0f, 0f, -zz);

        zz -= step;
        //System.out.println("zz is: " + zz);

        if ( Math.abs(zz) <  step + 0.1f ) {
            zz = 0f;
        }
    }

    public void draw() {
        imageMine.draw2();
        imageRaider.draw2();
    }

}
