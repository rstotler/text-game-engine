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

    public static float distanceBetweenPoints(Point p1, Point p2) {
        float xDiff = p2.x - p1.x;
        float yDiff = p2.y - p1.y;
        float xx = (float) Math.pow(xDiff, 2);
        float yy = (float) Math.pow(yDiff, 2);

        return (float) Math.sqrt(xx + yy);
    }

    public static Line insertCommas(int targetNum) {
        String targetNumString = "";
        String targetNumColorCode = "";
        boolean negativeCheck = targetNum < 0;
        targetNum = Math.abs(targetNum);

        for(int i = String.valueOf(targetNum).length() - 1; i >= 0; i--) {
            targetNumString = String.valueOf(targetNum).charAt(i) + targetNumString;
            targetNumColorCode = "1DDW" + targetNumColorCode;

            if((String.valueOf(targetNum).length() - i) % 3 == 0
            && i > 0) {
                targetNumString = "," + targetNumString;
                targetNumColorCode = "1DY" + targetNumColorCode;
            }
        }

        if(negativeCheck) {
            targetNumString = "-" + targetNumString;
            targetNumColorCode = "1DY" + targetNumColorCode;
        }

        return new Line(targetNumString, targetNumColorCode, "", true, true);
    }

    public static Line insertCommas(float targetNum) {
        Line targetNumLine = insertCommas((int) targetNum);

        if(String.valueOf(targetNum).contains(".")) {
            int pointIndex = String.valueOf(targetNum).indexOf('.');
            int endIndex = pointIndex + 3;
            if(endIndex > String.valueOf(targetNum).length()) {
                endIndex = String.valueOf(targetNum).length();
            }
            String pointString = String.valueOf(targetNum).substring(pointIndex, endIndex);
            targetNumLine.label += pointString;
            targetNumLine.colorCode += "1DY" + String.valueOf(pointString.length() - 1) + "DDW";
        }

        return targetNumLine;
    }
}
