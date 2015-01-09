package com.almagems.mineraider;

import java.util.ArrayList;

public class HUD {

    private final ArrayList<Font> scoreFonts = new ArrayList<Font>(12);

    // ctor
    public HUD() {
        System.out.println("HUD ctor...");
    }

    public void init() {

    }

    public void update() {

    }

    public void draw() {
        // TODO: draw score...
        // TODO: draw gem icons...
        Font font;
        int size = scoreFonts.size();
        for(int i = 0; i < size; ++i) {
            font = scoreFonts.get(i);
        }

    }
}
