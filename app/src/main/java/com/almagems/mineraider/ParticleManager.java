package com.almagems.mineraider;

import java.util.ArrayList;

import android.graphics.Color;


public class ParticleManager {

    public static Graphics graphics;
	private long globalStartTime;
	final Vector particleDirection = new Vector(2.0f, 0.0f, 0.0f);
	final float angleVarianceInDegrees = 5f;
	final float speedVariance = 1.6f;

    public ArrayList<ParticleEmitter> remove = new ArrayList<ParticleEmitter>(8);
	public ArrayList<ParticleEmitter> pool = new ArrayList<ParticleEmitter>(32);
	public ArrayList<ParticleEmitter> live = new ArrayList<ParticleEmitter>(16);
	
	private ParticleSystem particleSystem;
	

	public void init() {
		int color = Color.rgb(255, 255, 255);
		
		particleSystem = new ParticleSystem(10000);
		globalStartTime = System.nanoTime();

        ParticleEmitter emitter;

		emitter = new ParticleEmitter( new Vector(-10.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		emitter.numberOfParticlesToEmit = 100;

        live.add(emitter);

		emitter = new ParticleEmitter( new Vector(0.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		emitter.numberOfParticlesToEmit = 50;

        live.add(emitter);

		emitter = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		emitter.numberOfParticlesToEmit = 25;

		live.add(emitter);


        for(int i = 0; i < 24; ++i) {
            emitter = new ParticleEmitter(new Vector(10.0f, 18.0f, 0.0f),
                    particleDirection,
                    Color.rgb(100, 100, 100),
                    angleVarianceInDegrees,
                    speedVariance);

            pool.add(emitter);
        }
	}
	
	public void addParticleEmitterAt(float x, float y, int type) {
		//System.out.println("in MineRaiderRenderer add Particle Emitter at: " + x + ", " + y);
        int size = pool.size();
		if (size > 0) {
			ParticleEmitter pe = pool.remove(size - 1);
			pe.position.x = x;
            pe.position.y = y;
            pe.position.z = -2f;
			pe.numberOfParticlesToEmit = 10;
			pe.color = Color.rgb(100, 100, 100); //visuals.colorFromGemType(gemType);
			live.add(pe);
		}
	}
	
	public void draw() {
		float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

        int size = live.size();
        ParticleEmitter emitter;
        for (int i = 0; i < size; ++i) {
            emitter = live.get(i);
            if (emitter.isDone()) {
                remove.add(emitter);
            } else {
                emitter.addParticles(particleSystem, currentTime, 1);
            }
        }

        size = remove.size();
        for(int i = 0; i < size; ++i) {
            emitter = remove.get(i);
            pool.add(emitter);
            live.remove(emitter);
		}
        remove.clear();

		graphics.particleShader.useProgram();
		graphics.particleShader.setTexture(graphics.textureParticle);
		graphics.particleShader.setUniforms(graphics.viewProjectionMatrix, currentTime);
		particleSystem.bindData(graphics.particleShader);
		
		particleSystem.draw();
	}
	
	
}
