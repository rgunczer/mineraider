package com.almagems.mineraider.particlesystem;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static com.almagems.mineraider.Constants.GEM_TYPE_0;

import java.util.ArrayList;

import android.graphics.Color;

import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.Vector;


public class ParticleManager {
	private static ParticleManager instance = null;
	
	private long globalStartTime;
	final Vector particleDirection = new Vector(2.0f, 0.0f, 0.0f);
	final float angleVarianceInDegrees = 5f;
	final float speedVariance = 1.6f;

	public ArrayList<ParticleEmitter> deadParticleEmitters = new ArrayList<ParticleEmitter>();
	public ArrayList<ParticleEmitter> liveParticleEmitters = new ArrayList<ParticleEmitter>();
	
	private ParticleSystem particleSystem;
	
	// ctor
	private ParticleManager() {		
	}
		
	public static ParticleManager getInstance() {
		if (instance == null) {
			instance = new ParticleManager(); 
		}
		return instance;
	}	
	
	public void init() {		
		Visuals visuals = Visuals.getInstance();		
		int color = visuals.colorFromGemType(GEM_TYPE_0);
		
		particleSystem = new ParticleSystem(10000);
		globalStartTime = System.nanoTime();		
		
		ParticleEmitter particleEmitter0 = new ParticleEmitter( new Vector(-10.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		particleEmitter0.numberOfParticlesToEmit = 100;

		ParticleEmitter particleEmitter1 = new ParticleEmitter( new Vector(0.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		
		particleEmitter1.numberOfParticlesToEmit = 50;

		ParticleEmitter particleEmitter2 = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		
		particleEmitter2.numberOfParticlesToEmit = 25;

		liveParticleEmitters.add(particleEmitter0);
		liveParticleEmitters.add(particleEmitter1);
		liveParticleEmitters.add(particleEmitter2);

		
		ParticleEmitter particleEmitter3 = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				Color.rgb(255, 255, 0),
				angleVarianceInDegrees,
				speedVariance );

		ParticleEmitter particleEmitter4 = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				Color.rgb(255, 255, 0),
				angleVarianceInDegrees,
				speedVariance );
		
		ParticleEmitter particleEmitter5 = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				Color.rgb(255, 255, 0),
				angleVarianceInDegrees,
				speedVariance );
		
		ParticleEmitter particleEmitter6 = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				Color.rgb(255, 255, 0),
				angleVarianceInDegrees,
				speedVariance );

		ParticleEmitter particleEmitter7 = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				Color.rgb(255, 255, 0),
				angleVarianceInDegrees,
				speedVariance );

		deadParticleEmitters.add(particleEmitter3);
		deadParticleEmitters.add(particleEmitter4);
		deadParticleEmitters.add(particleEmitter5);
		deadParticleEmitters.add(particleEmitter6);
		deadParticleEmitters.add(particleEmitter7);
	}
	
	public void addParticleEmitterAt(float x, float y, int gemType) {
		Visuals visuals = Visuals.getInstance();
		//System.out.println("in MineRaiderRenderer add Particle Emitter at: " + x + ", " + y);		
		if (deadParticleEmitters.size() > 0) {
			ParticleEmitter pe = deadParticleEmitters.remove(0);
			pe.position = new Vector(x, y, -2f);
			pe.numberOfParticlesToEmit = 10;
			pe.color = visuals.colorFromGemType(gemType);
			liveParticleEmitters.add(pe);
		}
	}
	
	public void draw() {
		Visuals visuals = Visuals.getInstance();
		float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

		while(true) {
			boolean out = true;
			for (ParticleEmitter particleEmitter : liveParticleEmitters) {			
				if (particleEmitter.isDone()) {
					deadParticleEmitters.add(particleEmitter);
					liveParticleEmitters.remove(particleEmitter);
					out = false;
					break;
				}
			}
			if (out) {
				break;
			}
		}
		
		for (ParticleEmitter particleEmitter : liveParticleEmitters) {
			particleEmitter.addParticles(particleSystem, currentTime, 1);
		}
		
		visuals.particleShader.useProgram();
		visuals.particleShader.setTexture(visuals.textureParticle);
		visuals.particleShader.setUniforms(visuals.viewProjectionMatrix, currentTime);
		particleSystem.bindData(visuals.particleShader);
		
		glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_ONE, GL_ONE);
		glEnable(GL_BLEND);
		particleSystem.draw();
		glDisable(GL_BLEND);
	}
	
	
}
