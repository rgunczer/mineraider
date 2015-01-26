package com.almagems.mineraider.EffectAnims;

import com.almagems.mineraider.PositionInfo;

public class WahWah extends EffectAnim {

    private float elapsed;
    private float d;
    public float wahScale = 0.095f;

    // ctor
    public WahWah() {

    }

    @Override
    public void init(PositionInfo pos) {
        this.pos = pos;
        posOrigin.init(pos);
        elapsed = 0f;
        d = 0f;
    }

    @Override
    public void update() {
        elapsed += 0.9f;
        d = (((float)Math.sin(elapsed) + 1f) / 2f) * wahScale; // 0.095f; // for combo
        //d = (((float)Math.sin(elapsed) + 1f) / 2f) * 0.2f;
        pos.scale(1f, 1f + d, 1f);
    }
}
