package com.almagems.mineraider.anims;

public abstract class BaseAnimation {

	public abstract void update();
	public abstract void draw();
	public abstract void prepare();
	
	public boolean done;
}
