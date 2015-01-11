package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import android.content.Context;


import com.almagems.mineraider.R;

public class TextureShader extends BaseShader {
	// uniform locations
	private final int uMatrixLocation;
		
	// attribute locations
	private final int aPositionLocation;
    private final int aColorLocation;
	private final int aTextureCoordinatesLocation;
	
	public TextureShader(Context context) throws Exception {
		super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
		
		// retrieve uniform locations for the shader program
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
		
		// retrieve attribute location for the shader program
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, "a_Color");
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
	}

	public void setUniforms(float[] matrix) {
		// pass the matrix into the shader program
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);		
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}
	public int getColorAttributeLocation() { return aColorLocation; }
	public int getTextureAttributeLocation() {
		return aTextureCoordinatesLocation;
	}
}
