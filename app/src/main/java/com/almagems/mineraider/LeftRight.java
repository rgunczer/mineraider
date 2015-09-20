package com.almagems.mineraider;

public class LeftRight extends EffectAnim {

    private float dy = 0.01f;
    private boolean flip = false;
    private int cooling = 0;


    // ctor
    public LeftRight() {
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
            pos.tx = posOrigin.tx + dy;
        } else {
            pos.tx = posOrigin.tx - dy;
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
