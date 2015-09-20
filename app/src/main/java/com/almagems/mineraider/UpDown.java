package com.almagems.mineraider;

public final class UpDown extends EffectAnim {

    private float dy = 0.01f;
    private boolean flip = false;
    private int cooling = 0;


    // ctor
    public UpDown() {
    }

    @Override
    public void init(PositionInfo pos) {
        this.pos = pos;
        posOrigin.init(pos);
        cooling = 10;
    }

    @Override
    public void update() {
        --cooling;

        if (flip) {
            pos.ty = posOrigin.ty + dy;
        } else {
            pos.ty = posOrigin.ty - dy;
        }

        if (cooling % 2 == 0) {
            dy = 0.013f;

            if (cooling < 0) {
                flip = !flip;
                cooling = 3;
            }
        }
    }

}
