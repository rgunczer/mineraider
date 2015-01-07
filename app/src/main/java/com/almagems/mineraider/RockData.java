package com.almagems.mineraider;

public class RockData {
	public int rockId;
	public float x;
	public float y;
	public float z;
	public float degree;
	
	public RockData() {
		rockId = 0;
		x = 0f;
		y = 0f;
		z = -1.75f;
		degree = 0f;			
	}
				
	public String toString() {
        return "drawRock(visuals.rock" + rockId + ", " + x + "f, " + y + "f, " + z + "f, " + degree + "f);";                              
    }	
}
