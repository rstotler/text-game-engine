package com.jbs.textgameengine.screen.utility;

import com.badlogic.gdx.Gdx;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;

public class Mouse {
    public Point location;
    public UserInterfaceElement hoverUIElement;

    public Mouse() {
        location = new Point(0, 0);
        hoverUIElement = null;
    }

    public void update() {
        location.x = Gdx.input.getX();
        location.y = Gdx.graphics.getHeight() - Gdx.input.getY();

        float widthPercent = Gdx.graphics.getWidth() / (Settings.WINDOW_WIDTH + 0.0f);
        float heightPercent = Gdx.graphics.getHeight() / (Settings.WINDOW_HEIGHT + 0.0f);

        hoverUIElement = null;
        for(UserInterfaceElement userInterfaceElement : GameScreen.userInterface.getAllElements()) {
            if(location.x >= (userInterfaceElement.rect.x * widthPercent)
            && location.x < (userInterfaceElement.rect.x + userInterfaceElement.rect.width) * widthPercent
            && location.y >= userInterfaceElement.rect.y * heightPercent
            && location.y < (userInterfaceElement.rect.y + userInterfaceElement.rect.height) * heightPercent) {
                hoverUIElement = userInterfaceElement;
                break;
            }
        }
    }
}
