package com.almagems.mineraider;

public class RockData {
	public int rockId;

    public float x;
	public float y;
	public float z;

    public float rx;
    public float ry;
    public float rz;

    public float sx;
    public float sy;
    public float sz;

    public float degree;
	
	public RockData() {
		rockId = 0;
		x = 0f;
		y = 0f;
		z = -1.75f;

        rx = 0f;
        ry = 0f;
        rz = 0f;

        sx = 1f;
        sy = 1f;
        sz = 1f;

		degree = 0f;			
	}

    public RockData(int rockId,
                    float tx, float ty, float tz,
                    float rx, float ry, float rz,
                    float sx, float sy, float sz) {

        this.rockId = rockId;
        x = tx;
        y = ty;
        z = tz; //-1.75f;

        this.rx = rx;
        this.ry = ry;
        this.rz = rz;

        this.sx = sx;
        this.sy = sy;
        this.sz = sz;

        degree = 0f;
    }


	public String toString() {
        return "drawRock(visuals.rock" + rockId + ", " + x + "f, " + y + "f, " + z + "f, " + degree + "f);";                              
    }	
}
