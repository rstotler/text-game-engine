package com.jbs.textgameengine.gamedata.world.planetoid.planet;

import com.badlogic.gdx.graphics.Color;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.planetoid.Moon;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.gamescreen.userinterface.map.HeightMapData;
import com.jbs.textgameengine.screen.utility.OpenSimplex;

import java.util.*;

public class Planet extends Planetoid {
    public static boolean dawnMessage = true;
    public static boolean sunriseMessage = true;
    public static boolean noonMessage = true;
    public static boolean duskMessage = true;
    public static boolean sunsetMessage = true;

    public HashMap<String, Moon> moonMap;
    public HashMap<String, Area> areaMap;

    public float atmosphereLevel;

    public ArrayList<Room> landingPadList;

    public Color skyColor;
    public Color nightSkyColor;
    public Color currentSkyColor;
    public Color targetSkyColor;
    public boolean switchSkyColorCheck;

    public Planet(Line name, Location location, int distanceFromCenter, int orbitDirection, float axialTilt, float atmosphereLevel, int minutesInDay, int minutesInYear) {
        super(name, location, distanceFromCenter, orbitDirection, axialTilt, minutesInDay, minutesInYear);
        isPlanet = true;

        this.location.planetoid = this;

        moonMap = new HashMap<>();
        areaMap = new HashMap<>();

        this.atmosphereLevel = atmosphereLevel;

        landingPadList = new ArrayList<>();

        skyColor = new Color(20/255f, 60/255f, 140/255f, 1);
        nightSkyColor = new Color(5/255f, 5/255f, 20/255f, 1);
        currentSkyColor = new Color(skyColor.r, skyColor.g, skyColor.b, 1);
        targetSkyColor = new Color(skyColor.r, skyColor.g, skyColor.b, 1);
        switchSkyColorCheck = false;
    }

    public void generateOverworld() {
        // Debug Areas Created In A CW Circle Starting At The Top-Left Quadrant

        int worldSize = 20;
        int areaSize = worldSize / 2;
        HeightMapData heightMapData = generateHeightMapData(worldSize, worldSize);

        // Create Areas & Rooms //
        for(int aNum = 0; aNum < 4; aNum++) {
            String areaNameString = "Overworld Area " + String.valueOf(aNum + 1);
            String areaNameColorCode = "10CONT5CONT" + String.valueOf(aNum + 1).length() + "CONT";
            Line newAreaName = new Line(areaNameString, areaNameColorCode, "", true, true);
            Location newAreaLocation = new Location(location.galaxy, location.solarSystem, this);
            Area newArea = new Area(newAreaName, newAreaLocation);
            newArea.mapKey = "Overworld";
            areaMap.put(newAreaName.label, newArea);

            for(int y = 0; y < areaSize; y++) {
                for(int x = 0; x < areaSize; x++) {
                    int rNum = (areaSize * y) + x;
                    int tileX = x;
                    int tileY = y;
                    if(aNum == 1 || aNum == 3) {tileX += areaSize;}
                    if(aNum == 2 || aNum == 3) {tileY += areaSize;}
                    String tileType = heightMapData.getTileType(tileX, tileY);

                    String roomString = "";
                    String roomColorCode = "";
                    Line newRoomName = new Line(roomString, roomColorCode, "", true, true);
                    Location newRoomLocation = new Location(location.galaxy, location.solarSystem, this, newArea);
                    Room newRoom = new Room(newArea.roomList.size(), newRoomName, null, newRoomLocation);

                    Line tileName = HeightMapData.getTileName(tileType);
                    newRoom.name.label = tileName.label;
                    newRoom.name.colorCode = tileName.colorCode;
                    newRoom.tileType = tileType;
                    newRoom.coordinates.x = tileX;
                    newRoom.coordinates.y = worldSize - tileY;
                    newArea.roomList.add(newRoom);

                    // North & South Exits //
                    if(y > 0 || aNum > 1) {
                        Room targetRoom = null;
                        if(y > 0) {
                            if((rNum - areaSize) < newArea.roomList.size()) {
                                targetRoom = newArea.roomList.get(rNum - areaSize);
                            }
                        } else {
                            if(areaMap.containsKey("Overworld Area " + String.valueOf(aNum - 1))) {
                                Area targetArea = areaMap.get("Overworld Area " + String.valueOf(aNum - 1));
                                if((areaSize * areaSize) - areaSize + rNum < targetArea.roomList.size()) {
                                    targetRoom = targetArea.roomList.get((areaSize * areaSize) - areaSize + rNum);
                                }
                            }
                        }

                        if(targetRoom != null) {
                            newRoom.createExit("North", targetRoom);
                        }
                    }

                    // West & East Exits //
                    if(x > 0 || (aNum == 1 || aNum == 3)) {
                        Room targetRoom = null;
                        if(x > 0) {
                            if((rNum - 1) < newArea.roomList.size()) {
                                targetRoom = newArea.roomList.get(rNum - 1);
                            }
                        } else {
                            if(areaMap.containsKey("Overworld Area " + String.valueOf(aNum))) {
                                Area targetArea = areaMap.get("Overworld Area " + String.valueOf(aNum));
                                if(((y * areaSize) + (areaSize - 1)) < targetArea.roomList.size()) {
                                    targetRoom = targetArea.roomList.get((y * areaSize) + (areaSize - 1));
                                }
                            }
                        }

                        if(targetRoom != null) {
                            newRoom.createExit("West", targetRoom);
                        }
                    }
                }
            }
        }

        // Add Landing Pad //
        landingPadList.add(areaMap.get("Overworld Area 3").roomList.get(0));
    }

    public HeightMapData generateHeightMapData(int width, int height) {
        float widthPercent = width / 54.0f;
        float heightPercent = height / 54.0f;

        //Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        //pixmap.setColor(Color.WHITE);
        //pixmap.fill();

        float increment = .05f / widthPercent;
        float incrementRivers = .15f / heightPercent;
        int seedValue = new Random().nextInt(999999999);

        HeightMapData heightMapData = new HeightMapData(width, height);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                float elevation = OpenSimplex.noise2(seedValue, x * increment, y * increment);
                elevation = (elevation + 1.0f) / 2.0f;

                float valueRivers = OpenSimplex.noise2(seedValue, x * incrementRivers, y * incrementRivers);
                valueRivers = (valueRivers + 1.0f) / 2.0f;
                valueRivers -= .40f;

                float distanceX = (2.0f * x / width) - 1;
                float distanceY = (2.0f * y / height) - 1;
                double distance = Math.min(1, (Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0)) / Math.sqrt(1.25));
                elevation -= distance;
                elevation -= valueRivers;

                heightMapData.setData(x, y, elevation);

                //pixmap.setColor(new Color(value, value, value,1));
                //pixmap.drawPixel(x, y);
            }
        }

        //Texture noiseTexture = new Texture(pixmap);
        //pixmap.dispose();

        return heightMapData;
    }

    public void update() {
        super.update();

        float dayPercent = 0.0f;
        if(minutesInDay > 0) {dayPercent = (minuteCountDay + 0.0f) / minutesInDay;}

        // Set Current Time Of Day Messages & Target Set Sky Color //
        if(GameScreen.player.location.planetoid == this) {
            if(GameScreen.player.location.room != null
            && !(GameScreen.player.location.spaceship != null && !GameScreen.player.location.spaceship.status.equals("Landed"))) {
                if(!dawnMessage && dayPercent >= dawnPercent) {
                    dawnMessage = true;
                    currentSkyColor = new Color(nightSkyColor.r, targetSkyColor.g, targetSkyColor.b, 1);
                    targetSkyColor = new Color(240/255f, 130/255f, 30/255f, 1);
                    switchSkyColorCheck = false;
                    Line dawnLine = new Line("The sky begins to lighten.", "4CONT4SHIAC7CONT3CONT7CONT1DY", "", true, true);
                    GameScreen.userInterface.console.writeToConsole(dawnLine);
                }
                else if(!sunriseMessage && dayPercent >= sunrisePercent) {
                    sunriseMessage = true;
                    Line sunriseLine = new Line("The sun rises over the horizon.", "4CONT4SHIAY6CONT5CONT4CONT7CONT1DY", "", true, true);
                    GameScreen.userInterface.console.writeToConsole(sunriseLine);
                }
                else if(!noonMessage && dayPercent >= noonPercent) {
                    noonMessage = true;
                    Line noonLine = new Line("It is noon.", "3CONT3CONT4CONT1DY", "", true, true);
                    GameScreen.userInterface.console.writeToConsole(noonLine);
                }
                else if(!duskMessage && dayPercent >= duskPercent) {
                    duskMessage = true;
                    currentSkyColor = new Color(skyColor.r, skyColor.g, skyColor.b, 1);
                    targetSkyColor = new Color(240/255f, 130/255f, 30/255f, 1);
                    switchSkyColorCheck = false;
                    Line duskLine = new Line("The sun begins to set.", "4CONT4SHIAY7CONT3CONT3CONT1DY", "", true, true);
                    GameScreen.userInterface.console.writeToConsole(duskLine);
                }
                else if(!sunsetMessage && dayPercent >= sunsetPercent) {
                    sunsetMessage = true;
                    Line sunsetLine = new Line("The sun sinks beyond the horizon.", "4CONT4SHIAY6CONT7CONT4CONT7CONT1DY", "", true, true);
                    GameScreen.userInterface.console.writeToConsole(sunsetLine);
                }
            }
        }

        if(minuteCountDay == 0) {
            updateDayNightTimers();
        }
    }

    public void updateDayNightTimers() {
        float yearRatio = 0.0f;
        if(minutesInYear != 0) {
            yearRatio = (float) Math.cos(Math.toRadians(((minuteCountYear + 0.0f) / minutesInYear) * 360));
        }
        float ratio = ((axialTilt / 100.0f) * yearRatio) * (minutesInDay / 2.50f);
        int nightMinutes = minutesInDay - (int) ((minutesInDay / 1.90f) - ratio);

        dawnPercent = (nightMinutes / 2.01f) / minutesInDay;
        sunrisePercent = (nightMinutes / 1.80f) / minutesInDay;
        noonPercent = .50f;
        duskPercent = ((nightMinutes / 1.94f) + (minutesInDay - nightMinutes)) / minutesInDay;
        sunsetPercent = ((nightMinutes / 1.64f) + (minutesInDay - nightMinutes)) / minutesInDay;

        if(GameScreen.player.location.planetoid == this) {
            dawnMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= dawnPercent;
            sunriseMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= sunrisePercent;
            noonMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= noonPercent;
            duskMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= duskPercent;
            sunsetMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= sunsetPercent;
        }

        float dayPercent = (minuteCountDay + 0.0f) / minutesInDay;
        if(dayPercent < minutesInDay * dawnPercent
        || dayPercent > minutesInDay * sunsetPercent) {
            currentSkyColor = new Color(nightSkyColor.r, nightSkyColor.g, nightSkyColor.b, 1);
            targetSkyColor = nightSkyColor;
        }
    }

    public Color updateSkyColor(float dawnDuskPercent) {
        Color newColor = new Color(0, 0, 0, 1);

        for(int i = 0; i < 3; i++) {
            float targetSkyColorValue;
            float currentSkyColorValue;
            if(i == 0) {
                targetSkyColorValue = targetSkyColor.r;
                currentSkyColorValue = currentSkyColor.r;
            }
            else if(i == 1) {
                targetSkyColorValue = targetSkyColor.g;
                currentSkyColorValue = currentSkyColor.g;
            }
            else {
                targetSkyColorValue = targetSkyColor.b;
                currentSkyColorValue = currentSkyColor.b;
            }

            float differencePercent = (targetSkyColorValue - currentSkyColorValue) * dawnDuskPercent;
            int newColorValue = (int) ((currentSkyColorValue + differencePercent) * 255);
            if(newColorValue < 0) {newColorValue = 0;}
            else if(newColorValue > 255) {newColorValue = 255;}

            if(i == 0) {newColor.r = newColorValue/255f;}
            else if(i == 1) {newColor.g = newColorValue/255f;}
            else {newColor.b = newColorValue/255f;}
        }

        return newColor;
    }

    public boolean isDay() {
        return ((minuteCountDay + 0.0f) / minutesInDay) >= dawnPercent
            && ((minuteCountDay + 0.0f) / minutesInDay) < sunsetPercent;
    }

    public Line getTime() {
        int currentHours = minuteCountDay / 60;
        int currentMinutes = (minuteCountDay % 60);

        String amPMString = "";
        String amPMColorCode = "";
        if(minutesInDay == 1440) {
            amPMString = " AM";
            amPMColorCode = "3DW";
            if(currentHours >= 12) {
                amPMString = " PM";
                if(currentHours > 12) {
                    currentHours -= 12;
                }
            }
            else if(currentHours == 0) {
                currentHours = 12;
            }
        }

        String currentHoursString = String.valueOf(currentHours);
        String currentMinutesString = String.valueOf(currentMinutes);
        if(currentMinutesString.length() == 1) {
            currentMinutesString = "0" + currentMinutesString;
        }

        String timeLineLabel = "It is currently " + currentHoursString + ":" + currentMinutesString + amPMString + " on " + name.label + ".";
        String timeLineColorCode = "3CONT3CONT10CONT" + String.valueOf(currentHoursString.length()) + "W1DY" + String.valueOf(currentMinutesString.length()) + "W" + amPMColorCode + "1W3CONT" + name.colorCode + "1DY";

        return new Line(timeLineLabel, timeLineColorCode, "", true, true);
    }
}
