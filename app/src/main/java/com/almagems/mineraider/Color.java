package com.almagems.mineraider;

public final class Color {

	public static final Color CLEAR = new Color(0f, 0f, 0f, 0f);
	public static final Color BLACK = new Color(0f, 0f, 0f, 1f);
	public static final Color WHITE = new Color(1f, 1f, 1f, 1f);
    public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1f);

	public float r, g, b, a;

    // ctor
	public Color() {
		r = 0f;
		g = 0f;
		b = 0f;
		a = 0f;
	}

    // cctor
    public Color(Color another) {
        r = another.r;
        g = another.g;
        b = another.b;
        a = another.a;
    }

    public Color(int r, int g, int b, int a) {
        this.r = (float)r / 255f;
        this.g = (float)g / 255f;
        this.b = (float)b / 255f;
        this.a = (float)a / 255f;
    }

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;		
	}
	
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1f;
	}

    public void init(Color from) {
        this.r = from.r;
        this.g = from.g;
        this.b = from.b;
        this.a = from.a;
    }

    public String toString() {
        return "MyColor (" + this.r + ", " + this.g + ", " + this.b + ", " + this.a + ")";
    }

}
