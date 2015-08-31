package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.*;

import android.content.Context;

import com.almagems.mineraider.util.ShaderHelper;
import com.almagems.mineraider.util.TextResourceReader;
import com.almagems.mineraider.visuals.Visuals;

abstract class BaseShader {

	protected Visuals visuals;

	// uniform constants
	protected static final String U_MATRIX = "u_MvpMatrix";
	protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
	protected static final String U_COLOR = "u_Color";
	protected static final String U_TIME = "u_Time";
	
	// attribute constants
	protected static final String A_POSITION = "a_Position";
	protected static final String A_COLOR = "a_Color";
	protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
	protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
	protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
	
	protected final int program;
	protected int uTextureUnitLocation;
	
	protected BaseShader(Visuals visuals, int vertexShaderResourceId, int fragmentShaderResourceId) { //throws Exception {
        this.visuals = visuals;
		program = buildProgram(visuals.context, vertexShaderResourceId, fragmentShaderResourceId);
	}
	
	private int buildProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) { //throws Exception {
		//try {			
			int program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
													TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
			return program;
//		} catch (Exception ex) {
//			System.out.println(ex.toString());
//		}
//		return program;
	}
	
	public void useProgram() {
		glUseProgram(program);
	}
	
	public void setTexture(int textureId) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glUniform1i(uTextureUnitLocation, 0);
	}
	
	
}
