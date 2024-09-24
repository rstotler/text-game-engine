package com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Effect {
    public static ArrayList<String> effectList = new ArrayList<>(Arrays.asList("FADA", "FADB", "FADC", "GRAA", "GRAB", "GRAC", "GRAD"));

    public ArrayList<Integer> moveDirList;
    public ArrayList<Float> colorPercentList;

    public ArrayList<Float> targetRList;
    public ArrayList<Float> targetGList;
    public ArrayList<Float> targetBList;
    public ArrayList<Integer> currentColorIndexList;

    public String effectCode;
    public int maxCharLength;
    public int effectColorCount;

    public String primaryColor;
    public String secondaryColor;
    public String tertiaryColor;

    public Effect(String effectCode, int maxCharLength, int sectionCount) {
        // MaxCharLength: The number of characters in the effect code.
        // SectionCount: The total number of different color code sections in a single effect code string section.

        moveDirList = new ArrayList<>();
        colorPercentList = new ArrayList<>();

        targetRList = new ArrayList<>();
        targetGList = new ArrayList<>();
        targetBList = new ArrayList<>();
        currentColorIndexList = new ArrayList<>();

        this.effectCode = effectCode.substring(0, 4);
        this.maxCharLength = maxCharLength;

        // Set Color Data //
        effectColorCount = 1;
        if(effectCode.length() > 4) {
            String[] colorData = Line.getColorsFromString(effectCode.substring(4));
            primaryColor = colorData[0];
            secondaryColor = colorData[1];
            tertiaryColor = colorData[2];

            if(!tertiaryColor.isEmpty()) {effectColorCount = 3;}
            else if(!secondaryColor.isEmpty()) {effectColorCount = 2;}
            else {effectColorCount = 1;}
        }
        else {
            primaryColor = "W";
            secondaryColor = "";
            tertiaryColor = "";
        }

        // Set Starting Target Color //
        float targetRValue = 1.0f;
        float targetGValue = 1.0f;
        float targetBValue = 1.0f;
        if(!primaryColor.isEmpty()
        && Line.colorMap.containsKey(primaryColor)) {
            Color color = Line.colorMap.get(primaryColor);
            targetRValue = color.r;
            targetGValue = color.g;
            targetBValue = color.b;
        }

        // Initialize Arrays //
        for(int i = 0; i < sectionCount; i++) {
            moveDirList.add(1);
            colorPercentList.add(0.0f);
            targetRList.add(targetRValue);
            targetGList.add(targetGValue);
            targetBList.add(targetBValue);
            currentColorIndexList.add(0);
        }
    }

    public void update(int sectionIndex, BitmapFont font) {}
}
