package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static com.almagems.mineraider.Constants.*;


public final class ColorShader extends BaseShader {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    // uniform locations
    private final int uMatrixLocation;

    // attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShader() {
        super(R.raw.color_vertex_shader, R.raw.color_fragment_shader);

        // retrieve uniform locations for the shader program
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        // retrieve attribute locations for the shader program
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    public void bindData(VertexArray vertexArray) {
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
    }

}


