package com.almagems.mineraider;

import static android.opengl.GLES20.*;

import static com.almagems.mineraider.Constants.*;


public final class Model {

	private static final int POSITION_COMPONENT_COUNT = 3;              // x, y, z
	private static final int NORMAL_COMPONENT_COUNT = 3;                // nx, ny, nz
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;   // u v
    private static final int FLOATS_PER_VERTEX = POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
	private static final int STRIDE = (	POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT +	TEXTURE_COORDINATES_COMPONENT_COUNT ) * BYTES_PER_FLOAT;
	
	public String name;
	
	private final float[] vertexData;	
	private final VertexArray vertexArray;
	//private final VertexBuffer vertexArray;
	//private final VertexBuffer vertexArray;

	private final int startVertex;
	public final int numVertices;	

    // ctor
	public Model(ModelLoader modelLoader) {
		name = modelLoader.name;
		
		boolean hasTextureCoords = modelLoader.hasTextureCoords();
		
		int size = modelLoader.vertices.size();
		vertexData = new float[size * FLOATS_PER_VERTEX];
			
		int offset = 0;
		startVertex = 0;
		numVertices = modelLoader.vertices.size();
		
		for (Vertex v : modelLoader.vertices) {
			vertexData[offset++] = v.x;
			vertexData[offset++] = v.y;
			vertexData[offset++] = v.z;
						
			vertexData[offset++] = v.nx;
			vertexData[offset++] = v.ny;
			vertexData[offset++] = v.nz;
			
			if (hasTextureCoords) {
				vertexData[offset++] = v.u;
				vertexData[offset++] = v.v;
			} else {
				//System.out.println("Our guy!");
			}
		}		
		
		vertexArray = new VertexArray(vertexData);
		//vertexArray = new VertexBuffer(vertexData);
	}
	
	public void draw() {
		glDrawArrays(GL_TRIANGLES, startVertex, numVertices);
	}
	
	public void bindData(SingleColorShader colorProgram) {
        //vertexArray.bind();
		vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
	}
	
	public void bindData(TextureShader textureProgram) {
        //vertexArray.bind();
		vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
		
		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT, 
				textureProgram.getTextureAttributeLocation(), 
				TEXTURE_COORDINATES_COMPONENT_COUNT, 
				STRIDE);
	}
	
	public void bindData(DirLightShader shader) {
        //vertexArray.bind();
		vertexArray.setVertexAttribPointer(
                0,
                shader.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

		vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                shader.getNormalAttributeLocation(),
				NORMAL_COMPONENT_COUNT, 
				STRIDE);
		
		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT, 
				shader.getTextureAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT, 
				STRIDE);
	}

	public void bindData(PointLightShader shader) {
        //vertexArray.bind();
		vertexArray.setVertexAttribPointer(
                0,
                shader.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT, 
				shader.getNormalAttributeLocation(), 
				NORMAL_COMPONENT_COUNT, 
				STRIDE);
		
		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT, 
				shader.getTextureAttributeLocation(), 
				TEXTURE_COORDINATES_COMPONENT_COUNT, 
				STRIDE);
	}	
	
	public void bindData(PixelLightShader pixelProgram) {
        //vertexArray.bind();
		vertexArray.setVertexAttribPointer(0,
                pixelProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

		vertexArray.setVertexAttribPointer(	POSITION_COMPONENT_COUNT, 
											pixelProgram.getNormalAttributeLocation(), 
											NORMAL_COMPONENT_COUNT, 
											STRIDE);
	}
	
	public void bindData(NormalColorShader shader) {
        //vertexArray.bind();
		final int stride = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;
		vertexArray.setVertexAttribPointer(	0, 
											shader.getPositionAttributeLocation(), 
											POSITION_COMPONENT_COUNT, 
											stride);

		vertexArray.setVertexAttribPointer(	POSITION_COMPONENT_COUNT, 
											shader.getNormalAttributeLocation(), 
											NORMAL_COMPONENT_COUNT, 
											stride);
	}

    public void unbind() {
		//vertexArray.unbind();
    }

}
