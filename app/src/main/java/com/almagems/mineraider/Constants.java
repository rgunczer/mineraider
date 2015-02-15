package com.almagems.mineraider;


import com.almagems.mineraider.util.Rectangle;

public class Constants {
    public static final int BYTES_PER_SHORT = 2;
	public static final int BYTES_PER_FLOAT = 4;
	public static final int MAX_GEM_TYPES = 7;

	public static final int GEM_TYPE_NONE = -1;
	public static final int GEM_TYPE_0 = 0;
	public static final int GEM_TYPE_1 = 1;
	public static final int GEM_TYPE_2 = 2;
	public static final int GEM_TYPE_3 = 3;
	public static final int GEM_TYPE_4 = 4;
	public static final int GEM_TYPE_5 = 5;
	public static final int GEM_TYPE_6 = 6;	
	
	
	public static final float GEM_FRAGMENT_SIZE = 0.5f; //2.2f;
	public static final float GEM_DENSITY = 0.3f;
	
	public static final boolean DRAW_BUFFER_BOARD = false;
	
	public static final float EPSILON = 0.00000001f;
		
	public static final String adUnitId = "ca-app-pub-1002179312870743/4200681514";

    public static final int MAX_HELMET_TYPES = 4;

    public static final Rectangle rectBlueHelmet = new Rectangle(0f, 0f, 256f, 256f);
    public static final Rectangle rectGreenHelmet = new Rectangle(0f, 256f, 256f, 256f);
    public static final Rectangle rectRedHelmet = new Rectangle(256f, 256f, 256f, 256f);
    public static final Rectangle rectYellowHelmet = new Rectangle(256f, 0f, 256f, 256f);

    public static final int RED_HELMET = 0;
    public static final int GREEN_HELMET = 1;
    public static final int BLUE_HELMET = 2;
    public static final int YELLOW_HELMET = 3;

    public static enum ScenesEnum {
        None,
        Menu,
        HelmetSelect,
        Shaft,
        Level,
        Loading,
    }

/*
    public static enum HelmetTypes {
        RedHelmet(0),
        GreenHelmet(1),
        YellowHelmet(2),
        BlueHelmet(3);

        private final int value;
        private HelmetTypes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
*/

}
