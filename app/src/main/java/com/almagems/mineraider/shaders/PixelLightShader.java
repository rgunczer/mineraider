package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.*;

import com.almagems.mineraider.R;
import com.almagems.mineraider.util.Vector;

import android.content.Context;

public class PixelLightShader extends BaseShader {

	// uniform locations [vertex shader]
	private final int uProjection;
	private final int uModelview;
	private final int uNormalMatrix;
		
	// uniform location [fragment shader]
	private final int uLightPosition;
	private final int uAmbientMaterial;
	private final int uSpecularMaterial;
	private final int uShininess;	
	
	// attribute locations
	private final int aPosition;
	private final int aNormal;
	private final int aDiffuseMaterial;
	
	// ctor
	public PixelLightShader(Context context) throws Exception {
		super(context, R.raw.pixel_lighting_vertex_shader, R.raw.pixel_lighting_fragment_shader);

		// retrieve uniform locations for the shader program [vertex shader]
		uProjection = glGetUniformLocation(program, "Projection");
		uModelview = glGetUniformLocation(program, "Modelview");
		uNormalMatrix = glGetUniformLocation(program, "NormalMatrix");
		
		// retrieve uniform locations for the shader program [fragment shader]
		uLightPosition = glGetUniformLocation(program, "LightPosition");
		uAmbientMaterial = glGetUniformLocation(program, "AmbientMaterial");
		uSpecularMaterial = glGetUniformLocation(program, "SpecularMaterial");
		uShininess = glGetUniformLocation(program, "Shininess");
		
		// retrieve attribute locations for the shader program
		aPosition = glGetAttribLocation(program, "Position");
		aNormal = glGetAttribLocation(program, "Normal");
		aDiffuseMaterial = glGetAttribLocation(program, "DiffuseMaterial");
		
		// setup some default material params
		glUniform3f(uAmbientMaterial, 0.04f, 0.04f, 0.04f);
		glUniform3f(uSpecularMaterial, 0.5f, 0.5f, 0.5f);
		glUniform1f(uShininess, 50.0f);
	}
	
	public void setUniforms(float[] matrixProjection,
							float[] matrixModelview,
							float[] matrixNormalMatrix,
							Vector lightPos) {
		glUniformMatrix4fv(uProjection, 1, false, matrixProjection, 0);
		glUniformMatrix4fv(uModelview, 1, false, matrixModelview, 0);
		glUniformMatrix3fv(uNormalMatrix, 1, false, matrixNormalMatrix, 0);
		
		glUniform3f(uLightPosition, lightPos.x, lightPos.y, lightPos.z);
		
		
		glVertexAttrib4f(aDiffuseMaterial, 1.0f, 0.0f, 0.0f, 1.0f);
	}	
	
	public int getPositionAttributeLocation() {
		return aPosition;
	}
	
	public int getNormalAttributeLocation() {
		return aNormal;
	}	
}
