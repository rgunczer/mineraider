package com.almagems.mineraider.anims;

public class AnimationManager {	
	private BaseAnimation running;
	public BaseAnimation finished;
	
	public AnimationManager() {		
	}
	
	public void add(BaseAnimation anim) {
		finished = null;
		running = anim;
		running.prepare();
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
	
	public void draw() {
		if (running != null) {
			running.draw();
		}
	}
	
	public boolean isDone() {
		return running == null;
	}
}
