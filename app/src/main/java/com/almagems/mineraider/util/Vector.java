package com.almagems.mineraider.util;

import android.util.FloatMath;

	
public class Vector {
	public float x, y, z;
	
	// ctor
	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0f;
	}	
	
	// cctor
	public Vector(Vector another) {
		this.x = another.x;
		this.y = another.y;
		this.z = another.z;
	}
			
	public float length() {
		return FloatMath.sqrt(x*x + y*y + z*z);
	}
	
	public Vector crossProduct(Vector other) {
		return new Vector(
				(y * other.z) - (z * other.y),
				(z * other.x) - (x * other.z),
				(x * other.y) - (y * other.x));
	}
	
	public float dotProduct(Vector other) {
		return x * other.x
				+ y * other.y 
				+ z * other.z;
	}
	
	public Vector scale(float f) {
		return new Vector(x * f,
						  y * f,
						  z * f);
	}
	
	public float getMagnitude() {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector normalize(Vector other) {
		float mag = other.getMagnitude();
		return new Vector(x / mag, y / mag, z / mag);
	}	
}