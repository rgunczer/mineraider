package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class StatSectionBalance extends StatSectionBase {

    private final Text textBalanceCollected;
    private final Text textBalanceWasted;

    private final Quad libraVertical;
    private final Quad libraHorizontal;
    private final Quad libraBasketLeft;
    private final Quad libraBasketRight;

    // ctor
	public StatSectionBalance() {
        textBalanceCollected = new Text();
        textBalanceWasted = new Text();

        libraVertical = new Quad();
        libraHorizontal = new Quad();
        libraBasketLeft = new Quad();
        libraBasketRight = new Quad();
	}

	public void init() {
        final float fontScale = 0.9f;
        float y = -4.0f;

        // title
        textTitle.init("COLLECTED vs WASTED", new Color(1f, 1f, 0f), new Color(1f, 1f, 1f), 1.6f);
        textTitle.pos.tx = -textTitle.getTextWidth() / 2.0f;
        textTitle.pos.ty = y;
        textTitle.posYorigin = textTitle.pos.ty;

        y -= 0.9f;

        textBalanceCollected.init("" + scoreCounter.collectedGems, Color.GRAY, Color.WHITE, fontScale);
        textBalanceCollected.pos.tx = -0.5f - textBalanceCollected.getTextWidth() / 2.0f;
        textBalanceCollected.pos.ty = y;
        textBalanceCollected.posYorigin = textBalanceCollected.pos.ty;

        textBalanceWasted.init("" + scoreCounter.wastedGems, Color.GRAY, Color.WHITE, fontScale);
        textBalanceWasted.pos.tx = 0.5f - textBalanceWasted.getTextWidth() / 2.0f;
        textBalanceWasted.pos.ty = y;
        textBalanceWasted.posYorigin = textBalanceWasted.pos.ty;

        y += 0.3f;

        Rectangle rect;
        Texture textureObj = graphics.getTextureObj(Graphics.textureMenuItems);
        final float sc = 1.2f;

        rect = textureObj.getFrame("libra_vertical.png");
        libraVertical.init(Graphics.textureMenuItems, Color.WHITE, rect, false);
        libraVertical.pos.trans(0f, y, 0f);
        libraVertical.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        libraVertical.posYorigin = libraVertical.pos.ty;

        rect = textureObj.getFrame("libra_horizontal.png");
        libraHorizontal.init(Graphics.textureMenuItems, Color.WHITE, rect, false);
        libraHorizontal.pos.trans(0f, y + 0.3f, 0f);
        libraHorizontal.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        libraHorizontal.posYorigin = libraHorizontal.pos.ty;

        y+=0.05f;

        rect = textureObj.getFrame("libra_basket.png");
        libraBasketLeft.init(Graphics.textureMenuItems, Color.WHITE, rect, false);
        libraBasketLeft.pos.trans(-0.3f, y, 0f);
        libraBasketLeft.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        libraBasketLeft.posYorigin = libraBasketLeft.pos.ty;

        libraBasketRight.init(Graphics.textureMenuItems, Color.WHITE, rect, false);
        libraBasketRight.pos.trans(0.3f, y, 0f);
        libraBasketRight.pos.scale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        libraBasketRight.posYorigin = libraBasketRight.pos.ty;
    }

	public void update(float offsetY) {
        textTitle.pos.ty = textTitle.posYorigin + offsetY;
        textBalanceWasted.pos.ty = textBalanceWasted.posYorigin + offsetY;
        textBalanceCollected.pos.ty = textBalanceCollected.posYorigin + offsetY;

        libraVertical.pos.ty = libraVertical.posYorigin + offsetY;
        libraHorizontal.pos.ty = libraHorizontal.posYorigin + offsetY;
        libraBasketLeft.pos.ty = libraBasketLeft.posYorigin + offsetY;
        libraBasketRight.pos.ty = libraBasketRight.posYorigin + offsetY;
	}

	public void draw() {
		glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);
        
        textTitle.draw();
        textBalanceCollected.draw();
        textBalanceWasted.draw();

        // draw libra
        graphics.textureShader.setTexture(Graphics.textureMenuItems);
        libraVertical.draw();
        libraHorizontal.draw();
        libraBasketLeft.draw();
        libraBasketRight.draw();
	}

}