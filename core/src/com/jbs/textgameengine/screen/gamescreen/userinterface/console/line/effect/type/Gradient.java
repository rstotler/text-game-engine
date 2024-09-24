package com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect.type;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect.Effect;

public class Gradient extends Effect {
    // Gradient A: Gradient affects entire label section. Color code effects available.
    // Gradient B: Gradient moves from left to right. Color code effects available.
    // Gradient C: Gradient moves from right to left. Color code effects available.
    // Gradient D: Gradient color-shifts each label section independently. Color code effects available.

    public Gradient(String effectCode, int maxCharLength, int sectionCount) {
        super(effectCode, maxCharLength, sectionCount);

        if(this.effectCode.equals("GRAA")) {
            float colorPercent = 0.0f;
            int currentDirection = 1;
            int currentColorIndex = 0;
            Color nextTargetColor = Line.colorMap.get("W");
            if(!primaryColor.isEmpty() && Line.colorMap.containsKey(primaryColor)) {
                nextTargetColor = Line.colorMap.get(primaryColor);
            }

            colorPercentList.set(0, colorPercent);
            moveDirList.set(0, currentDirection);
            targetRList.set(0, nextTargetColor.r);
            targetGList.set(0, nextTargetColor.g);
            targetBList.set(0, nextTargetColor.b);
            currentColorIndexList.set(0, currentColorIndex);
        }

        else if(this.effectCode.equals("GRAB")
        || this.effectCode.equals("GRAC")) {
            float colorPercent = 0.0f;
            int currentDirection = 1;
            int currentColorIndex = 0;
            Color nextTargetColor = null;
            if(Line.colorMap.containsKey(primaryColor)) {
                nextTargetColor = Line.colorMap.get(primaryColor);
            }
            else {
                nextTargetColor = Line.colorMap.get("W");
            }

            for(int i = 0; i < colorPercentList.size(); i++) {
                colorPercentList.set(i, colorPercent);
                moveDirList.set(i, currentDirection);
                targetRList.set(i, nextTargetColor.r);
                targetGList.set(i, nextTargetColor.g);
                targetBList.set(i, nextTargetColor.b);
                currentColorIndexList.set(i, currentColorIndex);

                float colorModSpeed = .10f;
                colorPercent += colorModSpeed * currentDirection;
                if(colorPercent >= 1.0
                || (currentDirection == 1 && colorPercent >= 0.0 && (colorPercent - (colorModSpeed * currentDirection) < 0.0))) {
                    if(colorPercent >= 1.0) {
                        if(currentColorIndex != effectColorCount - 1) {
                            colorPercent -= 1.0f;
                        }
                        else {
                            colorPercent -= (colorPercent - 1.0f);
                            currentDirection *= -1;
                        }
                        currentColorIndex += 1;
                    }
                    else {
                        currentColorIndex = 0;
                    }

                    // Set Next Color //
                    if(currentColorIndex == 1 && Line.colorMap.containsKey(secondaryColor)) {
                        nextTargetColor = Line.colorMap.get(secondaryColor);
                    }
                    else if(currentColorIndex == 2 && Line.colorMap.containsKey(tertiaryColor)) {
                        nextTargetColor = Line.colorMap.get(tertiaryColor);
                    }
                    else if(currentColorIndex == 0) {
                        nextTargetColor = Line.colorMap.get(primaryColor);
                    }
                }
                else if(colorPercent <= -1.75) {
                    colorPercent = -1.75f + Math.abs(colorPercent + 1.75f);
                    currentDirection *= -1;
                }
            }
        }
    }

    public void update(int sectionIndex, BitmapFont font) {
        if(effectCode.equals("GRAA")) {
            updateA(sectionIndex, font);
        } else if(effectCode.equals("GRAB")
        || effectCode.equals("GRAC")) {
            updateBCFontColor(sectionIndex, font);
            updateBC(sectionIndex, font);
        }  else if(effectCode.equals("GRAD")) {
            updateD(sectionIndex, font);
        }
    }

    public void updateA(int sectionIndex, BitmapFont font) {
        float transitionTime = -1.0f;

        int moveDirection = moveDirList.get(0);
        float colorPercent = colorPercentList.get(0);
        int colorIndex = currentColorIndexList.get(0);

        Color defaultColor = font.getColor();
        Color currentColor = font.getColor();
        if(colorIndex == effectColorCount && colorPercent <= 0.0) {
            currentColor = defaultColor;
        }
        else if(colorIndex == 1 && Line.colorMap.containsKey(primaryColor)) {
            currentColor = Line.colorMap.get(primaryColor);
        }
        else if(colorIndex == 2 && Line.colorMap.containsKey(secondaryColor)) {
            currentColor = Line.colorMap.get(secondaryColor);
        }
        else if(colorIndex == 3 && Line.colorMap.containsKey(tertiaryColor)) {
            currentColor = Line.colorMap.get(tertiaryColor);
        }
        Color nextTargetColor = new Color(targetRList.get(0), targetGList.get(0), targetBList.get(0), 1.0f);

        if(sectionIndex == 0) {
            float colorModSpeed = .01f;
            colorPercent += (colorModSpeed * moveDirection);

            // Update Color Percent & Color Index //
            if(colorPercent >= 1.0
            || (moveDirection == 1 && colorPercent >= 0.0 && (colorPercent - (colorModSpeed * moveDirection) < 0.0))) {
                if(colorPercent >= 1.0) {
                    if(colorIndex != effectColorCount - 1) {
                        colorPercent -= 1.0f;
                    }
                    else {
                        colorPercent -= (colorPercent - 1.0f);
                        moveDirection *= -1;
                    }
                    colorIndex += 1;
                }
                else {
                    colorIndex = 0;
                }

                if(colorIndex == 1 && Line.colorMap.containsKey(secondaryColor)) {
                    nextTargetColor = Line.colorMap.get(secondaryColor);
                }
                else if(colorIndex == 2 && Line.colorMap.containsKey(tertiaryColor)) {
                    nextTargetColor = Line.colorMap.get(tertiaryColor);
                }
                else if(colorIndex == 0 && Line.colorMap.containsKey(primaryColor)) {
                    nextTargetColor = Line.colorMap.get(primaryColor);
                }
            }
            else if(colorPercent <= transitionTime) {
                colorPercent = transitionTime + Math.abs(colorPercent - transitionTime);
                moveDirection *= -1;
            }

            // Set Array Data //
            moveDirList.set(0, moveDirection);
            colorPercentList.set(0, colorPercent);
            targetRList.set(0, nextTargetColor.r);
            targetGList.set(0, nextTargetColor.g);
            targetBList.set(0, nextTargetColor.b);
            currentColorIndexList.set(0, colorIndex);
        }

        // Get Color Difference //
        float displayRValue = 0.0f;
        float displayGValue = 0.0f;
        float displayBValue = 0.0f;
        float colorPercentCopy = colorPercent;

        if(colorIndex == effectColorCount && colorPercent <= 0.0) {
            currentColor = defaultColor;
        }
        else if(colorIndex == 1 && Line.colorMap.containsKey(primaryColor)) {
            currentColor = Line.colorMap.get(primaryColor);
        }
        else if(colorIndex == 2 && Line.colorMap.containsKey(secondaryColor)) {
            currentColor = Line.colorMap.get(secondaryColor);
        }
        else if(colorIndex == 3 && Line.colorMap.containsKey(tertiaryColor)) {
            currentColor = Line.colorMap.get(tertiaryColor);
        }
        else {
            currentColor = font.getColor();
        }

        for(int i = 0; i < 3; i++) {
            float targetColorValue = 0.0f;
            float currentColorValue = 0.0f;

            if(i == 0) {
                if(colorIndex == effectColorCount) {
                    targetColorValue = defaultColor.r;
                    colorPercentCopy = 1.0f - colorPercent;
                } else {
                    targetColorValue = targetRList.get(0);
                }
                currentColorValue = currentColor.r;
            }
            else if(i == 1) {
                if(colorIndex == effectColorCount) {
                    targetColorValue = defaultColor.g;
                    colorPercentCopy = 1.0f - colorPercent;
                } else {
                    targetColorValue = targetGList.get(0);
                }
                currentColorValue = currentColor.g;
            }
            else {
                if(colorIndex == effectColorCount) {
                    targetColorValue = defaultColor.b;
                    colorPercentCopy = 1.0f - colorPercent;
                } else {
                    targetColorValue = targetBList.get(0);
                }
                currentColorValue = currentColor.b;
            }

            float targetColorDiff = targetColorValue - currentColorValue;
            float newColorValue = targetColorDiff * colorPercentCopy;

            if(i == 0) {displayRValue = currentColor.r + newColorValue;}
            else if(i == 1) {displayGValue = currentColor.g + newColorValue;}
            else {displayBValue = currentColor.b + newColorValue;}
        }

        font.setColor(displayRValue, displayGValue, displayBValue, currentColor.a);
    }

    public void updateBCFontColor(int sectionIndex, BitmapFont font) {
        if(sectionIndex < moveDirList.size()
        && sectionIndex < colorPercentList.size()
        && sectionIndex < targetRList.size()) {
            int moveDirection = moveDirList.get(sectionIndex);
            float colorPercent = colorPercentList.get(sectionIndex);
            int colorIndex = currentColorIndexList.get(sectionIndex);

            Color defaultColor = font.getColor();
            Color currentColor = font.getColor();
            if(colorIndex == effectColorCount && moveDirection == 1) {
                currentColor = defaultColor;
            }
            else if(colorIndex == 1 && Line.colorMap.containsKey(primaryColor)) {
                currentColor = Line.colorMap.get(primaryColor);
            }
            else if(colorIndex == 2 && Line.colorMap.containsKey(secondaryColor)) {
                currentColor = Line.colorMap.get(secondaryColor);
            }
            else if(colorIndex == 3 && Line.colorMap.containsKey(tertiaryColor)) {
                currentColor = Line.colorMap.get(tertiaryColor);
            }

            // Get New RGB Values //
            float displayRValue = 0.0f;
            float displayGValue = 0.0f;
            float displayBValue = 0.0f;
            float colorPercentCopy = colorPercent;

            for(int i = 0; i < 3; i++) {
                float targetColorValue = 0.0f;
                float currentColorValue = 0.0f;

                if(i == 0) {
                    if(colorIndex == effectColorCount && colorPercent <= 0.0) {
                        targetColorValue = defaultColor.r;
                        colorPercentCopy = colorPercent / -1.75f;
                    } else {
                        targetColorValue = targetRList.get(sectionIndex);
                    }
                    currentColorValue = currentColor.r;
                }
                else if(i == 1) {
                    if(colorIndex == effectColorCount && colorPercent <= 0.0) {
                        targetColorValue = defaultColor.g;
                        colorPercentCopy = colorPercent / -1.75f;
                    } else {
                        targetColorValue = targetGList.get(sectionIndex);
                    }
                    currentColorValue = currentColor.g;
                }
                else {
                    if(colorIndex == effectColorCount && colorPercent <= 0.0) {
                        targetColorValue = defaultColor.b;
                        colorPercentCopy = colorPercent / -1.75f;
                    } else {
                        targetColorValue = targetBList.get(sectionIndex);
                    }
                    currentColorValue = currentColor.b;
                }

                float targetColorDiff = targetColorValue - currentColorValue;
                float newColorValue = targetColorDiff * colorPercentCopy;

                if(i == 0) {displayRValue = currentColor.r + newColorValue;}
                else if(i == 1) {displayGValue = currentColor.g + newColorValue;}
                else {displayBValue = currentColor.b + newColorValue;}
            }

            // Update Section Font Color //
            font.setColor(displayRValue, displayGValue, displayBValue, defaultColor.a);

            // Update Effect Data //
            moveDirList.set(sectionIndex, moveDirection);
            colorPercentList.set(sectionIndex, colorPercent);
        }
    }

    public void updateBC(int sectionIndex, BitmapFont font) {
        int dirMod = 1;
        if(effectCode.equals("GRAC")) {dirMod = -1;}

        float colorModSpeed = .05f * dirMod;
        int moveDirection = moveDirList.get(sectionIndex);
        int colorIndex = currentColorIndexList.get(sectionIndex);
        float colorPercent = colorPercentList.get(sectionIndex) + (colorModSpeed * moveDirection);
        Color nextTargetColor = new Color(targetRList.get(sectionIndex), targetGList.get(sectionIndex), targetBList.get(sectionIndex), 1.0f);

        // Update Variables - Forwards Gradient //
        if(effectCode.equals("GRAB")) {
            if(colorPercent >= 1.0
            || (moveDirection == 1 && colorPercent >= 0.0 && (colorPercent - (colorModSpeed * moveDirection) < 0.0))) {
                if(colorPercent >= 1.0) {
                    if(colorIndex != effectColorCount - 1) {
                        colorPercent -= 1.0f;
                    }
                    else {
                        colorPercent -= (colorPercent - 1.0f);
                        moveDirection *= -1;
                    }
                    colorIndex += 1;
                }
                else {
                    colorIndex = 0;
                }

                if(colorIndex == 1 && Line.colorMap.containsKey(secondaryColor)) {
                    nextTargetColor = Line.colorMap.get(secondaryColor);
                }
                else if(colorIndex == 2 && Line.colorMap.containsKey(tertiaryColor)) {
                    nextTargetColor = Line.colorMap.get(tertiaryColor);
                }
                else if(colorIndex == 0 && Line.colorMap.containsKey(primaryColor)) {
                    nextTargetColor = Line.colorMap.get(primaryColor);
                }
            }
            else if(colorPercent <= -1.75) {
                colorPercent = -1.75f + Math.abs(colorPercent + 1.75f);
                moveDirection *= -1;
            }
        }

        // Update Variables - Backwards Gradient //
        else {
            if((colorPercent < 0.0 && colorIndex != effectColorCount)
            || (colorPercent > 1.0 && colorIndex == effectColorCount)) {
                if(colorIndex > 0) {
                    if(colorIndex == effectColorCount) {
                        moveDirection *= -1;
                    }
                    else {
                        colorPercent += 1.0f;
                    }
                    colorIndex -= 1;
                }
                else {
                    colorIndex = effectColorCount;
                }

                if(colorIndex == effectColorCount) {
                    if(effectColorCount == 3 && Line.colorMap.containsKey(tertiaryColor)) {
                        nextTargetColor = Line.colorMap.get(tertiaryColor);
                    } else if(effectColorCount == 2 && Line.colorMap.containsKey(secondaryColor)) {
                        nextTargetColor = Line.colorMap.get(secondaryColor);
                    } else if(effectColorCount == 1 && Line.colorMap.containsKey(primaryColor)) {
                        nextTargetColor = Line.colorMap.get(primaryColor);
                    }
                }
                else if(colorIndex == 2 && Line.colorMap.containsKey(tertiaryColor)) {
                    nextTargetColor = Line.colorMap.get(tertiaryColor);
                }
                else if(colorIndex == 1 && Line.colorMap.containsKey(secondaryColor)) {
                    nextTargetColor = Line.colorMap.get(secondaryColor);
                }
                else if(colorIndex == 0 && Line.colorMap.containsKey(primaryColor)) {
                    nextTargetColor = Line.colorMap.get(primaryColor);
                }
            }
            else if(colorPercent <= -1.75) {
                colorPercent = -1.75f + Math.abs(colorPercent + 1.75f);
                moveDirection *= -1;
            }
        }

        // Set Array Data //
        moveDirList.set(sectionIndex, moveDirection);
        colorPercentList.set(sectionIndex, colorPercent);
        targetRList.set(sectionIndex, nextTargetColor.r);
        targetGList.set(sectionIndex, nextTargetColor.g);
        targetBList.set(sectionIndex, nextTargetColor.b);
        currentColorIndexList.set(sectionIndex, colorIndex);
    }

    public void updateD(int sectionIndex, BitmapFont font) {
    }
}
