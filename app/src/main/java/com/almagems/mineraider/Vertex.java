package com.almagems.mineraider;

public final class Vertex {

	// vertex
	public float x;
	public float y;
	public float z;
	
	// texture
	public float u;
	public float v;
	
	// normal
	public float nx;
	public float ny;
	public float nz;
	
	public Vertex(	float x, float y, float z, 
					float u, float v, 
					float nx, float ny, float nz) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.u = u;
		this.v = v;
		
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}
}
