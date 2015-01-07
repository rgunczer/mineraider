package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.GEM_TYPE_NONE;

import com.almagems.mineraider.util.Geometry.Sphere;

public class GemPosition {

	public final int boardX;
	public final int boardY;

	public ObjectPosition op = new ObjectPosition();	
	public Sphere boundingSphere;
	public int gemType;	
	public float animDirAndStep = 0.0f;
	public String animAxis;
	public boolean visible = true;	
	
	public GemPosition() {
		boardX = 0;
		boardY = 0;
	}
	
	public GemPosition(int boardX, int boardY) {
		this.boardX = boardX;
		this.boardY = boardY;
		this.gemType = GEM_TYPE_NONE;		
	}
	
	// cctor
	public GemPosition(GemPosition another) {
		this.boardX = another.boardX;
		this.boardY = another.boardY;
	
		this.op = new ObjectPosition(another.op);
		
		this.boundingSphere = another.boundingSphere;
		this.gemType = another.gemType;
		this.animDirAndStep = another.animDirAndStep;
		this.animAxis = another.animAxis;
		this.visible = another.visible;		
		
	}
	
	public void init( float tx, float ty, float tz,
			 		  float sx, float sy, float sz) {
	
		op.setPosition(tx, ty, tz);
		op.setRot(0f, 0f, 0f);
		op.setScale(sx, sy, sz);
		
		final float radius = 1.5f;
		this.boundingSphere = new Sphere(tx, ty, tz, radius);		
	}
}
