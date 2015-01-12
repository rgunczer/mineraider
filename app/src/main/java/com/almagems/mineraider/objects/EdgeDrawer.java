package com.almagems.mineraider.objects;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glDrawArrays;
import static com.almagems.mineraider.Constants.BYTES_PER_FLOAT;

import com.almagems.mineraider.data.VertexArray;
import com.almagems.mineraider.shaders.ColorShader;
import com.almagems.mineraider.util.Vector;


public class EdgeDrawer {
	private static final int POSITION_COMPONENT_COUNT = 3;	
	private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT;	
	private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;
	
	private final float[] particles;
	private final VertexArray vertexArray;	
	
	private int index;
	private int lineCount;
	
	// ctor
	public EdgeDrawer(int maxLineCount) {
		particles = new float[maxLineCount * 2 * TOTAL_COMPONENT_COUNT];
		vertexArray = new VertexArray(particles);				
	}
	
	public void begin() {
		index = 0;
		lineCount = 0;
	}
	
	public void addLine(float fromX, float fromY, float fromZ,
                        float toX, float toY, float toZ) {
		int start = index;
		
		particles[index++] = fromX;
		particles[index++] = fromY;
		particles[index++] = fromZ;

		particles[index++] = toX;
		particles[index++] = toY;
		particles[index++] = toZ;
		
		++lineCount;
		vertexArray.updateBuffer(particles, start, 2 * TOTAL_COMPONENT_COUNT);
	}
	
	public void bindData(ColorShader colorProgram) {
		int dataOffset = 0;
		vertexArray.setVertexAttribPointer(	dataOffset, 
											colorProgram.getPositionAttributeLocation(), 
											POSITION_COMPONENT_COUNT, 
											STRIDE);
	}
	
	public void draw() {
		glDrawArrays(GL_LINES, 0, lineCount * 2);
	}
}