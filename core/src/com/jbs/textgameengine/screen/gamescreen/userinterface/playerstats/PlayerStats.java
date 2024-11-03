package com.jbs.textgameengine.screen.gamescreen.userinterface.playerstats;

import com.badlogic.gdx.graphics.Color;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.utility.Rect;

public class PlayerStats extends UserInterfaceElement {
    public PlayerStats() {
        int playerStatsX = Settings.INPUT_BAR_WIDTH;
        int playerStatsY = 0;
        int playerStatsWidth = Settings.WINDOW_WIDTH - Settings.INPUT_BAR_WIDTH;
        int playerStatsHeight = Settings.PLAYER_STATS_HEIGHT;
        rect = new Rect(playerStatsX, playerStatsY, playerStatsWidth, playerStatsHeight);
        rect.fillColor = new Color(0/255f, 0/255f, 15/255f, 0);
    }
}
