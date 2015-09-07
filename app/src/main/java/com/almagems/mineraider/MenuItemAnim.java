package com.almagems.mineraider;

public final class MenuItemAnim {
    public MenuItem menuItem;
    private final float menuAnimSpeed = 0.025f;
    private float menuAnimStartValue;
    private float menuAnimCurrentValue;
    private float menuAnimEndValue;
    private boolean done;

    public MenuItemAnim() {

    }

    public boolean isDone() {
        return done;
    }

    public void init(MenuItem menuItem) {
        done = false;
        this.menuItem = menuItem;
        menuAnimStartValue = menuItem.getScaleY();
        menuAnimCurrentValue = menuAnimStartValue;
        menuAnimEndValue = menuAnimCurrentValue + 0.1f;
    }

    public void update() {
        if (!done) {
            menuAnimCurrentValue += menuAnimSpeed;

            if (menuAnimCurrentValue > menuAnimEndValue) {
                done = true;
                menuItem.setScaleY(menuAnimStartValue);
            }
        }
    }

    public void draw() {
        menuItem.setScaleY(menuAnimCurrentValue);
        menuItem.getQuad().draw();
    }
}
