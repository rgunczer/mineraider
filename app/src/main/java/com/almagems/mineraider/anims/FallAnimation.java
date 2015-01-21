package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;

public class FallAnimation {
	public final GemPosition animGemFrom;
	public final GemPosition animGemTo;

    public boolean isDone;

	//private final float easing = 0.1f;
	//private final float spring = 0.03f;
	private float vy = 0.0f;
	//private final float friction = 0.9f;
	private final float g = 0.065f;
	private int bounceCounter = 0;
    int type;

    // ctor
	public FallAnimation() {
        animGemFrom = new GemPosition();
        animGemTo = new GemPosition();
    }

    public void init(GemPosition from, GemPosition to) {
		this.isDone = false;

        this.animGemFrom.init(from);
		this.animGemTo.init(to);
		
		this.animGemFrom.type = from.type;
		this.animGemFrom.op.setPosition(from.op.tx, from.op.ty, from.op.tz);
		this.animGemFrom.op.setScale(from.op.sx, from.op.sy, from.op.sz);
		
		this.animGemTo.op.setPosition(to.op.tx, to.op.ty, to.op.tz);

        this.type = from.type;

		vy = 0.1f;
		bounceCounter = 0;
	}	

	public void update() {
		if (!isDone) {
			vy += g;
			animGemFrom.op.ty -= vy;
			
			if (animGemFrom.op.ty < animGemTo.op.ty) {
				animGemFrom.op.ty = animGemTo.op.ty;
				vy *= -0.4f;			
				++bounceCounter;
			}
			
			if (bounceCounter > 4) {
				isDone = true;
			}
		}
	}
}
