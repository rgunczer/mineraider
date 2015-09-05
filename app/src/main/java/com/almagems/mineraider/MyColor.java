package com.almagems.mineraider;

public class MyColor {
	public float r, g, b, a;

    // ctor
	public MyColor() {
		r = 0f;
		g = 0f;
		b = 0f;
		a = 0f;
	}

    // cctor
    public MyColor(MyColor another) {
        r = another.r;
        g = another.g;
        b = another.b;
        a = another.a;
    }

	public MyColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;		
	}
	
	public MyColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1f;
	}

    public String toString() {
        return "MyColor (" + this.r + ", " + this.g + ", " + this.b + ", " + this.a + ")";
    }

}
