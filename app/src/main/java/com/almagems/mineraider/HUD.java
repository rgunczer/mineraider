package com.almagems.mineraider;

import com.almagems.mineraider.objects.GemIkon;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Text;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;


public class HUD {
    private int cachedScore;

    private final Text scoreText;
    private final Text extraText;
    private final GemIkon ikon;
    private final Quad quad;

    // ctor
    public HUD() {
        scoreText = new Text();
        extraText = new Text();
        ikon = new GemIkon();
        quad = new Quad();
    }

    public void init() {
        scoreText.init(-0.8f, -Visuals.aspectRatio, "SCORE:" + cachedScore, new MyColor(1f, 1f, 0f, 1f), 0.5f);
        extraText.init(-0.5f, -0.5f, "WATCH FOR MINECARTS", new MyColor(0.6f, 0.6f, 0.6f, 1f), 0.75f);

        float textWidth = extraText.getTextWidth();
        System.out.println("text width is: " + textWidth);

        extraText.pos.x = -textWidth / 2f;

        ikon.init();

        float scale = Visuals.aspectRatio * 0.1f;

        quad.init(Visuals.getInstance().textureIkon, new MyColor(1f, 1f, 1f, 1f));
        quad.op.setPosition(0f, Visuals.aspectRatio - scale, 0f);
        quad.op.setRot(0f, 0f, 0f);
        quad.op.setScale(scale, scale, 1f);
    }

    public void updateScore(int score) {
        if (score != cachedScore) {
            String str = "SCORE:" + score;
            scoreText.setSpacingScale(0.05f);
            scoreText.init(-0.8f, -Visuals.aspectRatio - 0.02f, str, new MyColor(1f, 1f, 0f, 1f), 0.5f);
            cachedScore = score;
        }
    }

    public void update() {
        ikon.update();
        scoreText.update();
        quad.update();
    }

    public void draw() {
        Visuals visuals = Visuals.getInstance();
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        visuals.textureShader.useProgram();

        ikon.draw();
        visuals.textureShader.setTexture(visuals.textureFonts);
        scoreText.draw();
        extraText.draw();

        quad.draw();
    }
}
