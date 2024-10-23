package com.jbs.textgameengine.screen.gamescreen.userinterface;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.utility.Rect;

public class UserInterfaceElement {
    public FrameBuffer frameBuffer;
    public Rect rect;

    public UserInterfaceElement() {
        frameBuffer = null;
        rect = null;
    }

    public void render() {
        if(rect != null) {
            rect.renderShape(Screen.camera);
        }
    }
}
