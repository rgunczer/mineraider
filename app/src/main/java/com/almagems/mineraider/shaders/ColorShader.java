package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUniform4f;
import static com.almagems.mineraider.Constants.BYTES_PER_FLOAT;

import android.content.Context;

import com.almagems.mineraider.R;
import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.util.MyColor;


public class ColorShader extends BaseShader {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

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
		glUniform4f(uColorLocation, color.r, color.g, color.b, color.a);
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

    public void bindData(VertexArray vertexArray) {
        vertexArray.setVertexAttribPointer(0,
                getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
    }


}
