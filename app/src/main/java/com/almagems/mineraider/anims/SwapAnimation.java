package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.objects.Model;


public class SwapAnimation extends BaseAnimation {
	public GemPosition firstAnim;
	public GemPosition secondAnim;
	
	private GemPosition first;
	private GemPosition second;
	
	private boolean animAxisY;	
	public boolean undo;	
	
	private float prevDiff1;
	private float prevDiff2;
	
	private final float animSpeed = 0.3f;

    // ctor
	public SwapAnimation() {

    }

    public void init(GemPosition firstSelectedGem, GemPosition secondSelectedGem, boolean undo) {
		this.undo = undo;
		
		firstSelectedGem.visible = false;
		secondSelectedGem.visible = false;
		
		this.first = firstSelectedGem;
		this.second = secondSelectedGem;
		
		this.firstAnim = new GemPosition(first);
		this.secondAnim = new GemPosition(second);
		
		this.firstAnim.type = first.type;
		this.secondAnim.type = second.type;
		
		this.firstAnim.op.setPosition(first.op.tx, first.op.ty, first.op.tz);
		this.secondAnim.op.setPosition(second.op.tx, second.op.ty, second.op.tz);
		
		final float scale = 0.1f;
		
		this.firstAnim.op.setScale(first.op.sx + scale, first.op.sy + scale, first.op.sz + scale);
		this.secondAnim.op.setScale(second.op.sx - scale, second.op.sy - scale, second.op.sz - scale);		
		
		// determinde axis
		if (first.boardX == second.boardX) {
			//System.out.println("col anim...");			
			animAxisY = true;			
			
			// determine direction
			if (first.boardY < second.boardY) {
				this.firstAnim.animDirAndStep = animSpeed;				
				this.secondAnim.animDirAndStep = -animSpeed;
			} else {				
				this.firstAnim.animDirAndStep = -animSpeed;			
				this.secondAnim.animDirAndStep = animSpeed;				
			}			
		} else if (first.boardY == second.boardY) {
			//System.out.println("row anim...");			
			animAxisY = false;
						
			if (first.boardX > second.boardX) {
				this.firstAnim.animDirAndStep = -animSpeed;				
				this.secondAnim.animDirAndStep = animSpeed;	
			} else {
				this.firstAnim.animDirAndStep = animSpeed;				
				this.secondAnim.animDirAndStep = -animSpeed;				
			}
		} else {
			//System.out.println("Unable to animate not in the same row or column!");
			isDone = true;
		}			

		// calc prevDiff
		if (animAxisY) {
			firstAnim.op.setPosition(firstAnim.op.tx, firstAnim.op.ty + firstAnim.animDirAndStep, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx, secondAnim.op.ty + secondAnim.animDirAndStep, secondAnim.op.tz);			
			
			prevDiff1 = Math.abs(second.op.ty - firstAnim.op.ty);
			prevDiff2 = Math.abs(first.op.ty - secondAnim.op.ty);								
		} else {
			firstAnim.op.setPosition(firstAnim.op.tx + firstAnim.animDirAndStep, firstAnim.op.ty, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx + secondAnim.animDirAndStep, secondAnim.op.ty, secondAnim.op.tz);			
			
			prevDiff1 = Math.abs(second.op.tx - firstAnim.op.tx);
			prevDiff2 = Math.abs(first.op.tx - secondAnim.op.tx);									
		}
		
		isDone = false;
	}

    @Override
    public void reset() {
    }

	@Override
	public void prepare() {
	}
	
	@Override
	public void update() {				
		if (animAxisY) {			
			firstAnim.op.setPosition(firstAnim.op.tx, firstAnim.op.ty + firstAnim.animDirAndStep, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx, secondAnim.op.ty + secondAnim.animDirAndStep, secondAnim.op.tz);			
			
			float diff1 = Math.abs(second.op.ty - firstAnim.op.ty);
			float diff2 = Math.abs(first.op.ty - secondAnim.op.ty);
					
			boolean don1 = false;
			boolean don2 = false;
			
			if (diff1 < prevDiff1) {
				prevDiff1 = diff1;
			} else {
				don1 = true;
			}
			
			if (diff2 < prevDiff2) {
				prevDiff2 = diff2;
			} else {
				don2 = true;
			}

			if (don1 && don2) {
				isDone = true;
			}
		} else {
			firstAnim.op.setPosition(firstAnim.op.tx + firstAnim.animDirAndStep, firstAnim.op.ty, firstAnim.op.tz);
			secondAnim.op.setPosition(secondAnim.op.tx + secondAnim.animDirAndStep, secondAnim.op.ty, secondAnim.op.tz);			
			
			float diff1 = Math.abs(second.op.tx - firstAnim.op.tx);
			float diff2 = Math.abs(first.op.tx - secondAnim.op.tx);
		
			boolean don1 = false;
			boolean don2 = false;
			
			if (diff1 < prevDiff1) {
				prevDiff1 = diff1;
			} else {
				don1 = true;
			}
			
			if (diff2 < prevDiff2) {
				prevDiff2 = diff2;
			} else {
				don2 = true;
			}

			if (don1 && don2) {
				isDone = true;
			}			
		}
	}
	
	@Override
	public void draw() {
		Model gem;
		Visuals visuals = Visuals.getInstance();
		
		visuals.dirLightShader.setTexture(visuals.textureGems);
		
		gem = visuals.gems[firstAnim.type];
		visuals.calcMatricesForObject(firstAnim.op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setUniforms(gem.color, visuals.lightColor, visuals.lightNorm);
		gem.bindData(visuals.dirLightShader);
		gem.draw();

		gem = visuals.gems[secondAnim.type];
		visuals.calcMatricesForObject(secondAnim.op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setUniforms(gem.color, visuals.lightColor, visuals.lightNorm);
		gem.bindData(visuals.dirLightShader);
		gem.draw();
	}
}
