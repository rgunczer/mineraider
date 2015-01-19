package com.almagems.mineraider.util;

import java.util.Random;

public class MyUtils {

	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}	
	
	public static float clamp(float value, float min, float max) {
		return Math.min(max, Math.max(min, value));
	}

    public static float LERP(float a, float b, float t) {
        //#define LERP (a, b, t)(a + t * (b - a))
        return (a + t * (b - a));

    }
}
