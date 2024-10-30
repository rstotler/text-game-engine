package com.jbs.textgameengine.screen.gamescreen.userinterface.roomview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.AreaAndRoomData;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.utility.Point;
import com.jbs.textgameengine.screen.utility.Rect;

import java.util.ArrayList;
import java.util.Arrays;

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
        AreaAndRoomData surroundingAreaAndRoomData = RoomView.getSurroundingAreaAndRoomData(targetLocation.room, 5);

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

                    System.out.println(imageManager.floorMap.get(rowString).get(columnString).keySet());
                    if(imageManager.floorMap.containsKey(rowString)
                    && imageManager.floorMap.get(rowString).containsKey(columnString)
                    && imageManager.floorMap.get(rowString).get(columnString).containsKey("Default")) {
                        Texture floorTileTexture = imageManager.floorMap.get(rowString).get(columnString).get("Default");
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

    // Utility Functions //
    public static AreaAndRoomData getSurroundingAreaAndRoomData(Room targetRoom, int maxDistance) {
        return examineRoomData(targetRoom, maxDistance, "", new ArrayList<Room>(), new Point(0, 0), new Room[7][7]);
    }

    public static AreaAndRoomData examineRoomData(Room targetRoom, int maxDistance, String targetDirection, ArrayList<Room> examinedRoomList, Point coordinatePoint, Room[][] roomViewRoomList) {
        // Helper Function For GetSurroundingAreaAndRoomData()

        if(!examinedRoomList.contains(targetRoom)) {
            examinedRoomList.add(targetRoom);

            // RoomView Data //
            int roomViewX = 3 + (int) coordinatePoint.x;
            int roomViewY = 3 + (int) coordinatePoint.y;
            if(roomViewX >= 0 && roomViewX < roomViewRoomList.length
            && roomViewY >= 0 && roomViewY < roomViewRoomList[0].length
            && roomViewRoomList[roomViewX][roomViewY] == null) {
                roomViewRoomList[roomViewX][roomViewY] = targetRoom;
            }
        }

        if(coordinatePoint.x + coordinatePoint.y < maxDistance) {
            Point firstCoordinatePoint = new Point(coordinatePoint);
            ArrayList<String> potentialDirectionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West"));
            if(!targetDirection.isEmpty() && potentialDirectionList.contains(Location.getOppositeDirection(targetDirection))) {
                potentialDirectionList.remove(Location.getOppositeDirection(targetDirection));
            }

            for(String direction : potentialDirectionList) {
                if(!direction.equals("North")) {
                    coordinatePoint = new Point(firstCoordinatePoint);
                }
                if(targetRoom.exitMap.containsKey(direction)) {
                    TargetRoomData targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(targetRoom, new ArrayList<>(Arrays.asList(direction)), true, true);

                    if(!examinedRoomList.contains(targetRoomData.targetRoom)
                    && targetRoomData.targetRoom.location.area != null) {
                        if(direction.equals("North")) {
                            coordinatePoint.y += 1;
                        }
                        else if(direction.equals("East")) {
                            coordinatePoint.x += 1;
                        }
                        else if(direction.equals("South")) {
                            coordinatePoint.y -= 1;
                        }
                        else if(direction.equals("West")) {
                            coordinatePoint.x -= 1;
                        }

                        AreaAndRoomData targetAreaAndRoomData = RoomView.examineRoomData(targetRoomData.targetRoom, maxDistance, direction, examinedRoomList, coordinatePoint, roomViewRoomList);
                        examinedRoomList = targetAreaAndRoomData.roomList;
                        roomViewRoomList = targetAreaAndRoomData.roomViewRoomList;
                    }
                }
            }
        }

        return new AreaAndRoomData(null, examinedRoomList, roomViewRoomList);
    }
}
