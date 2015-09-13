package com.almagems.mineraider;

import java.util.ArrayList;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class StatSectionGemTypes extends StatSectionBase {

    public static final Quad gemImages = new Quad();

    private final BatchGemDrawer batchGemDrawer;

    private final Text[] textsGemTypes;
    private final ColoredQuad[] barsGemTypes;
    private final float[] positionsY;
    private final ArrayList<GemPosition> list;

    // ctor
    public StatSectionGemTypes() {    	
    	System.out.println("StatSectionGemTypes ctor...");

    	scoreCounter = null;

        final int maxItemCountPerGemType = 1;
        batchGemDrawer = new BatchGemDrawer(maxItemCountPerGemType);
        positionsY = new float[MAX_GEM_TYPES];
        textsGemTypes = new Text[MAX_GEM_TYPES];
        barsGemTypes = new ColoredQuad[MAX_GEM_TYPES];

        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i] = new ColoredQuad();
            textsGemTypes[i] = new Text();
        }

        list = new ArrayList<GemPosition>(MAX_GEM_TYPES);
    }

    public void init() {
        float y = 0.75f;
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
        ArrayList<ScoreByGemType> gems = new ArrayList<ScoreByGemType>(MAX_GEM_TYPES);
        ArrayList<ScoreByGemType> original = scoreCounter.getScoreByGemTypesAsIntArray();
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            ScoreByGemType scoreByGemType = new ScoreByGemType();
            scoreByGemType.type = original.get(i).type;
            scoreByGemType.value = original.get(i).value;
            gems.add(scoreByGemType);
        }

        // texts
        final float fontScale = 0.9f;
        final Color textColor = new Color(Color.WHITE);
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            textsGemTypes[i].init("" + gems.get(i).value, textColor, textColor, fontScale);
        }

        int min = gems.get( gems.size() - 1).value;
        int max = gems.get( 0 ).value;
        //System.out.println("Min : " + min + ", max: " + max);

        // adjust
        if (min != max) {
            int range = max - min;
            int delta = min - range / 2; //(int)((float)range * 0.1f);
            for (int i = 0; i < MAX_GEM_TYPES; ++i) {
                gems.get(i).value -= delta;
            }
        }

        // at this point values in array are between 0 and (max-min) value
        // recalc max and min values
        min = gems.get( gems.size() - 1).value;
        max = gems.get( 0 ).value;

        if (min == 0 && max == 0) {
            max = 10;
            for (int i = 0; i < MAX_GEM_TYPES; ++i) {
                gems.get(i).value = 1;
            }
        }

        // bars
        final float xpos = -0.6f;
        final float wmax = 0.65f;
        final float h = 0.05f;
        final float xminus = wmax;
        float w;

        w = ((float)gems.get(0).value / (float)max) * wmax;
        barsGemTypes[0].init(scoreCounter.scoreByGemTypes.get(0).color, w, h);
        barsGemTypes[0].pos.tx = w - xminus;

        w = ((float)gems.get(1).value / (float)max) * wmax;
        barsGemTypes[1].init(scoreCounter.scoreByGemTypes.get(1).color, w, h);
        barsGemTypes[1].pos.tx = w - xminus;

        w = ((float)gems.get(2).value / (float)max) * wmax;
        barsGemTypes[2].init(scoreCounter.scoreByGemTypes.get(2).color, w, h);
        barsGemTypes[2].pos.tx = w - xminus;

        w = ((float)gems.get(3).value / (float)max) * wmax;
        barsGemTypes[3].init(scoreCounter.scoreByGemTypes.get(3).color, w, h);
        barsGemTypes[3].pos.tx = w - xminus;

        w = ((float)gems.get(4).value / (float)max) * wmax;
        barsGemTypes[4].init(scoreCounter.scoreByGemTypes.get(4).color, w, h);
        barsGemTypes[4].pos.tx = w - xminus;

        w = ((float)gems.get(5).value / (float)max) * wmax;
        barsGemTypes[5].init(scoreCounter.scoreByGemTypes.get(5).color, w, h);
        barsGemTypes[5].pos.tx = w - xminus;

        w = ((float)gems.get(6).value / (float)max) * wmax;
        barsGemTypes[6].init(scoreCounter.scoreByGemTypes.get(6).color, w, h);
        barsGemTypes[6].pos.tx = w - xminus;

        // title
        textTitle.init("MATCHED GEMS", new Color(1f, 1f, 0f), new Color(1f, 1f, 1f), 1.6f);
        textTitle.pos.tx = -textTitle.getTextWidth() / 2.0f;
        textTitle.pos.ty = 1f;

        // text x positions
        textsGemTypes[0].pos.tx = xpos; // barsGemTypes[0].pos.tx - textsGemTypes[0].getTextWidth() / 2f;
        textsGemTypes[1].pos.tx = xpos; // barsGemTypes[1].pos.tx - textsGemTypes[1].getTextWidth() / 2f;
        textsGemTypes[2].pos.tx = xpos; // barsGemTypes[2].pos.tx - textsGemTypes[2].getTextWidth() / 2f;
        textsGemTypes[3].pos.tx = xpos; // barsGemTypes[3].pos.tx - textsGemTypes[3].getTextWidth() / 2f;
        textsGemTypes[4].pos.tx = xpos; // barsGemTypes[4].pos.tx - textsGemTypes[4].getTextWidth() / 2f;
        textsGemTypes[5].pos.tx = xpos; // barsGemTypes[5].pos.tx - textsGemTypes[5].getTextWidth() / 2f;
        textsGemTypes[6].pos.tx = xpos; // barsGemTypes[6].pos.tx - textsGemTypes[6].getTextWidth() / 2f;

        // gems images
        GemPosition gp;
        list.clear();
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            gp = new GemPosition();
            gp.type = scoreCounter.scoreByGemTypes.get(i).type;
            gp.pos.tx = -0.8f;
            gp.pos.ty = positionsY[i];
            gp.posYorigin = gp.pos.ty;
            gp.pos.scale(0.075f, 0.075f, 1f);
            list.add(gp);
        }

        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i].posYorigin = barsGemTypes[i].pos.ty;
            textsGemTypes[i].posYorigin = textsGemTypes[i].pos.ty;
        }

        textTitle.posYorigin = textTitle.pos.ty;
    }

    public void update(float offsetY) {
        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i].pos.ty = barsGemTypes[i].posYorigin + offsetY;
            textsGemTypes[i].pos.ty = textsGemTypes[i].posYorigin + offsetY;
            list.get(i).pos.ty = list.get(i).posYorigin + offsetY;
        }

        textTitle.pos.ty = textTitle.posYorigin + offsetY;
    }

    public void draw() {

        // gems
        graphics.setProjectionMatrix2D();
        graphics.updateViewProjMatrix();

        /*
        glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_ONE, GL_ZERO);
        //glBlendFuncSeparate(GL_ONE, GL_ZERO, GL_ONE, GL_ZERO);
        //glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_COLOR);
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_ZERO, GL_ONE);

        glDisable(GL_BLEND);
        gemImages.pos.scale(0.1f, 0.1f, 1f);
        gemImages.draw();
        glDepthMask(true);
*/
        glDepthMask(false);

        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);

        // draw gems
        graphics.dirLightShader.useProgram();
        graphics.dirLightShader.setTexture(Graphics.textureGems);
        batchGemDrawer.begin();
        batchGemDrawer.add(list);
        batchGemDrawer.drawAll();


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
        textTitle.draw();

        glDepthMask(true);
    }


}