package com.almagems.mineraider;

import com.almagems.mineraider.util.Sphere;

import static com.almagems.mineraider.Constants.GEM_TYPE_NONE;


public class GemPosition {

    public enum GemExtras {
        Nothing,
        Match,                  //
        HorizontalExplosive,    // dynamite in position -
        VerticalExplosive,      // dynamite in position |
        RadialExplosive         // dynamite in position #
    }

    public int boardX;
	public int boardY;

	public ObjectPosition op = new ObjectPosition();	
	public Sphere boundingSphere;
	public int type;
	public float animDirAndStep = 0.0f;
	public String animAxis;
	public boolean visible = true;	
    public GemExtras extra = GemExtras.Nothing;

	public GemPosition() {
		boardX = 0;
		boardY = 0;
	}
	
	public GemPosition(int boardX, int boardY) {
		this.boardX = boardX;
		this.boardY = boardY;
		this.type = GEM_TYPE_NONE;
        this.extra = GemExtras.Nothing;
	}
	
	// cctor
	public GemPosition(GemPosition another) {
        init(another);
	}

    public void init(GemPosition another) {
        this.boardX = another.boardX;
        this.boardY = another.boardY;

        this.op.init(another.op);

        this.boundingSphere = another.boundingSphere;
        this.type = another.type;
        this.animDirAndStep = another.animDirAndStep;
        this.animAxis = another.animAxis;
        this.visible = another.visible;
        this.extra = another.extra;
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
