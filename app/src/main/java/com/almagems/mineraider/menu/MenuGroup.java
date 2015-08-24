package com.almagems.mineraider.menu;

import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.Geometry;
import com.almagems.mineraider.util.Vector;

import java.util.ArrayList;

public class MenuGroup {

    public ArrayList<MenuItem> items = new ArrayList<MenuItem>(10);
    public ArrayList<MenuImage> images = new ArrayList<MenuImage>(10);

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
