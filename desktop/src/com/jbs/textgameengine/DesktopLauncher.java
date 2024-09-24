package com.jbs.textgameengine;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		config.setResizable(false);
		config.setForegroundFPS(Settings.FPS);
		config.setTitle(Settings.TITLE);
		config.setWindowPosition(0, 20);
		new Lwjgl3Application(new TextGameEngine(), config);
	}
}
