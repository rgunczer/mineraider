package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class StatSectionExtras extends StatSectionBase {

    private final int maxTextCount = 4;
    private final Text[] texts = new Text[maxTextCount];

    // ctor
	public StatSectionExtras() {
        for(int i = 0; i < maxTextCount; ++i) {
            texts[i] = new Text();
        }
	}

	public void init() {
        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);

        float x = -0.75f;
        float y = -2.7f;

        // title
        textTitle.init("EXTRAS", new Color(1f, 1f, 0f), new Color(1f, 1f, 1f), 1.6f);
        textTitle.pos.tx = -textTitle.getTextWidth() / 2.0f;
        textTitle.pos.ty = y;
        textTitle.posYorigin = textTitle.pos.ty;

        y -= 0.275f;

        texts[0].init("Shared Matches: " + scoreCounter.sharedMatchesCounter, textColor, textColor, fontScale);
        texts[0].pos.tx = x;
        texts[0].pos.ty = y;
        texts[0].posYorigin = texts[0].pos.ty;

        y -= 0.2f;
        texts[1].init("Highest Combos: " + scoreCounter.highestComboCounter, textColor, textColor, fontScale);
        texts[1].pos.tx = x;
        texts[1].pos.ty = y;
        texts[1].posYorigin = texts[1].pos.ty;

        y -= 0.2f;
        texts[2].init("Perfect Swaps: " + scoreCounter.perfectSwapCounter, textColor, textColor, fontScale);
        texts[2].pos.tx = x;
        texts[2].pos.ty = y;
        texts[2].posYorigin = texts[2].pos.ty;

        y -= 0.2f;
        texts[3].init("Hints: " + scoreCounter.hintCounter, textColor, textColor, fontScale);
        texts[3].pos.tx = x;
        texts[3].pos.ty = y;
        texts[3].posYorigin = texts[3].pos.ty;
    }

	public void update(float offsetY) {
        textTitle.pos.ty = textTitle.posYorigin + offsetY;
        for(int i = 0; i < maxTextCount; ++i) {
            texts[i].pos.ty = texts[i].posYorigin + offsetY;
        }
	}

	public void draw() {
        glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);

        textTitle.draw();
        for(int i = 0; i < maxTextCount; ++i) {
            texts[i].draw();
        }
	}

}