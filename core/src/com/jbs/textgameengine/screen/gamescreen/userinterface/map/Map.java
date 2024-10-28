package com.jbs.textgameengine.screen.gamescreen.userinterface.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.utility.OpenSimplex;
import com.jbs.textgameengine.screen.utility.Point;
import com.jbs.textgameengine.screen.utility.Rect;

import java.util.*;

public class Map extends UserInterfaceElement {
    public Texture tileTexture = new Texture("images/Tile.png");

    public OrthographicCamera cameraDraw;
    public OrthographicCamera cameraBuffer;
    public FrameBuffer mapFrameBuffer;
    public FrameBuffer overworldMapFrameBuffer;
    public Point mapFrameBufferOffset;

    public int tileSize;
    public int tileCountWidth;
    public int tileCountHeight;

    public Rect playerIconRect;

    public Texture heightMap = generateHeightMap();

    public Map() {
        int mapX = Settings.INPUT_BAR_WIDTH;
        int mapY = Settings.WINDOW_HEIGHT - Settings.MAP_HEIGHT;
        int mapWindowWidth = Settings.WINDOW_WIDTH - Settings.INPUT_BAR_WIDTH;
        int mapWindowHeight = Settings.MAP_HEIGHT;
        rect = new Rect(mapX, mapY, mapWindowWidth, mapWindowHeight);
        rect.fillColor = new Color(0/255f, 0/255f, 0/255f, 0);

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, rect.width, rect.height, false);
        mapFrameBuffer = null;
        overworldMapFrameBuffer = null;

        cameraDraw = new OrthographicCamera();
        cameraBuffer = new OrthographicCamera();

        float widthPercent = (Gdx.graphics.getWidth() + 0.0f) / Settings.WINDOW_WIDTH;
        float heightPercent = (Gdx.graphics.getHeight() + 0.0f) / Settings.WINDOW_HEIGHT;
        cameraDraw.setToOrtho(true, rect.width * widthPercent, rect.height * heightPercent);

        mapFrameBufferOffset = new Point(0, 0);

        tileSize = 32;
        tileCountWidth = 0;
        tileCountHeight = 0;

        int playerIconSize = (int) (tileSize * .60f);
        int playerIconX = (rect.width / 2) - (playerIconSize / 2);
        int playerIconY = (rect.height / 2) - (playerIconSize / 2);
        playerIconRect = new Rect(playerIconX, playerIconY, playerIconSize, playerIconSize);
    }

    public Texture generateHeightMap() {
        int size = 520;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        float increment = .005f;
        float incrementRivers = .015f;
        int seedValue = new Random().nextInt(999999999);

        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                float value = OpenSimplex.noise2(seedValue, x * increment, y * increment);
                value = (value + 1.0f) / 2.0f;

                float valueRivers = OpenSimplex.noise2(seedValue, x * incrementRivers, y * incrementRivers);
                valueRivers = (valueRivers + 1.0f) / 2.0f;
                valueRivers -= .40f;

                float distanceX = (2.0f * x / size) - 1;
                float distanceY = (2.0f * y / size) - 1;
                double dMod = (Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0)) / Math.sqrt(1.35);
                double distance = Math.min(1, dMod);
                value -= distance;
                value -= valueRivers;

                pixmap.setColor(new Color(value, value, value,1));
                pixmap.drawPixel(x, y);
            }
        }

        Texture noiseTexture = new Texture(pixmap);
        pixmap.dispose();

        return noiseTexture;
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

        // Load Target FrameBuffer //
        FrameBuffer targetFrameBuffer;
        if(startLocation.area.mapKey.equals("Overworld")) {
            if(overworldMapFrameBuffer != null) {overworldMapFrameBuffer.dispose();}
            overworldMapFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, mapWidth, mapHeight, false);
            targetFrameBuffer = overworldMapFrameBuffer;
        }
        else {
            if(mapFrameBuffer != null) {mapFrameBuffer.dispose();}
            mapFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, mapWidth, mapHeight, false);
            targetFrameBuffer = mapFrameBuffer;
        }

        // Draw Map FrameBuffer //
        targetFrameBuffer.begin();
        Gdx.graphics.getGL20().glClearColor(0/255f, 0f, 0/255f, 0);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        cameraBuffer.setToOrtho(true, targetFrameBuffer.getWidth(), targetFrameBuffer.getHeight());

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

            GameScreen.shapeRenderer.setProjectionMatrix(cameraBuffer.combined);
            GameScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            GameScreen.shapeRenderer.setColor(targetColor);
            GameScreen.shapeRenderer.rect(drawX, drawY, tileSize, tileSize);
            GameScreen.shapeRenderer.end();

            //GameScreen.spriteBatch.setProjectionMatrix(cameraBuffer.combined);
            //GameScreen.spriteBatch.begin();
            //GameScreen.spriteBatch.draw(tileTexture, drawX, drawY);
            //GameScreen.spriteBatch.end();
        }

        targetFrameBuffer.end();
    }

    public void render() {
        FrameBuffer targetFrameBuffer = mapFrameBuffer;
        if(GameScreen.player.location.area.mapKey.equals("Overworld")) {
            targetFrameBuffer = overworldMapFrameBuffer;
        }

        frameBuffer.begin();
        Gdx.graphics.getGL20().glClearColor(0/255f, 0/255f, 14/255f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameScreen.spriteBatch.setProjectionMatrix(cameraDraw.combined);
        GameScreen.spriteBatch.begin();

        if(targetFrameBuffer != null) {
            Texture frameBufferTexture = targetFrameBuffer.getColorBufferTexture();
            GameScreen.spriteBatch.draw(frameBufferTexture, mapFrameBufferOffset.x, mapFrameBufferOffset.y);
        }

        GameScreen.spriteBatch.end();

        // Player Icon //
        GameScreen.shapeRenderer.setProjectionMatrix(cameraDraw.combined);
        GameScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        GameScreen.shapeRenderer.setColor(Color.RED);
        GameScreen.shapeRenderer.rect(playerIconRect.x, playerIconRect.y, playerIconRect.width, playerIconRect.height);
        GameScreen.shapeRenderer.end();

        frameBuffer.end();

        GameScreen.spriteBatch.setProjectionMatrix(GameScreen.camera.combined);
        GameScreen.spriteBatch.begin();
        GameScreen.spriteBatch.draw(frameBuffer.getColorBufferTexture(), rect.x, rect.y);
        GameScreen.spriteBatch.end();
    }

    public void updateOffset(Room targetRoom) {
        mapFrameBufferOffset.x = (rect.width / 2) - (tileSize / 2) - (targetRoom.coordinates.x * tileSize);
        mapFrameBufferOffset.y = (rect.height / 2) - (tileSize / 2) - (targetRoom.coordinates.y * tileSize);
    }

    public void zoom(int targetDirection) {
        cameraDraw.zoom += .25f * targetDirection;
        if(cameraDraw.zoom < 1.0) {
            cameraDraw.zoom = 1.0f;
        }
        else if(cameraDraw.zoom >= 3.0) {
            cameraDraw.zoom = 3.0f;
        }
        cameraDraw.update();
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
        ArrayList<String> directionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West", "Final"));
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
                if(i < directionList.size() - 1) {
                    PreviousRoomData previousRoomData = new PreviousRoomData(currentRoom, new Point(currentLocationPoint), i);
                    previousRoomMap.put(currentRoom.exitMap.get(targetDirection), previousRoomData);
                }

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

            if(i == directionList.size() - 2) {
                if(previousRoomMap.containsKey(currentRoom)) {
                    PreviousRoomData previousRoomData = previousRoomMap.get(currentRoom);
                    currentRoom = previousRoomData.previousRoom;
                    currentLocationPoint = previousRoomData.previousLocationPoint;
                    i = previousRoomData.previousDirectionIndex;
                }
            }

            if(i == directionList.size() - 1
            && previousRoomMap.containsKey(currentRoom)) {
                PreviousRoomData previousRoomData = previousRoomMap.get(currentRoom);
                currentRoom = previousRoomData.previousRoom;
                currentLocationPoint = previousRoomData.previousLocationPoint;
                i = previousRoomData.previousDirectionIndex;
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