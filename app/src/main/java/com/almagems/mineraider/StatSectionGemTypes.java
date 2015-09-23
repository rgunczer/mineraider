package com.almagems.mineraider;

import java.util.ArrayList;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class StatSectionGemTypes extends StatSectionBase {

    private final BatchGemDrawer batchGemDrawer;

    private final Text[] textsGemTypes;
    private final SingleColoredQuad[] barsGemTypes;
    private final float[] positionsY;
    private final ArrayList<GemPosition> list;
    private final ArrayList<Color> gemColors;
    final float fontScale = 0.9f;
    private static final String titleText = "MATCHED GEMS";


    // ctor
    public StatSectionGemTypes() {    	
    	//System.out.println("StatSectionGemTypes ctor...");

        final int maxItemCountPerGemType = 1;
        batchGemDrawer = new BatchGemDrawer(maxItemCountPerGemType);
        positionsY = new float[MAX_GEM_TYPES];
        textsGemTypes = new Text[MAX_GEM_TYPES];
        barsGemTypes = new SingleColoredQuad[MAX_GEM_TYPES];

        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            barsGemTypes[i] = new SingleColoredQuad();
            textsGemTypes[i] = new Text();
        }

        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            textsGemTypes[i].init("00000000", Color.WHITE, Color.GRAY, fontScale);
        }

        list = new ArrayList<GemPosition>(MAX_GEM_TYPES);
        gemColors = new ArrayList<Color>(MAX_GEM_TYPES);

        // set gem type colors
        Color color;
        color = new Color(188,  38,  38, 255); gemColors.add(color);
        color = new Color(197,  94, 124, 255); gemColors.add(color);
        color = new Color(194, 150,  76, 255); gemColors.add(color);
        color = new Color( 26,  61, 186, 255); gemColors.add(color);
        color = new Color(193, 102, 193, 255); gemColors.add(color);
        color = new Color(196, 123,  99, 255); gemColors.add(color);
        color = new Color(172, 152, 158, 255); gemColors.add(color);

        textTitle.init(titleText, Color.YELLOW, Color.WHITE, 1.6f);

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
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            textsGemTypes[i].updateText("" + gems.get(i).value, Color.WHITE, Color.GRAY, fontScale);
        }

        int min = gems.get( gems.size() - 1 ).value;
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
        final float xminus = wmax - 0.01f;
        float w;

        w = ((float)gems.get(0).value / (float)max) * wmax;
        barsGemTypes[0].init(gemColors.get(scoreCounter.scoreByGemTypes.get(0).type), w, h);
        barsGemTypes[0].pos.tx = w - xminus;

        w = ((float)gems.get(1).value / (float)max) * wmax;
        barsGemTypes[1].init(gemColors.get(scoreCounter.scoreByGemTypes.get(1).type), w, h);
        barsGemTypes[1].pos.tx = w - xminus;

        w = ((float)gems.get(2).value / (float)max) * wmax;
        barsGemTypes[2].init(gemColors.get(scoreCounter.scoreByGemTypes.get(2).type), w, h);
        barsGemTypes[2].pos.tx = w - xminus;

        w = ((float)gems.get(3).value / (float)max) * wmax;
        barsGemTypes[3].init(gemColors.get(scoreCounter.scoreByGemTypes.get(3).type), w, h);
        barsGemTypes[3].pos.tx = w - xminus;

        w = ((float)gems.get(4).value / (float)max) * wmax;
        barsGemTypes[4].init(gemColors.get(scoreCounter.scoreByGemTypes.get(4).type), w, h);
        barsGemTypes[4].pos.tx = w - xminus;

        w = ((float)gems.get(5).value / (float)max) * wmax;
        barsGemTypes[5].init(gemColors.get(scoreCounter.scoreByGemTypes.get(5).type), w, h);
        barsGemTypes[5].pos.tx = w - xminus;

        w = ((float)gems.get(6).value / (float)max) * wmax;
        barsGemTypes[6].init(gemColors.get(scoreCounter.scoreByGemTypes.get(6).type), w, h);
        barsGemTypes[6].pos.tx = w - xminus;

        // title
        textTitle.updateText(titleText, Color.YELLOW, Color.WHITE, 1.6f);
        textTitle.pos.tx = -textTitle.getTextWidth() / 2.0f;
        textTitle.pos.ty = 1f;

        // text x positions
        textsGemTypes[0].pos.tx = xpos;
        textsGemTypes[1].pos.tx = xpos;
        textsGemTypes[2].pos.tx = xpos;
        textsGemTypes[3].pos.tx = xpos;
        textsGemTypes[4].pos.tx = xpos;
        textsGemTypes[5].pos.tx = xpos;
        textsGemTypes[6].pos.tx = xpos;

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
        graphics.singleColorShader.useProgram();
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