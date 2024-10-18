package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.HashMap;

public class Planet extends Planetoid {
    public static boolean dawnMessage = true;
    public static boolean sunriseMessage = true;
    public static boolean noonMessage = true;
    public static boolean duskMessage = true;
    public static boolean sunsetMessage = true;

    public HashMap<String, Area> areaMap;
    public ArrayList<Room> landingPadList;

    public Planet(Line name, Location location, int distanceFromCenter, int orbitDirection, float axialTilt, int minutesInDay, int minutesInYear) {
        super(name, location, distanceFromCenter, orbitDirection, axialTilt, minutesInDay, minutesInYear);
        isPlanet = true;

        this.location.planetoid = this;

        areaMap = new HashMap<>();
        landingPadList = new ArrayList<>();
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
