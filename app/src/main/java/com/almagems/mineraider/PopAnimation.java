package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;

import java.util.ArrayList;
import java.util.Random;


public final class PopAnimation extends BaseAnimation {

    private static Random rand = new Random();

	public enum State {
        Blow,
		Pop,
		Fall
	}

    private int dt;
    private State state = State.Pop;

    private ArrayList<GemPosition> pool = new ArrayList<GemPosition>();
	private ArrayList<GemPosition> list = new ArrayList<GemPosition>();


	// ctor
	public PopAnimation() {
        reset();
	}

    public void reset() {
        done = false;
        state = State.Blow;

        int size = list.size();
        for(int i = 0; i < size; ++i) {
            pool.add( list.get(i) );
        }
        list.clear();
    }

    public boolean isEmpty() {
        return (list.size() == 0);
    }

    public int count() {
        return list.size();
    }

    public GemPosition getAt(int index) {
        return list.get(index);
    }

    private GemPosition getFromPool() {
        GemPosition gp;
        int size = pool.size();
        if (size > 0) {
            gp = pool.get( size - 1 );
            pool.remove( size - 1 );
        } else {
            gp = new GemPosition();
        }
        return gp;
    }

	public void add(GemPosition item) {
        GemPosition gp;
        int size = list.size();
		for (int i = 0; i < size; ++i) {
            gp = list.get(i);
			if (gp.boardX == item.boardX && gp.boardY == item.boardY) {
                return;
            }
		}

		item.visible = false;
		gp = getFromPool();
        gp.init(item);
		list.add(gp);
	}

	private void addPhysicsEntity(float x, float y, int gemType) {
        float d = Constants.GEM_FRAGMENT_SIZE;
        float tx = d / 2f;
        float ty = d / 2f;
        float degree = (float)rand.nextInt(360);
        float theta = (float)Math.toRadians(degree);
        float s = (float)Math.sin(theta);
        float c = (float)Math.cos(theta);
        float xn = tx * c - ty * s;
        float yn = tx * s + ty * c;
		float x1 = x + xn;
		float y1 = y + yn;

		switch(gemType) {
            case GEM_TYPE_0: Physics.addFragmentGem0(x1, y1); break;
            case GEM_TYPE_1: Physics.addFragmentGem1(x1, y1); break;
            case GEM_TYPE_2: Physics.addFragmentGem2(x1, y1); break;
            case GEM_TYPE_3: Physics.addFragmentGem3(x1, y1); break;
            case GEM_TYPE_4: Physics.addFragmentGem4(x1, y1); break;
            case GEM_TYPE_5: Physics.addFragmentGem5(x1, y1); break;
            case GEM_TYPE_6: Physics.addFragmentGem6(x1, y1); break;
		}
	}	

	@Override
	public void update() {
        if (!done) {
            innerUpdate();
        }
    }

    private void innerUpdate() {
		switch(state) {
            case Pop:
                --dt;
                if (dt < 0) {
                    state = State.Fall;
                }
            break;

            case Fall: {
                GemPosition gp;
                int size = list.size();
                 for(int i = 0; i < size; ++i) {
                    gp = list.get(i);
                    addPhysicsEntity(gp.pos.tx, gp.pos.ty, gp.type);
                }
                done = true;
            }
            break;

            case Blow: {
                dt = 3;
                GemPosition gp;
                ParticleManager particleManager = Engine.getInstance().graphics.particleManager;
                int size = list.size();
                for(int i = 0; i < size; ++i) {
                    gp = list.get(i);
                    particleManager.addParticleEmitterAt(gp.pos.tx, gp.pos.ty, gp.type);
                }
                state = State.Pop;
            }
            break;
        }
	}
}
