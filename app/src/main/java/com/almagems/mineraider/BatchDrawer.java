package com.almagems.mineraider;

import com.almagems.mineraider.anims.AnimationManager;
import com.almagems.mineraider.anims.BaseAnimation;
import com.almagems.mineraider.anims.FallAnimation;
import com.almagems.mineraider.anims.FallGroupAnimation;
import com.almagems.mineraider.anims.PopAnimation;
import com.almagems.mineraider.anims.SwapAnimation;
import com.almagems.mineraider.objects.Model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
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
    public ArrayList<ObjectPosition> gemsType0 = new ArrayList<ObjectPosition>(90);
    public ArrayList<ObjectPosition> gemsType1 = new ArrayList<ObjectPosition>(90);
    public ArrayList<ObjectPosition> gemsType2 = new ArrayList<ObjectPosition>(90);
    public ArrayList<ObjectPosition> gemsType3 = new ArrayList<ObjectPosition>(90);
    public ArrayList<ObjectPosition> gemsType4 = new ArrayList<ObjectPosition>(90);
    public ArrayList<ObjectPosition> gemsType5 = new ArrayList<ObjectPosition>(90);
    public ArrayList<ObjectPosition> gemsType6 = new ArrayList<ObjectPosition>(90);

    private static final int MAX_POOL_GEMPOSITIONS = 630;
    private ArrayList<ObjectPosition> pool = new ArrayList<ObjectPosition>(MAX_POOL_GEMPOSITIONS);

    private final Visuals visuals;
    private ObjectPosition pos = new ObjectPosition();

    // ctor
    public BatchDrawer() {
        visuals = Visuals.getInstance();

        for (int i = 0; i < MAX_POOL_GEMPOSITIONS; ++i) {
            pool.add( new ObjectPosition() );
        }
    }

    private ObjectPosition getFromPool() {
        ObjectPosition pos;
        int size = pool.size();
        //if (size > 0) {
            pos = pool.get( size - 1 );
            pool.remove( size - 1 );
        //} /*else {
            //pos = new ObjectPosition();
        //}
        return pos;
    }

    private void recycle(ArrayList<ObjectPosition> list) {
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
        ObjectPosition pos;
        GemPosition gp;
        int size = match3.gemsList.size();
        for(int i = 0; i < size; ++i) {
            gp = match3.gemsList.get(i);
            pos = getFromPool();
            pos.init(gp.op);
            sortItem(pos, gp.type, gp.visible);
        }
    }

    public void add(AnimationManager animManager) {
        if (!animManager.isDone()) {
            ObjectPosition pos1;
            ObjectPosition pos2;
            BaseAnimation baseAnim = animManager.running;
            if (baseAnim instanceof SwapAnimation) {
                SwapAnimation anim = (SwapAnimation)baseAnim;

                pos1 = getFromPool();
                pos2 = getFromPool();
                pos1.init(anim.firstAnim.op);
                pos2.init(anim.secondAnim.op);
                sortItem(pos1, anim.firstAnim.type, true);
                sortItem(pos2, anim.secondAnim.type, true);
                return;
            }

            if (baseAnim instanceof FallGroupAnimation) {
                ObjectPosition pos;
                FallGroupAnimation anim = (FallGroupAnimation)baseAnim;
                FallAnimation fall;
                int size = anim.count();
                for (int i = 0; i < size; ++i) {
                    fall = anim.getAnimAt(i);
                    pos = getFromPool();
                    pos.init(fall.animGemFrom.op);
                    sortItem(pos, fall.animGemFrom.type, true);
                }
            }

//            if (baseAnim instanceof PopAnimation) {
//            }
        }
    }

    public void add(Physics physics) {
        int gemType;
        ObjectPosition position;
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
            position.setPosition(pos.x, pos.y, 1.0f);
            position.setRot(0f, 0f, degree);
            position.setScale(d, d, 1f);
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

    public void sortItem(ObjectPosition pos, int type, boolean visible) {
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

    void drawGemsByType(ArrayList<ObjectPosition> gems, int type) {
        int size = gems.size();
        if (size > 0) {
            ObjectPosition pos;
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

    void drawGemsPlatesByType(ArrayList<ObjectPosition> gems, int type) {
        int size = gems.size();
        if (size > 0) {
            float temp;
            ObjectPosition pos;
            Model model = visuals.gemsPlates[type];
            model.bindData(visuals.pointLightShader);

            for (int i = 0; i < size; ++i) {
                pos = gems.get(i);
                temp = pos.tz;
                pos.tz -= 0.11f;
                visuals.calcMatricesForObject(pos);
                visuals.pointLightShader.setUniforms();
                model.draw();
                pos.tz = temp;
            }
        }
    }
}
