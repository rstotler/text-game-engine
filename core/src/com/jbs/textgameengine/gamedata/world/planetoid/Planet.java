package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.HashMap;

public class Planet extends Planetoid {
    public HashMap<String, Area> areaMap;

    public Planet(Line name, Location location, int distanceFromCenter, int orbitDirection, int minutesInDay, int minutesInYear) {
        super(name, location, distanceFromCenter, orbitDirection, minutesInDay, minutesInYear);
        isPlanet = true;

        this.location.planetoid = this;

        areaMap = new HashMap<>();
    }

    public void update() {
        super.update();

        if(minuteCountDay == 0) {
            updateDayNightTimers();
        }
    }

    public void updateDayNightTimers() {
    }
}
