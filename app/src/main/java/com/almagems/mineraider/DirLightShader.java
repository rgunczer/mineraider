package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.BYTES_PER_FLOAT;


public final class DirLightShader extends BaseShader {
	// uniform locations
	private final int uMVPMatrixLocation;
	private final int uMVMatrixLocation;
	private final int uLightPosLocation;
	private final int uColorLocation;
	
	// attribute locations
	private final int aPositionLocation;
	private final int aNormalLocation;
	private final int aTextureCoordinatesLocation;

	
	public DirLightShader() { // throws Exception {
		super(R.raw.dir_light_vertex_shader, R.raw.dir_light_fragment_shader);
		
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
		glUniform4f(uColorLocation, graphics.whiteColor.r, graphics.whiteColor.g, graphics.whiteColor.b, graphics.whiteColor.a);
		glUniformMatrix4fv(uMVPMatrixLocation, 1, false, graphics.mvpMatrix, 0);
		glUniformMatrix4fv(uMVMatrixLocation, 1, false, graphics.mvMatrix, 0);
		glUniform3f(uLightPosLocation, 	graphics.mLightPosInEyeSpace[0],
										graphics.mLightPosInEyeSpace[1],
										graphics.mLightPosInEyeSpace[2]);
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

    public void bindData(VertexArray vertexArray) {
        final int POSITION_COMPONENT_COUNT = 3;
        final int NORMAL_COMPONENT_COUNT = 3;
        final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
        final int STRIDE = (POSITION_COMPONENT_COUNT +
                            NORMAL_COMPONENT_COUNT +
                            TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

        vertexArray.setVertexAttribPointer(
                0,
                getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                getNormalAttributeLocation(),
                NORMAL_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT,
                getTextureAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

}
