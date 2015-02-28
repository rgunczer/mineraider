package com.almagems.mineraider.anims;

import static com.almagems.mineraider.Constants.*;

import java.util.ArrayList;
import java.util.Random;

import com.almagems.mineraider.Constants;
import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.Physics;
import com.almagems.mineraider.particlesystem.ParticleManager;

public class PopAnimation extends BaseAnimation {
	
	public enum State {
        Blow,
		Pop,
		Fall
	}

    private int[] gemTypesCounter = new int[MAX_GEM_TYPES];
    public static Physics physics;

    private ArrayList<GemPosition> pool = new ArrayList<GemPosition>();
	private ArrayList<GemPosition> list = new ArrayList<GemPosition>();
	public State state = State.Pop;
		
	// ctor
	public PopAnimation() {
	}

    public void reset() {
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            gemTypesCounter[i] = 0;
        }
        isDone = false;
        state = State.Blow;

        int size = list.size();
        for(int i = 0; i < size; ++i) {
            pool.add( list.get(i) );
        }
        list.clear();
    }

    public int getNumberOfDifferentGemTypes() {
        int counter = 0;
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            if (gemTypesCounter[i] != 0) {
                ++counter;
            }
        }
        return counter;
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

        ++gemTypesCounter[ item.type ];

		item.visible = false;
		gp = getFromPool();
        gp.init(item);
		list.add(gp);
	}

	private void addPhysicsEntity(float x, float y, int gemType) {
        //System.out.println("addPhysicsEntity for gemType: " + gemType);
		Random rand = new Random();
        float d = Constants.GEM_FRAGMENT_SIZE;
        float tx = d / 2f;
        float ty = d / 2f;


        float degree = (float)rand.nextInt(360);
        float theta = (float)Math.toRadians(degree);
        float s = (float)Math.sin(theta);
        float c = (float)Math.cos(theta);
        float xnew = tx * c - ty * s;
        float ynew = tx * s + ty * c;
		float x1 = x + xnew;
		float y1 = y + ynew;

        degree = (float)rand.nextInt(360);
        theta = (float)Math.toRadians(degree);
        s = (float)Math.sin(theta);
        c = (float)Math.cos(theta);
        xnew = tx * c - ty * s;
        ynew = tx * s + ty * c;
		float x2 = x + xnew;
		float y2 = y + ynew;

        degree = (float)rand.nextInt(360);
        theta = (float)Math.toRadians(degree);
        s = (float)Math.sin(theta);
        c = (float)Math.cos(theta);
        xnew = tx * c - ty * s;
        ynew = tx * s + ty * c;
		float x3 = x + xnew;
		float y3 = y + ynew;
		
		switch(gemType) {
		case GEM_TYPE_0:
			physics.addFragmentGem0(x1, y1);
			//physics.addFragmentGem0(x2, y2);
			//physics.addFragmentGem0(x3, y3);
			break;
		
		case GEM_TYPE_1:
			physics.addFragmentGem1(x1, y1);
			//physics.addFragmentGem1(x2, y2);
			//physics.addFragmentGem1(x3, y3);
			break;
		
		case GEM_TYPE_2:
			physics.addFragmentGem2(x1, y1);
			//physics.addFragmentGem2(x2, y2);
			//physics.addFragmentGem2(x3, y3);
			break;
		
		case GEM_TYPE_3:
			physics.addFragmentGem3(x1, y1);
			//physics.addFragmentGem3(x2, y2);
			//physics.addFragmentGem3(x3, y3);
			break;
			
		case GEM_TYPE_4:
			physics.addFragmentGem4(x1, y1);
			//physics.addFragmentGem4(x2, y2);
			//physics.addFragmentGem4(x3, y3);
			break;
		
		case GEM_TYPE_5:
			physics.addFragmentGem5(x1, y1);
			//physics.addFragmentGem5(x2, y2);
			//physics.addFragmentGem5(x3, y3);
			break;
		
		case GEM_TYPE_6:
			physics.addFragmentGem6(x1, y1);
			//physics.addFragmentGem6(x2, y2);
			//physics.addFragmentGem6(x3, y3);
			break;
		}
	}	

	@Override
	public void update() {			
		switch(state) {
            case Pop: {
                float z = 0.0f;
                int size = list.size();
                GemPosition gp;
                for (int i = 0; i < size; ++i) {
                    gp = list.get(i);
                    gp.pos.tz += 0.4f;

                    if (z < gp.pos.tz) {
                        z = gp.pos.tz;
                    }
                }

                if (z > 1.0f) {
                    state = State.Fall;
                }
            }
                break;

            case Fall: {
                int size = list.size();
                GemPosition gp;
                 for(int i = 0; i < size; ++i) {
                     gp = list.get(i);
                    addPhysicsEntity(gp.pos.tx, gp.pos.ty, gp.type);
                }
                isDone = true;
            }
                break;

            case Blow: {
                ParticleManager particleManager = ParticleManager.getInstance();
                int size = list.size();
                GemPosition gp;
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
