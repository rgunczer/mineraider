package com.almagems.mineraider.EffectAnims;

import com.almagems.mineraider.ObjectPosition;

public abstract class EffectAnim {

    public ObjectPosition posOrigin;
    public ObjectPosition pos;

    public abstract void init(ObjectPosition pos);
    public abstract void update();

}
