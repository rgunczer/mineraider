package com.almagems.mineraider;

import com.almagems.mineraider.anims.AnimationManager;
import com.almagems.mineraider.anims.BaseAnimation;
import com.almagems.mineraider.anims.FallAnimation;
import com.almagems.mineraider.anims.FallGroupAnimation;
import com.almagems.mineraider.anims.SwapAnimation;
import com.almagems.mineraider.objects.Model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.util.ArrayList;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.GEM_FRAGMENT_SIZE;
import static com.almagems.mineraider.Constants.GEM_TYPE_0;
import static com.almagems.mineraider.Constants.GEM_TYPE_1;
import static com.almagems.mineraider.Constants.GEM_TYPE_2;
import static com.almagems.mineraider.Constants.GEM_TYPE_3;
import static com.almagems.mineraider.Constants.GEM_TYPE_4;
import static com.almagems.mineraider.Constants.GEM_TYPE_5;
import static com.almagems.mineraider.Constants.GEM_TYPE_6;
import static com.almagems.mineraider.Constants.GEM_TYPE_NONE;

public class BatchDrawer {
    public ArrayList<PositionInfo> gemsType0 = new ArrayList<PositionInfo>(90);
    public ArrayList<PositionInfo> gemsType1 = new ArrayList<PositionInfo>(90);
    public ArrayList<PositionInfo> gemsType2 = new ArrayList<PositionInfo>(90);
    public ArrayList<PositionInfo> gemsType3 = new ArrayList<PositionInfo>(90);
    public ArrayList<PositionInfo> gemsType4 = new ArrayList<PositionInfo>(90);
    public ArrayList<PositionInfo> gemsType5 = new ArrayList<PositionInfo>(90);
    public ArrayList<PositionInfo> gemsType6 = new ArrayList<PositionInfo>(90);

    private static final int MAX_POOL_GEMPOSITIONS = 630;
    private ArrayList<PositionInfo> pool = new ArrayList<PositionInfo>(MAX_POOL_GEMPOSITIONS);

    private final Visuals visuals;
    private PositionInfo pos = new PositionInfo();

    // ctor
    public BatchDrawer() {
        visuals = Visuals.getInstance();

        for (int i = 0; i < MAX_POOL_GEMPOSITIONS; ++i) {
            pool.add( new PositionInfo() );
        }
    }

    private PositionInfo getFromPool() {
        PositionInfo pos;
        int size = pool.size();
        //if (size > 0) {
            pos = pool.get( size - 1 );
            pool.remove( size - 1 );
        //} /*else {
            //pos = new PositionInfo();
        //}
        return pos;
    }

    private void recycle(ArrayList<PositionInfo> list) {
        int size = list.size();
        for(int i = size - 1; i > -1; --i) {
            pool.add( list.get(i) );
        }
    }

    public void begin() {
        recycle(gemsType0);
        recycle(gemsType1);
        recycle(gemsType2);
        recycle(gemsType3);
        recycle(gemsType4);
        recycle(gemsType5);
        recycle(gemsType6);

        gemsType0.clear();
        gemsType1.clear();
        gemsType2.clear();
        gemsType3.clear();
        gemsType4.clear();
        gemsType5.clear();
        gemsType6.clear();
    }

    public void add(Match3 match3) {
        PositionInfo pos;
        GemPosition gp;
        int size = match3.gemsList.size();
        for(int i = 0; i < size; ++i) {
            gp = match3.gemsList.get(i);
            pos = getFromPool();
            pos.init(gp.pos);
            sortItem(pos, gp.type, gp.visible);
        }
    }

    public void add(AnimationManager animManager) {
        if (!animManager.isDone()) {
            PositionInfo pos1;
            PositionInfo pos2;
            BaseAnimation baseAnim = animManager.running;
            if (baseAnim instanceof SwapAnimation) {
                SwapAnimation anim = (SwapAnimation)baseAnim;

                pos1 = getFromPool();
                pos2 = getFromPool();
                pos1.init(anim.firstAnim.pos);
                pos2.init(anim.secondAnim.pos);
                sortItem(pos1, anim.firstAnim.type, true);
                sortItem(pos2, anim.secondAnim.type, true);
                return;
            }

            if (baseAnim instanceof FallGroupAnimation) {
                PositionInfo pos;
                FallGroupAnimation anim = (FallGroupAnimation)baseAnim;
                FallAnimation fall;
                int size = anim.count();
                for (int i = 0; i < size; ++i) {
                    fall = anim.getAnimAt(i);
                    pos = getFromPool();
                    pos.init(fall.animGemFrom.pos);
                    sortItem(pos, fall.animGemFrom.type, true);
                }
            }

//            if (baseAnim instanceof PopAnimation) {
//            }
        }
    }

    public void add(Physics physics) {
        int gemType;
        PositionInfo position;
        Body body;
        Vec2 pos;
        float degree;
        float d = GEM_FRAGMENT_SIZE;
        int size = physics.fragments.size();
        for(int i = 0; i < size; ++i) {
            body = physics.fragments.get(i);
            pos = body.getPosition();
            degree = (float) Math.toDegrees( body.getAngle() );
            gemType = (Integer)body.m_userData;
            position = getFromPool();
            position.trans(pos.x, pos.y, 1.0f);
            position.rot(0f, 0f, degree);
            position.scale(d, d, 1f);
            sortItem(position, gemType, true);
        }
    }

    public void drawAll() {
        glEnable(GL_BLEND);
        drawGemsPlatesByType(gemsType0, GEM_TYPE_0);
        drawGemsPlatesByType(gemsType1, GEM_TYPE_1);
        drawGemsPlatesByType(gemsType2, GEM_TYPE_2);
        drawGemsPlatesByType(gemsType3, GEM_TYPE_3);
        drawGemsPlatesByType(gemsType4, GEM_TYPE_4);
        drawGemsPlatesByType(gemsType5, GEM_TYPE_5);
        drawGemsPlatesByType(gemsType6, GEM_TYPE_6);

        glDisable(GL_BLEND);
        drawGemsByType(gemsType0, GEM_TYPE_0);
        drawGemsByType(gemsType1, GEM_TYPE_1);
        drawGemsByType(gemsType2, GEM_TYPE_2);
        drawGemsByType(gemsType3, GEM_TYPE_3);
        drawGemsByType(gemsType4, GEM_TYPE_4);
        drawGemsByType(gemsType5, GEM_TYPE_5);
        drawGemsByType(gemsType6, GEM_TYPE_6);
    }

    public void sortItem(PositionInfo pos, int type, boolean visible) {
        if (visible && type != GEM_TYPE_NONE && pos.tx < 14.5f) {
            switch (type) {
                case GEM_TYPE_0:
                    gemsType0.add(pos);
                    break;
                case GEM_TYPE_1:
                    gemsType1.add(pos);
                    break;
                case GEM_TYPE_2:
                    gemsType2.add(pos);
                    break;
                case GEM_TYPE_3:
                    gemsType3.add(pos);
                    break;
                case GEM_TYPE_4:
                    gemsType4.add(pos);
                    break;
                case GEM_TYPE_5:
                    gemsType5.add(pos);
                    break;
                case GEM_TYPE_6:
                    gemsType6.add(pos);
                    break;
            }
        } else {
            pool.add(pos);
        }
    }

    void drawGemsByType(ArrayList<PositionInfo> gems, int type) {

        int size = gems.size();
        if (size > 0) {
            PositionInfo pos;
            Model model = visuals.gems[type];
            model.bindData(visuals.pointLightShader);

            for (int i = 0; i < size; ++i) {
                pos = gems.get(i);
                visuals.calcMatricesForObject(pos);
                visuals.pointLightShader.setUniforms();
                model.draw();
            }
        }

    }

    void drawGemsPlatesByType(ArrayList<PositionInfo> gems, int type) {



        int size = gems.size();
        if (size > 0) {

            visuals.colorShader.useProgram();

            float temp;
            PositionInfo pos;
            Model model = visuals.gemsPlates[type];
            model.bindData(visuals.colorShader);

            for (int i = 0; i < size; ++i) {
                pos = gems.get(i);
                temp = pos.tz;
                pos.tz -= 0.11f;
                visuals.calcMatricesForObject(pos);
                visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.blackColor);
                model.draw();
                pos.tz = temp;
            }

            visuals.pointLightShader.useProgram();
        }

    }
}
