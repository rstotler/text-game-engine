package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Point;

import java.util.ArrayList;
import java.util.Random;

public class Planetoid {
    public Line name;
    public ArrayList<String> nameKeyList;
    public Location location;
    public boolean isPlanet;
    public Point coordinates;

    public int distanceFromCenter;
    public int orbitDirection;
    public float axialTilt;

    public int minuteCountDay;
    public int minuteCountYear;
    public int minutesInDay;
    public int minutesInYear;

    public float dawnPercent;
    public float sunrisePercent;
    public float noonPercent;
    public float duskPercent;
    public float sunsetPercent;

    public Planetoid(Line name, Location location, int distanceFromCenter, int orbitDirection, float axialTilt, int minutesInDay, int minutesInYear) {
        this.name = name;
        nameKeyList = Entity.createNameKeyList(name.label);
        this.location = new Location(location.galaxy, location.solarSystem, location.planetoid, null, null, null);
        isPlanet = false;
        coordinates = new Point(0, 0);

        this.distanceFromCenter = distanceFromCenter;
        this.orbitDirection = orbitDirection;
        this.axialTilt = axialTilt;

        int randomYearMinutes = 0;
        if(minutesInYear > 0) {
            randomYearMinutes = new Random().nextInt((int) (minutesInYear * .95f));
        }
        minuteCountDay = randomYearMinutes % minutesInDay;
        minuteCountYear = randomYearMinutes;
        this.minutesInDay = minutesInDay;
        this.minutesInYear = minutesInYear;

        dawnPercent = 0.0f;
        sunrisePercent = 0.0f;
        noonPercent = 0.0f;
        duskPercent = 0.0f;
        sunsetPercent = 0.0f;

        updatePosition();
    }

    public void update() {
        minuteCountDay += 1;
        if(distanceFromCenter > 0
        && minutesInYear > 0) {
            minuteCountYear += 1;
            updatePosition();
        }

        if(minuteCountDay >= minutesInDay) {
            minuteCountDay = 0;
        }
        if(minuteCountYear >= minutesInYear) {
            minuteCountYear = 0;
        }
    }

    public void updatePosition() {
        coordinates.x = distanceFromCenter;
        coordinates.y = 0;

        if(minutesInYear > 0) {
            coordinates.x = (float) Math.cos(Math.toRadians((minuteCountYear / (minutesInYear + 0.0f)) * 360)) * distanceFromCenter;
            coordinates.y = (float) (Math.sin(Math.toRadians((minuteCountYear / (minutesInYear + 0.0f)) * 360)) * distanceFromCenter) * orbitDirection;
        }
    }

    public boolean isDay() {
        return false;
    }

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }
}
