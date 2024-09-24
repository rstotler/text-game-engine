package com.jbs.textgameengine.screen.gamescreen.userinterface;

import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.utility.Rect;

public class UserInterfaceElement {
    public Rect rect;

    public UserInterfaceElement() {
        rect = null;
    }

    public void render() {
        if(rect != null) {
            rect.renderShape(Screen.camera);
        }
    }
}
