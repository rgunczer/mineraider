package com.almagems.mineraider;

import static com.almagems.mineraider.MyUtils.LERP;


public final class Fade extends EffectAnim {
    private final Color colorFrom;
    private final Color colorTo;
    public final Color colorCurrent;
    private float t;
    private float dt = 0.05f; // speed
    public boolean done = false;
    private final ColoredQuad coloredQuad;

    // ctor
    public Fade() {
        System.out.println("Fade ctor...");
        colorFrom = new Color();
        colorTo = new Color();
        colorCurrent = new Color(colorFrom);
        coloredQuad = new ColoredQuad();
    }

    public void init(Color from, Color to) {
        colorFrom.init(from);
        colorTo.init(to);
        colorCurrent.init(from);
        t = 0.0f;
        done = false;
        pos = posOrigin;

        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);

        coloredQuad.init(colorFrom, 1f, Graphics.aspectRatio);
    }

    public void init(Color from, Color to, Rectangle rect) {
        colorFrom.init(from);
        colorTo.init(to);
        colorCurrent.init(from);
        t = 0.0f;
        done = false;
        pos = posOrigin;

        coloredQuad.init(colorFrom, 1f, Graphics.aspectRatio);
    }

    @Override
    public void init(PositionInfo pos) {
    }

    @Override
    public void update() {
        if (!done) {
            t += dt;
            if (t > 1f) {
                t = 1f;
                done = true;
            }
            colorCurrent.r = LERP(colorFrom.r, colorTo.r, t);
            colorCurrent.g = LERP(colorFrom.g, colorTo.g, t);
            colorCurrent.b = LERP(colorFrom.b, colorTo.b, t);
            colorCurrent.a = LERP(colorFrom.a, colorTo.a, t);

            coloredQuad.color.init(colorCurrent);
            //System.out.println("Fade update... " + t + ", " + colorCurrent.toString());
        }
    }

    public void draw() {
        coloredQuad.draw();
    }

}
