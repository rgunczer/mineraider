package com.almagems.mineraider;


public final class Fade extends EffectAnim {

    private final Color colorFrom;
    private final Color colorTo;
    public final Color colorCurrent;
    private float t;
    private float dt = 0.05f; // speed
    public boolean done = false;
    public final SingleColoredQuad singleColoredQuad;
    public final ColoredQuad coloredQuad;
    private boolean useSingleColoredQuad;

    // ctor
    public Fade() {
        System.out.println("Fade ctor...");
        colorFrom = new Color();
        colorTo = new Color();
        colorCurrent = new Color(colorFrom);
        singleColoredQuad = new SingleColoredQuad();
        coloredQuad = new ColoredQuad();
    }

    public void reset() {
        colorCurrent.init(colorFrom);
        done = false;
        t = 0f;
    }

    public void init(Color colorTop, Color colorBottom, float h) {
        useSingleColoredQuad = false;
        t = 0.0f;
        done = false;
        pos = posOrigin;

        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);

        coloredQuad.init(1f, h, colorTop, colorBottom);
    }

    public void init(Color from, Color to) {
        useSingleColoredQuad = true;

        colorFrom.init(from);
        colorTo.init(to);
        colorCurrent.init(from);
        t = 0.0f;
        done = false;
        pos = posOrigin;

        pos.trans(0f, 0f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);

        singleColoredQuad.init(colorFrom, 1f, Graphics.aspectRatio);
    }

    public void init(Color from, Color to, Rectangle rect) {
        useSingleColoredQuad = true;

        colorFrom.init(from);
        colorTo.init(to);
        colorCurrent.init(from);
        t = 0.0f;
        done = false;
        pos = posOrigin;

        singleColoredQuad.init(colorFrom, 1f, Graphics.aspectRatio);
    }

    @Override
    public void init(PositionInfo pos) {
    }

    @Override
    public void update() {
        if (useSingleColoredQuad) {
            if (!done) {
                t += dt;
                if (t > 1f) {
                    t = 1f;
                    done = true;
                }
                colorCurrent.r = MyUtils.lerp(colorFrom.r, colorTo.r, t);
                colorCurrent.g = MyUtils.lerp(colorFrom.g, colorTo.g, t);
                colorCurrent.b = MyUtils.lerp(colorFrom.b, colorTo.b, t);
                colorCurrent.a = MyUtils.lerp(colorFrom.a, colorTo.a, t);

                singleColoredQuad.color.init(colorCurrent);
                //System.out.println("Fade update... " + t + ", " + colorCurrent.toString());
            }
        }
    }

    public void draw() {
        if (useSingleColoredQuad) {
            singleColoredQuad.draw();
        } else {
            coloredQuad.draw();
        }
    }

}
