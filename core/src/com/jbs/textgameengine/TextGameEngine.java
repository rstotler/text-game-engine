package com.jbs.textgameengine;

import com.badlogic.gdx.ApplicationAdapter;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.gamescreen.*;

public class TextGameEngine extends ApplicationAdapter {
	Screen screen;

	@Override
	public void create () {
		screen = new GameScreen();
	}

	@Override
	public void render () {
		String updateString = screen.update();

		if(updateString.equals("New Game")) {
			screen.dispose();
			screen = new GameScreen();
		}

		screen.render();
	}
	
	@Override
	public void dispose () {}
}
