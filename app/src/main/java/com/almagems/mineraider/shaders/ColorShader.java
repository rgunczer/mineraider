package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUniform4f;
import android.content.Context;

import com.almagems.mineraider.R;
import com.almagems.mineraider.util.MyColor;


public class ColorShader extends BaseShader {
	// uniform locations
	private final int uMatrixLocation;
	private final int uColorLocation;
	
	// attribute locations
	private final int aPositionLocation;
		
	public ColorShader(Context context) throws Exception {
		super(context, R.raw.color_vertex_shader, R.raw.color_fragment_shader);
		
		// retrieve uniform locations for the shader program
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		
		// retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
	}
	
	public void setUniforms(float[] matrix, MyColor color) {
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniform4f(uColorLocation, color.r, color.g, color.b, 1f);
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}
	
}
