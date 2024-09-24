package com.jbs.textgameengine.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Screen {
    public static OrthographicCamera camera = new OrthographicCamera();
    public static ShapeRenderer shapeRenderer;
    public static SpriteBatch spriteBatch;

    public Screen() {
        camera.setToOrtho(false, 1920, 1080);
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
    }

    public String update() { return ""; }
    public void render() {}

    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }
}
