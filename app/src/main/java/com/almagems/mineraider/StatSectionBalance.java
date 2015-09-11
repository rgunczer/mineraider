package com.almagems.mineraider;

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

        // two numbers text
        // and images to depict a libra

        textBalanceCollected.init("" + scoreCounter.getCollectedCount(), textColor, textColor, fontScale);
        textBalanceWasted.init("" + scoreCounter.getWastedCount(), textColor, textColor, fontScale);
	}

	public void update(float offsetY) {

	}

	public void draw() {
		glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);
        

	}

}