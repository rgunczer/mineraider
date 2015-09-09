package com.almagems.mineraider;

public final class MenuItem {
    private String name;
    private Quad quad;
    public Menu.MenuOptions menuOption;
    private MenuItemAnim anim;
    public String tag;
    public static Graphics graphics;

    public MenuItem() {
        quad = new Quad();
    }

    public Quad getQuad() {
        return quad;
    }

    public MenuItemAnim getAnim() {
        return anim;
    }

    public boolean isHit(float x, float y) {
        return quad.isHit(x, y);
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

    public void setScaleY(float y) { quad.pos.sy = y; }
    public float getScaleY() { return quad.pos.sy; }

    public void setAnim(MenuItemAnim anim) {
        this.anim = anim;
        this.anim.init(this);
    }

    public void init(String name, Menu.MenuOptions menuOption, int textureId, Color color, Rectangle rect, boolean flipUTextureCoordinates) {
        this.name = name;
        this.menuOption = menuOption;

        quad = new Quad();
        quad.init(textureId, color, rect, flipUTextureCoordinates);
    }

    public void update() {
        if (anim != null) {
            anim.update();
            if (anim.isDone()) {
                anim = null;
            }
        }
    }

    public void draw() {
        //System.out.println("MenuItem draw...");
        if (anim != null) {
            anim.draw();
        } else {
            quad.draw();
        }
    }
}
