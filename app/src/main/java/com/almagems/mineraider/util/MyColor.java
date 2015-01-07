package com.almagems.mineraider.util;

public class MyColor {
	public float r, g, b, a;
	
	public MyColor() {
		r = 0f;
		g = 0f;
		b = 0f;
		a = 0f;
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
}
