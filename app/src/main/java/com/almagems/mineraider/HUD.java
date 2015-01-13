package com.almagems.mineraider;

import com.almagems.mineraider.objects.EdgeDrawer;
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
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;


public class HUD {
    private int cachedScore;

    private final Text scoreText;
    private final Text extraText;
    private final GemIkon ikon;
    private final Quad quad;
    private final EdgeDrawer edgeDrawer;

    private float scoreX;
    private float scoreY;
    private float fontScale;

    // ctor
    public HUD() {
        scoreText = new Text();
        extraText = new Text();
        ikon = new GemIkon();
        quad = new Quad();
        edgeDrawer = new EdgeDrawer(32);

    }

    public void init() {
        fontScale = 0.9f;
        scoreText.setSpacingScale(0.06f);
        scoreText.init("SCORE:" + cachedScore, new MyColor(1f, 1f, 0f, 1f), fontScale);

        scoreX = -0.8f;
        scoreY = -Visuals.aspectRatio + (scoreText.getTextHeight() / 3f);
        scoreText.pos.x = scoreX;
        scoreText.pos.y = scoreY;

        extraText.setSpacingScale(0.065f);
        extraText.init("WATCH FOR MINECARTS", new MyColor(0.6f, 0.6f, 0.6f, 1f), 1.4f);
        extraText.pos.x = 0.0f;
        extraText.pos.y = -0.5f;

        float textWidth = extraText.getTextWidth();
        //System.out.println("text width is: " + textWidth);
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
            scoreText.init(str, new MyColor(1f, 1f, 0f, 1f), fontScale);
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
/*
        MyColor color = new MyColor(1f, 1f, 0f, 1f);

        edgeDrawer.begin();
        edgeDrawer.addLine(	-1f, 0f, 0f,
                             1f, 0f, 0f);

        edgeDrawer.addLine( 0f, -1f, 0f,
                            0f,  1f, 0f);

        setIdentityM(visuals.modelMatrix, 0);
        multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);

        visuals.colorShader.useProgram();
        visuals.colorShader.setUniforms(visuals.mvpMatrix, color);
        edgeDrawer.bindData(visuals.colorShader);
        edgeDrawer.draw();
*/
    }
}
