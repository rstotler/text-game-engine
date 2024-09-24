package com.jbs.textgameengine.screen.gamescreen.userinterface.roomview;

import com.badlogic.gdx.graphics.Color;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.utility.Rect;

public class RoomView extends UserInterfaceElement {
    public RoomView() {
        int roomViewX = 0;
        int roomViewY = Settings.WINDOW_HEIGHT - Settings.ROOM_VIEW_HEIGHT;
        int roomViewWidth = Settings.INPUT_BAR_WIDTH;
        int roomViewHeight = Settings.ROOM_VIEW_HEIGHT;
        rect = new Rect(roomViewX, roomViewY, roomViewWidth, roomViewHeight);

        rect.fillColor = new Color(0/255f, 0/255f, 5/255f, 1);
    }
}
