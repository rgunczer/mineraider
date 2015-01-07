package com.almagems.mineraider.data;

import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.GL_FLOAT;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.almagems.mineraider.Constants;

public class VertexBuffer {

	private final int bufferId;
	
	public VertexBuffer(float[] vertexData) {
		// allocate buffer
		final int buffers[] = new int[1];
		glGenBuffers(buffers.length, buffers, 0);
		
		if (buffers[0] == 0) {
			throw new RuntimeException("Could not create vertex buffer");
		}
		
		bufferId = buffers[0];
		
		// bind the buffer
		glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);

		// Transfer data to native memory
		FloatBuffer vertexArray = ByteBuffer
				.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer()
				.put(vertexData);
		vertexArray.position(0);
		
		// Transfer data from native memory to GPU buffer
		glBufferData(GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT, vertexArray, GL_STATIC_DRAW);
		
		// IMPORTANT: unbind from the buffer when we're done with it
		glBindBuffer(GL_ARRAY_BUFFER, 0);		
	}
	
	public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
		glBindBuffer(GL_ARRAY_BUFFER, bufferId);
		glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, dataOffset);
		glEnableVertexAttribArray(attributeLocation);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, bufferId);
	}
	
	public void unbind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
}
