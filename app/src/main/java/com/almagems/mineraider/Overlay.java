package com.almagems.mineraider;

public abstract class Overlay {
    public static Graphics graphics;
    
    protected float touchDownX;
    protected float touchDownY;

    protected final Color colorBackground = new Color(0f, 0f, 0f, 0.4f);
    protected final Fade background = new Fade();
}
