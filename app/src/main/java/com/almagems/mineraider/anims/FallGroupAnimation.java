package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.Visuals;

import java.util.ArrayList;


public class FallGroupAnimation extends BaseAnimation {

    private static Visuals visuals;
	private ArrayList<FallAnimation> list = new ArrayList<FallAnimation>();

	public FallGroupAnimation() {
		visuals = Visuals.getInstance();
        FallAnimation.visuals = visuals;
        done = false;
	}

    public void add(GemPosition from, GemPosition to) {
        list.add( new FallAnimation(from, to) );
    }

    public void reset() {
        list.clear();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public FallAnimation getAnimAt(int index) {
        return list.get(index);
    }

    public int count() {
        return list.size();
    }

	@Override
	public void prepare() {					
	}	
	
	@Override
	public void update() {
		done = true;
		FallAnimation item;
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
        visuals.pointLightShader.useProgram();
        visuals.pointLightShader.setTexture(visuals.textureGems);

        int size = list.size();
		FallAnimation item;
		for (int i = 0; i < size; ++i) {
			item = list.get(i);
			item.draw();
		}
	}
}
