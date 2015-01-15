package com.almagems.mineraider.EffectAnims;

import com.almagems.mineraider.ObjectPosition;

public abstract class EffectAnim {

    public ObjectPosition posOrigin = new ObjectPosition();
    public ObjectPosition pos;

    public abstract void init(ObjectPosition pos);
    public abstract void update();

}
