package com.almagems.mineraider;

public final class Sphere {
    public final Vector center;
    public final float radius;

    public Sphere(float x, float y, float z, float radius) {
        this.center = new Vector(x, y, z);
        this.radius = radius;
    }
}
