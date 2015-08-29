package com.almagems.mineraider.menu;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.Geometry;
import com.almagems.mineraider.util.Vector;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class MenuGroup {

    public ArrayList<MenuItem> items = new ArrayList<MenuItem>(10);
    public ArrayList<MenuImage> images = new ArrayList<MenuImage>(10);

    public MenuVolumeControl musicVolumeControl = null;
    public MenuVolumeControl soundVolumeControl = null;

    public String name;

    private MenuGameTitleAnim gameTitleAnim;

    public MenuGroup() {
        System.out.println("MenuGroup ctor...");
    }

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
        int size = items.size();
        for(int i = 0; i < size; ++i) {
            menuItem = items.get(i);
            menuItem.update();
        }

        if (gameTitleAnim != null) {
            gameTitleAnim.update();
        }

        if (musicVolumeControl != null && soundVolumeControl != null) {
            musicVolumeControl.volumeValue = ClassicSingleton.getInstance().audio.musicVolume;
            soundVolumeControl.volumeValue = ClassicSingleton.getInstance().audio.soundVolume;
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

            Visuals visuals = Visuals.getInstance();

            visuals.setProjectionMatrix2D();
            visuals.updateViewProjMatrix();
            visuals.bindNoTexture();



            musicVolumeControl.draw();
            soundVolumeControl.draw();
        }
    }

    public MenuItem handleTouchPress(float normalizedX, float normalizedY) {
        Visuals visuals = Visuals.getInstance();
        Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, visuals.invertedViewProjectionMatrix);

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

/*
        if (menuItemPlay.isHit(pos.x, pos.y)) {
            gameState = GameState.ButtonAnim;
            selectedMenuOption = MenuOptions.Play;
            menuAnimCurrentValue = menuItemPlay.pos.sy;
            menuAnimEndValue = menuAnimCurrentValue + 0.1f;
        } else if (menuItemOptions.isHit(pos.x, pos.y)) {
            gameState = GameState.ButtonAnim;
            selectedMenuOption = MenuOptions.Options;
            menuAnimCurrentValue = menuItemOptions.pos.sy;
            menuAnimEndValue = menuAnimCurrentValue + 0.1f;
        } else if (menuItemHelp.isHit(pos.x, pos.y)) {
            gameState = GameState.ButtonAnim;
            selectedMenuOption = MenuOptions.Help;
            menuAnimCurrentValue = menuItemHelp.pos.sy;
            menuAnimEndValue = menuAnimCurrentValue + 0.1f;
        } else {
            selectedMenuOption = MenuOptions.None;
        }
*/


}
