package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Planet extends Planetoid {
    public static boolean dawnMessage = true;
    public static boolean sunriseMessage = true;
    public static boolean noonMessage = true;
    public static boolean duskMessage = true;
    public static boolean sunsetMessage = true;

    public HashMap<String, Moon> moonMap;
    public HashMap<String, Area> areaMap;
    public ArrayList<Room> landingPadList;

    public Planet(Line name, Location location, int distanceFromCenter, int orbitDirection, float axialTilt, int minutesInDay, int minutesInYear) {
        super(name, location, distanceFromCenter, orbitDirection, axialTilt, minutesInDay, minutesInYear);
        isPlanet = true;

        this.location.planetoid = this;

        moonMap = new HashMap<>();
        areaMap = new HashMap<>();
        landingPadList = new ArrayList<>();
    }

    public void generateOverworld() {
        // Debug Areas Created In A CW Circle Starting At The Top-Left Quadrant

        // Create Areas & Rooms //
        int areaSize = 50;
        for(int aNum = 0; aNum < 4; aNum++) {
            String areaNameString = "Overworld Area " + String.valueOf(aNum + 1);
            String areaNameColorCode = "10CONT5CONT" + String.valueOf(aNum + 1).length() + "CONT";
            Line newAreaName = new Line(areaNameString, areaNameColorCode, "", true, true);
            Location newAreaLocation = new Location(location.galaxy, location.solarSystem, this);
            Area newArea = new Area(newAreaName, newAreaLocation);
            newArea.mapKey = "Overworld";
            areaMap.put(newAreaName.label, newArea);

            for(int rNum = 0; rNum < (areaSize * areaSize); rNum++) {
                int xLoc = rNum % areaSize;
                int yLoc = rNum / areaSize;
                String roomString = "Room X:" + xLoc + ", Y:" + yLoc + " Area:" + (aNum + 1);
                String roomColorCode = "5CONT1W1DY" + String.valueOf(xLoc).length() + "CONT2DY1W1DY" + String.valueOf(yLoc).length() + "CONT1W4CONT1DY" + String.valueOf(aNum + 1) + "CONT";
                Line newRoomName = new Line(roomString, roomColorCode, "", true, true);
                Location newRoomLocation = new Location(location.galaxy, location.solarSystem, this, newArea);
                Room newRoom = new Room(newArea.roomList.size(), newRoomName, null, newRoomLocation);
                newArea.roomList.add(newRoom);
            }
        }

        // Connect Room Exits //
        for(int aNum = 0; aNum < 4; aNum++) {
            if(areaMap.containsKey("Overworld Area " + String.valueOf(aNum + 1))) {
                Area currentArea = areaMap.get("Overworld Area " + String.valueOf(aNum + 1));
                for(int rNum = 0; rNum < (areaSize * areaSize); rNum++) {
                    if(rNum < currentArea.roomList.size()) {
                        Room currentRoom = currentArea.roomList.get(rNum);
                        int xLoc = rNum % areaSize;
                        int yLoc = rNum / areaSize;

                        // North & South Exits //
                        if(yLoc > 0 || aNum > 1) {
                            Room targetRoom = null;
                            if(yLoc > 0) {
                                if((rNum - areaSize) < currentArea.roomList.size()) {
                                    targetRoom = currentArea.roomList.get(rNum - areaSize);
                                }
                            } else {
                                if(areaMap.containsKey("Overworld Area " + String.valueOf(aNum - 1))) {
                                    Area targetArea = areaMap.get("Overworld Area " + String.valueOf(aNum - 1));
                                    if(((areaSize * areaSize) - areaSize + rNum) < targetArea.roomList.size()) {
                                        targetRoom = targetArea.roomList.get((areaSize * areaSize) - areaSize + rNum);
                                    }
                                }
                            }

                            if(targetRoom != null) {
                                currentRoom.createExit("North", targetRoom);
                            }
                        }

                        // East & West Exits //
                        if(xLoc < (areaSize - 1) || (aNum == 0 || aNum == 2)) {
                            Room targetRoom = null;
                            if(xLoc < (areaSize - 1)) {
                                if((rNum + 1) < currentArea.roomList.size()) {
                                    targetRoom = currentArea.roomList.get(rNum + 1);
                                }
                            } else {
                                if(areaMap.containsKey("Overworld Area " + String.valueOf(aNum + 2))) {
                                    Area targetArea = areaMap.get("Overworld Area " + String.valueOf(aNum + 2));
                                    if((yLoc * areaSize) < targetArea.roomList.size()) {
                                        targetRoom = targetArea.roomList.get(yLoc * areaSize);
                                    }
                                }
                            }

                            if(targetRoom != null) {
                                currentRoom.createExit("East", targetRoom);
                            }
                        }
                    }
                }
            }
        }

        // Change Target Area Data //
        Area targetArea = areaMap.get("Overworld Area 4");
        targetArea.name = new Line("Area 1", "5CONT1DDW", "", true, true);
        targetArea.mapKey = "";
        areaMap.remove("Overworld Area 4");
        areaMap.put("Area 1", targetArea);
    }

    public void update() {
        super.update();

        float dayPercent = 0.0f;
        if(minutesInDay > 0) {dayPercent = (minuteCountDay + 0.0f) / minutesInDay;}

        // Time Of Day Messages //
        if(GameScreen.player.location.planetoid == this) {
            if(GameScreen.player.location.room != null
            && !(GameScreen.player.location.spaceship != null && !GameScreen.player.location.spaceship.status.equals("Landed"))) {
                if(!dawnMessage && dayPercent >= dawnPercent) {
                    dawnMessage = true;
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
            dawnMessage = ((minuteCountDay + 0.0f) / minuteCountDay) >= dawnPercent;
            sunriseMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= sunrisePercent;
            noonMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= noonPercent;
            duskMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= duskPercent;
            sunsetMessage = ((minuteCountDay + 0.0f) / minutesInDay) >= sunsetPercent;
        }
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
