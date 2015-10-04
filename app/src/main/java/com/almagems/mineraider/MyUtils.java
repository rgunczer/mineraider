package com.almagems.mineraider;


public final class MyUtils {

	public static final RandomXS128 rand = new RandomXS128();

	public static int randInt(int min, int max) {
	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    return rand.nextInt((max - min) + 1) + min;
	}	
	
	public static float clamp(float value, float min, float max) {
		return Math.min(max, Math.max(min, value));
	}

    public static float lerp(float fromValue, float toValue, float progress) {        
        return fromValue + (toValue - fromValue) * progress;

    }

}
