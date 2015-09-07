package com.almagems.mineraider;


public abstract class Scene {

    protected enum SwipeDir {
        SwipeNone,
        SwipeLeft,
        SwipeRight,
        SwipeUp,
        SwipeDown
    }

    protected SwipeDir swipeDir;

    protected float touchDownX;
    protected float touchDownY;


}
