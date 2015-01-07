package com.almagems.mineraider.anims;

public class AnimationManager {	
	private BaseAnimation anim;	
	public BaseAnimation finishedAnim;
	
	public AnimationManager() {		
	}
	
	public void add(BaseAnimation anim) {
		this.finishedAnim = null;
		this.anim = anim;		 
		this.anim.prepare();
	}
	
	public void update() {		
		if (anim != null) {
			anim.update();
			
			if (anim.done) {
				finishedAnim = anim;
				anim = null;
			}
		}
	}
	
	public void draw() {
		if (anim != null) {
			anim.draw();
		}
	}
	
	public boolean isDone() {
		return anim == null;
	}
}
