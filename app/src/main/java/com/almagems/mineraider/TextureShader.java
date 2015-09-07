package com.almagems.mineraider;

import static android.opengl.GLES20.*;

import static com.almagems.mineraider.Constants.*;


public final class TextureShader extends BaseShader {

	// uniform locations
	private final int uMatrixLocation;
		
	// attribute locations
	private final int aPositionLocation;
    private final int aColorLocation;
	private final int aTextureCoordinatesLocation;
	
	public TextureShader() { //throws Exception {
		super(R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
		
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
	
	public int getPositionAttributeLocation() {	return aPositionLocation; }
	public int getColorAttributeLocation() { return aColorLocation; }
	public int getTextureAttributeLocation() {
		return aTextureCoordinatesLocation;
	}

    public void bindData(VertexBuffer vb) {
        final int POSITION_COMPONENT_COUNT = 3;
        final int COLOR_COMPONENT_COUNT = 4;
        final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
        final int STRIDE = (POSITION_COMPONENT_COUNT +
                COLOR_COMPONENT_COUNT +
                TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

        vb.setVertexAttribPointer(
                0,
                getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vb.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT * BYTES_PER_FLOAT,
                getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);

        vb.setVertexAttribPointer(
                (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT,
                getTextureAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void bindData(VertexArray vertexArray) {
        final int POSITION_COMPONENT_COUNT = 3;
        final int COLOR_COMPONENT_COUNT = 4;
        final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
        final int STRIDE = (POSITION_COMPONENT_COUNT +
                            COLOR_COMPONENT_COUNT +
                            TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

        vertexArray.setVertexAttribPointer(
                0,
                getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT,
                getTextureAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

}
