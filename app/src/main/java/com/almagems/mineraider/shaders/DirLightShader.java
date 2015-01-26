package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.*;
import android.content.Context;

import com.almagems.mineraider.R;
import com.almagems.mineraider.Visuals;


public class DirLightShader extends BaseShader {	
	// uniform locations
	private final int uMVPMatrixLocation;
	private final int uMVMatrixLocation;
	private final int uLightPosLocation;
	private final int uColorLocation;
	
	// attribute locations
	private final int aPositionLocation;
	private final int aNormalLocation;
	private final int aTextureCoordinatesLocation;

	
	public DirLightShader(Context context) throws Exception {
		super(context, R.raw.dir_light_vertex_shader, R.raw.dir_light_fragment_shader);
		
		// retrieve uniform locations for the shader program
		uMVPMatrixLocation = glGetUniformLocation(program, "u_MVPMatrix");
		uMVMatrixLocation = glGetUniformLocation(program, "u_MVMatrix");		
		uLightPosLocation = glGetUniformLocation(program, "u_LightPos");
		uColorLocation = glGetUniformLocation(program, "u_Color");
		
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);		
				
		// retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, "a_Position");
		aNormalLocation = glGetAttribLocation(program, "a_Normal");
		aTextureCoordinatesLocation = glGetAttribLocation(program, "a_TextureCoordinates");
	}
		
	public void setUniforms() {
        Visuals visuals = Visuals.getInstance();
		glUniform4f(uColorLocation, visuals.whiteColor.r, visuals.whiteColor.g, visuals.whiteColor.b, visuals.whiteColor.a);
		glUniformMatrix4fv(uMVPMatrixLocation, 1, false, visuals.mvpMatrix, 0);
		glUniformMatrix4fv(uMVMatrixLocation, 1, false, visuals.mvMatrix, 0);
		glUniform3f(uLightPosLocation, 	visuals.mLightPosInEyeSpace[0], 
										visuals.mLightPosInEyeSpace[1], 
										visuals.mLightPosInEyeSpace[2]);			
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}
	
	public int getNormalAttributeLocation() {
		return aNormalLocation;
	}
	
	public int getTextureAttributeLocation() {
		return aTextureCoordinatesLocation;
	}	
}
