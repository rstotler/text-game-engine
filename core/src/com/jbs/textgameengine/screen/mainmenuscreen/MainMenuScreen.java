package com.jbs.textgameengine.screen.mainmenuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.utility.AnimatedString;

import java.util.ArrayList;

public class MainMenuScreen extends Screen {
    public static BitmapFont FONT_MENU = new BitmapFont(Gdx.files.internal("fonts/Code_New_Roman_90.fnt"), Gdx.files.internal("fonts/Code_New_Roman_90.png"), false);
    public static GlyphLayout glyphLayout;

    public ArrayList<AnimatedString> mainMenuOptionList;
    public AnimatedString currentMainMenuOption;
    public AnimatedString selectedMainMenuOption;

    public MainMenuScreen() {
        super();

        mainMenuOptionList = new ArrayList<>();
        mainMenuOptionList.add(new AnimatedString("New Game"));
        mainMenuOptionList.add(new AnimatedString("Continue"));
        mainMenuOptionList.add(new AnimatedString("Quit"));

        currentMainMenuOption = mainMenuOptionList.get(0);
        selectedMainMenuOption = null;

        initInputProcessor();
    }

    public void initInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            // Keyboard Input //
            @Override
            public boolean keyDown(int keyCode) {
                String key = Input.Keys.toString(keyCode);

                // Exit Game //
                if(key.equals("Escape")) {
                    dispose();
                    System.exit(0);
                }

                // Toggle Full Screen //
                else if(key.equals("F5")) {
                    if(Gdx.graphics.isFullscreen()) {
                        Gdx.graphics.setWindowedMode(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
                    } else {
                        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                        Gdx.graphics.setFullscreenMode(currentMode);
                    }
                }

                // Navigate Main Menu //
                else if(key.equals("Down") || key.equals("Up")) {
                    int selectedMainMenuOptionIndex = mainMenuOptionList.indexOf(currentMainMenuOption);
                    if(key.equals("Down")) {
                        selectedMainMenuOptionIndex += 1;
                    } else {
                        selectedMainMenuOptionIndex -= 1;
                    }

                    if(selectedMainMenuOptionIndex >= mainMenuOptionList.size()) {
                        selectedMainMenuOptionIndex = 0;
                    } else if(selectedMainMenuOptionIndex < 0) {
                        selectedMainMenuOptionIndex = mainMenuOptionList.size() - 1;
                    }

                    currentMainMenuOption = mainMenuOptionList.get(selectedMainMenuOptionIndex);
                }

                // Choose Main Menu Option //
                else if(key.equals("Enter")) {
                    if(currentMainMenuOption.label.equals("New Game")) {
                        selectedMainMenuOption = currentMainMenuOption;
                    }

                    else if(currentMainMenuOption.label.equals("Quit")) {
                        dispose();
                        System.exit(0);
                    }
                }

                return true;
            }
        });
    }

    public String update() {
        String updateString = "";

        // Select New Game //
        if(selectedMainMenuOption != null
        && selectedMainMenuOption.label.equals("New Game")) {
            updateString = selectedMainMenuOption.label;
        }

        // Update Main Menu //
        else {
            for(AnimatedString animatedString : mainMenuOptionList) {
                animatedString.update();
            }
        }

        return updateString;
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Main Menu Title //
        FONT_MENU.setColor(Color.WHITE);
        FONT_MENU.getData().setScale(2.0f);
        String mainMenuTitleString = "TextGame Engine";
        glyphLayout = new GlyphLayout(FONT_MENU, mainMenuTitleString);
        int mainMenuTitleX = (Settings.WINDOW_WIDTH / 2) - ((int) glyphLayout.width / 2);
        int mainMenuTitleY = 800;

        FONT_MENU.draw(spriteBatch, mainMenuTitleString, mainMenuTitleX, mainMenuTitleY);

        // Main Menu Options //
        int animatedStringY = 500;
        FONT_MENU.getData().setScale(0.60f);
        for(int i = 0; i < mainMenuOptionList.size(); i++) {
            AnimatedString animatedString = mainMenuOptionList.get(i);

            glyphLayout = new GlyphLayout(FONT_MENU, animatedString.label);
            int animatedStringX = (Settings.WINDOW_WIDTH / 2) - ((int) glyphLayout.width / 2);

            if(animatedString == currentMainMenuOption)
                FONT_MENU.setColor(Color.WHITE);
            else
                FONT_MENU.setColor(Color.GRAY);

            FONT_MENU.draw(spriteBatch, animatedString.getCurrentLabel(), animatedStringX, animatedStringY);

            // Currently Selected Menu Option //
            if(animatedString == currentMainMenuOption) {
                FONT_MENU.setColor(Color.YELLOW);
                FONT_MENU.draw(spriteBatch, ">", animatedStringX - 33, animatedStringY);
            }

            animatedStringY -= 65;
        }

        FONT_MENU.getData().setScale(1.0f);
        spriteBatch.end();
    }

    public void dispose() {
        super.dispose();

        FONT_MENU.dispose();
    }
}
