package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.ObjectPosition;

public class SwapHint {

	public final ObjectPosition op;
	
	private float animStep = 0.0f;
	private float centerX = 0.0f;
	private float centerY = 0.0f;
	private float limit = 0.25f;
	
	public SwapHint() {
        op = new ObjectPosition();
    }

    public void init(GemPosition first, GemPosition second) {
		float x = 0.0f;
		float y = 0.0f;
		float z = -1.5f;
		float r = 0.0f;
		
		animStep = 0.025f;
		
		// calc position
		if (first.boardX == second.boardX) { // same col
			x = first.op.tx;
			y = 0.0f;
			float diff = Math.abs(first.op.ty - second.op.ty);
			
			if (first.boardY > second.boardY) {
				y = first.op.ty - (diff / 2.0f);
			} else {
				y = first.op.ty + (diff / 2.0f);
			}
			r = 90.0f;			
		} else if (first.boardY == second.boardY) { // same row
			y = first.op.ty;
			float diff = Math.abs(first.op.tx - second.op.tx);
			
			if (first.boardX > second.boardX) {
				x = first.op.tx - (diff / 2.0f);
			} else {
				x = first.op.tx + (diff / 2.0f);
			}			
		} else {
			//System.out.println("Could not be hint here!");
		}
	
		op.setPosition(x, y, z);
		op.setScale(1.2f, 1.2f, 1.2f);
		op.setRot(0f, 0f, r);
		
		centerX = op.tx;
		centerY = op.ty;
	}

	public void update() {	
		if (op.rz == 0.0f) {
			op.tx += animStep;
			
			if (op.tx > centerX + limit) {
				animStep *= -1.0f;
			} else if (op.tx < centerX - limit) {
				animStep *= -1.0f;
			}
		}
		
		if (op.rz == 90.0f) {
			op.ty += animStep;
			
			if (op.ty > centerY + limit) {
				animStep *= -1.0f;
			} else if (op.ty < centerY - limit) {
				animStep *= -1.0f;
			}
		}
	}
	
//	@Override
//	public void draw() {
//		Visuals visuals = Visuals.getInstance();
//		visuals.calcMatricesForObject(_op);
//		visuals.dirLightShader.useProgram();
//		Model gem = visuals.hintMarker;
//		visuals.dirLightShader.setTexture(visuals.textureHintArrow);
//		visuals.dirLightShader.setUniforms(gem.color, visuals.lightColor, visuals.lightNorm);
//		gem.bindData(visuals.dirLightShader);
//		gem.draw();
//	}
}
