package com.almagems.mineraider;

import java.util.ArrayList;


public final class FallGroupAnimation extends BaseAnimation {

    private ArrayList<FallAnimation> pool = new ArrayList<FallAnimation>(30);
	private ArrayList<FallAnimation> list = new ArrayList<FallAnimation>(30);

	public FallGroupAnimation() {
        done = false;
	}

    public void add(GemPosition from, GemPosition to) {
        FallAnimation fall = getFromPool();
        fall.init(from, to);
        list.add( fall );
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

    public void reset() {
        done = false;
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
	public void update() {
		done = true;
		FallAnimation fall;
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			fall = list.get(i);
			fall.update();
			
			if (!fall.isDone) {
                done = false;
            }
		}
	}

}
