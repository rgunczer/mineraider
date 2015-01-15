package com.almagems.mineraider.particlesystem;

import java.util.Random;

import static android.opengl.Matrix.setRotateEulerM;
import static android.opengl.Matrix.multiplyMV;

import android.graphics.Color;

import com.almagems.mineraider.util.Vector;

public class ParticleEmitter {
	public Vector position;
	public int color;
	private final float angleVariance;
	private final float speedVariance;
	
	private final Random random = new Random();
	
	private float[] rotationMatrix = new float[16];
	private float[] directionVector = new float[4];
	private float[] resultVector = new float[4];
	
	public int numberOfParticlesToEmit;
	
	public ParticleEmitter( Vector position,
							Vector direction, 
							int color,
							float angleVarianceInDegrees, 
							float speedVariance) {
		this.position = position;
		this.color = color;
		
		this.angleVariance = angleVarianceInDegrees;
		this.speedVariance = speedVariance;
		
		directionVector[0] = direction.x;
		directionVector[1] = direction.y;
		directionVector[2] = direction.z;
	}
	
	public boolean isDone() {
		return numberOfParticlesToEmit < 0;
	}
	
	public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {		
//		int r = Color.red(color);
//		int g = Color.green(color);
//		int b = Color.blue(color);
		
		//System.out.println("Particle: " + r + "," + g + "," + b);
				
		numberOfParticlesToEmit -= count; 
		for (int i = 0; i < count; ++i) {			
			setRotateEulerM(rotationMatrix, 0,
							(random.nextFloat() - 0.5f) * angleVariance,
							(random.nextFloat() - 0.5f) * angleVariance,
							(random.nextFloat() - 0.5f) * angleVariance);
			
			multiplyMV(
					resultVector, 0,
					rotationMatrix, 0,
					directionVector, 0);
			
			float speedAdjustment = 1f + random.nextFloat() * speedVariance;
			
			Vector thisDirection = new Vector(
					(random.nextFloat() - 0.5f) * speedAdjustment,  //resultVector[0] * speedAdjustment, 
					(random.nextFloat() - 0.5f) * speedAdjustment, // resultVector[1] * speedAdjustment,
					resultVector[2] * speedAdjustment);
						
			particleSystem.addParticle(position, color, thisDirection, currentTime);
		}
	}
}
