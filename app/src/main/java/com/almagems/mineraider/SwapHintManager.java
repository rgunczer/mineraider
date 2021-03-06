package com.almagems.mineraider;

import java.util.ArrayList;

public final class SwapHintManager {

    private ArrayList<SwapHint> pool = new ArrayList<SwapHint>(20);
    private ArrayList<SwapHint> live = new ArrayList<SwapHint>(20);

    private final Graphics graphics;

    // ctor
    public SwapHintManager(Graphics graphics) {
        this.graphics = graphics;
    }

    public void reset() {
        final int size = live.size();
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
        final int size = pool.size();
        if (size > 0) {
            SwapHint swapHint = pool.get( size - 1 );
            pool.remove( size - 1 );
            return swapHint;
        } else {
            return new SwapHint();
        }
    }

    public void draw() {
        SwapHint hint;
        final int size = live.size();
        for(int i = 0; i < size; ++i) {
            hint = live.get(i);
            hint.update();
            graphics.calcMatricesForObject(hint.pos);
            graphics.pointLightShader.setUniforms();
            graphics.hint.draw();
        }
    }

    public void selectOneHint() {
        final int index =  MyUtils.randInt(0, live.size() - 1);

        SwapHint swapHint = live.get(index);
        reset();
        add(swapHint.first, swapHint.second);
    }
}