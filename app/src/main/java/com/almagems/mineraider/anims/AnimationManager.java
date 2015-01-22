package com.almagems.mineraider.anims;

public class AnimationManager {	
	public BaseAnimation running;
	public BaseAnimation finished;
	
	public AnimationManager() {		
	}
	
	public void add(BaseAnimation anim) {
		finished = null;
		running = anim;
	}
	
	public void update() {		
		if (running != null) {
			running.update();
			
			if (running.isDone) {
				finished = running;
				running = null;
			}
		}
	}

	public boolean isDone() {
		return running == null;
	}
}
