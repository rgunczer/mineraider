package com.almagems.mineraider.menu;

import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;

public class MenuImage {

    private String name;
    private Quad quad;

    public void init(String name, int textureId, MyColor color, Rectangle rect, boolean flipUTextureCoordinates) {
        this.name = name;

        quad = new Quad();
        quad.init(textureId, color, rect, flipUTextureCoordinates);
    }

    public void setTrans(float x, float y, float z) {
        quad.pos.trans(x, y, z);
    }

    public void setRot(float x, float y, float z) {
        quad.pos.rot(x, y, z);
    }

    public void setScale(float x, float y, float z) {
        quad.pos.scale(x, y, z);
    }

    public void update() {

    }

    public void draw() {
        //System.out.println("MenuImage draw...");
        quad.draw2();
    }
}
