package com.jbs.textgameengine;

public class Settings {
    public static int FPS = 60;
    public static String TITLE = "TextGame Engine";
    public static int WINDOW_WIDTH = 1920;
    public static int WINDOW_HEIGHT = 1080;

    public static int INPUT_BAR_WIDTH = (int) (WINDOW_WIDTH * .725);
    public static int INPUT_BAR_HEIGHT = 40;
    public static int ROOM_VIEW_HEIGHT = (int) (WINDOW_HEIGHT * .55);
    public static int MAP_HEIGHT = WINDOW_WIDTH - INPUT_BAR_WIDTH;
    public static int PLAYER_STATS_HEIGHT = 100;
}
