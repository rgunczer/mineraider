package com.almagems.mineraider;

public class Circle {
    public final Vector center;
    public final float radius;

    public Circle(Vector center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle scale(float scale) {
        return new Circle(center, radius * scale);
    }
}
