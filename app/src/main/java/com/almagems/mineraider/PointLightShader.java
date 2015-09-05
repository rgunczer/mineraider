package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public class PointLightShader extends BaseShader {
	
	// uniform locations	
	private final int uModelViewLocation;	
	private final int uProjectionLocation;
	private final int uColorLocation;
				
	// attribute locations
	private final int aPositionLocation;
	private final int aNormalLocation;
	private final int aTextureCoordinatesLocation;		
	
	public PointLightShader() { // throws Exception {
		super(R.raw.point_light_vertex_shader, R.raw.point_light_fragment_shader);
		
		// retrieve uniform locations for the shader program
		uModelViewLocation = glGetUniformLocation(program, "modelView");
		uProjectionLocation = glGetUniformLocation(program, "projection");
		uColorLocation = glGetUniformLocation(program, "u_Color");
					
		uTextureUnitLocation = glGetUniformLocation(program, "u_TextureUnit");
						
		// retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, "vertexPosition");
		aNormalLocation = glGetAttribLocation(program, "vertexNormal");
		aTextureCoordinatesLocation = glGetAttribLocation(program, "textureCoordinates");		
	}
	
	public void setUniforms() {
		glUniformMatrix4fv(uModelViewLocation, 1, false, graphics.mvMatrix, 0);
		glUniformMatrix4fv(uProjectionLocation, 1, false, graphics.projectionMatrix, 0);
		
		glUniform4f(uColorLocation, graphics.whiteColor.r, graphics.whiteColor.g, graphics.whiteColor.b, graphics.whiteColor.a);
        //glUniform4f(uColorLocation, 0f, 0f, 0f, 1f);
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
