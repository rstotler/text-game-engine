package com.jbs.textgameengine.screen.gamescreen.userinterface.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.utility.Point;
import com.jbs.textgameengine.screen.utility.Rect;

import java.util.ArrayList;
import java.util.Random;

public class Map extends UserInterfaceElement {
    public DebugMouse dm = new DebugMouse();
    public Point mapOffset = new Point(0,0);

    public Map() {
        int mapX = Settings.INPUT_BAR_WIDTH;
        int mapY = Settings.WINDOW_HEIGHT - Settings.MAP_HEIGHT;
        int mapWidth = Settings.WINDOW_WIDTH - Settings.INPUT_BAR_WIDTH;
        int mapHeight = Settings.MAP_HEIGHT;
        rect = new Rect(mapX, mapY, mapWidth, mapHeight);

        rect.fillColor = new Color(0/255f, 0/255f, 14/255f, 1);
    }

    public void buffer(Location startLocation) {

        // Get Area & Room Data //
        SurroundingAreaAndRoomData surroundingAreaAndRoomData = SurroundingAreaAndRoomData.getSurroundingAreaAndRoomData(startLocation.room);
        int tileCountWidth = (int) TargetRoomData.highestPoint.x - (int) TargetRoomData.lowestPoint.x;
        int tileCountHeight = (int) TargetRoomData.highestPoint.y - (int) TargetRoomData.lowestPoint.y;

        // Load FrameBuffer //
        int tileSize = 20;
        int frameBufferWidth = Settings.WINDOW_WIDTH;
        int frameBufferHeight = Settings.WINDOW_HEIGHT;
//        if(tileCountWidth * tileSize > frameBufferWidth) {
//            frameBufferWidth = tileCountWidth * tileSize;
//        }
//        if(tileCountHeight * tileSize > frameBufferHeight) {
//            frameBufferHeight = tileCountHeight * tileSize;
//        }
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, frameBufferWidth, frameBufferHeight, false);
        GameScreen.spriteBatch.setProjectionMatrix(GameScreen.camera.combined);
        frameBuffer.begin();

        Gdx.graphics.getGL20().glClearColor(0f, 0f, 0f, 0f);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw Tiles //
        Screen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(TargetRoomData targetRoomData : surroundingAreaAndRoomData.targetRoomDataList) {
            int drawX = (int) targetRoomData.coordinates.x * tileSize;
            int drawY = (int) targetRoomData.coordinates.y * tileSize;
            Screen.shapeRenderer.setColor(new Color(new Random().nextFloat() - .2f, 222/255f, 122/255f, 1));
            Screen.shapeRenderer.rect(drawX, drawY, tileSize, tileSize);
        }
        Screen.shapeRenderer.end();

        GameScreen.spriteBatch.begin();

        // Draw Textures Here //

        GameScreen.spriteBatch.end();
        frameBuffer.end();
    }

    public void render() {
        super.render();

        GameScreen.spriteBatch.setProjectionMatrix(GameScreen.camera.combined);
        GameScreen.spriteBatch.begin();

        float lastLocationX = Gdx.input.getX();
        float lastLocationY = Gdx.graphics.getHeight() - Gdx.input.getY();
        int xDiff = (int) lastLocationX - (int) dm.lastLocation.x;
        int yDiff = (int) lastLocationY - (int) dm.lastLocation.y;

        if(Math.abs(xDiff) < 10 && Math.abs(yDiff) < 10) {
            mapOffset.x += xDiff;
            mapOffset.y += yDiff;
        }
        dm.update();

        GameScreen.spriteBatch.draw(frameBuffer.getColorBufferTexture(), rect.x, rect.y, frameBuffer.getWidth(), frameBuffer.getHeight(), (int) mapOffset.x, (int) mapOffset.y, frameBuffer.getWidth(), frameBuffer.getHeight(), false, true);
        GameScreen.spriteBatch.end();
    }
}

class SurroundingAreaAndRoomData {
    public ArrayList<TargetRoomData> targetRoomDataList;
    public Point lowestPoint;

    public SurroundingAreaAndRoomData(ArrayList<TargetRoomData> targetRoomDataList, Point lowestPoint) {
        this.targetRoomDataList = targetRoomDataList;
        this.lowestPoint = lowestPoint;
    }

    public static SurroundingAreaAndRoomData getSurroundingAreaAndRoomData(Room startRoom) {
        TargetRoomData.lowestPoint = new Point(0, 0);
        TargetRoomData.highestPoint = new Point(0, 0);

        ArrayList<TargetRoomData> targetRoomDataList = TargetRoomData.examineAreaAndRoomData(startRoom, -1, "", new ArrayList<TargetRoomData>(), new ArrayList<Room>(), new Point(0, 0));
        TargetRoomData.normalizeCoordinates(targetRoomDataList, TargetRoomData.lowestPoint);
        return new SurroundingAreaAndRoomData(targetRoomDataList, new Point(TargetRoomData.lowestPoint));
    }
}

class DebugMouse {
    public Point lastLocation;
    public DebugMouse() {
        lastLocation = new Point(0, 0);
    }
    public void update() {
        lastLocation.x = Gdx.input.getX();
        lastLocation.y = Gdx.graphics.getHeight() - Gdx.input.getY();
    }
}