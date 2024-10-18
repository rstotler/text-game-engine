package com.jbs.textgameengine.screen.utility;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.textgameengine.screen.Screen;

import java.util.Random;

public class Rect {
    public float x;
    public float y;
    public int width;
    public int height;

    public Color fillColor;

    public Rect(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        fillColor = new Color(new Random().nextInt(256)/255f,
                              new Random().nextInt(256)/255f,
                              new Random().nextInt(256)/255f, 1);
    }

    public Rect(int width, int height) {
        this(0, 0, width, height);
    }

    public void renderShape(OrthographicCamera camera) {
        if(camera != null) {
            Screen.shapeRenderer.setProjectionMatrix(camera.combined);
        }
        else {
            Screen.shapeRenderer.setProjectionMatrix(null);
        }

        Screen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Screen.shapeRenderer.setColor(fillColor);
        Screen.shapeRenderer.rect(x, y, width, height);
        Screen.shapeRenderer.end();
    }
}
