package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.ObjectPosition;

public class SwapHint {
	public final ObjectPosition op;
	
	private float animStep = 0.0f;
	private float centerX = 0.0f;
	private float centerY = 0.0f;
    private boolean isHorizontal;
    private static float limit = 0.25f;

    // ctor
	public SwapHint() {
        op = new ObjectPosition();
    }

    public void init(GemPosition first, GemPosition second) {
		float x;
		float y;
		float z = first.op.tz + 0.6f;
		float r = 0.0f;
		float diff;
		animStep = 0.025f;

		// calc position
		if (first.boardX == second.boardX) { // same col
            isHorizontal = false;
			x = first.op.tx;
			diff = Math.abs(first.op.ty - second.op.ty);
			
			if (first.boardY > second.boardY) {
				y = first.op.ty - (diff / 2.0f);
			} else {
				y = first.op.ty + (diff / 2.0f);
			}
			r = 90.0f;			
		} else { //if (first.boardY == second.boardY) { // same row
            isHorizontal = true;
			y = first.op.ty;
			diff = Math.abs(first.op.tx - second.op.tx);
			
			if (first.boardX > second.boardX) {
				x = first.op.tx - (diff / 2.0f);
			} else {
                x = first.op.tx + (diff / 2.0f);
			}			
		}
	
		op.setPosition(x, y, z);
		op.setScale(1.2f, 1.2f, 1.0f);
		op.setRot(0f, 0f, r);
		
		centerX = op.tx;
		centerY = op.ty;
	}

	public void update() {
        if (isHorizontal) {
			op.tx += animStep;
			
			if (op.tx > centerX + limit) {
				animStep *= -1.0f;
			} else if (op.tx < centerX - limit) {
				animStep *= -1.0f;
			}
		} else {
			op.ty += animStep;
			
			if (op.ty > centerY + limit) {
				animStep *= -1.0f;
			} else if (op.ty < centerY - limit) {
				animStep *= -1.0f;
			}
		}
	}
}
