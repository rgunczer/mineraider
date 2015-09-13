package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class StatSectionBalance extends StatSectionBase {

    private final Text textBalanceCollected;
    private final Text textBalanceWasted;

    // ctor
	public StatSectionBalance() {
        textBalanceCollected = new Text();
        textBalanceWasted = new Text();
	}

	public void init() {
        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);

        float y = -4.0f;

        // title
        textTitle.init("COLLECTED vs WASTED", new Color(1f, 1f, 0f), new Color(1f, 1f, 1f), 1.6f);
        textTitle.pos.tx = -textTitle.getTextWidth() / 2.0f;
        textTitle.pos.ty = y;
        textTitle.posYorigin = textTitle.pos.ty;

        y -= 0.9f;

        textBalanceCollected.init("" + scoreCounter.getCollectedCount(), textColor, textColor, fontScale);
        textBalanceCollected.pos.tx = -0.5f;
        textBalanceCollected.pos.ty = y;
        textBalanceCollected.posYorigin = textBalanceCollected.pos.ty;

        textBalanceWasted.init("" + scoreCounter.getWastedCount(), textColor, textColor, fontScale);
        textBalanceWasted.pos.tx = 0.5f;
        textBalanceWasted.pos.ty = y;
        textBalanceWasted.posYorigin = textBalanceWasted.pos.ty;
    }

	public void update(float offsetY) {
        textTitle.pos.ty = textTitle.posYorigin + offsetY;
        textBalanceWasted.pos.ty = textBalanceWasted.posYorigin + offsetY;
        textBalanceCollected.pos.ty = textBalanceCollected.posYorigin + offsetY;
	}

	public void draw() {
		glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);
        
        textTitle.draw();
        textBalanceCollected.draw();
        textBalanceWasted.draw();
	}

}