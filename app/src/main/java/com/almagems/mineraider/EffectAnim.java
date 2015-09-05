package com.almagems.mineraider;

import com.almagems.mineraider.PositionInfo;

public abstract class EffectAnim {

    public PositionInfo posOrigin = new PositionInfo();
    public PositionInfo pos;

    public abstract void init(PositionInfo pos);
    public abstract void update();

}
