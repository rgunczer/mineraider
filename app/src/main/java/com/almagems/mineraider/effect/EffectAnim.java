package com.almagems.mineraider.effect;

import com.almagems.mineraider.Graphics;
import com.almagems.mineraider.PositionInfo;

public abstract class EffectAnim {

    public static Graphics graphics;

    public PositionInfo posOrigin = new PositionInfo();
    public PositionInfo pos;

    public abstract void init(PositionInfo pos);
    public abstract void update();

}
