package com.jbs.textgameengine.gamedata.world.solarsystem;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class SolarSystem {
    public Line name;
    public Location location;

    public ArrayList<Planetoid> planetoidList;

    public SolarSystem(Line name, Location location) {
        this.name = name;
        this.location = location;

        planetoidList = new ArrayList<>();
    }
}
