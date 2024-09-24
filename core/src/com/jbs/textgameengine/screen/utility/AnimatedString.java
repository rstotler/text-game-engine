package com.jbs.textgameengine.screen.utility;

import java.util.Random;

public class AnimatedString {
    public String label;
    public int updateTimer;
    public int displaySpeed; // Higher = Slower, Lower = Faster

    public AnimatedString(String label) {
        this.label = label;
        updateTimer = -(new Random().nextInt(25));
        displaySpeed = 3;
    }

    public void update() {
        if(updateTimer < label.length() * displaySpeed) {
            updateTimer += 1;
        }
    }

    public String getCurrentLabel() {
        String currentLabel = "";

        if(updateTimer >= 0) {
            int charDisplayCount = updateTimer / displaySpeed;
            if(charDisplayCount > label.length()) {
                charDisplayCount = label.length();
            }
            currentLabel = label.substring(0, charDisplayCount);
        }

        return currentLabel;
    }
}
