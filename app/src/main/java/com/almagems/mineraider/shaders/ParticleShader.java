package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUniform1f;


import android.content.Context;

import com.almagems.mineraider.R;


public class ParticleShader extends BaseShader {

	// uniform locations
	private final int uMatrixLocation;
	private final int uTimeLocation;
		
	// attribute locations
	private final int aPositionLocation;
	private final int aColorLocation;
	private final int aDirectVectorLocation;
	private final int aParticleStartTimeLocation;
	
	// ctor
	public ParticleShader(Context context) throws Exception {
		super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);
	
		// retrieve uniform locations for the shader program
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uTimeLocation = glGetUniformLocation(program, U_TIME);
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
		
		// retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aColorLocation = glGetAttribLocation(program, A_COLOR);
		aDirectVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
		aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME);
	}
		
	public void setUniforms(float[] matrix, float elapsedTime) {
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniform1f(uTimeLocation, elapsedTime);		
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}
	
	public int getColorAttributeLocation() {
		return aColorLocation;
	}
	
	public int getDirectionVectorAttributeLocation() {
		return aDirectVectorLocation;
	}
	
	public int getParticleStartTimeAttributeLocation() {
		return aParticleStartTimeLocation;
	}
}
