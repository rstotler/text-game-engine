package com.jbs.textgameengine.screen.gamescreen.userinterface.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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

public class Map extends UserInterfaceElement {
    public Point frameBufferOffset = new Point(0, 0);
    public Point mapOffset = new Point(0,0);

    public int tileSize;
    public int tileCountWidth;
    public int tileCountHeight;

    public Map() {
        int mapX = Settings.INPUT_BAR_WIDTH;
        int mapY = Settings.WINDOW_HEIGHT - Settings.MAP_HEIGHT;
        int mapWidth = Settings.WINDOW_WIDTH - Settings.INPUT_BAR_WIDTH;
        int mapHeight = Settings.MAP_HEIGHT;
        rect = new Rect(mapX, mapY, mapWidth, mapHeight);

        rect.fillColor = new Color(0/255f, 0/255f, 14/255f, 1);

        tileSize = 20;
        tileCountWidth = 0;
        tileCountHeight = 0;
    }

    public void buffer(Location startLocation) {

        // Get Area & Room Data //
        SurroundingAreaAndRoomData surroundingAreaAndRoomData = SurroundingAreaAndRoomData.getSurroundingAreaAndRoomData(startLocation.room);
        TargetRoomData.normalizeRoomCoordinates(surroundingAreaAndRoomData.targetRoomDataList, TargetRoomData.lowestPoint);
        tileCountWidth = (int) TargetRoomData.highestPoint.x - (int) TargetRoomData.lowestPoint.x + 1;
        tileCountHeight = (int) TargetRoomData.highestPoint.y - (int) TargetRoomData.lowestPoint.y + 1;

        // Load FrameBuffer //
        int frameBufferWidth = Gdx.graphics.getWidth();
        int frameBufferHeight = Gdx.graphics.getHeight();
        if(tileCountWidth * tileSize > frameBufferWidth) {
            frameBufferWidth = tileCountWidth * tileSize;
        }
        if(tileCountHeight * tileSize > frameBufferHeight) {
            frameBufferHeight = tileCountHeight * tileSize;
        }
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, frameBufferWidth, frameBufferHeight, false);
        GameScreen.spriteBatch.setProjectionMatrix(GameScreen.camera.combined);
        frameBuffer.begin();

        Gdx.graphics.getGL20().glClearColor(0f, 0f, 0f, 0f);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw Tiles //
        Screen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(TargetRoomData targetRoomData : surroundingAreaAndRoomData.targetRoomDataList) {
            int drawX = (int) targetRoomData.targetRoom.coordinates.x * tileSize;
            int drawY = (int) targetRoomData.targetRoom.coordinates.y * tileSize;
            Color targetColor = new Color(targetRoomData.targetRoom.location.area.mapColor);
            if(targetRoomData.targetRoom.mapColorTargetColor.equals("R")) {
                targetColor.r += targetRoomData.targetRoom.mapColorMod;
            }
            if(targetRoomData.targetRoom.mapColorTargetColor.equals("G")) {
                targetColor.g += targetRoomData.targetRoom.mapColorMod;
            }
            if(targetRoomData.targetRoom.mapColorTargetColor.equals("B")) {
                targetColor.b += targetRoomData.targetRoom.mapColorMod;
            }
            Screen.shapeRenderer.setColor(targetColor);
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

        GameScreen.spriteBatch.draw(frameBuffer.getColorBufferTexture(), rect.x + frameBufferOffset.x, rect.y + frameBufferOffset.y, frameBuffer.getWidth(), frameBuffer.getHeight(), (int) mapOffset.x, (int) mapOffset.y, frameBuffer.getWidth(), frameBuffer.getHeight(), false, true);
        GameScreen.spriteBatch.end();
    }

    public void updateOffset(Room targetRoom) {
        frameBufferOffset.x = 0;
        frameBufferOffset.y = 0;

        int mapX = ((int) targetRoom.coordinates.x * tileSize) + (tileSize / 2);
        int mapY = ((int) targetRoom.coordinates.y * tileSize) + (tileSize / 2);
        mapOffset.x = mapX - (rect.width / 2);
        mapOffset.y = mapY - (rect.height / 2);

        int diffX = (rect.width / 2) - (tileSize / 2) - ((int) targetRoom.coordinates.x * tileSize);
        int diffY = (rect.height / 2) - (tileSize / 2) - ((int) targetRoom.coordinates.y * tileSize);

        if(diffX > 0) {
            mapOffset.x += diffX;
            frameBufferOffset.x += diffX;
        }
        if(diffY > 0) {
            mapOffset.y += diffY;
            frameBufferOffset.y += diffY;
        }
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
        return new SurroundingAreaAndRoomData(targetRoomDataList, new Point(TargetRoomData.lowestPoint));
    }
}
