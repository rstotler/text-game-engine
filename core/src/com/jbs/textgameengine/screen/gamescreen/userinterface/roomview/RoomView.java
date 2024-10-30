package com.jbs.textgameengine.screen.gamescreen.userinterface.roomview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.AreaAndRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.utility.Rect;

public class RoomView extends UserInterfaceElement {
    public OrthographicCamera cameraDraw;
    public ImageManager imageManager;

    public RoomView() {
        int roomViewX = 0;
        int roomViewY = Settings.WINDOW_HEIGHT - Settings.ROOM_VIEW_HEIGHT;
        int roomViewWidth = Settings.INPUT_BAR_WIDTH;
        int roomViewHeight = Settings.ROOM_VIEW_HEIGHT;
        rect = new Rect(roomViewX, roomViewY, roomViewWidth, roomViewHeight);

        rect.fillColor = new Color(0/255f, 0/255f, 15/255f, 1);

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, rect.width, rect.height, false);

        cameraDraw = new OrthographicCamera();
        cameraDraw.setToOrtho(true, rect.width, rect.height);

        imageManager = new ImageManager();
    }

    public void buffer(Location targetLocation, String facingDirection) {
        AreaAndRoomData surroundingAreaAndRoomData = AreaAndRoomData.getSurroundingAreaAndRoomData(targetLocation.room, 5, true, true);

        frameBuffer.begin();
        Gdx.graphics.getGL20().glClearColor(rect.fillColor.r, rect.fillColor.g, rect.fillColor.b, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameScreen.spriteBatch.setProjectionMatrix(cameraDraw.combined);
        GameScreen.spriteBatch.begin();

        int rowIndexStart = 5;
        int columnIndexStart = 0;

        if(facingDirection.equals("East")) {
            rowIndexStart = 6;
            columnIndexStart = 5;
        }
        else if(facingDirection.equals("South")) {
            rowIndexStart = 1;
            columnIndexStart = 6;
        }
        else if(facingDirection.equals("West")) {
            rowIndexStart = 0;
            columnIndexStart = 1;
        }

        int rowIndex = rowIndexStart;
        int columnIndex = columnIndexStart;

        for(int y = 0; y < 3; y++) {
            if(facingDirection.equals("North")) {columnIndex = columnIndexStart;}
            else if(facingDirection.equals("East")) {rowIndex = rowIndexStart;}
            else if(facingDirection.equals("South")) {columnIndex = columnIndexStart;}
            else if(facingDirection.equals("West")) {rowIndex = rowIndexStart;}

            for(int x = 0; x < 7; x++) {
                if(columnIndex > 0 && columnIndex < surroundingAreaAndRoomData.roomViewRoomList.length
                && rowIndex > 0 && rowIndex < surroundingAreaAndRoomData.roomViewRoomList[0].length
                && surroundingAreaAndRoomData.roomViewRoomList[columnIndex][rowIndex] != null) {
                    String rowString = "Back";
                    if(y == 1) {rowString = "Middle";}
                    else if(y == 2) {rowString = "Front";}

                    String columnString = "Left3";
                    if(x == 1) {columnString = "Left2";}
                    else if(x == 2) {columnString = "Left1";}
                    else if(x == 3) {columnString = "Middle";}
                    else if(x == 4) {columnString = "Right1";}
                    else if(x == 5) {columnString = "Right2";}
                    else if(x == 6) {columnString = "Right3";}

                    if(imageManager.tileMap.containsKey(rowString)
                    && imageManager.tileMap.get(rowString).containsKey(columnString)
                    && imageManager.tileMap.get(rowString).get(columnString).containsKey("Default")) {
                        Texture floorTileTexture = imageManager.tileMap.get(rowString).get(columnString).get("Default");
                        GameScreen.spriteBatch.draw(floorTileTexture, 0, 0);
                    }
                }

                if(facingDirection.equals("North")) {columnIndex++;}
                else if(facingDirection.equals("East")) {rowIndex--;}
                else if(facingDirection.equals("South")) {columnIndex--;}
                else if(facingDirection.equals("West")) {rowIndex++;}
            }

            if(facingDirection.equals("North")) {rowIndex--;}
            else if(facingDirection.equals("East")) {columnIndex--;}
            else if(facingDirection.equals("South")) {rowIndex++;}
            else if(facingDirection.equals("West")) {columnIndex++;}
        }

        GameScreen.spriteBatch.end();
        frameBuffer.end();
    }

    public void render() {
        GameScreen.spriteBatch.setProjectionMatrix(GameScreen.camera.combined);
        GameScreen.spriteBatch.begin();
        GameScreen.spriteBatch.draw(frameBuffer.getColorBufferTexture(), rect.x, rect.y);
        GameScreen.spriteBatch.end();
    }
}
