package com.jbs.textgameengine.screen.gamescreen.userinterface.prompt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Rect;

import java.util.ArrayList;
import java.util.HashMap;

public class InputBar extends UserInterfaceElement {
    public static int padding = 10;
    public static String inputCharString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=[];',./Space";
    public HashMap<String, String> upperCaseCharMap;

    public BitmapFont font;
    public GlyphLayout glyphLayout;

    public String userInput;
    public int updateTimer;
    public int backspaceTimer;

    public ArrayList<String> inputList;
    public int inputListLimit;
    public int currentInputIndex;

    public InputBar() {
        super();

        upperCaseCharMap = new HashMap<String, String>() {{
            put("1", "!");
            put("2", "@");
            put("3", "#");
            put("4", "$");
            put("5", "%");
            put("6", "^");
            put("7", "&");
            put("8", "*");
            put("9", "(");
            put("0", ")");
            put("-", "_");
            put("=", "+");
            put("[", "{");
            put("]", "}");
            put(";", ":");
            put("'", "\"");
            put(",", "<");
            put(".", ">");
            put("/", "?");
        }};

        font = new BitmapFont(Gdx.files.internal("fonts/Consolas_28.fnt"), Gdx.files.internal("fonts/Consolas_28.png"), false);
        font.setFixedWidthGlyphs("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-=_+[]{};':\",<.>/? ");
        glyphLayout = new GlyphLayout(font, " ");

        userInput = "";
        updateTimer = 0;
        backspaceTimer = 0;

        inputList = new ArrayList<>();
        inputListLimit = 20;
        currentInputIndex = -1;

        int promptX = 0;
        int promptY = 0;
        int promptWidth = Settings.INPUT_BAR_WIDTH;
        int promptHeight = Settings.INPUT_BAR_HEIGHT;
        rect = new Rect(promptX, promptY, promptWidth, promptHeight);

        rect.fillColor = new Color(0/255f, 0/255f, 20/255f, 1);
    }

    public void update() {
        updateTimer += 1;
        if(updateTimer >= 60) {
            updateTimer = 0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            if(backspaceTimer == 0
            && userInput.length() > 0) {
                if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                    userInput = "";
                } else {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
            }

            backspaceTimer += 1;
            if(backspaceTimer >= 5) {
                backspaceTimer = 0;
            }
        }
    }

    public void render() {
        super.render();

        String inputDisplayString = userInput;
        if(inputDisplayString.length() > ((rect.width - (padding * 2)) / glyphLayout.width) - 2) {
            int inputStringStartIndex = inputDisplayString.length() - (((rect.width - (padding * 2)) / (int) glyphLayout.width) - 2);
            inputDisplayString = inputDisplayString.substring(inputStringStartIndex);
        }

        if(updateTimer >= 30) {
            inputDisplayString += "_";
        }

        Line inputDisplayLine = new Line("> " + inputDisplayString, "2Y" + String.valueOf(inputDisplayString.length()) + "W");
        int paddingVertical = padding + (int) glyphLayout.height + 2;
        Line.draw(inputDisplayLine, padding, paddingVertical, font, Screen.camera);
    }

    public void concatinateUserInput(String key) {
        if(key.equals("Space")) {
            key = " ";
        }

        key = key.toLowerCase();

        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
        || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            key = key.toUpperCase();

            if(upperCaseCharMap.containsKey(key)) {
                key = upperCaseCharMap.get(key);
            }
        }

        userInput += key;
    }

    public void enterUserInput() {
        if(userInput.length() > 1) {
            inputList.add(0, userInput);
            if(inputList.size() > inputListLimit) {
                inputList.remove(inputList.size() - 1);
            }
        }

        userInput = "";
        currentInputIndex = -1;
    }

    public void scrollUserInput(String key) {
        if(key.equals("Up") && currentInputIndex < inputList.size() - 1) {
            currentInputIndex += 1;
        }
        else if(key.equals("Down") && currentInputIndex > -1) {
            currentInputIndex -= 1;
        }

        if(currentInputIndex > -1) {
            userInput = inputList.get(currentInputIndex);
        }
        else {
            userInput = "";
        }
    }
}
