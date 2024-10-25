package com.jbs.textgameengine.screen.gamescreen.userinterface.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
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

import java.util.*;

public class Map extends UserInterfaceElement {
    public OrthographicCamera camera;
    public FrameBuffer mapFrameBuffer;

    public Texture textureOverworld;
    public Texture textureMap;
    public Point mapTextureOffset;

    public int tileSize;
    public int tileCountWidth;
    public int tileCountHeight;

    public Rect playerIcon;

    public Map() {
        int mapX = Settings.INPUT_BAR_WIDTH;
        int mapY = Settings.WINDOW_HEIGHT - Settings.MAP_HEIGHT;
        int mapWindowWidth = Settings.WINDOW_WIDTH - Settings.INPUT_BAR_WIDTH;
        int mapWindowHeight = Settings.MAP_HEIGHT;
        rect = new Rect(mapX, mapY, mapWindowWidth, mapWindowHeight);
        rect.fillColor = new Color(0/255f, 0/255f, 14/255f, 1);

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        textureOverworld = null;
        textureMap = null;
        mapTextureOffset = new Point(0, 0);

        tileSize = 32;
        tileCountWidth = 0;
        tileCountHeight = 0;

        int playerIconSize = (int) (tileSize * .60f);
        int playerIconX = (int) rect.x + (rect.width / 2) - (playerIconSize / 2);
        int playerIconY = (int) rect.y + (rect.height / 2) - (playerIconSize / 2);
        playerIcon = new Rect(playerIconX, playerIconY, playerIconSize, playerIconSize);
    }

    public void buffer(Location startLocation) {

        // Get Area & Room Data //
        SurroundingAreaAndRoomData surroundingAreaAndRoomData = SurroundingAreaAndRoomData.getAreaAndRoomData(startLocation.room);
        surroundingAreaAndRoomData.normalizeRoomCoordinates(surroundingAreaAndRoomData.targetRoomDataList, surroundingAreaAndRoomData.lowestPoint);
        tileCountWidth = (int) surroundingAreaAndRoomData.highestPoint.x - (int) surroundingAreaAndRoomData.lowestPoint.x + 1;
        tileCountHeight = (int) surroundingAreaAndRoomData.highestPoint.y - (int) surroundingAreaAndRoomData.lowestPoint.y + 1;

        // Get Map Size //
        int mapWidth = tileCountWidth * tileSize;
        int mapHeight = tileCountHeight * tileSize;



        // Draw Pixmap //
//        Pixmap mapPixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);
//
//        for(TargetRoomData targetRoomData : surroundingAreaAndRoomData.targetRoomDataList) {
//            int drawX = (int) targetRoomData.targetRoom.coordinates.x * tileSize;
//            int drawY = (int) (targetRoomData.targetRoom.coordinates.y + 1) * tileSize;
//            Color targetColor = new Color(targetRoomData.targetRoom.location.area.mapColor);
//            if(targetRoomData.targetRoom.mapColorTargetColor.equals("R")) {
//                targetColor.r += targetRoomData.targetRoom.mapColorMod;
//            }
//            if(targetRoomData.targetRoom.mapColorTargetColor.equals("G")) {
//                targetColor.g += targetRoomData.targetRoom.mapColorMod;
//            }
//            if(targetRoomData.targetRoom.mapColorTargetColor.equals("B")) {
//                targetColor.b += targetRoomData.targetRoom.mapColorMod;
//            }
//
//            mapPixmap.setColor(targetColor);
//            mapPixmap.fillRectangle(drawX, mapHeight - drawY, tileSize, tileSize);
//        }

        GameScreen.spriteBatch.begin();
        // Draw Textures Here
        GameScreen.spriteBatch.end();

        if(!startLocation.area.mapKey.equals("Overworld")) {
            if(textureMap != null) {textureMap.dispose();}
            //textureMap = new Texture(mapPixmap);
        }
        else {
            if(textureOverworld != null) {textureOverworld.dispose();}
            //textureOverworld = new Texture(mapPixmap);
        }
        //mapPixmap.dispose();
    }

    public void render() {
        super.render();

        Texture targetTexture = textureMap;
        if(GameScreen.player.location.area.mapKey.equals("Overworld")) {
            targetTexture = textureOverworld;
        }

        if(targetTexture != null) {
            frameBuffer.begin();
            Gdx.graphics.getGL20().glClearColor(0f, 0f, 0f, 0f);
            Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

            //camera.zoom -= .001f;
            //camera.update();
            GameScreen.spriteBatch.setProjectionMatrix(camera.combined);

            GameScreen.spriteBatch.begin();
            GameScreen.spriteBatch.draw(targetTexture, mapTextureOffset.x, mapTextureOffset.y);
            GameScreen.spriteBatch.end();
            frameBuffer.end();

            GameScreen.spriteBatch.begin();
            GameScreen.spriteBatch.draw(frameBuffer.getColorBufferTexture(), rect.x, rect.y, frameBuffer.getWidth(), frameBuffer.getHeight(), 0, 0, 1, 1);
            GameScreen.spriteBatch.end();
        }

        // Player Icon //
        Screen.shapeRenderer.setProjectionMatrix(GameScreen.camera.combined);
        Screen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Screen.shapeRenderer.setColor(Color.RED);
        Screen.shapeRenderer.rect(playerIcon.x, playerIcon.y, playerIcon.width, playerIcon.height);
        Screen.shapeRenderer.end();
    }

    public void updateOffset(Room targetRoom) {
        float heightPercent = Gdx.graphics.getWidth() / (Settings.WINDOW_WIDTH + 0.0f);
        float widthPercent = Gdx.graphics.getHeight() / (Settings.WINDOW_HEIGHT + 0.0f);
        mapTextureOffset.x = ((rect.width * widthPercent) / 2) - ((tileSize - playerIcon.width) / 2) - 2 - (targetRoom.coordinates.x * tileSize);
        mapTextureOffset.y = ((rect.height * heightPercent) / 2) - ((tileSize - playerIcon.height) / 2) - 2 - (targetRoom.coordinates.y * tileSize);
    }
}

class SurroundingAreaAndRoomData {
    public ArrayList<TargetRoomData> targetRoomDataList;
    public Point lowestPoint;
    public Point highestPoint;

    public SurroundingAreaAndRoomData() {
        this.targetRoomDataList = new ArrayList<>();
        lowestPoint = new Point(0, 0);
        highestPoint = new Point(0, 0);
    }

    public static SurroundingAreaAndRoomData getAreaAndRoomData(Room startRoom) {
        SurroundingAreaAndRoomData surroundingAreaAndRoomData = new SurroundingAreaAndRoomData();

        ArrayList<TargetRoomData> examinedAreaAndRoomDataList = new ArrayList<>();
        ArrayList<Room> examinedRoomList = new ArrayList<>();
        ArrayList<String> directionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West"));
        HashMap<Room, PreviousRoomData> previousRoomMap = new HashMap<>();
        Point currentLocationPoint = new Point(0, 0);
        String targetMapKey = startRoom.location.area.mapKey;

        TargetRoomData startRoomData = new TargetRoomData();
        startRoomData.targetRoom = startRoom;
        startRoomData.coordinates = new Point(currentLocationPoint);
        examinedAreaAndRoomDataList.add(startRoomData);
        examinedRoomList.add(startRoom);

        Room currentRoom = startRoom;

        for(int i = 0; i < directionList.size(); i++) {
            String targetDirection = directionList.get(i);

            if(currentRoom.exitMap.containsKey(targetDirection)
            && currentRoom.exitMap.get(targetDirection) != null
            && !examinedRoomList.contains(currentRoom.exitMap.get(targetDirection))
            && ((!targetMapKey.isEmpty() && currentRoom.exitMap.get(targetDirection).location.area.mapKey.equals(targetMapKey))
            || (targetMapKey.isEmpty() && currentRoom.exitMap.get(targetDirection).location.area == currentRoom.location.area))) {
                PreviousRoomData previousRoomData = new PreviousRoomData(currentRoom, new Point(currentLocationPoint), i);
                previousRoomMap.put(currentRoom.exitMap.get(targetDirection), previousRoomData);

                if(targetDirection.equals("East")) {
                    currentLocationPoint.x += 1;
                }
                else if(targetDirection.equals("West")) {
                    currentLocationPoint.x -= 1;
                }
                else if(targetDirection.equals("North")) {
                    currentLocationPoint.y += 1;
                }
                else if(targetDirection.equals("South")) {
                    currentLocationPoint.y -= 1;
                }

                if(currentLocationPoint.x < surroundingAreaAndRoomData.lowestPoint.x) {
                    surroundingAreaAndRoomData.lowestPoint.x = currentLocationPoint.x;
                }
                else if(currentLocationPoint.x > surroundingAreaAndRoomData.highestPoint.x) {
                    surroundingAreaAndRoomData.highestPoint.x = currentLocationPoint.x;
                }
                if(currentLocationPoint.y < surroundingAreaAndRoomData.lowestPoint.y) {
                    surroundingAreaAndRoomData.lowestPoint.y = currentLocationPoint.y;
                }
                else if(currentLocationPoint.y > surroundingAreaAndRoomData.highestPoint.y) {
                    surroundingAreaAndRoomData.highestPoint.y = currentLocationPoint.y;
                }

                TargetRoomData targetRoomData = new TargetRoomData();
                targetRoomData.targetRoom = currentRoom.exitMap.get(targetDirection);
                targetRoomData.coordinates = new Point(currentLocationPoint);
                examinedAreaAndRoomDataList.add(targetRoomData);
                examinedRoomList.add(currentRoom);

                currentRoom = currentRoom.exitMap.get(targetDirection);
                i = -1;
            }

            else if(i == directionList.size() - 1) {
                if(previousRoomMap.containsKey(currentRoom)) {
                    PreviousRoomData previousRoomData = previousRoomMap.get(currentRoom);
                    currentRoom = previousRoomData.previousRoom;
                    currentLocationPoint = previousRoomData.previousLocationPoint;
                    i = previousRoomData.previousDirectionIndex;
                }
            }
        }

        surroundingAreaAndRoomData.targetRoomDataList = examinedAreaAndRoomDataList;
        return surroundingAreaAndRoomData;
    }

    public static void normalizeRoomCoordinates(ArrayList<TargetRoomData> targetRoomDataList, Point coordinateOffset) {
        for(TargetRoomData targetRoomData : targetRoomDataList) {
            targetRoomData.targetRoom.coordinates.x = targetRoomData.coordinates.x - coordinateOffset.x;
            targetRoomData.targetRoom.coordinates.y = targetRoomData.coordinates.y - coordinateOffset.y;
        }
    }
}

class PreviousRoomData {
    public Room previousRoom;
    public Point previousLocationPoint;
    public int previousDirectionIndex;

    public PreviousRoomData(Room previousRoom, Point previousLocationPoint, int previousDirectionIndex) {
        this.previousRoom = previousRoom;
        this.previousLocationPoint = previousLocationPoint;
        this.previousDirectionIndex = previousDirectionIndex;
    }
}