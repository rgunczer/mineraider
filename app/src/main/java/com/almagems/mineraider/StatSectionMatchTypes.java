package com.almagems.mineraider;

public final class StatSectionMatchTypes extends StatSectionBase {
	
    private final Text[] textsMatchTypesRows;
    private final Text[] textsMatchTypesCols;
    private final Text[][] textsMatchTypesValues;

    private final int matchTypesMaxRows;
    private final int matchTypesMaxCols;


	// ctor
	public StatSectionMatchTypes() {
		System.out.println("StatSectionMatchTypes ctor...");
        
        matchTypesMaxRows = 6;
        matchTypesMaxCols = 3;

        textsMatchTypesCols = new Text[matchTypesMaxCols]; // type, horizontal, vertical
        textsMatchTypesRows = new Text[matchTypesMaxRows]; // 3, 4, 5, 6, 7, 8    
        textsMatchTypesValues = new Text[matchTypesMaxRows][matchTypesMaxCols];

        for (int i = 0; i < matchTypesMaxCols; ++i) {
            textsMatchTypesCols[i] = new Text();
        }

        for (int i = 0; i < matchTypesMaxRows; ++i) {
            textsMatchTypesRows[i] = new Text();
        }

        for (int row = 0; row < matchTypesMaxRows; ++row) {
            for (int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col] = new Text();
            }
        }		
	}

	public void init() {
        // 3 columns: type | horizontal | vertical
        // rows 3, 4, 5, 6, 7, 8

        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);

        textsMatchTypesCols[0].init("TYPE", textColor, textColor, fontScale);
        textsMatchTypesCols[1].init("HORIZONTAL", textColor, textColor, fontScale);
        textsMatchTypesCols[2].init("VERTICAL", textColor, textColor, fontScale);

        textsMatchTypesRows[0].init("3", textColor, textColor, fontScale);
        textsMatchTypesRows[1].init("4", textColor, textColor, fontScale);
        textsMatchTypesRows[2].init("5", textColor, textColor, fontScale);
        textsMatchTypesRows[3].init("6", textColor, textColor, fontScale);
        textsMatchTypesRows[4].init("7", textColor, textColor, fontScale);
        textsMatchTypesRows[5].init("8", textColor, textColor, fontScale);

        for(int row = 0; row < matchTypesMaxRows; ++row) {
            for(int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col].init("" + MyUtils.randInt(100, 1000), textColor, textColor, fontScale );
            }
        }

        // setup positions
        float x[] = new float[] { -0.8f, -0.4f, 0.0f, 0.4f };
        final float yStart = 1f;
        final float yStep = -0.25f;
        float y = yStart;

        textsMatchTypesCols[0].pos.tx = x[1];
        textsMatchTypesCols[0].pos.ty = y;

        textsMatchTypesCols[1].pos.tx = x[2];
        textsMatchTypesCols[1].pos.ty = y;

        textsMatchTypesCols[2].pos.tx = x[3];
        textsMatchTypesCols[2].pos.ty = y;

        y = yStart;
        textsMatchTypesRows[0].pos.tx = x[0];
        textsMatchTypesRows[0].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[1].pos.tx = x[0];
        textsMatchTypesRows[1].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[2].pos.tx = x[0];
        textsMatchTypesRows[2].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[3].pos.tx = x[0];
        textsMatchTypesRows[3].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[4].pos.tx = x[0];
        textsMatchTypesRows[4].pos.ty = y;

        y += yStep;
        textsMatchTypesRows[5].pos.tx = x[0];
        textsMatchTypesRows[5].pos.ty = y;

        float xpos;
        float ypos = yStart + yStep;

        for(int row = 0; row < matchTypesMaxRows; ++row) {
            xpos = x[1];
            for(int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col].pos.tx = xpos;
                textsMatchTypesValues[row][col].pos.ty = ypos;
            }
            ypos += yStep;
        }
	}

	public void update(float offsetY) {

	}

	public void draw() {
        glEnable(GL_BLEND);
        graphics.textureShader.setTexture(Graphics.textureFonts);

        // draw cols
        for (int i = 0; i < matchTypesMaxCols; ++i) {
            textsMatchTypesCols[i].draw();
        }
        
        // draw rows
        for (int i = 0; i < matchTypesMaxRows; ++i) {
            textsMatchTypesRows[0].draw();        
        }

        // draw data
        for(int row = 0; row < matchTypesMaxRows; ++row) {
            for(int col = 0; col < matchTypesMaxCols; ++col) {
                textsMatchTypesValues[row][col].draw();
            }
        }
	}
}