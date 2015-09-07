package com.almagems.mineraider;

public final class SwapHint {
	public final PositionInfo pos;
	
	private float animStep = 0.0f;
	private float centerX = 0.0f;
	private float centerY = 0.0f;
    private boolean isHorizontal;
    private static float limit = 0.2f;

    public GemPosition first;
    public GemPosition second;

    // ctor
	public SwapHint() {
        pos = new PositionInfo();
    }

	public void init(SwapHint other) {
		init(other.first, other.second);
	}

    public void init(GemPosition first, GemPosition second) {
        this.first = first;
        this.second = second;
		float x;
		float y;
		float z = first.pos.tz + 0.7f;
		float r = 0.0f;
		float diff;
		//animStep = 0.025f;
		animStep = 0.05f;

		// calc position
		if (first.boardX == second.boardX) { // same col
            isHorizontal = false;
			x = first.pos.tx;
			diff = Math.abs(first.pos.ty - second.pos.ty);
			
			if (first.boardY > second.boardY) {
				y = first.pos.ty - (diff / 2.0f);
			} else {
				y = first.pos.ty + (diff / 2.0f);
			}
			r = 90.0f;			
		} else { //if (first.boardY == second.boardY) { // same row
            isHorizontal = true;
			y = first.pos.ty;
			diff = Math.abs(first.pos.tx - second.pos.tx);
			
			if (first.boardX > second.boardX) {
				x = first.pos.tx - (diff / 2.0f);
			} else {
                x = first.pos.tx + (diff / 2.0f);
			}			
		}
	
		pos.trans(x, y, z);
		pos.scale(1.2f, 1.2f, 1.0f);
		pos.rot(0f, 0f, r);
		
		centerX = pos.tx;
		centerY = pos.ty;
	}

	public void update() {
        if (isHorizontal) {
			pos.tx += animStep;
			
			if (pos.tx > centerX + limit) {
				animStep *= -1.0f;
			} else if (pos.tx < centerX - limit) {
				animStep *= -1.0f;
			}
		} else {
			pos.ty += animStep;
			
			if (pos.ty > centerY + limit) {
				animStep *= -1.0f;
			} else if (pos.ty < centerY - limit) {
				animStep *= -1.0f;
			}
		}
	}
}
