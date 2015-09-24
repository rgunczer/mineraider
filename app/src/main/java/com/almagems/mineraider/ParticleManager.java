package com.almagems.mineraider;

import java.util.ArrayList;

import android.graphics.Color;


public final class ParticleManager {

    public static Graphics graphics;

	private long globalStartTime;
	final Vector particleDirection = new Vector(2f, 0f, 0f);
	final float angleVarianceInDegrees = 5f;
	final float speedVariance = 1.6f;

    public final ArrayList<ParticleEmitter> remove = new ArrayList<ParticleEmitter>(8);
	public final ArrayList<ParticleEmitter> pool = new ArrayList<ParticleEmitter>(32);
	public final ArrayList<ParticleEmitter> live = new ArrayList<ParticleEmitter>(16);
	
	private final ParticleSystem particleSystem;


    // ctor
    public ParticleManager() {
        particleSystem = new ParticleSystem(10000);
    }

	public void init() {
		//int color = Color.rgb(255, 255, 255);
		globalStartTime = System.nanoTime();

        ParticleEmitter emitter;
/*
		emitter = new ParticleEmitter( new Vector(-10.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		emitter.numberOfParticlesToEmit = 1;

        live.add(emitter);

		emitter = new ParticleEmitter( new Vector(0.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		emitter.numberOfParticlesToEmit = 1;

        live.add(emitter);

		emitter = new ParticleEmitter( new Vector(10.0f, 18.0f, 0.0f),
				particleDirection, 
				color,
				angleVarianceInDegrees,
				speedVariance );
		emitter.numberOfParticlesToEmit = 1;

		live.add(emitter);
*/

        for(int i = 0; i < 24; ++i) {
            emitter = new ParticleEmitter(new Vector(10.0f, 18.0f, 0.0f),
                    particleDirection,
                    Color.rgb(100, 100, 100),
                    angleVarianceInDegrees,
                    speedVariance);

            pool.add(emitter);
        }
	}

	public void addParticleEmitterAtWastedGems(float x, float y, int type) {
		//System.out.println("in MineRaiderRenderer add Particle Emitter at: " + x + ", " + y);
		final int size = pool.size();
		if (size > 0) {
			ParticleEmitter pe = pool.remove(size - 1);
			pe.position.x = x;
			pe.position.y = y;
			pe.position.z = 0f;
			pe.numberOfParticlesToEmit = 3;
			pe.color = Color.rgb(60, 0, 0); //visuals.colorFromGemType(gemType);
			live.add(pe);
		}
	}

	public void addParticleEmitterAt(float x, float y, int type) {
		//System.out.println("in MineRaiderRenderer add Particle Emitter at: " + x + ", " + y);
        final int size = pool.size();
		if (size > 0) {
			ParticleEmitter pe = pool.remove(size - 1);
			pe.position.x = x;
            pe.position.y = y;
            pe.position.z = -2f;
			pe.numberOfParticlesToEmit = 9;
			pe.color = Color.rgb(75, 75, 75); //visuals.colorFromGemType(gemType);
			//pe.color = Color.rgb(100, 73, 43);
            //pe.color = Color.rgb(46, 42, 27);
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
		graphics.particleShader.setTexture(Graphics.textureParticle);
		graphics.particleShader.setUniforms(graphics.viewProjectionMatrix, currentTime);
		particleSystem.bindData(graphics.particleShader);
		
		particleSystem.draw();
	}
	
	
}
