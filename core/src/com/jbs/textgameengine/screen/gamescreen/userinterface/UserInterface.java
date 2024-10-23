package com.jbs.textgameengine.screen.gamescreen.userinterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.Console;
import com.jbs.textgameengine.screen.gamescreen.userinterface.map.Map;
import com.jbs.textgameengine.screen.gamescreen.userinterface.prompt.InputBar;
import com.jbs.textgameengine.screen.gamescreen.userinterface.roomview.RoomView;

public class UserInterface {
    public InputBar inputBar;
    public Console console;
    public RoomView roomView;
    public Map map;

    public UserInterface() {
        inputBar = new InputBar();
        console = new Console();
        roomView = new RoomView();
        map = new Map();
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        inputBar.render();
        console.render();
        roomView.render();
        map.render();
    }

    public void dispose() {
        inputBar.font.dispose();
        console.font.dispose();
        map.frameBuffer.dispose();
    }
}
