package com.almagems.mineraider.particlesystem;

import static android.opengl.GLES20.*;

import java.util.ArrayList;

import android.graphics.Color;

import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.Vector;


public class ParticleManager {
	private static ParticleManager instance = null;

    private final Visuals visuals;
	private long globalStartTime;
	final Vector particleDirection = new Vector(2.0f, 0.0f, 0.0f);
	final float angleVarianceInDegrees = 5f;
	final float speedVariance = 1.6f;

    public ArrayList<ParticleEmitter> remove = new ArrayList<ParticleEmitter>(8);
	public ArrayList<ParticleEmitter> pool = new ArrayList<ParticleEmitter>(32);
	public ArrayList<ParticleEmitter> live = new ArrayList<ParticleEmitter>(16);
	
	private ParticleSystem particleSystem;
	
	// ctor
	private ParticleManager() {
        visuals = Visuals.getInstance();
	}
		
	public static ParticleManager getInstance() {
		if (instance == null) {
			instance = new ParticleManager(); 
		}
		return instance;
	}	
	
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
