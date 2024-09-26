package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Point;

public class Planetoid {
    public Line name;
    public Location location;
    public boolean isPlanet;
    public Point coordinates;

    public int distanceFromCenter;
    public int orbitDirection;

    public int minuteCountDay;
    public int minuteCountYear;
    public int minutesInDay;
    public int minutesInYear;

    public Planetoid(Line name, Location location, int distanceFromCenter, int orbitDirection, int minutesInDay, int minutesInYear) {
        this.name = name;
        this.location = new Location(location.galaxy, location.solarSystem, location.planetoid, null, null, null);
        isPlanet = false;
        coordinates = new Point(0, 0);

        this.distanceFromCenter = distanceFromCenter;
        this.orbitDirection = orbitDirection;

        minuteCountDay = 0;
        minuteCountYear = 0;
        this.minutesInDay = minutesInDay;
        this.minutesInYear = minutesInYear;

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
}
