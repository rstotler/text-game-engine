package com.jbs.textgameengine.screen.gamescreen.userinterface.map;

import com.badlogic.gdx.graphics.Color;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.utility.Rect;

public class Map extends UserInterfaceElement {
    public Map() {
        int mapX = Settings.INPUT_BAR_WIDTH;
        int mapY = Settings.WINDOW_HEIGHT - Settings.MAP_HEIGHT;
        int mapWidth = Settings.WINDOW_WIDTH - Settings.INPUT_BAR_WIDTH;
        int mapHeight = Settings.MAP_HEIGHT;
        rect = new Rect(mapX, mapY, mapWidth, mapHeight);

        rect.fillColor = new Color(0/255f, 0/255f, 14/255f, 1);
    }
}
