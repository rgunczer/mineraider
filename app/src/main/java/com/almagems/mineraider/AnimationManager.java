package com.almagems.mineraider;

public final class AnimationManager {

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
			
			if (running.done) {
				finished = running;
				running = null;
			}
		}
	}

    public void clear() {
        running = null;
        finished = null;
    }

	public boolean isDone() {
		return running == null;
	}
}
