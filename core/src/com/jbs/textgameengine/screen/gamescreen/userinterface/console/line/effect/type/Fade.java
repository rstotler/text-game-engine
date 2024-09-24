package com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect.type;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.effect.Effect;

public class Fade extends Effect {
    // Fade A: Label section fades in/out.
    // Fade B: Label section fades left to right.
    // Fade C: Label section fades right to left.

    public Fade(String effectCode, int maxCharLength, int sectionCount) {
        super(effectCode, maxCharLength, sectionCount);

        if(this.effectCode.equals("FADA")) {
            for(int i = 0; i < colorPercentList.size(); i++) {
                colorPercentList.set(i, 1.0f);
            }
        }

        else if(this.effectCode.equals("FADB")
        || this.effectCode.equals("FADC")) {
            float currentAlpha = 1.0f;
            int currentDirection = 1;
            float fadeAmount = .065f + (.05f * maxCharLength);
            if(fadeAmount > .15) {fadeAmount = .15f;}

            for(int i = 0; i < colorPercentList.size(); i++) {
                currentAlpha += (fadeAmount * currentDirection);
                if(currentAlpha >= 1.25) {
                    currentAlpha -= (currentAlpha - 1.25f);
                    currentDirection *= -1;
                } else if(currentAlpha <= -0.10) {
                    currentAlpha *= -1;
                    currentDirection *= -1;
                }

                colorPercentList.set(i, currentAlpha);
                moveDirList.set(i, currentDirection);
            }
        }
    }

    public void update(int sectionIndex, BitmapFont font) {
        if(effectCode.equals("FADA")) {
            updateA(sectionIndex, font);
        } else if(effectCode.equals("FADB")
        || effectCode.equals("FADC")) {
            updateBC(sectionIndex, font);
        }
    }

    public void updateA(int sectionIndex, BitmapFont font) {
        float currentAlpha = colorPercentList.get(0);

        if(sectionIndex == 0) {
            int moveDirection = moveDirList.get(sectionIndex);
            currentAlpha += (.0075f * moveDirection);

            if((moveDirection == 1 && currentAlpha >= 1.25)
            || (moveDirection == -1 && currentAlpha <= -0.10)) {
                if(moveDirection == 1 && currentAlpha > 1.25) {
                    currentAlpha = 1.25f;
                } else if(moveDirection == -1 && currentAlpha < -0.10) {
                    currentAlpha = -0.10f;
                }
                moveDirList.set(sectionIndex, moveDirList.get(sectionIndex) * -1);
            }
            colorPercentList.set(sectionIndex, currentAlpha);
        }

        Color currentColor = font.getColor();
        font.setColor(currentColor.r, currentColor.g, currentColor.b, currentAlpha);
    }

    public void updateBC(int sectionIndex, BitmapFont font) {
        if(sectionIndex < colorPercentList.size()) {
            int dirMod = 1;
            if(effectCode.equals("FADB")) {dirMod = -1;}

            int speedCharCount = maxCharLength;
            if(speedCharCount > 15) {speedCharCount = 15;}
            float glowSpeed = (.015f - (speedCharCount * .00025f)) * dirMod;

            int moveDirection = moveDirList.get(sectionIndex);
            float newAlpha = colorPercentList.get(sectionIndex) + (glowSpeed * moveDirection);

            if((moveDirection == 1 * dirMod && newAlpha >= 1.25)
            || (moveDirection == -1 * dirMod && newAlpha <= -0.10)) {
                if(moveDirection == 1 *dirMod && newAlpha >= 1.25) {
                    newAlpha -= (newAlpha - 1.25f);
                    colorPercentList.set(sectionIndex, newAlpha);
                } else if(moveDirection == -1 * dirMod && newAlpha <= -0.10) {
                    newAlpha *= -1;
                    colorPercentList.set(sectionIndex, newAlpha);
                }
                moveDirection *= -1;
                moveDirList.set(sectionIndex, moveDirection);
            }

            Color currentColor = font.getColor();
            font.setColor(currentColor.r, currentColor.g, currentColor.b, newAlpha);

            colorPercentList.set(sectionIndex, newAlpha);
        }
    }
}
