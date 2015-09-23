package com.almagems.mineraider;


public final class ZigZag extends EffectAnim {

    private float dx = 0.02f;
    private float dy = 0.02f;
    private boolean flip = false;
    private int cooling = 0;


    // ctor
    public ZigZag() {

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
            pos.tx = posOrigin.tx - dx;
        } else {
            pos.ty = posOrigin.ty - dy;
            pos.tx = posOrigin.tx + dx;
        }

        if (cooling % 2 == 0) {
            dx = MyUtils.rand.nextFloat() * 0.01f;
            dy = MyUtils.rand.nextFloat() * 0.01f;

            if (cooling < 0) {
                flip = !flip;
                cooling = 3;
            }
        }
    }
}
