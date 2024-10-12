package com.jbs.textgameengine.screen.utility;

import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Utility {
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public static boolean isInteger(char c) {
        return isInteger(String.valueOf(c));
    }

    public static Line insertCommas(int targetNum) {
        String targetNumString = "";
        String targetNumColorCode = "";

        for(int i = String.valueOf(targetNum).length() - 1; i >= 0; i--) {
            targetNumString = String.valueOf(targetNum).charAt(i) + targetNumString;
            targetNumColorCode = "1DDW" + targetNumColorCode;

            if((String.valueOf(targetNum).length() - i) % 3 == 0
            && i > 0) {
                targetNumString = "," + targetNumString;
                targetNumColorCode = "1DDGR" + targetNumColorCode;
            }
        }

        return new Line(targetNumString, targetNumColorCode, "", true, true);
    }
}
