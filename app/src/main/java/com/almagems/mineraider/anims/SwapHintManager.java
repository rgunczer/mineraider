package com.almagems.mineraider.anims;

import java.util.ArrayList;
import java.util.Random;

import com.almagems.mineraider.util.MyUtils;
import com.almagems.mineraider.visuals.Visuals;
import com.almagems.mineraider.GemPosition;

import static com.almagems.mineraider.Constants.MAX_GEM_TYPES;

public class SwapHintManager {

    private ArrayList<SwapHint> pool = new ArrayList<SwapHint>();
    private ArrayList<SwapHint> live = new ArrayList<SwapHint>();

    private final Visuals visuals;

    // ctor
    public SwapHintManager(Visuals visuals) {
        this.visuals = visuals;
    }

    public void reset() {
        int size = live.size();
        for(int i = 0; i < size; ++i) {
            pool.add( live.get(i) );
        }
        live.clear();
    }

    public boolean isEmpty() {
        return live.size() == 0;
    }
    public int count() {
        return live.size();
    }

    public void add(GemPosition first, GemPosition second) {
        SwapHint swapHint = getFromPool();
        swapHint.init(first, second);
        live.add(swapHint);
    }

    private SwapHint getFromPool() {
        int size = pool.size();
        if (size > 0) {
            SwapHint swapHint = pool.get( size - 1 );
            pool.remove( size - 1 );
            return swapHint;
        } else {
            return new SwapHint();
        }
    }

    public void draw() {
        visuals.hint.bindData(visuals.pointLightShader);

        SwapHint hint;
        int size = live.size();
        for(int i = 0; i < size; ++i) {
            hint = live.get(i);
            hint.update();
            visuals.calcMatricesForObject( hint.pos );
            visuals.pointLightShader.setUniforms();
            visuals.hint.draw();
        }
    }

    public void selectOneHint() {
        int index =  MyUtils.randInt(0, live.size() - 1);

        SwapHint swapHint = live.get(index);
        reset();
        add(swapHint.first, swapHint.second);
    }
}