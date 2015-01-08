package com.almagems.mineraider.anims;

import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.objects.Model;

public class FallAnimation {
    public static Visuals visuals;

	public GemPosition animGemFrom;
	public GemPosition animGemTo;

    public boolean isDone;

	//private final float easing = 0.1f;
	//private final float spring = 0.03f;
	private float vy = 0.0f;
	//private final float friction = 0.9f;
	private final float g = 0.065f;
	private int bounceCounter = 0;
    private Model gem;

    // ctor
	public FallAnimation() {
    }

    public void init(GemPosition from, GemPosition to) {
		this.isDone = false;

        this.animGemFrom = new GemPosition(from);
		this.animGemTo = new GemPosition(to);
		
		this.animGemFrom.type = from.type;
		this.animGemFrom.op.setPosition(from.op.tx, from.op.ty, from.op.tz);
		this.animGemFrom.op.setScale(from.op.sx, from.op.sy, from.op.sz);
		
		this.animGemTo.op.setPosition(to.op.tx, to.op.ty, to.op.tz);

        gem = visuals.gems[ from.type ];

		vy = 0.1f;
		bounceCounter = 0;
	}	

	public void update() {
		if (!isDone) {
			//System.out.println("FallAnimation update... from(" + animGemFrom.boardX + "," + animGemFrom.boardY + ") to(" + animGemTo.boardX + "," + animGemTo.boardY + ")");
			vy += g;
			animGemFrom.op.ty -= vy;
			
			if (animGemFrom.op.ty < animGemTo.op.ty) {
				animGemFrom.op.ty = animGemTo.op.ty;
				vy *= -0.4f;			
				++bounceCounter;
			}
			
			if (bounceCounter > 4) {
				isDone = true;
			}
		}
	}

	public void draw() {
		animGemFrom.op.setScale(1f, 1f, 1f);
		visuals.calcMatricesForObject(animGemFrom.op);
		visuals.pointLightShader.setUniforms(gem.color, visuals.lightColor, visuals.lightNorm);
		gem.bindData(visuals.pointLightShader);
		gem.draw();
	}
}
