package com.almagems.mineraider;


public final class FallAnimation {
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
		this.animGemFrom.pos.trans(from.pos.tx, from.pos.ty, from.pos.tz);
		this.animGemFrom.pos.scale(from.pos.sx, from.pos.sy, from.pos.sz);
		
		this.animGemTo.pos.trans(to.pos.tx, to.pos.ty, to.pos.tz);

        this.type = from.type;

		vy = 0.1f;
		bounceCounter = 0;
	}	

	public void update() {
		if (!isDone) {
			vy += g;
			animGemFrom.pos.ty -= vy;
			
			if (animGemFrom.pos.ty < animGemTo.pos.ty) {
				animGemFrom.pos.ty = animGemTo.pos.ty;
				vy *= -0.4f;			
				++bounceCounter;
			}
			
			if (bounceCounter > 4) {
				isDone = true;
			}
		}
	}
}
