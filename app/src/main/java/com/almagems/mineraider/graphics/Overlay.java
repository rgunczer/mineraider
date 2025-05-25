package com.almagems.mineraider.graphics;

import com.almagems.mineraider.Color;
import com.almagems.mineraider.effect.Fade;
import com.almagems.mineraider.Graphics;

public abstract class Overlay {
    public static Graphics graphics;
    
    protected float touchDownX;
    protected float touchDownY;

    protected final Color colorBackground = new Color(0f, 0f, 0f, 0.4f);
    protected final Fade background = new Fade();
}
