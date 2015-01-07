package com.almagems.mineraider.anims;

import java.util.ArrayList;


public class FallGroupAnimation extends BaseAnimation {

	public ArrayList<FallAnimation> list = new ArrayList<FallAnimation>();

	public FallGroupAnimation() {
		done = false;
	}
	
	@Override
	public void prepare() {					
	}	
	
	@Override
	public void update() {
		done = true;
		FallAnimation item = null;
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			item = list.get(i);
			item.update();
			
			if (!item.done)
				done = false;
		}
	}

	@Override
	public void draw() {
		int size = list.size();
		FallAnimation item = null;
		for (int i = 0; i < size; ++i) {
			item = list.get(i);
			item.draw();
		}
	}
}
