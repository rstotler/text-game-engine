package com.jbs.textgameengine.gamedata.entity;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Entity {
    public int id;
    public Location location;
    public Line name;

    public Entity(Location startLocation) {
        id = -1;
        this.location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, startLocation.area, startLocation.room, startLocation.spaceship);
        name = null;
    }
}
