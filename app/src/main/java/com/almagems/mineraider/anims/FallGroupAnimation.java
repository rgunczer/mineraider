package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.Visuals;

import java.util.ArrayList;


public class FallGroupAnimation extends BaseAnimation {

    private static Visuals visuals;
    private ArrayList<FallAnimation> pool = new ArrayList<FallAnimation>();
	private ArrayList<FallAnimation> list = new ArrayList<FallAnimation>();

	public FallGroupAnimation() {
		visuals = Visuals.getInstance();
        FallAnimation.visuals = visuals;
        isDone = false;
	}

    public void add(GemPosition from, GemPosition to) {
        FallAnimation fall = getFromPool();
        fall.init(from, to);
        list.add( fall ); // new FallAnimation(from, to)
    }

    private FallAnimation getFromPool() {
        FallAnimation fall;
        int size = pool.size();
        if (size > 0) {
            fall = pool.get( size - 1 );
            pool.remove( size - 1 );
        } else {
            fall = new FallAnimation();
        }
        return fall;
    }

    @Override
    public void reset() {
        isDone = false;
        FallAnimation fall;
        int size = list.size();
        for (int i = 0; i < size; ++i) {
            fall = list.get(i);
            pool.add(fall);
        }
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
		isDone = true;
		FallAnimation fall;
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			fall = list.get(i);
			fall.update();
			
			if (!fall.isDone) {
                isDone = false;
            }
		}
	}

	@Override
	public void draw() {
        FallAnimation fall;
        int size = list.size();
		for (int i = 0; i < size; ++i) {
			fall = list.get(i);
			fall.draw();
		}
	}
}
