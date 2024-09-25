package com.jbs.textgameengine.screen.utility;

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
}
