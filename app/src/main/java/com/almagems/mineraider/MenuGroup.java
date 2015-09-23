package com.almagems.mineraider;

import java.util.ArrayList;

import static android.opengl.GLES20.*;


public final class MenuGroup {

    public ArrayList<MenuItem> items = new ArrayList<MenuItem>(10);
    public ArrayList<MenuImage> images = new ArrayList<MenuImage>(10);

    public ProgressBarControl musicVolumeControl = null;
    public ProgressBarControl soundVolumeControl = null;

    public String name;
    public static Graphics graphics;
    private MenuGameTitleAnim gameTitleAnim;


    public void init(String name) {
        this.name = name;
    }

    public void add(MenuItem menuItem) {
        items.add(menuItem);
    }

    public void add(MenuImage menuImage) {
        images.add(menuImage);
    }

    public void reset() {
        if (gameTitleAnim != null) {
            gameTitleAnim.reset();
        }
    }

    public void add(MenuGameTitleAnim anim) {
        this.gameTitleAnim = anim;
    }

    public void update() {
        MenuItem menuItem;
        final int size = items.size();
        for(int i = 0; i < size; ++i) {
            menuItem = items.get(i);
            menuItem.update();
        }

        if (gameTitleAnim != null) {
            gameTitleAnim.update();
        }

        if (musicVolumeControl != null && soundVolumeControl != null) {
            musicVolumeControl.value = Engine.audio.musicVolume;
            soundVolumeControl.value = Engine.audio.soundVolume;
        }
    }

    public void draw() {
        int size;

        MenuItem menuItem;
        size = items.size();
        for(int i = 0; i < size; ++i) {
            menuItem = items.get(i);
            menuItem.draw();
        }

        MenuImage menuImage;
        size = images.size();
        for(int i = 0; i < size; ++i) {
            menuImage = images.get(i);
            menuImage.draw();
        }

        if (gameTitleAnim != null) {
            gameTitleAnim.draw();
        }

        if (musicVolumeControl != null && soundVolumeControl != null) {
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_BLEND);

            glDisable(GL_DEPTH_TEST);
            glEnable(GL_BLEND);

            //visuals.setProjectionMatrix2D();
            //visuals.updateViewProjMatrix();
            graphics.bindNoTexture();
            graphics.singleColorShader.useProgram();

            musicVolumeControl.draw();
            soundVolumeControl.draw();
        }
    }

    public MenuItem handleTouchPress(float normalizedX, float normalizedY) {
        final Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, graphics.invertedViewProjectionMatrix);

        MenuItem item;
        int size = items.size();
        for (int i = 0; i < size; ++i) {
            item = items.get(i);
            if (item.isHit(pos.x, pos.y)) {
                return item;
            }
        }
        return null;
    }

}
