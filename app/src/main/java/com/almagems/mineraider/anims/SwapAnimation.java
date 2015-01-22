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
		firstAnim.op.setScale(firstAnim.op.sx + scale, firstAnim.op.sy + scale, firstAnim.op.sz + scale);
		secondAnim.op.setScale(secondAnim.op.sx - scale, secondAnim.op.sy - scale, secondAnim.op.sz - scale);
		
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
			firstAnim.op.setPosition(firstAnim.op.tx, firstAnim.op.ty + firstAnimStepAndDir, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx, secondAnim.op.ty + secondAnimStepAndDir, secondAnim.op.tz);
			
			prevDiff1 = Math.abs(secondAnim.op.ty - firstAnim.op.ty);
			prevDiff2 = Math.abs(firstAnim.op.ty - secondAnim.op.ty);
		} else {
			firstAnim.op.setPosition(firstAnim.op.tx + firstAnimStepAndDir, firstAnim.op.ty, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx + secondAnimStepAndDir, secondAnim.op.ty, secondAnim.op.tz);
			
			prevDiff1 = Math.abs(secondAnim.op.tx - firstAnim.op.tx);
			prevDiff2 = Math.abs(firstAnim.op.tx - secondAnim.op.tx);
		}
		
		isDone = false;
	}
	
	@Override
	public void update() {				
		if (animAxisY) {			
			firstAnim.op.setPosition(firstAnim.op.tx, firstAnim.op.ty + firstAnimStepAndDir, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx, secondAnim.op.ty + secondAnimStepAndDir, secondAnim.op.tz);
			
			float diff1 = Math.abs(secondGem.op.ty - firstAnim.op.ty);
			float diff2 = Math.abs(firstGem.op.ty - secondAnim.op.ty);
					
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
			firstAnim.op.setPosition(firstAnim.op.tx + firstAnimStepAndDir, firstAnim.op.ty, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx + secondAnimStepAndDir, secondAnim.op.ty, secondAnim.op.tz);
			
			float diff1 = Math.abs(secondGem.op.tx - firstAnim.op.tx);
			float diff2 = Math.abs(firstGem.op.tx - secondAnim.op.tx);
		
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
