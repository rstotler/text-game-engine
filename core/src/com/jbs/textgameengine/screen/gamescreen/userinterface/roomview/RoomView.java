package com.jbs.textgameengine.screen.gamescreen.userinterface.roomview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
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
        AreaAndRoomData surroundingAreaAndRoomData = RoomView.getSurroundingAreaAndRoomData(targetLocation.room, 6);

        frameBuffer.begin();
        GameScreen.spriteBatch.setProjectionMatrix(cameraDraw.combined);
        GameScreen.spriteBatch.begin();

        bufferBackground(targetLocation, facingDirection);

        GameScreen.spriteBatch.draw(imageManager.backgroundFloor, 0, 0);

        // Get Start Coordinate //
        int coordinateXStart = 0;
        int coordinateYStart = 6;
        if(facingDirection.equals("East")) {
            coordinateXStart = 6;
            coordinateYStart = 8;
        }
        else if(facingDirection.equals("South")) {
            coordinateXStart = 8;
            coordinateYStart = 2;
        }
        else if(facingDirection.equals("West")) {
            coordinateXStart = 2;
            coordinateYStart = 0;
        }

        int coordinateX = coordinateXStart;
        int coordinateY = coordinateYStart;

        ArrayList<String> columnStringList = new ArrayList<>(Arrays.asList("Left1", "Left2", "Left3", "Left4", "Right1", "Right2", "Right3", "Right4", "Middle"));

        for(int y = 0; y < 3; y++) {
            if(y > 0) {
                if(facingDirection.equals("North") || facingDirection.equals("South")) {
                    coordinateX = coordinateXStart;
                }
                else {
                    coordinateY = coordinateYStart;
                }
            }

            String rowString = "Back";
            if(y == 1) {rowString = "Middle";}
            else if(y == 2) {rowString = "Front";}
            String columnString = "Left1";
            int columnStringIndex = 0;

            for(int x = 0; x < 9 - y; x++) {
                if(x >= y && x < 9- y) {
                    boolean drawFloor = true;

                    Room targetRoom = null;
                    Room behindRoom = null;
                    if(surroundingAreaAndRoomData.roomViewRoomList[coordinateX][coordinateY] != null) {
                        targetRoom = surroundingAreaAndRoomData.roomViewRoomList[coordinateX][coordinateY];
                        if(targetRoom.exitMap.containsKey(facingDirection)
                        && targetRoom.exitMap.get(facingDirection) != null) {
                            behindRoom = targetRoom.exitMap.get(facingDirection);
                        }
                    }
                    if(targetRoom == null) {drawFloor = false;}

                    // Back Wall //
                    if(rowString.equals("Back")
                    && GameScreen.player.location.room.inside
                    && (behindRoom == null || !behindRoom.inside)) {
                        if(imageManager.wallMap.containsKey("Backwall")
                        && imageManager.wallMap.get("Backwall").containsKey(columnString)
                        && imageManager.wallMap.get("Backwall").get(columnString).containsKey("Default")) {
                            Texture wallTexture = imageManager.wallMap.get("Backwall").get(columnString).get("Default");
                            GameScreen.spriteBatch.draw(wallTexture, 0, 0);
                        }
                    }

                    // Ceiling //
                    if(targetRoom != null
                    && targetRoom.inside
                    && !(!GameScreen.player.location.room.inside && rowString.equals("Middle"))) {
                        if(imageManager.ceilingMap.containsKey(rowString)
                        && imageManager.ceilingMap.get(rowString).containsKey(columnString)
                        && imageManager.ceilingMap.get(rowString).get(columnString).containsKey("Default")) {
                            Texture ceilingTexture = imageManager.ceilingMap.get(rowString).get(columnString).get("Default");
                            GameScreen.spriteBatch.draw(ceilingTexture, 0, 0);
                        }
                    }

                    // Outside Walls //
                    if(targetRoom != null
                    && targetRoom.inside) {
                        if(!GameScreen.player.location.room.inside) {
                            if(imageManager.wallMap.containsKey(rowString)
                            && imageManager.wallMap.get(rowString).containsKey(columnString)
                            && imageManager.wallMap.get(rowString).get(columnString).containsKey("Default")) {
                                Texture wallTexture = imageManager.wallMap.get(rowString).get(columnString).get("Default");
                                GameScreen.spriteBatch.draw(wallTexture, 0, 0);
                                drawFloor = false;
                            }
                        }
                    }

                    // Inside Walls //
                    else if(GameScreen.player.location.room.inside
                    && (targetRoom == null || !targetRoom.inside)) {
                        if(imageManager.wallMap.containsKey(rowString)
                        && imageManager.wallMap.get(rowString).containsKey(columnString)
                        && imageManager.wallMap.get(rowString).get(columnString).containsKey("Default")) {
                            Texture wallTexture = imageManager.wallMap.get(rowString).get(columnString).get("Default");
                            GameScreen.spriteBatch.draw(wallTexture, 0, 0);
                            drawFloor = false;
                        }
                    }

                    // Draw Floor //
                    if(drawFloor) {
                        if(imageManager.floorMap.containsKey(rowString)
                        && imageManager.floorMap.get(rowString).containsKey(columnString)
                        && imageManager.floorMap.get(rowString).get(columnString).containsKey("Default")) {
                            Texture floorTexture = imageManager.floorMap.get(rowString).get(columnString).get("Default");
                            GameScreen.spriteBatch.draw(floorTexture, 0, 0);
                        }
                    }
                }

                if(facingDirection.equals("North")) {
                    if(x < 3) {
                        coordinateX++;
                    } else if(x == 3) {
                        coordinateX = 8 - y;
                    } else {
                        coordinateX--;
                    }
                }
                else if(facingDirection.equals("East")) {
                    if(x < 3) {
                        coordinateY--;
                    } else if(x == 3) {
                        coordinateY = y;
                    } else {
                        coordinateY++;
                    }
                }
                else if(facingDirection.equals("South")) {
                    if(x < 3) {
                        coordinateX--;
                    } else if(x == 3) {
                        coordinateX = y;
                    } else {
                        coordinateX++;
                    }
                }
                else if(facingDirection.equals("West")) {
                    if(x < 3) {
                        coordinateY++;
                    } else if(x == 3) {
                        coordinateY = 8 - y;
                    } else {
                        coordinateY--;
                    }
                }

                if(x >= y && x < 9 - y) {
                    columnStringIndex += 1;
                    if(columnStringIndex >= columnStringList.size()) {
                        columnStringIndex = 0;
                    }
                    if(x == 3) {columnStringIndex = 4;}
                    else if(x == 7 - y) {columnStringIndex = 8;}

                    columnString = columnStringList.get(columnStringIndex);
                }
            }

            if(facingDirection.equals("North")) {coordinateY--;}
            else if(facingDirection.equals("East")) {coordinateX--;}
            else if(facingDirection.equals("South")) {coordinateY++;}
            else if(facingDirection.equals("West")) {coordinateX++;}
        }

        GameScreen.spriteBatch.end();
        frameBuffer.end();
    }

    public void bufferBackground(Location targetLocation, String facingDirection) {
        float dayPercent = targetLocation.planetoid.minuteCountDay / (targetLocation.planetoid.minutesInDay + 0.0f);

        Color skyColor = ((Planet) targetLocation.planetoid).skyColor;
        if(dayPercent < targetLocation.planetoid.dawnPercent
        || dayPercent >= targetLocation.planetoid.sunsetPercent) {
            skyColor = ((Planet) targetLocation.planetoid).nightSkyColor;
        }

        boolean dawnCheck = false;
        boolean duskCheck = false;
        float dawnDuskPercent = 0.0f;
        int minutesBeforeDusk;
        int minutesBeforeDawn = (int) (targetLocation.planetoid.dawnPercent * targetLocation.planetoid.minutesInDay);

        if(dayPercent >= targetLocation.planetoid.dawnPercent
        && dayPercent < targetLocation.planetoid.sunrisePercent) {
            int dawnMinutes = (int) (targetLocation.planetoid.sunrisePercent * targetLocation.planetoid.minutesInDay) - minutesBeforeDawn;
            dawnDuskPercent = (targetLocation.planetoid.minuteCountDay - minutesBeforeDawn) / (dawnMinutes + 0.0f);
            dawnCheck = true;
        }
        else if(dayPercent >= targetLocation.planetoid.duskPercent
        && dayPercent < targetLocation.planetoid.sunsetPercent) {
            minutesBeforeDusk = (int) (targetLocation.planetoid.duskPercent * targetLocation.planetoid.minutesInDay);
            int duskMinutes = (int) (targetLocation.planetoid.sunsetPercent * targetLocation.planetoid.minutesInDay) - minutesBeforeDusk;
            dawnDuskPercent = (targetLocation.planetoid.minuteCountDay - minutesBeforeDusk) / (duskMinutes + 0.0f);
            duskCheck = true;
        }

        if(dawnCheck || duskCheck) {
            float dawnDuskPercentMod;
            if(dawnDuskPercent <= .50) {dawnDuskPercentMod = dawnDuskPercent * 2;}
            else {dawnDuskPercentMod = (dawnDuskPercent - .50f) * 2;}
            if(dawnDuskPercent > .50 && !((Planet) targetLocation.planetoid).switchSkyColorCheck) {
                ((Planet) targetLocation.planetoid).switchSkyColorCheck = true;
                Color targetSkyColor = ((Planet) targetLocation.planetoid).targetSkyColor;
                ((Planet) targetLocation.planetoid).currentSkyColor = new Color(targetSkyColor.r, targetSkyColor.g, targetSkyColor.b, 1);
                if(duskCheck) {
                    ((Planet) targetLocation.planetoid).targetSkyColor = ((Planet) targetLocation.planetoid).nightSkyColor;
                } else {
                    ((Planet) targetLocation.planetoid).targetSkyColor = ((Planet) targetLocation.planetoid).skyColor;
                }
            }

            skyColor = ((Planet) targetLocation.planetoid).updateSkyColor(dawnDuskPercentMod);
        }

        Gdx.graphics.getGL20().glClearColor(skyColor.r, skyColor.g, skyColor.b, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void render() {
        GameScreen.spriteBatch.setProjectionMatrix(GameScreen.camera.combined);
        GameScreen.spriteBatch.begin();
        GameScreen.spriteBatch.draw(frameBuffer.getColorBufferTexture(), rect.x, rect.y);
        GameScreen.spriteBatch.end();
    }

    // Utility Functions //
    public static AreaAndRoomData getSurroundingAreaAndRoomData(Room targetRoom, int maxDistance) {
        return examineRoomData(targetRoom, maxDistance, "", new ArrayList<Room>(), new Point(0, 0), new Room[9][9]);
    }

    public static AreaAndRoomData examineRoomData(Room targetRoom, int maxDistance, String targetDirection, ArrayList<Room> examinedRoomList, Point coordinatePoint, Room[][] roomViewRoomList) {
        // Helper Function For GetSurroundingAreaAndRoomData()

        if(!examinedRoomList.contains(targetRoom)) {
            examinedRoomList.add(targetRoom);

            // RoomView Data //
            int roomViewX = 4 + (int) coordinatePoint.x;
            int roomViewY = 4 + (int) coordinatePoint.y;
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
                    TargetRoomData targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(targetRoom, new ArrayList<>(Arrays.asList(direction)), true, false);

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
