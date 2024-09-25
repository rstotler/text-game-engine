package com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.pattern;

import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Pattern {
    // Alternate A: Alternates between two colors.
    // Alternate B: Alternates between three colors.
    //     Fade In: Fades label section in from black.
    //    Fade Out: Fades label section out to black.
    //    Gradient: Fades label section in and out. (Dark/Light/Dark)
    //   Shimmer A: Alternates between four color shades from dark to light.
    //   Shimmer B: Alternates between four color shades from light to dark.
    //    Contrast: First letter is highlighted.

    public static ArrayList<String> patternList = new ArrayList<>(Arrays.asList("ALTA", "ALTB", "FADI", "FADO", "GRAD", "SHIA", "SHIB", "CONT"));

    public static String formatColorCode(String label, String patternCode) {
        String newColorCode = "";

        char[] patternCodeArray = patternCode.toCharArray();
        String patternCodeSectionCountString = "";
        String patternCodeSectionCodeString = "";
        boolean patternCodeCheck = false;

        for(int i = 0; i < patternCodeArray.length; i++) {
            char c = patternCodeArray[i];

            if(!Character.isDigit(c)) {
                patternCodeSectionCodeString += c;
                if(!patternCodeCheck) {patternCodeCheck = true;}
            }

            if(i == patternCodeArray.length - 1
            || (Character.isDigit(c) && patternCodeCheck && !Character.isDigit(patternCodeArray[i - 1]))) {
                if(patternCodeSectionCountString.isEmpty()) {
                    patternCodeSectionCountString = String.valueOf(label.length());
                }

                if(patternCodeSectionCodeString.length() >= 4
                && patternList.contains(patternCodeSectionCodeString.substring(0, 4))) {
                    newColorCode += Pattern.convertPatternCodeToColorCode(patternCodeSectionCodeString, Integer.valueOf(patternCodeSectionCountString));
                }
                else {
                    newColorCode += patternCodeSectionCountString + patternCodeSectionCodeString;
                }

                patternCodeSectionCountString = "";
                patternCodeSectionCodeString = "";
            }

            if(Character.isDigit(c)) {
                patternCodeSectionCountString += c;
            }
        }

        return newColorCode;
    }

    public static String convertPatternCodeToColorCode(String patternCode, int length) {
        String colorCodeString = "";

        // Get Default Colors //
        String[] colorData = Line.getColorsFromString(patternCode.substring(4));

        String primaryColorString = "W";
        String secondaryColorString = "DDW";
        String tertiaryColorString = "DDDW";
        boolean setPrimary = false;
        boolean setSecondary = false;
        boolean setTertiary = false;

        if(!colorData[0].isEmpty()) {
            primaryColorString = colorData[0];
            setPrimary = true;
        }
        if(!colorData[1].isEmpty()) {
            secondaryColorString = colorData[1];
            setSecondary = true;
        }
        if(!colorData[2].isEmpty()) {
            tertiaryColorString = colorData[2];
            setTertiary = true;
        }

        // Get Pattern Color Code //
        if(true) {
            if(patternCode.substring(0, 4).equals("ALTA")) {
                String altaSecondaryColorString = "DD" + primaryColorString;
                if(setSecondary) {
                    altaSecondaryColorString = secondaryColorString;
                }

                for(int i = 0; i < Math.ceil(length / 2.0); i++) {
                    colorCodeString += "1" + primaryColorString + "1" + altaSecondaryColorString;
                }
                if(length % 2 != 0) {
                    colorCodeString = colorCodeString.substring(0, colorCodeString.length() - 1 - altaSecondaryColorString.length());
                }
            }

            else if(patternCode.substring(0, 4).equals("ALTB")) {
                String altbSecondaryColorString = "DD" + primaryColorString;
                String altbTertiaryColorString = "DDD" + primaryColorString;
                if(setSecondary) {
                    altbSecondaryColorString = secondaryColorString;
                }
                if(setTertiary) {
                    altbTertiaryColorString = tertiaryColorString;
                }

                for(int i = 0; i < Math.ceil(length / 3.0); i++) {
                    colorCodeString += "1" + primaryColorString + "1" + altbSecondaryColorString + "1" + altbTertiaryColorString;
                }
                if(length % 3 != 0) {
                    int subPatternWidth = 0;
                    if(length % 3 == 1) {
                        subPatternWidth = 1 + altbTertiaryColorString.length();
                    } else if(length % 3 == 2) {
                        subPatternWidth = 1 + altbSecondaryColorString.length() + 1 + altbTertiaryColorString.length();
                    }
                    colorCodeString = colorCodeString.substring(0, colorCodeString.length() - subPatternWidth);
                }
            }

            else if(patternCode.substring(0, 4).equals("FADI") || patternCode.substring(0, 4).equals("FADO")) {
                String firstColorMod = "DDD";
                String secondColorMod = "DD";
                String thirdColorMod = "D";
                String fourthColotMod = "";
                if(patternCode.substring(0, 4).equals("FADO")) {
                    firstColorMod = "";
                    secondColorMod = "D";
                    thirdColorMod = "DD";
                    fourthColotMod = "DDD";
                }

                String fadiPrimaryColorString = primaryColorString;
                int charWidthCount = (int) (length * .25);
                colorCodeString += String.valueOf(charWidthCount) + firstColorMod + fadiPrimaryColorString
                        + String.valueOf(charWidthCount) + secondColorMod + fadiPrimaryColorString
                        + String.valueOf(charWidthCount) + thirdColorMod + fadiPrimaryColorString;
                if(charWidthCount * 4 > length) {
                    charWidthCount = length - (charWidthCount * 3);
                }

                colorCodeString += String.valueOf(charWidthCount) + fourthColotMod + fadiPrimaryColorString;
            }

            else if(patternCode.substring(0, 4).equals("GRAD")) {
                if(length < 7) {
                    if(length == 1) {
                        colorCodeString = "1" + primaryColorString;
                    } else if(length == 2) {
                        colorCodeString = "2" + primaryColorString;
                    } else if(length == 3) {
                        colorCodeString = "1D" + primaryColorString + "1" + primaryColorString + "1D" + primaryColorString;
                    } else if(length == 4) {
                        colorCodeString = "1D" + primaryColorString + "2" + primaryColorString + "1D" + primaryColorString;
                    } else if(length == 5) {
                        colorCodeString = "1DD" + primaryColorString + "1D" + primaryColorString + "1" + primaryColorString + "1D" + primaryColorString + "1DD" + primaryColorString;
                    } else if(length == 6) {
                        colorCodeString = "1DD" + primaryColorString + "1D" + primaryColorString + "2" + primaryColorString + "1D" + primaryColorString + "1DD" + primaryColorString;
                    }
                }

                else {
                    int firstSectionCharCount = (int) (length * .16);
                    int secondSectionCharCount = (int) (length * .14);
                    int thirdSectionCharCount = (int) (length * .12);

                    if(firstSectionCharCount == 0) {firstSectionCharCount = 1;}
                    if(secondSectionCharCount == 0) {secondSectionCharCount = 1;}
                    if(thirdSectionCharCount == 0) {thirdSectionCharCount = 1;}

                    int middleSectionCount = length - ((firstSectionCharCount + secondSectionCharCount + thirdSectionCharCount) * 2);

                    colorCodeString = String.valueOf(firstSectionCharCount) + "DDD" + primaryColorString
                            + String.valueOf(secondSectionCharCount) + "DD" + primaryColorString
                            + String.valueOf(thirdSectionCharCount) + "D" + primaryColorString
                            + String.valueOf(middleSectionCount) + primaryColorString
                            + String.valueOf(thirdSectionCharCount) + "D" + primaryColorString
                            + String.valueOf(secondSectionCharCount) + "DD" + primaryColorString
                            + String.valueOf(firstSectionCharCount) + "DDD" + primaryColorString;
                }
            }

            else if(patternCode.substring(0, 4).equals("SHIA") || patternCode.substring(0, 4).equals("SHIB")) {
                colorCodeString += "1" + primaryColorString;

                int firstStringSectionWidth = 2;
                if(patternCode.substring(0, 4).equals("SHIB")) {
                    firstStringSectionWidth = 4;
                }

                for(int i = 0; i < Math.ceil((length - 1) / 3.0); i++) {
                    if(patternCode.substring(0, 4).equals("SHIA")) {
                        colorCodeString += "1DDD" + primaryColorString + "1DD" + primaryColorString + "1D" + primaryColorString;
                    } else {
                        colorCodeString += "1D" + primaryColorString + "1DD" + primaryColorString + "1DDD" + primaryColorString;
                    }
                }
                if((length - 1) % 3 != 0) {
                    int subPatternWidth = 0;
                    if((length - 1) % 3 == 1) {
                        subPatternWidth = firstStringSectionWidth + primaryColorString.length() + 3 + primaryColorString.length();
                    } else if((length - 1) % 3 == 2) {
                        subPatternWidth = firstStringSectionWidth + primaryColorString.length();
                    }
                    colorCodeString = colorCodeString.substring(0, colorCodeString.length() - subPatternWidth);
                }
            }

            else if(patternCode.substring(0, 4).equals("CONT")) {
                colorCodeString = "1" + primaryColorString;

                for(int i = 0; i < length - 1; i++) {
                    colorCodeString += "1DD" + primaryColorString;
                }
            }
        }

        return colorCodeString;
    }
}
