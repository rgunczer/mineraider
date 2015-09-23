package com.almagems.mineraider;

public final class Rectangle {

    public float x;
    public float y;
    public float w;
    public float h;

    // ctor
    public Rectangle() {
        x = 0f;
        y = 0f;
        w = 1f;
        h = 1;
    }

    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // cctor
    public Rectangle(Rectangle other) {
        this.x = other.x;
        this.y = other.y;
        this.w = other.w;
        this.h = other.h;
    }

}
