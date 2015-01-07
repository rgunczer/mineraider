package com.almagems.mineraider.anims;

import java.util.ArrayList;

import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.GemPosition;


public class SwapHintManager {

    private ArrayList<SwapHint> dead = new ArrayList<SwapHint>();
    private ArrayList<SwapHint> live = new ArrayList<SwapHint>();

    private final Visuals visuals;

    // ctor
    public SwapHintManager() {
        visuals = Visuals.getInstance();
    }

    public void reset() {
        int size = live.size();
        for(int i = 0; i < size; ++i) {
            dead.add( live.get(i) );
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
        SwapHint swapHint = getFromDead();
        swapHint.init(first, second);
        live.add(swapHint);
    }

    private SwapHint getFromDead() {
        int size = dead.size();
        if (size > 0) {
            SwapHint swapHint = dead.get( size - 1 );
            dead.remove( size - 1 );
            return swapHint;
        } else {
            return new SwapHint();
        }
    }

    public void draw() {
        SwapHint hint;
        visuals.dirLightShader.useProgram();
        visuals.dirLightShader.setTexture(visuals.textureHintArrow);

        int size = live.size();
        for(int i = 0; i < size; ++i) {
            hint = live.get(i);
            hint.update();
            visuals.calcMatricesForObject( hint.op );
            visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);
            visuals.hintMarker.bindData(visuals.dirLightShader);
            visuals.hintMarker.draw();
        }
    }
}