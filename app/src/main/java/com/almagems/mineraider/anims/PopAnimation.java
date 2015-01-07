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
		Pop,
		Fall
	};
	
	public ArrayList<GemPosition> list = new ArrayList<GemPosition>();
	public State state = State.Pop;
		
	// ctor
	public PopAnimation() {
	}
	
	public void add(GemPosition item) {						
		for (GemPosition gp : list) {
			if (gp.boardX == item.boardX && gp.boardY == item.boardY)
				return;
		}		
		item.visible = false;
		GemPosition gp = new GemPosition(item);
		list.add(gp);
	}

	private void addPhysicsEntity(float x, float y, int gemType) {
		Physics physics = Physics.getInstance();
		Random rand = new Random();
		float d = Constants.GEM_FRAGMENT_SIZE;
		float x1 = x + (rand.nextFloat() - 0.5f);
		float x2 = x + (rand.nextFloat() - 0.5f);
		float x3 = x + (rand.nextFloat() - 0.5f);
		float y1 = y + (rand.nextFloat() - 0.5f);
		float y2 = y + (rand.nextFloat() - 0.5f);
		float y3 = y + (rand.nextFloat() - 0.5f);
		
		switch(gemType) {
		case GEM_TYPE_0:
			physics.addFragmentGem0(x1, y1);
			physics.addFragmentGem0(x2, y2);
			physics.addFragmentGem0(x3, y3);			
			break;
		
		case GEM_TYPE_1:
			physics.addFragmentGem1(x1, y1);
			physics.addFragmentGem1(x2, y2);
			physics.addFragmentGem1(x3, y3);
			break;
		
		case GEM_TYPE_2:
			physics.addFragmentGem2(x1, y1);
			physics.addFragmentGem2(x2, y2);			
			physics.addFragmentGem2(x3, y3);
			break;
		
		case GEM_TYPE_3:
			physics.addFragmentGem3(x1, y1);
			physics.addFragmentGem3(x2, y2);
			physics.addFragmentGem3(x3, y3);						
			break;
			
		case GEM_TYPE_4:
			physics.addFragmentGem4(x1, y1);
			physics.addFragmentGem4(x2, y2);
			physics.addFragmentGem4(x3, y3);						
			break;
		
		case GEM_TYPE_5:
			physics.addFragmentGem5(x, y);
			physics.addFragmentGem5(x, y);
			physics.addFragmentGem5(x, y);							
			break;
		
		case GEM_TYPE_6:
			physics.addFragmentGem6(x, y);
			physics.addFragmentGem6(x, y);
			physics.addFragmentGem6(x, y);
			break;
		}
	}	
	
	@Override
	public void prepare() {
		ParticleManager particleManager = ParticleManager.getInstance();		
		for(GemPosition item : list) {
			particleManager.addParticleEmitterAt(item.op.tx, item.op.ty, item.gemType);
		}
	}
	
	@Override
	public void update() {			
		switch(state) {
		case Pop:			
			float z = 0.0f;
			for (GemPosition item : list) {
				item.op.tz += 0.3f;
								
				if (z < item.op.tz) {
					z = item.op.tz;
				}
			}
			
			if (z > 1.0f) {
				state = State.Fall;
			}
			break;
			
		case Fall:							
			for(GemPosition item : list) {
				addPhysicsEntity(item.op.tx, item.op.ty, item.gemType);																				
			}
			done = true;
			break;
		}
	}

	@Override
	public void draw() {
//		Visuals visuals = Visuals.getInstance();		
//		for (ObjectPosition item : list) {
//			Model gem = visuals.gems[item.gemType];	
//			ObjectPosition gp = new ObjectPosition(item.boardX, item.boardY);			
//			gp.gemType = item.gemType;
//			gp.position = new Vector(item.position.x, item.position.y, item.position.z);
//			gp.scale = new Vector(item.scale.x, item.scale.y, item.scale.z);
//			
//			visuals.calcMatricesForObject(gp);
//			visuals.pointLightShader.useProgram();			
//			visuals.pointLightShader.setTexture(visuals.textures[gp.gemType]);
//			visuals.pointLightShader.setUniforms(gem.color, visuals.lightColor, visuals.lightNorm);
//			gem.bindData(visuals.pointLightShader);
//			gem.draw();
//		}
	}
}
