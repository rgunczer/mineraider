package com.almagems.mineraider;

public final class StatSectionExtras extends StatSectionBase {

    private final Text textMaxComboCount;
    private final Text textHintShownCount;
    private final Text textLongestComboCount;

    // ctor
	public StatSectionExtras() {
        textMaxComboCount = new Text();
        textHintShownCount = new Text();
        textLongestComboCount = new Text();
	}

	public void init() {
        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);

        // plain text and a number at the end (simple)

        textMaxComboCount.init("Combo Count: " + scoreCounter.getComboCount(), textColor, textColor, fontScale);
        textHintShownCount.init("Hint Shown: " + scoreCounter.getHintsShownCount(), textColor, textColor, fontScale);
        textLongestComboCount.init("Longest Combo: " + scoreCounter.getLongestComboNumber(), textColor, textColor, fontScale);
	}

	public void update(float offsetY) {

	}

	public void draw() {
        glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);

	}

}