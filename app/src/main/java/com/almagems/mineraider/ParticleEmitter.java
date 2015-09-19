package com.almagems.mineraider;

import static android.opengl.Matrix.*;


public final class ParticleEmitter {

	public Vector position;
    private final Vector direction = new Vector();
	public int color;
	private final float angleVariance;
	private final float speedVariance;

	//private final Random random = new Random();

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

		RandomXS128 random = MyUtils.rand;

		numberOfParticlesToEmit -= count;
        float speedAdjustment;
		for (int i = 0; i < count; ++i) {			
			setRotateEulerM(rotationMatrix, 0,
							(random.nextFloat() - 0.5f) * angleVariance,
							(random.nextFloat() - 0.5f) * angleVariance,
							(random.nextFloat() - 0.5f) * angleVariance);
			
			multiplyMV(
                    resultVector, 0,
                    rotationMatrix, 0,
                    directionVector, 0);
			
			speedAdjustment = 1f + random.nextFloat() * speedVariance;
			
			direction.x = (random.nextFloat() - 0.5f) * speedAdjustment;
            direction.y = (random.nextFloat() - 0.5f) * speedAdjustment;
            direction.z = resultVector[2] * speedAdjustment;
						
			particleSystem.addParticle(position, color, direction, currentTime);
		}
	}
}
