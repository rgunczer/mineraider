package com.almagems.mineraider;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.almagems.mineraider.Constants.*;


public final class VertexBuffer {

	private final int vbo;

	// ctor
	public VertexBuffer(float[] vertexData) {
        FloatBuffer vertexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexArray.put(vertexData).position(0);


		final int tmp[] = new int[1];
		glGenBuffers(1, tmp, 0);
		vbo = tmp[0];

        if (vbo < 1) {
            System.out.println("VBO Error");
        }

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexArray.capacity() * BYTES_PER_FLOAT, vertexArray, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);		
	}
	
	public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        //glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, dataOffset * BYTES_PER_FLOAT);
        glEnableVertexAttribArray(attributeLocation);
        //glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
	}
	
	public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
}
