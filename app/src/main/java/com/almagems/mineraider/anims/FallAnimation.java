package com.almagems.mineraider.anims;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.objects.Model;
import com.almagems.mineraider.util.MyColor;

public class FallAnimation extends BaseAnimation {
	public GemPosition animGemFrom;
	public GemPosition animGemTo;
	
	//private final float easing = 0.1f;
	//private final float spring = 0.03f;
	private float vy = 0.0f;
	//private final float friction = 0.9f;
	private final float g = 0.065f;
	private int bounceCounter = 0;
	
	public FallAnimation(GemPosition from, GemPosition to) {
		//System.out.println("FallAnimation ctor... from (" + from.boardX + "," + from.boardY + "), to (" + to.boardX + "," + to.boardY + ")");		
		this.animGemFrom = new GemPosition(from);
		this.animGemTo = new GemPosition(to);
		
		this.animGemFrom.gemType = from.gemType;
		this.animGemFrom.op.setPosition(from.op.tx, from.op.ty, from.op.tz);
		this.animGemFrom.op.setScale(from.op.sx, from.op.sy, from.op.sz);
		
		this.animGemTo.op.setPosition(to.op.tx, to.op.ty, to.op.tz);
		
		vy = 0.1f;
		bounceCounter = 0;
	}	
	
	@Override
	public void prepare() {
	}
	
	@Override
	public void update() {		
		if (!done) {				
			//System.out.println("FallAnimation update... from(" + animGemFrom.boardX + "," + animGemFrom.boardY + ") to(" + animGemTo.boardX + "," + animGemTo.boardY + ")");
			vy += g;
			animGemFrom.op.ty -= vy;
			
			if (animGemFrom.op.ty < animGemTo.op.ty) {
				animGemFrom.op.ty = animGemTo.op.ty;
				vy *= -0.4f;			
				++bounceCounter;
			}
			
			if (bounceCounter > 4) {
				done = true;
			}
		}
	}

	@Override
	public void draw() {
		
		//MyColor color = new MyColor(0f, 0f, 0f);
		Visuals visuals = Visuals.getInstance();		
		Model gem = visuals.gems[animGemFrom.gemType];
		visuals.pointLightShader.useProgram();
		visuals.pointLightShader.setTexture(visuals.textureGems);
/*		
		animGemFrom.op.setScale(1.1f, 1.1f, 1.1f);		
		visuals.calcMatricesForObject(animGemFrom.op);
		
				
		visuals.pointLightShader.setUniforms(color, visuals.lightColor, visuals.lightNorm);
		gem.bindData(visuals.pointLightShader);
		gem.draw();
		*/
		animGemFrom.op.setScale(1f, 1f, 1f);		
		visuals.calcMatricesForObject(animGemFrom.op);
		//glDisable(GL_DEPTH_TEST);
		visuals.pointLightShader.setUniforms(gem.color, visuals.lightColor, visuals.lightNorm);
		gem.bindData(visuals.pointLightShader);
		gem.draw();
		//glEnable(GL_DEPTH_TEST);
	}
}
