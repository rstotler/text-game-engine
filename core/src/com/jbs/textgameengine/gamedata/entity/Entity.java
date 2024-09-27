package com.jbs.textgameengine.gamedata.entity;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Entity {
    public int id;
    public Location location;
    public Line name;
    public ArrayList<String> nameKeyList;

    public Entity(Location startLocation) {
        id = -1;
        this.location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, startLocation.area, startLocation.room, startLocation.spaceship);
        name = null;
        nameKeyList = null;
    }

    public static ArrayList<String> createNameKeyList(String targetString) {
        ArrayList<String> nameKeyList = new ArrayList<>();

        for(String substring : targetString.split(" ")) {
        }

        return nameKeyList;
    }
}
