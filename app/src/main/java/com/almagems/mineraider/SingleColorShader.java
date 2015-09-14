package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class SingleColorShader extends BaseShader {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 4;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT +
                                       COLOR_COMPONENT_COUNT +
                                       TEXTURE_COORDINATES_COMPONENT_COUNT +
                                       NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	// uniform locations
	private final int uMatrixLocation;
	private final int uColorLocation;
	
	// attribute locations
	private final int aPositionLocation;
		
	public SingleColorShader() { //throws Exception {
		super(R.raw.single_color_vertex_shader, R.raw.single_color_fragment_shader);
		
		// retrieve uniform locations for the shader program
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		
		// retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
	}
	
	public void setUniforms(float[] matrix, Color color) {
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniform4f(uColorLocation, color.r, color.g, color.b, color.a);
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

    public void bindData(VertexArray vertexArray) {
        vertexArray.setVertexAttribPointer(
                0,
                getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }


}
