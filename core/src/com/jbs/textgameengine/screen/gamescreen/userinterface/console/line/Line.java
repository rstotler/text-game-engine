package com.jbs.textgameengine.screen.gamescreen.userinterface.console.line;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect.Effect;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect.type.Fade;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect.type.Gradient;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.pattern.Pattern;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Line {
    public static HashMap<String, Color> colorMap = loadColorMap();
    public static GlyphLayout glyphLayout;
    public static String spaceString = "                                                                                                    ";

    public String label;
    public String colorCode;
    public String effectCode;

    public ArrayList<Effect> effectModList;

    int maxDrawCountTimer;
    int drawCountTimerLength;
    int drawCountTimerSize;

    boolean isLastLine;

    public Line(String label, String colorCode, String effectCode) {
        this.label = label;
        this.colorCode = Pattern.formatColorCode(label, colorCode.toUpperCase());

        if(!effectCode.isEmpty() && !Utility.isInteger(effectCode.substring(0, 1))) {
            effectCode = String.valueOf(label.length()) + effectCode;
        }
        this.effectCode = effectCode;

        effectModList = null;

        maxDrawCountTimer = -9999;
        drawCountTimerLength = -9999;
        drawCountTimerSize = -9999;

        isLastLine = true;

        if(!effectCode.isEmpty()) {
            initEffectModLists(this.colorCode);
        }
    }

    public Line(String label, String colorCode) {
        this(label, colorCode, "");
    }

    public Line(String label) {
        this(label, String.valueOf(label.length()) + "W", "");
    }

    public Line(String label, String colorCode, String effectCode, boolean isLastLine) {
        this(label, colorCode, effectCode);
        this.isLastLine = isLastLine;
    }

    public Line(String label, String colorCode, String effectCode, boolean isLastLine, boolean scrollLabel) {
        this(label, colorCode, effectCode, isLastLine);

        if(scrollLabel) {
            maxDrawCountTimer = 1;
            drawCountTimerLength = 2;

            drawCountTimerSize = label.length() / 10;
            if(drawCountTimerSize == 0) {drawCountTimerSize = 1;}
            drawCountTimerSize += new Random().nextInt(2);
        }
    }

    public void initEffectModLists(String colorCode) {
        effectModList = new ArrayList<>();

        char[] effectCodeArray = effectCode.toUpperCase().toCharArray();
        String effectCodeSectionCountString = "";
        String effectCodeSectionCodeString = "";
        boolean effectCodeCheck = false;

        for(int i = 0; i < effectCodeArray.length; i++) {
            char c = effectCodeArray[i];

            if(!Character.isDigit(c)) {
                effectCodeSectionCodeString += c;
                if(!effectCodeCheck) {effectCodeCheck = true;}
            }

            if(i == effectCodeArray.length - 1
            || (Character.isDigit(c) && effectCodeCheck && !Character.isDigit(effectCodeArray[i - 1]))) {
                int effectCodeSectionCount = 0;
                if(Utility.isInteger(effectCodeSectionCountString)) {
                    effectCodeSectionCount = Integer.parseInt(effectCodeSectionCountString);
                }

                if(effectCodeSectionCodeString.length() >= 4
                && Effect.effectList.contains(effectCodeSectionCodeString.substring(0, 4))) {
                    int trimmedCodeLength = trimColorCode(colorCode, effectCodeSectionCount).length();
                    int colorCodeSubstringIndex = colorCode.length() - trimmedCodeLength;
                    String colorCodeSubstring = colorCode.substring(0, colorCodeSubstringIndex);
                    int colorCodeSectionCount = splitCodeString(colorCodeSubstring)[1].size();

                    if(effectCodeSectionCodeString.substring(0, 3).equals("FAD")) {
                        effectModList.add(new Fade(effectCodeSectionCodeString, effectCodeSectionCount, colorCodeSectionCount));
                    } else if(effectCodeSectionCodeString.substring(0, 3).equals("GRA")) {
                        effectModList.add(new Gradient(effectCodeSectionCodeString, effectCodeSectionCount, colorCodeSectionCount));
                    }
                }

                colorCode = trimColorCode(colorCode, effectCodeSectionCount);
                effectCodeSectionCountString = "";
                effectCodeSectionCodeString = "";
            }

            if(Character.isDigit(c)) {
                effectCodeSectionCountString += c;
            }
        }
    }

    public int getSpaceHeight() {
        // Returns The Height Of The Space Between Lines

        if(isLastLine) {
            return 20;
        }

        return 10;
    }

    public static String getUnderlineString(String label) {
        float endPercent = .20f;
        int dashCount = (int) (label.length() * endPercent);
        String underlineString = "";
        for(int i = 0; i < label.length(); i++) {
            if(i < dashCount || i >= label.length() - dashCount) {
                underlineString += "-";
            } else {
                underlineString += "=";
            }
        }

        return underlineString;
    }

    public static void draw(Line line, int drawX, int drawY, BitmapFont font, OrthographicCamera camera) {
        int maxDrawCount = -9999;
        if(line.maxDrawCountTimer != -9999 && line.drawCountTimerLength != 0) {
            maxDrawCount = line.maxDrawCountTimer / line.drawCountTimerLength;
        }
        int drawCount = 0;

        char[] colorCodeArray = line.colorCode.toUpperCase().toCharArray();
        String colorCodeSectionCountString = "";
        String colorCodeSectionCodeString = "";
        String labelSectionString = line.label;
        String effectCodeSectionString = line.effectCode;
        boolean colorCodeCheck = false;
        int effectIndex = 0;
        int effectIndexCodeCount = 0;
        glyphLayout = new GlyphLayout(font, " ");

        if(camera != null) {
            Screen.spriteBatch.setProjectionMatrix(camera.combined);
        }
        else {
            Screen.spriteBatch.setProjectionMatrix(null);
        }
        Screen.spriteBatch.begin();

        // Draw Label Color Code Section By Color Code Section //
        for(int i = 0; i < colorCodeArray.length; i++) {
            char c = colorCodeArray[i];

            if(!Character.isDigit(c)) {
                colorCodeSectionCodeString += c;
                if(!colorCodeCheck) {colorCodeCheck = true;}
            }

            if(i == colorCodeArray.length - 1
            || (Character.isDigit(c) && colorCodeCheck && !Character.isDigit(colorCodeArray[i - 1]))) {

                // Get Label Section String //
                int colorCodeSectionCount = 0;
                if(colorCodeSectionCountString.isEmpty()) {
                    colorCodeSectionCount = labelSectionString.length();
                }
                else if(Utility.isInteger(colorCodeSectionCountString)) {
                    colorCodeSectionCount = Integer.parseInt(colorCodeSectionCountString);
                }
                if(colorCodeSectionCount > labelSectionString.length()) {
                    colorCodeSectionCount = labelSectionString.length();
                }
                String labelSectionStringSubstring = labelSectionString.substring(0, colorCodeSectionCount);
                labelSectionString = labelSectionString.substring(colorCodeSectionCount);

                // Set Font Color //
                if(colorMap.containsKey(colorCodeSectionCodeString)) {
                    font.setColor(colorMap.get(colorCodeSectionCodeString));
                }
                else {
                    font.setColor(220/255f, 220/255f, 220/255f, 1);
                }

                // Update Effect Code & Effect Code Section String //
                if(!effectCodeSectionString.isEmpty()) {
                    int effectCodeSectionStartIndex = 0;
                    for(int effectCodeIndex = 0; effectCodeIndex < 5; effectCodeIndex++) {
                        if(effectCodeSectionString.length() > effectCodeIndex + 1 && Utility.isInteger(effectCodeSectionString.charAt(effectCodeIndex)) && !Utility.isInteger(effectCodeSectionString.charAt(effectCodeIndex + 1))) {
                            effectCodeSectionStartIndex = effectCodeIndex + 1;
                            break;
                        }
                    }

                    if(effectCodeSectionString.substring(effectCodeSectionStartIndex).length() >= 4) {
                        String effectCodeSectionCodeString = effectCodeSectionString.substring(effectCodeSectionStartIndex, effectCodeSectionStartIndex + 4);
                        if(Effect.effectList.contains(effectCodeSectionCodeString)
                        && effectIndex < line.effectModList.size()) {
                            line.effectModList.get(effectIndex).update(effectIndexCodeCount, font);

                            effectIndexCodeCount += colorCodeSectionCount;
                            if(effectIndexCodeCount >= line.effectModList.get(effectIndex).maxCharLength) {
                                effectIndexCodeCount = 0;
                                effectIndex += 1;
                            }
                        }
                    }

                    effectCodeSectionString = trimColorCode(effectCodeSectionString, colorCodeSectionCount);
                }

                // Draw Label Section //
                if(line.maxDrawCountTimer == -9999
                || drawCount < maxDrawCount) {
                    String labelSectionStringSubstringCopy = labelSectionStringSubstring;
                    if(line.maxDrawCountTimer != -9999
                    && drawCount + labelSectionStringSubstringCopy.length() > maxDrawCount) {
                        int copyStringDrawCount = (drawCount + labelSectionStringSubstringCopy.length()) - maxDrawCount;
                        labelSectionStringSubstringCopy = labelSectionStringSubstringCopy.substring(0, copyStringDrawCount);
                    }
                    font.draw(Screen.spriteBatch, labelSectionStringSubstringCopy, drawX, drawY);
                }
                drawX += labelSectionStringSubstring.length() * (int) glyphLayout.width;
                drawCount += labelSectionStringSubstring.length();
                colorCodeSectionCountString = "";
                colorCodeSectionCodeString = "";
            }

            if(Character.isDigit(c)) {
                colorCodeSectionCountString += c;
            }
        }

        Screen.spriteBatch.end();

        // Update Draw Count Timer //
        if(line.maxDrawCountTimer != -9999
        && maxDrawCount < line.label.length()) {
            line.maxDrawCountTimer += line.drawCountTimerSize;
        }
    }

    public static ArrayList<Line> wrap(Line line, int maxLineCharCount) {
        ArrayList<Line> wrappedLineList = new ArrayList<>();

        String currentLabel = "";
        String currentColorCode = "";
        String colorCodeString = line.colorCode.toUpperCase();
        String defaultColorCodeString = "";
        String effectCodeString = line.effectCode.toUpperCase();
        String currentEffectCode = "";
        if(colorMap.containsKey(colorCodeString)) {
            defaultColorCodeString = colorCodeString;
        }
        else if(!colorCodeString.isEmpty() && !Character.isDigit(colorCodeString.charAt(0))) {
            defaultColorCodeString = "W";
        }

        boolean scrollLabel = line.maxDrawCountTimer != -9999;

        for(String word : line.label.split(" ")) {
            if(currentLabel.length() + word.length() + 1 > maxLineCharCount) {
                String colorCodeDisplayString = currentColorCode;
                if(!defaultColorCodeString.isEmpty()) {
                    colorCodeDisplayString = defaultColorCodeString;
                }
                wrappedLineList.add(new Line(currentLabel, colorCodeDisplayString, currentEffectCode, false, scrollLabel));

                currentLabel = word;
                colorCodeString = trimColorCode(colorCodeString, 1);
                currentColorCode = getSubColorCode(colorCodeString, word.length());
                colorCodeString = trimColorCode(colorCodeString, word.length());

                if(!effectCodeString.isEmpty()) {
                    effectCodeString = trimColorCode(effectCodeString, 1);
                    currentEffectCode = getSubColorCode(effectCodeString, word.length());
                    effectCodeString = trimColorCode(effectCodeString, word.length());
                }
            }

            else {
                String spaceString = "";
                if(!currentLabel.isEmpty()) {spaceString = " ";}

                currentLabel += spaceString + word;
                String subColorCodeString = getSubColorCode(colorCodeString, word.length() + spaceString.length());
                currentColorCode = combineColorCodeString(currentColorCode, subColorCodeString);
                colorCodeString = trimColorCode(colorCodeString, word.length() + spaceString.length());

                if(!effectCodeString.isEmpty()) {
                    String subEffectCodeString = getSubColorCode(effectCodeString, word.length() + spaceString.length());
                    currentEffectCode = combineColorCodeString(currentEffectCode, subEffectCodeString);
                    effectCodeString = trimColorCode(effectCodeString, word.length() + spaceString.length());
                }
            }
        }
        if(!currentLabel.isEmpty()) {
            String colorCodeDisplayString = currentColorCode;
            if(!defaultColorCodeString.isEmpty()) {
                colorCodeDisplayString = defaultColorCodeString;
            }
            wrappedLineList.add(new Line(currentLabel, colorCodeDisplayString, currentEffectCode, false, scrollLabel));
        }

        if(line.isLastLine
        && !wrappedLineList.isEmpty()) {
            wrappedLineList.get(wrappedLineList.size() - 1).isLastLine = true;
        }
        return wrappedLineList;
    }

    public static ArrayList<String>[] splitCodeString(String targetCode) {
        ArrayList<String>[] arrayLists = new ArrayList[2];
        arrayLists[0] = new ArrayList<String>(); // Index 0: Code Count
        arrayLists[1] = new ArrayList<String>(); // Index 1: Code Color/Pattern

        char[] codeArray = targetCode.toUpperCase().toCharArray();
        String codeSectionCountString = "";
        String codeSectionCodeString = "";
        boolean codeCheck = false;

        for(int i = 0; i < codeArray.length; i++) {
            char c = codeArray[i];

            if(!Character.isDigit(c)) {
                codeSectionCodeString += c;
                if (!codeCheck) {codeCheck = true;}
            }

            if(i == codeArray.length - 1
            || (Character.isDigit(c) && codeCheck && !Character.isDigit(codeArray[i - 1]))) {
                arrayLists[0].add(codeSectionCountString);
                arrayLists[1].add(codeSectionCodeString);

                codeSectionCountString = "";
                codeSectionCodeString = "";
            }

            if(Character.isDigit(c)) {
                codeSectionCountString += c;
            }
        }

        return arrayLists;
    }

    public static String getSubColorCode(String colorCodeString, int maxCharCount) {
        // Returns a portion of a color code (from the front) given a user-defined length.

        String subColorCodeString = "";

        char[] colorCodeArray = colorCodeString.toUpperCase().toCharArray();
        String colorCodeSectionCountString = "";
        String colorCodeSectionCodeString = "";
        int alreadyCounted = 0;
        boolean colorCodeCheck = false;

        for(int i = 0; i < colorCodeArray.length; i++) {
            char c = colorCodeArray[i];

            if(!Character.isDigit(c)) {
                colorCodeSectionCodeString += c;
                if(!colorCodeCheck) {colorCodeCheck = true;}
            }

            if(i == colorCodeArray.length - 1
            || (Character.isDigit(c) && colorCodeCheck && !Character.isDigit(colorCodeArray[i - 1]))) {
                int colorCodeSectionCount = 0;
                if(Utility.isInteger(colorCodeSectionCountString)) {
                    colorCodeSectionCount = Integer.valueOf(colorCodeSectionCountString);
                }
                if(colorCodeSectionCount + alreadyCounted > maxCharCount) {
                    colorCodeSectionCount = maxCharCount - alreadyCounted;
                }

                if(colorCodeSectionCount != 0) {
                    subColorCodeString += String.valueOf(colorCodeSectionCount);
                }
                subColorCodeString += colorCodeSectionCodeString;
                colorCodeSectionCountString = "";
                colorCodeSectionCodeString = "";

                alreadyCounted += colorCodeSectionCount;
                if(alreadyCounted >= maxCharCount) {
                    break;
                }
            }

            if(Character.isDigit(c)) {
                colorCodeSectionCountString += c;
            }
        }

        return subColorCodeString;
    }

    public static String combineColorCodeString(String colorCode, String subColorCode) {
        if(colorCode.isEmpty()) {
            return subColorCode;
        }

        // Get Previous Color Code's Prefix & Ending Color //
        String lastColorInCode = colorCode;
        String prefixCountString = "";
        String colorCodePrefix = ""; // Color Code String With Matching Color Trimmed
        char[] colorCodeArray = colorCode.toCharArray();
        boolean colorCodeCheck = false;
        int codeCountEndIndex = 0;
        for(int i = colorCodeArray.length - 1; i >= 0; i--) {
            if(!colorCodeCheck
            && Character.isDigit(colorCodeArray[i])) {
                lastColorInCode = colorCode.substring(i + 1);
                colorCodeCheck = true;
            }

            if((!Character.isDigit(colorCodeArray[i]) && colorCodeCheck)
            || (Character.isDigit(colorCodeArray[i]) && i == 0)) {
                int indexMod = 0;
                if(!Character.isDigit(colorCodeArray[i]) && colorCodeCheck) {
                    indexMod = 1;
                }
                colorCodePrefix = colorCode.substring(0, i + indexMod);
                prefixCountString = colorCode.substring(i + indexMod, codeCountEndIndex);
                break;
            }

            if(!Character.isDigit(colorCodeArray[i])) {
                codeCountEndIndex = i;
            }
        }

        // Get Suffix & Starting Color Of Color Code To Be Added //
        String firstColorInCode = subColorCode;
        String suffixCountString = "";
        String colorCodeSuffix = "";
        char[] subColorCodeArray = subColorCode.toCharArray();
        colorCodeCheck = false;
        int colorCodeStartIndex = 0;
        for(int i = 0; i < subColorCode.length(); i++) {
            if(!Character.isDigit(subColorCodeArray[i])) {
                if(!colorCodeCheck) {colorCodeCheck = true;}
            }

            if((Character.isDigit(subColorCodeArray[i]) && colorCodeCheck)
            || (!Character.isDigit(subColorCodeArray[i]) && i == subColorCode.length() - 1)) {
                int indexMod = 0;
                if((!Character.isDigit(subColorCodeArray[i]) && i == subColorCode.length() - 1)) {
                    indexMod = 1;
                }
                firstColorInCode = subColorCode.substring(colorCodeStartIndex, i + indexMod);
                colorCodeSuffix = subColorCode.substring(i + indexMod);
                suffixCountString = subColorCode.substring(0, colorCodeStartIndex);
                break;
            }

            if(Character.isDigit(subColorCodeArray[i])) {
                colorCodeStartIndex = i + 1;
            }
        }

        // Get Return String //
        String returnString = colorCode + subColorCode;
        if(firstColorInCode.equals(lastColorInCode)) {
            if(Utility.isInteger(prefixCountString)
            && Utility.isInteger(suffixCountString)) {
                String colorCodeCombined = String.valueOf(Integer.valueOf(prefixCountString) + Integer.valueOf(suffixCountString)) + firstColorInCode;
                returnString = colorCodePrefix + colorCodeCombined + colorCodeSuffix;
            }
        }

        return returnString;
    }

    public static String trimColorCode(String colorCodeString, int length) {
        // Returns a color code minus the user-defined length (from the front).

        String trimmedColorCodeString = "";

        char[] colorCodeArray = colorCodeString.toUpperCase().toCharArray();
        String colorCodeSectionCountString = "";
        String colorCodeSectionCodeString = "";
        int trimmedCount = 0;
        boolean colorCodeCheck = false;

        for(int i = 0; i < colorCodeArray.length; i++) {
            char c = colorCodeArray[i];

            if(!Character.isDigit(c)) {
                colorCodeSectionCodeString += c;
                if(!colorCodeCheck) {colorCodeCheck = true;}
            }

            if(i == colorCodeArray.length - 1
            || (Character.isDigit(c) && colorCodeCheck && !Character.isDigit(colorCodeArray[i - 1]))) {
                if(trimmedCount >= length) {
                    trimmedColorCodeString += colorCodeSectionCountString + colorCodeSectionCodeString;
                    colorCodeSectionCountString = "";
                    colorCodeSectionCodeString = "";
                }

                else {
                    int colorCodeSectionCount = 0;
                    if(Utility.isInteger(colorCodeSectionCountString)) {
                        colorCodeSectionCount = Integer.valueOf(colorCodeSectionCountString);
                    }
                    if(colorCodeSectionCount + trimmedCount > length) {
                        trimmedColorCodeString += String.valueOf(colorCodeSectionCount - (length - trimmedCount)) + colorCodeSectionCodeString;
                        colorCodeSectionCount = length - trimmedCount;
                    }

                    trimmedCount += colorCodeSectionCount;
                    colorCodeSectionCountString = "";
                    colorCodeSectionCodeString = "";
                }
            }

            if(Character.isDigit(c)) {
                colorCodeSectionCountString += c;
            }
        }

        return trimmedColorCodeString;
    }

    public static String[] getColorsFromString(String colorString) {
        String[] colorStringArray = new String[3];
        colorStringArray[0] = "";
        colorStringArray[1] = "";
        colorStringArray[2] = "";

        if(!colorString.isEmpty()) {
            char[] rawColorStringArray = colorString.toCharArray();
            String colorSectionString = "";
            int colorCount = 0;

            for(int i = 0; i < rawColorStringArray.length; i++) {
                char c = rawColorStringArray[i];
                if((c != '-' && c != '_')) {
                    colorSectionString += c;
                }

                if(c == '-' || c == '_'
                || i == rawColorStringArray.length - 1) {
                    if(colorMap.containsKey(colorSectionString)) {
                        colorStringArray[colorCount] = colorSectionString;
                    }

                    colorSectionString = "";
                    colorCount += 1;
                    if(colorCount >= 3) {
                        break;
                    }
                }
            }
        }

        return colorStringArray;
    }

    public static HashMap<String, Color> loadColorMap() {
        return new HashMap<String, Color>() {{
            put("LR", new Color(255/255f, 75/255f, 75/255f, 1));
            put("R", new Color(245/255f, 0/255f, 0/255f, 1));
            put("DR", new Color(115/255f, 0/255f, 0/255f, 1));
            put("DDR", new Color(80/255f, 0/255f, 0/255f, 1));
            put("DDDR", new Color(40/255f, 0/255f, 0/255f, 1));
            put("LO", new Color(240/255f, 145/255f, 70/255f, 1));
            put("O", new Color(210/255f, 90/255f, 0/255f, 1));
            put("DO", new Color(130/255f, 50/255f, 0/255f, 1));
            put("DDO", new Color(70/255f, 30/255f, 0/255f, 1));
            put("DDDO", new Color(35/255f, 15/255f, 0/255f, 1));
            put("LY", new Color(255/255f, 255/255f, 90/255f, 1));
            put("Y", new Color(255/255f, 255/255f, 0/255f, 1));
            put("DY", new Color(130/255f, 130/255f, 0/255f, 1));
            put("DDY", new Color(70/255f, 70/255f, 0/255f, 1));
            put("DDDY", new Color(35/255f, 35/255f, 0/255f, 1));
            put("LG", new Color(90/255f, 255/255f, 90/255f, 1));
            put("G", new Color(0/255f, 255/255f, 0/255f, 1));
            put("DG", new Color(0/255f, 130/255f, 0/255f, 1));
            put("DDG", new Color(0/255f, 70/255f, 0/255f, 1));
            put("DDDG", new Color(0/255f, 35/255f, 0/255f, 1));
            put("LC", new Color(130/255f, 255/255f, 255/255f, 1));
            put("C", new Color(0/255f, 255/255f, 255/255f, 1));
            put("DC", new Color(0/255f, 140/255f, 140/255f, 1));
            put("DDC", new Color(0/255f, 70/255f, 70/255f, 1));
            put("DDDC", new Color(0/255f, 35/255f, 35/255f, 1));
            put("LB", new Color(65/255f, 65/255f, 230/255f, 1));
            put("B", new Color(0/255f, 0/255f, 255/255f, 1));
            put("DB", new Color(0/255f, 0/255f, 140/255f, 1));
            put("DDB", new Color(0/255f, 0/255f, 80/255f, 1));
            put("DDDB", new Color(0/255f, 0/255f, 47/255f, 1));
            put("LV", new Color(245/255f, 75/255f, 245/255f, 1));
            put("V", new Color(255/255f, 0/255f, 255/255f, 1));
            put("DV", new Color(140/255f, 0/255f, 140/255f, 1));
            put("DDV", new Color(80/255f, 0/255f, 80/255f, 1));
            put("DDDV", new Color(40/255f, 0/255f, 40/255f, 1));
            put("LM", new Color(135/255f, 75/255f, 235/255f, 1));
            put("M", new Color(145/255f, 0/255f, 255/255f, 1));
            put("DM", new Color(50/255f, 0/255f, 110/255f, 1));
            put("DDM", new Color(40/255f, 0/255f, 80/255f, 1));
            put("DDDM", new Color(15/255f, 0/255f, 35/255f, 1));
            put("LW", new Color(255/255f, 255/255f, 255/255f, 1));
            put("W", new Color(220/255f, 220/255f, 220/255f, 1));
            put("DW", new Color(170/255f, 170/255f, 170/255f, 1));
            put("DDW", new Color(85/255f, 85/255f, 85/255f, 1));
            put("DDDW", new Color(45/255f, 45/255f, 45/255f, 1));
            put("LGR", new Color(220/255f, 220/255f, 220/255f, 1));
            put("GR", new Color(170/255f, 170/255f, 170/255f, 1));
            put("DGR", new Color(85/255f, 85/255f, 85/255f, 1));
            put("DDGR", new Color(45/255f, 45/255f, 45/255f, 1));
            put("DDDGR", new Color(20/255f, 20/255f, 20/255f, 1));
            put("BL", new Color(0/255f, 0/255f, 0/255f, 1));
        }};
    }
}
