package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.HashMap;

public class Planet extends Planetoid {
    public Line name;
    public Location location;

    public HashMap<String, Area> areaMap;

    public Planet(Line name, Location location) {
        super();
        isPlanet = true;

        this.name = name;
        this.location = location;

        areaMap = new HashMap<>();
    }
}
