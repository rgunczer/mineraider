package com.almagems.mineraider;

import java.util.ArrayList;

import static android.opengl.GLES20.*;


public final class Menu extends Overlay {

    public enum MenuOptions {
        None,
        Play,
        Options,
        Stats,
        Help,
        Back
    }

    private MenuItem selectedMenuItem;
    private MenuOptions selectedMenuOption = MenuOptions.None;

    private final MenuItemAnim menuItemAnim = new MenuItemAnim();

    private ArrayList<MenuGroup> groups = new ArrayList<MenuGroup>(9);
    private MenuGroup currentGroup;

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


    // ctor
    public Menu() {

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
        float aspect = Graphics.aspectRatio;
        sc = 1.76f;
        float itemY = aspect * 0.18f;
        float itemYStep = 0.4f;

        Texture textureObj = graphics.getTextureObj(graphics.textureMenuItems);

        // main menu
        menuGroup = new MenuGroup();
        menuGroup.init("MainMenu");

        MenuGameTitleAnim gameTitleAnim = new MenuGameTitleAnim();
        gameTitleAnim.init(sc, textureObj);
        menuGroup.add(gameTitleAnim);

        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_play.png");
        menuItem.init("Play", MenuOptions.Play, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, itemY - (0f * itemYStep) , 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuItem);

        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_options.png");
        menuItem.init("Options", MenuOptions.Options, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, itemY - (1f * itemYStep), 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuItem);

        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_stats.png");
        menuItem.init("Stats", MenuOptions.Stats, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, itemY - (2f * itemYStep), 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Stats";
        menuGroup.add(menuItem);

        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_help.png");
        menuItem.init("Help", MenuOptions.Help, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, itemY - (3f * itemYStep), 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Help";
        menuGroup.add(menuItem);

        groups.add(menuGroup);

        currentGroup = menuGroup;

        // options
        menuGroup = new MenuGroup();
        menuGroup.init("Options");


        // back
        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_back.png");
        menuItem.init("Back", MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.4f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "MainMenu";
        menuGroup.add(menuItem);

        // music
        menuImage = new MenuImage();
        rect = textureObj.getFrame("menu_title_music.png");
        menuImage.init("music", graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.5f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);

        // sound
        menuImage = new MenuImage();
        rect = textureObj.getFrame("menu_title_sound.png");
        menuImage.init("sound", graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.1f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);


        // music volume control
        Color colorBody = new Color(143f/255f, 127f/255f, 96f/255f, 1f);
        Color colorFrame = new Color(10f/255f, 12f/255f, 9f/255f, 1f);

        float volumeControlWidth = 1.8f;
        float volumeControlHeight = 0.05f;
        float volumeControlBorder = 0.02f;

        menuGroup.musicVolumeControl = new ProgressBarControl("Music", colorFrame, colorBody, volumeControlWidth, volumeControlHeight, volumeControlBorder);
        menuGroup.musicVolumeControl.init(aspect * 0.35f);

        menuGroup.soundVolumeControl = new ProgressBarControl("Sound", colorFrame, colorBody, volumeControlWidth, volumeControlHeight, volumeControlBorder);
        menuGroup.soundVolumeControl.init(-aspect * 0.06f);

        groups.add(menuGroup);


        // Help
        menuGroup = new MenuGroup();
        menuGroup.name = "Help";

        // credits
        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_credits.png");
        menuItem.init("Credits", MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, aspect * 0.2f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Credits";
        menuGroup.add(menuItem);

        // about
        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_about.png");
        menuItem.init("About", MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.1f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "About";
        menuGroup.add(menuItem);

        // back
        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_back.png");
        menuItem.init("Back", MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.4f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "MainMenu";
        menuGroup.add(menuItem);

        groups.add(menuGroup);


        // credits
        menuGroup = new MenuGroup();
        menuGroup.name = "Credits";

        // credits image
        menuImage = new MenuImage();
        rect = textureObj.getFrame("menu_credits.png");
        menuImage.init("credits", graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.2f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);

        // back
        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_back.png");
        menuItem.init("Back", MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.4f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Help";
        menuGroup.add(menuItem);

        groups.add(menuGroup);


        // about
        menuGroup = new MenuGroup();
        menuGroup.name = "About";

        // about image
        menuImage = new MenuImage();
        rect = textureObj.getFrame("menu_about_text.png");
        menuImage.init("about", graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuImage.setTrans(0f, aspect * 0.2f, 0f);
        menuImage.setRot(0f, 0f, 0f);
        menuImage.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuGroup.add(menuImage);

        // back
        menuItem = new MenuItem();
        rect = textureObj.getFrame("menu_item_back.png");
        menuItem.init("Back", MenuOptions.Back, graphics.textureMenuItems, Color.WHITE, rect, flipUTextureCoordinate);
        menuItem.setTrans(0f, -aspect * 0.5f, 0f);
        menuItem.setRot(0f, 0f, 0f);
        menuItem.setScale((rect.w / Graphics.referenceScreenWidth) * sc, (rect.h / Graphics.referenceScreenWidth) * sc, 1.0f);
        menuItem.tag = "Help";
        menuGroup.add(menuItem);

        groups.add(menuGroup);

        background.init(colorBackground, colorBackground);
    }

    public void update() {
        background.update();
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

        graphics.bindNoTexture();
        background.draw();

        graphics.textureShader.useProgram();
        graphics.textureShader.setTexture(Graphics.textureMenuItems);
        currentGroup.draw();
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        if (selectedMenuItem == null) {
            selectedMenuItem = currentGroup.handleTouchPress(normalizedX, normalizedY);

            if (selectedMenuItem != null) {
                //System.out.println("Selected Menu Option is: " + selectedMenuItem.menuOption.toString());
                selectedMenuItem.setAnim(menuItemAnim);
                Engine engine = Engine.getInstance();
                engine.audio.playSound();
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
            Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, graphics.invertedViewProjectionMatrix);

            if (pos.y > currentGroup.musicVolumeControl.pos.ty - diff  &&
                pos.y < currentGroup.musicVolumeControl.pos.ty + diff) {
                // -1.0f <=> 1.0f
                float minValue = -1f;
                float maxValue = 1f;
                float norm = (normalizedX - minValue) / (maxValue - minValue);

                //System.out.println("normalized is: " + normalizedX);
                //System.out.println("norm is: " + norm);
                Engine.getInstance().audio.setMusicVolume(norm);
            }

            if (pos.y > currentGroup.soundVolumeControl.pos.ty - diff  &&
                pos.y < currentGroup.soundVolumeControl.pos.ty + diff) {
                // -1.0f <=> 1.0f
                float minValue = -1f;
                float maxValue = 1f;
                float norm = (normalizedX - minValue) / (maxValue - minValue);

                //System.out.println("normalized is: " + normalizedX);
                //System.out.println("norm is: " + norm);
                Engine.getInstance().audio.setSoundVolume(norm);
            }
        }
    }

    public void resetBackground() {
        background.reset();
    }

    public void reset() {
        currentGroup.reset();
        selectedMenuItem = null;
        selectedMenuOption = MenuOptions.None;
    }

    public void resetSelectedMenuOption() {
        selectedMenuOption = MenuOptions.None;
    }
}
