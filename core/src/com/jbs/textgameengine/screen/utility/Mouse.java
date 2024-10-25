package com.jbs.textgameengine.screen.utility;

import com.badlogic.gdx.Gdx;
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
        location.y = Gdx.input.getY();

        hoverUIElement = null;
        for(UserInterfaceElement userInterfaceElement : GameScreen.userInterface.getAllElements()) {
            if(location.x >= userInterfaceElement.rect.x
            && location.x < (userInterfaceElement.rect.x + userInterfaceElement.rect.width)
            && location.y >= userInterfaceElement.rect.y
            && location.y < (userInterfaceElement.rect.y + userInterfaceElement.rect.height)) {
                hoverUIElement = userInterfaceElement;
                break;
            }
        }
    }
}
