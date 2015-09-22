package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class StatSectionMatchTypes extends StatSectionBase {
	
    private final Text[][] texts;

    private final int maxRows;
    private final int maxCols;

    private final float fontScale = 0.9f;


	// ctor
	public StatSectionMatchTypes() {
        System.out.println("StatSectionMatchTypes ctor...");

        maxRows = 4;
        maxCols = 3;
        texts = new Text[maxRows][maxCols];

        for (int row = 0; row < maxRows; ++row) {
            for (int col = 0; col < maxCols; ++col) {
                texts[row][col] = new Text();
            }
        }
        textTitle.init("MATCH TYPES", Color.YELLOW, Color.WHITE, 1.6f);

        texts[0][0].init("TYPE", Color.GRAY, Color.WHITE, fontScale);
        texts[0][1].init("HORIZONTAL", Color.GRAY, Color.WHITE, fontScale);
        texts[0][2].init("VERTICAL", Color.GRAY, Color.WHITE, fontScale);

        texts[1][0].init("3", Color.GRAY, Color.WHITE, fontScale);
        texts[2][0].init("4", Color.GRAY, Color.WHITE, fontScale);
        texts[3][0].init("5", Color.GRAY, Color.WHITE, fontScale);

        final String maxNumbers = "000000";

        // values
        texts[1][1].init(maxNumbers, Color.GRAY, Color.WHITE, fontScale);
        texts[1][2].init(maxNumbers, Color.GRAY, Color.WHITE, fontScale);

        texts[2][1].init(maxNumbers, Color.GRAY, Color.WHITE, fontScale);
        texts[2][2].init(maxNumbers, Color.GRAY, Color.WHITE, fontScale);

        texts[3][1].init(maxNumbers, Color.GRAY, Color.WHITE, fontScale);
        texts[3][2].init(maxNumbers, Color.GRAY, Color.WHITE, fontScale);
    }

	public void init() {
        // 3 columns: type | horizontal | vertical
        // rows 3, 4, 5

        final float[] x = new float[] { -0.75f, -0.4f,  0.3f };
        final float yStart = -1.2f;
            final float yStep = -0.25f;

        // title
        textTitle.updateText("MATCH TYPES", Color.YELLOW, Color.WHITE, 1.6f);
        textTitle.pos.tx = -textTitle.getTextWidth() / 2.0f;
        textTitle.pos.ty = yStart;

        float y = yStart - 0.275f;

        // grid
        for(int row = 0; row < maxRows; ++row) {
            for(int col = 0; col < maxCols; ++col) {
                //texts[row][col].updateText("", Color.GRAY, Color.WHITE, fontScale);
                texts[row][col].pos.tx = x[col];
                texts[row][col].pos.ty = y;
            }
            y += yStep;
        }

        texts[0][0].updateText("TYPE", Color.GRAY, Color.WHITE, fontScale);
        texts[0][1].updateText("HORIZONTAL", Color.GRAY, Color.WHITE, fontScale);
        texts[0][2].updateText("VERTICAL", Color.GRAY, Color.WHITE, fontScale);

        texts[1][0].updateText("3", Color.GRAY, Color.WHITE, fontScale);
        texts[2][0].updateText("4", Color.GRAY, Color.WHITE, fontScale);
        texts[3][0].updateText("5", Color.GRAY, Color.WHITE, fontScale);

        // values
        texts[1][1].updateText("" + scoreCounter.match3CountHorizontal, Color.GRAY, Color.WHITE, fontScale);
        texts[1][2].updateText("" + scoreCounter.match3CountVertical, Color.GRAY, Color.WHITE, fontScale);

        texts[2][1].updateText("" + scoreCounter.match4CountHorizontal, Color.GRAY, Color.WHITE, fontScale);
        texts[2][2].updateText("" + scoreCounter.match4CountVertical, Color.GRAY, Color.WHITE, fontScale);

        texts[3][1].updateText("" + scoreCounter.match5CountHorizontal, Color.GRAY, Color.WHITE, fontScale);
        texts[3][2].updateText("" + scoreCounter.match5CountVertical, Color.GRAY, Color.WHITE, fontScale);


        // pos y origin
        textTitle.posYorigin = textTitle.pos.ty;

        for(int row = 0; row < maxRows; ++row) {
            for (int col = 0; col < maxCols; ++col) {
                texts[row][col].posYorigin = texts[row][col].pos.ty;
            }
        }
    }

	public void update(float offsetY) {
        for(int row = 0; row < maxRows; ++row) {
            for (int col = 0; col < maxCols; ++col) {
                texts[row][col].pos.ty = texts[row][col].posYorigin + offsetY;
            }
        }
        textTitle.pos.ty = textTitle.posYorigin + offsetY;
	}

	public void draw() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        graphics.textureShader.setTexture(Graphics.textureFonts);
        graphics.textureShader.useProgram();

        for(int row = 0; row < maxRows; ++row) {
            for(int col = 0; col < maxCols; ++col) {
                texts[row][col].draw();
            }
        }
        textTitle.draw();
	}
}