package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class StatSectionGemTypes extends StatSectionBase {

    private final Text[] textsGemTypes;
    private final ColoredQuad[] barsGemTypes;
    private final float[] positionsY;

    private float screenOffsetY;
    private ScoreCounter scoreCounter;

    // ctor
    public StatSectionGemTypes() {    	
    	System.out.println("StatSectionGemTypes ctor...");

    	screenOffsetY = 0f; // later used for scrolling...
    	scoreCounter = null;

        positionsY = new float[MAX_GEM_TYPES];
        textsGemTypes = new Text[MAX_GEM_TYPES];
        barsGemTypes = new ColoredQuad[MAX_GEM_TYPES];

        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i] = new ColoredQuad();
            textsGemTypes[i] = new Text();
        }
    }

    public void init(ScoreCounter scoreCounter) {
    	this.scoreCounter = scoreCounter;

        float y = 1.0f;
        float step = -0.25f;
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            positionsY[i] = y;
            y += step;
        }
        
        barsGemTypes[0].pos.ty = positionsY[0];
        textsGemTypes[0].pos.ty = positionsY[0];
        
        barsGemTypes[1].pos.ty = positionsY[1];
        textsGemTypes[1].pos.ty = positionsY[1];

        barsGemTypes[2].pos.ty = positionsY[2];
        textsGemTypes[2].pos.ty = positionsY[2];

        barsGemTypes[3].pos.ty = positionsY[3];
        textsGemTypes[3].pos.ty = positionsY[3];

        barsGemTypes[4].pos.ty = positionsY[4];
        textsGemTypes[4].pos.ty = positionsY[4];

        barsGemTypes[5].pos.ty = positionsY[5];
        textsGemTypes[5].pos.ty = positionsY[5];

        barsGemTypes[6].pos.ty = positionsY[6];
        textsGemTypes[6].pos.ty = positionsY[6];


        // calc min - max among collected gems        
        int[] realGems = scoreCounter.getScoreByGemTypesAsIntArray();
        int min = calcMin(realGems);
        int max = calcMax(realGems);
        System.out.println("Min : " + min + ", max: " + max);

        int[] altGems = realGems.clone();

        // adjust
        if (min != max) {
            int range = max - min;
            int delta = min - (range / 2);
            int len = altGems.length;
            for (int i = 0; i < len; ++i) {
                altGems[i] -= delta;
            }
        }

        // at this point values in array are between 0 and (max-min) value
        // recalc max and min values
        min = calcMin(altGems);
        max = calcMax(altGems);

        if (min == 0 && max == 0) {
            max = 10;
            int len = altGems.length;
            for (int i = 0; i < len; ++i) {
                altGems[i] = 1;
            }
        }

        // calc bars width
        final float wmax = 0.75f;
        final float h = 0.05f;
        final float xminus = 0.8f;
        float w;
        w = ((float)altGems[0] / (float)max) * wmax;
        barsGemTypes[0].init(scoreCounter.scoreByGemTypes.get(0).color, w, h);
        barsGemTypes[0].pos.tx = w - xminus;

        w = ((float)altGems[1] / (float)max) * wmax;
        barsGemTypes[1].init(scoreCounter.scoreByGemTypes.get(1).color, w, h);
        barsGemTypes[1].pos.tx = w - xminus;

        w = ((float)altGems[2] / (float)max) * wmax;
        barsGemTypes[2].init(scoreCounter.scoreByGemTypes.get(2).color, w, h);
        barsGemTypes[2].pos.tx = w - xminus;

        w = ((float)altGems[3] / (float)max) * wmax;
        barsGemTypes[3].init(scoreCounter.scoreByGemTypes.get(3).color, w, h);
        barsGemTypes[3].pos.tx = w - xminus;

        w = ((float)altGems[4] / (float)max) * wmax;
        barsGemTypes[4].init(scoreCounter.scoreByGemTypes.get(4).color, w, h);
        barsGemTypes[4].pos.tx = w - xminus;

        w = ((float)altGems[5] / (float)max) * wmax;
        barsGemTypes[5].init(scoreCounter.scoreByGemTypes.get(5).color, w, h);
        barsGemTypes[5].pos.tx = w - xminus;

        w = ((float)altGems[6] / (float)max) * wmax;
        barsGemTypes[6].init(scoreCounter.scoreByGemTypes.get(6).color, w, h);
        barsGemTypes[6].pos.tx = w - xminus;

        // setup texts
        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);        

        textsGemTypes[0].init("" + realGems[0], textColor, textColor, fontScale);
        textsGemTypes[0].pos.tx = barsGemTypes[0].pos.tx - textsGemTypes[0].getTextWidth() / 2f;

        textsGemTypes[1].init("" + realGems[1], textColor, textColor, fontScale);
        textsGemTypes[1].pos.tx = barsGemTypes[1].pos.tx - textsGemTypes[1].getTextWidth() / 2f;

        textsGemTypes[2].init("" + realGems[2], textColor, textColor, fontScale);
        textsGemTypes[2].pos.tx = barsGemTypes[2].pos.tx - textsGemTypes[2].getTextWidth() / 2f;

        textsGemTypes[3].init("" + realGems[3], textColor, textColor, fontScale);
        textsGemTypes[3].pos.tx = barsGemTypes[3].pos.tx - textsGemTypes[3].getTextWidth() / 2f;

        textsGemTypes[4].init("" + realGems[4], textColor, textColor, fontScale);
        textsGemTypes[4].pos.tx = barsGemTypes[4].pos.tx - textsGemTypes[4].getTextWidth() / 2f;

        textsGemTypes[5].init("" + realGems[5], textColor, textColor, fontScale);
        textsGemTypes[5].pos.tx = barsGemTypes[5].pos.tx - textsGemTypes[5].getTextWidth() / 2f;

        textsGemTypes[6].init("" + realGems[6], textColor, textColor, fontScale);
        textsGemTypes[6].pos.tx = barsGemTypes[6].pos.tx - textsGemTypes[6].getTextWidth() / 2f;
    }

    public void update(float offsetY) {
    	// TODO: calc scrolling offset and update positions
    	
    }

    public void draw() {    
        // draw bars
        graphics.colorShader.useProgram();
        glDisable(GL_BLEND);
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i].drawBatch();
        }

        // draw texts
        glEnable(GL_BLEND);
        graphics.textureShader.useProgram();
        graphics.textureShader.setTexture(Graphics.textureFonts);
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            textsGemTypes[i].draw();
        }    
    }

   	private int calcMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        int len = arr.length;
        
        for(int i = 0; i < len; ++i) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    private int calcMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        int len = arr.length;

        for(int i = 0; i < len; ++i) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }


}