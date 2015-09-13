package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;


public final class GemPosition {

    public enum GemExtras {
        Nothing,
        Match,                  //
        HorizontalExplosive,    // dynamite in position -
        VerticalExplosive,      // dynamite in position |
        RadialExplosive         // dynamite in position #
    }

	public float posYorigin;

    public int boardX;
	public int boardY;

	public PositionInfo pos = new PositionInfo();
	public Sphere boundingSphere;
	public int type;
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

        this.pos.init(another.pos);

        this.boundingSphere = another.boundingSphere;
        this.type = another.type;
        this.visible = another.visible;
        this.extra = another.extra;
    }

	public void init( float tx, float ty, float tz,
			 		  float sx, float sy, float sz) {
	
		pos.trans(tx, ty, tz);
		pos.rot(0f, 0f, 0f);
		pos.scale(sx, sy, sz);
		
		final float radius = 1.65f;
		this.boundingSphere = new Sphere(tx, ty, tz, radius);		
	}
}
