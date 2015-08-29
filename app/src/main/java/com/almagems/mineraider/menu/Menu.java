package com.almagems.mineraider.menu;

import static android.opengl.GLES20.*;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.EffectAnims.Fade;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.Geometry;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Vector;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;

public class Menu {
    public static Visuals visuals;

    public enum MenuOptions {
        None,
        Play,
        Options,
        Help,
        Back
    }

    private MenuItem selectedMenuItem;
    private MenuOptions selectedMenuOption = MenuOptions.None;

    private final MenuItemAnim menuItemAnim = new MenuItemAnim();

    private ArrayList<MenuGroup> groups = new ArrayList<MenuGroup>(9);
    private MenuGroup currentGroup;

    private final Fade fade = new Fade();

    public Menu(Visuals visuals) {
        System.out.println("Menu ctor...");
        Menu.visuals = visuals;
    }

    public MenuOptions getSelectedMenuOption() {
        return selectedMenuOption;
    }

    private MenuGroup getMenuGroupByName(String name) {
        MenuGroup group;
        int size = groups.size();
        for(int i = 0; i < size; ++i) {
            group = groups.get(i);
            if (group.name == name) {
                return group;
            }
        }
        return null;
    }

    public void init() {
        selectedMenuItem = null;
        selectedMenuOption = MenuOptions.None;

        float sc;
        MenuGroup menuGroup;
        MenuItem menuItem;
        MenuImage menuImage;
        final boolean flipUTextureCoordinate = false;
        Rectangle rect;
        float aspect = Visuals.aspectRatio;
        sc = 1.76f;

        // main menu
        menuGroup = new MenuGroup();
        menuGroup.init("MainMenu");

        MenuGameTitleAnim gameTitleAnim = new MenuGameTitleAnim();
        gameTitleAnim.init(sc);
        menuGroup.add(gameTitleAnim);

        menuItem = new MenuItem();
        rect = new Rectangle(447, 763+98, 277, 98);
        menuItem.init("Play", MenuOptions.Play, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, aspect * 0.2f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuItem);

        menuItem = new MenuItem();
        rect = new Rectangle(0, 763+96, 447, 96);
        menuItem.init("Options", MenuOptions.Options, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.05f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuItem);

        menuItem = new MenuItem();
        rect = new Rectangle(1625, 520+94, 301, 94);
        menuItem.init("Help", MenuOptions.Help, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.3f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Help";
        menuGroup.add(menuItem);

        groups.add(menuGroup);

        currentGroup = menuGroup;

        // options
        menuGroup = new MenuGroup();
        menuGroup.init("Options");


        // back
        menuItem = new MenuItem();
        rect = new Rectangle(926, 520+94, 299, 94);
        menuItem.init("Back", MenuOptions.Back, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.4f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "MainMenu";
        menuGroup.add(menuItem);

        // music
        menuImage = new MenuImage();
        rect = new Rectangle(724, 763+74, 207, 74);
        menuImage.init("music", visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.5f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);

        // sound
        menuImage = new MenuImage();
        rect = new Rectangle(931, 763+75, 214, 75);
        menuImage.init("sound", visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.1f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);


        // music volume control
        menuGroup.musicVolumeControl = new MenuVolumeControl("Music");
        menuGroup.musicVolumeControl.init(aspect * 0.35f);

        menuGroup.soundVolumeControl = new MenuVolumeControl("Sound");
        menuGroup.soundVolumeControl.init(-aspect * 0.06f);

        groups.add(menuGroup);


        // Help
        menuGroup = new MenuGroup();
        menuGroup.name = "Help";

        // credits
        menuItem = new MenuItem();
        rect = new Rectangle(1225, 520+98, 400, 98);
        menuItem.init("Credits", MenuOptions.Back, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, aspect * 0.2f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Credits";
        menuGroup.add(menuItem);

        // about
        menuItem = new MenuItem();
        rect = new Rectangle(591, 520+96, 335, 96);
        menuItem.init("About", MenuOptions.Back, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.1f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "About";
        menuGroup.add(menuItem);

        // back
        menuItem = new MenuItem();
        rect = new Rectangle(926, 520+94, 299, 94);
        menuItem.init("Back", MenuOptions.Back, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.4f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "MainMenu";
        menuGroup.add(menuItem);

        groups.add(menuGroup);


        // credits
        menuGroup = new MenuGroup();
        menuGroup.name = "Credits";

        // credits image
        menuImage = new MenuImage();
        rect = new Rectangle(604, 0+387, 493, 387);
        menuImage.init("credits", visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.2f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);

        // back
        menuItem = new MenuItem();
        rect = new Rectangle(926, 520+94, 299, 94);
        menuItem.init("Back", MenuOptions.Back, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.4f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Help";
        menuGroup.add(menuItem);

        groups.add(menuGroup);


        // about
        menuGroup = new MenuGroup();
        menuGroup.name = "About";

        // about image
        menuImage = new MenuImage();
        rect = new Rectangle(0, 0+520, 604, 520);
        menuImage.init("about", visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.2f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);

        // back
        menuItem = new MenuItem();
        rect = new Rectangle(926, 520+94, 299, 94);
        menuItem.init("Back", MenuOptions.Back, visuals.textureMenuItems, visuals.whiteColor, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.4f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Visuals.referenceScreenWidth) * sc, (rect.h / Visuals.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Help";
        menuGroup.add(menuItem);

        groups.add(menuGroup);



        // fade
        fade.init(new MyColor(0f, 0f, 0f, 0.4f), new MyColor(0f, 0f, 0f, 0.4f));
    }

    public void update() {
        currentGroup.update();

        if (selectedMenuItem != null) {
            if (selectedMenuItem.getAnim() == null) {
                selectedMenuOption = selectedMenuItem.menuOption;

                switch (selectedMenuOption) {
                    case Options: {
                        MenuGroup group = getMenuGroupByName("Options");
                        if (group != null) {
                            currentGroup = group;
                            selectedMenuOption = MenuOptions.None;
                        } else {
                            System.out.println("Something is wrong Menu Options button");
                        }
                    }
                    break;

                    case Help:
                    case Back: {
                        String tag = selectedMenuItem.tag;
                        MenuGroup group = getMenuGroupByName(tag);
                        if (group != null) {
                            currentGroup = group;
                            selectedMenuOption = MenuOptions.None;
                        } else {
                            System.out.println("Something is wrong Tag button");
                        }
                    }
                    break;
                }

                selectedMenuItem = null;
            }
        }
    }

    public void draw() {
        //System.out.println("Menu draw...");
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();
        visuals.bindNoTexture();
        fade.draw();

        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();
        visuals.textureShader.useProgram();

        glDisable(GL_DEPTH_TEST);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        visuals.textureShader.setTexture(visuals.textureMenuItems);

        currentGroup.draw();
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        if (selectedMenuItem == null) {
            selectedMenuItem = currentGroup.handleTouchPress(normalizedX, normalizedY);

            if (selectedMenuItem != null) {
                //System.out.println("Selected Menu Option is: " + selectedMenuItem.menuOption.toString());
                selectedMenuItem.setAnim(menuItemAnim);
                ClassicSingleton singleton = ClassicSingleton.getInstance();
                singleton.audio.playSound();
            } else {
                updateVolumes(normalizedX, normalizedY);
            }
        }
    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {
        updateVolumes(normalizedX, normalizedY);
    }

    private void updateVolumes(float normalizedX, float normalizedY) {
        float diff = 0.1f;
        if (currentGroup.soundVolumeControl != null && currentGroup.musicVolumeControl != null) {
            Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, visuals.invertedViewProjectionMatrix);

            if (pos.y > currentGroup.musicVolumeControl.pos.ty - diff  &&
                pos.y < currentGroup.musicVolumeControl.pos.ty + diff) {
                // -1.0f <=> 1.0f
                float minValue = -1f;
                float maxValue = 1f;
                float norm = (normalizedX - minValue) / (maxValue - minValue);

                //System.out.println("normalized is: " + normalizedX);
                //System.out.println("norm is: " + norm);
                ClassicSingleton.getInstance().audio.setMusicVolume(norm);
            }

            if (pos.y > currentGroup.soundVolumeControl.pos.ty - diff  &&
                pos.y < currentGroup.soundVolumeControl.pos.ty + diff) {
                // -1.0f <=> 1.0f
                float minValue = -1f;
                float maxValue = 1f;
                float norm = (normalizedX - minValue) / (maxValue - minValue);

                //System.out.println("normalized is: " + normalizedX);
                //System.out.println("norm is: " + norm);
                ClassicSingleton.getInstance().audio.setSoundVolume(norm);
            }
        }
    }

    public void reset() {
        currentGroup.reset();
        selectedMenuItem = null;
        selectedMenuOption = MenuOptions.None;
    }
}
