package com.almagems.mineraider;

import static android.opengl.GLES20.*;

public final class NormalColorShader extends BaseShader {

	// uniform locations
	private final int uMatrixLocation;
	private final int uColorLocation;
	private final int uLightColorLocation;
	private final int uLightDirectionLocation;
	private final int uNormalMatrixLocation;
	
	// attribute locations
	private final int aPositionLocation;
	private final int aNormalLocation;
		
	public NormalColorShader() { //throws Exception {
		super(R.raw.normal_color_vertex_shader, R.raw.normal_color_fragment_shader);
		
		// retrieve uniform locations for the shader program
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uNormalMatrixLocation = glGetUniformLocation(program, "u_NormalMatrix");
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		uLightColorLocation = glGetUniformLocation(program, "u_LightColor");
		uLightDirectionLocation = glGetUniformLocation(program, "u_LightDirection");
				
		// retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, "a_Position");
		aNormalLocation = glGetAttribLocation(program, "a_Normal");		
	}
	
	public void setUniforms(float[] matrix, Color color, Color lightColor, Vector lightDir) {
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniformMatrix4fv(uNormalMatrixLocation, 1, false, graphics.normalMatrix, 0);
		glUniform4f(uColorLocation, color.r, color.g, color.b, 1f);
		glUniform3f(uLightColorLocation, lightColor.r, lightColor.g, lightColor.b);
		glUniform3f(uLightDirectionLocation, lightDir.x, lightDir.y, lightDir.z);		
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}
		
	public int getNormalAttributeLocation() {
		return aNormalLocation;
	}
}
