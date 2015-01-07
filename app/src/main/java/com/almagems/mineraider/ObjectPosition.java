package com.almagems.mineraider;

public class ObjectPosition {
	
	public float tx; // translation along x axis
	public float ty; // translation along y axis
	public float tz; // translation along z axis

	public float rx; // rotation along x axis in degrees
	public float ry; // rotation along y axis in degrees
	public float rz; // rotation along z axis in degrees	
	
	public float sx; // scale along x axis
	public float sy; // scale along y axis
	public float sz; // scale along z axis	
						
	// ctor
	public ObjectPosition() {
	}
	
	// cctor
	public ObjectPosition(ObjectPosition another) {
		// translate
		this.tx = another.tx;
		this.ty = another.ty;
		this.tz = another.tz;

		// rotate
		this.rx = another.rx;
		this.ry = another.ry;
		this.rz = another.rz;
		
		// scale
		this.sx = another.sx;
		this.sy = another.sy;
		this.sz = another.sz;	
	}
	
	public void init(float tx, float ty, float tz,
					 float sx, float sy, float sz) {
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;

		this.rx = 0f;
		this.ry = 0f;
		this.rz = 0f;		
		
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;				
	}
	
	public void setPosition(float x, float y, float z) {
		tx = x;
		ty = y;
		tz = z;
	}

	public void setRot(float x, float y, float z) {		
		rx = x;
		ry = y;
		rz = z;		
	}	
	
	public void setScale(float x, float y, float z) {
		sx = x;
		sy = y;
		sz = z;
	}
}