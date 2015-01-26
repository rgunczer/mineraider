package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;

public class SwapAnimation extends BaseAnimation {
	public final GemPosition firstAnim;
	public final GemPosition secondAnim;

    private GemPosition firstGem;
    private GemPosition secondGem;

	private boolean animAxisY;	
	public boolean undo;	
	
	private float prevDiff1;
	private float prevDiff2;
	
	private final float animStep = 0.3f;

    private float firstAnimStepAndDir;
    private float secondAnimStepAndDir;

    // ctor
	public SwapAnimation() {
        firstAnim = new GemPosition();
        secondAnim = new GemPosition();
    }

    public void init(GemPosition firstSelected, GemPosition secondSelected, boolean undo) {
		this.undo = undo;

        firstGem = firstSelected;
        secondGem = secondSelected;

		firstSelected.visible = false;
		secondSelected.visible = false;

		this.firstAnim.init(firstSelected);
		this.secondAnim.init(secondSelected);

		final float scale = 0.1f;
		firstAnim.pos.scale(firstAnim.pos.sx + scale, firstAnim.pos.sy + scale, firstAnim.pos.sz + scale);
		secondAnim.pos.scale(secondAnim.pos.sx - scale, secondAnim.pos.sy - scale, secondAnim.pos.sz - scale);
		
		// determinde axis
		if (firstAnim.boardX == secondAnim.boardX) {
			animAxisY = true;			
			
			// determine direction
			if (firstAnim.boardY < secondAnim.boardY) {
				firstAnimStepAndDir = animStep;
				secondAnimStepAndDir = -animStep;
			} else {				
				firstAnimStepAndDir = -animStep;
				secondAnimStepAndDir = animStep;
			}			
		} else if (firstAnim.boardY == secondAnim.boardY) {
			animAxisY = false;
						
			if (firstAnim.boardX > secondAnim.boardX) {
				firstAnimStepAndDir = -animStep;
				secondAnimStepAndDir = animStep;
			} else {
				firstAnimStepAndDir = animStep;
				secondAnimStepAndDir = -animStep;
			}
		} else {
			//System.out.println("Unable to animate not in the same row or column!");
			isDone = true;
		}			

		// calc prevDiff
		if (animAxisY) {
			firstAnim.pos.trans(firstAnim.pos.tx, firstAnim.pos.ty + firstAnimStepAndDir, firstAnim.pos.tz);
			secondAnim.pos.trans(secondAnim.pos.tx, secondAnim.pos.ty + secondAnimStepAndDir, secondAnim.pos.tz);
			
			prevDiff1 = Math.abs(secondAnim.pos.ty - firstAnim.pos.ty);
			prevDiff2 = Math.abs(firstAnim.pos.ty - secondAnim.pos.ty);
		} else {
			firstAnim.pos.trans(firstAnim.pos.tx + firstAnimStepAndDir, firstAnim.pos.ty, firstAnim.pos.tz);
			secondAnim.pos.trans(secondAnim.pos.tx + secondAnimStepAndDir, secondAnim.pos.ty, secondAnim.pos.tz);
			
			prevDiff1 = Math.abs(secondAnim.pos.tx - firstAnim.pos.tx);
			prevDiff2 = Math.abs(firstAnim.pos.tx - secondAnim.pos.tx);
		}
		
		isDone = false;
	}
	
	@Override
	public void update() {				
		if (animAxisY) {			
			firstAnim.pos.trans(firstAnim.pos.tx, firstAnim.pos.ty + firstAnimStepAndDir, firstAnim.pos.tz);
			secondAnim.pos.trans(secondAnim.pos.tx, secondAnim.pos.ty + secondAnimStepAndDir, secondAnim.pos.tz);
			
			float diff1 = Math.abs(secondGem.pos.ty - firstAnim.pos.ty);
			float diff2 = Math.abs(firstGem.pos.ty - secondAnim.pos.ty);
					
			boolean don1 = false;
			boolean don2 = false;
			
			if (diff1 <= prevDiff1) {
				prevDiff1 = diff1;
			} else {
				don1 = true;
			}
			
			if (diff2 <= prevDiff2) {
				prevDiff2 = diff2;
			} else {
				don2 = true;
			}

			if (don1 && don2) {
				isDone = true;
			}
		} else {
			firstAnim.pos.trans(firstAnim.pos.tx + firstAnimStepAndDir, firstAnim.pos.ty, firstAnim.pos.tz);
			secondAnim.pos.trans(secondAnim.pos.tx + secondAnimStepAndDir, secondAnim.pos.ty, secondAnim.pos.tz);
			
			float diff1 = Math.abs(secondGem.pos.tx - firstAnim.pos.tx);
			float diff2 = Math.abs(firstGem.pos.tx - secondAnim.pos.tx);
		
			boolean don1 = false;
			boolean don2 = false;
			
			if (diff1 <= prevDiff1) {
				prevDiff1 = diff1;
			} else {
				don1 = true;
			}
			
			if (diff2 <= prevDiff2) {
				prevDiff2 = diff2;
			} else {
				don2 = true;
			}

			if (don1 && don2) {
				isDone = true;
			}			
		}
	}
}
