package com.jbs.textgameengine.gamedata.entity.spaceship;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.HashMap;

public class Spaceship extends Entity {
    public HashMap<String, Area> areaMap;
    public String hatchStatus;
    public int hatchKeyNum;

    public Spaceship(Location startLocation) {
        super(startLocation);

        areaMap = new HashMap<>();
        hatchStatus = "Closed";
        hatchKeyNum = -9999;

        loadMainCabin(startLocation);
    }

    public void loadMainCabin(Location startLocation) {

        // Area - Main Cabin //
        Line mainCabinLine = new Line("Main Cabin", "5CONT5CONT", "", true, true);
        Location mainCabinLocation = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, null, null, this);
        Area areaMainCabin = new Area(mainCabinLine, mainCabinLocation);
        areaMap.put(mainCabinLine.label, areaMainCabin);

        // Room 00 - A Ship Hallway //
        Line room00Line = new Line("A Ship Hallway", "2CONT5CONT7CONT", "", true, true);
        Location room00Location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, areaMainCabin, null, this);
        Room room00 = new Room(room00Line, null, room00Location);
        areaMainCabin.roomList.add(room00);

        // Room 01 - Cockpit //
        Line room01Line = new Line("Cockpit", "7CONT", "", true, true);
        Location room01Location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, areaMainCabin, null, this);
        Room room01 = new Room(room01Line, null, room01Location);
        room01.createExit("South", room00, "Automatic", 1234);
        areaMainCabin.roomList.add(room01);
    }

    public static Spaceship load(int id, Location startLocation) {
        Spaceship spaceship = new Spaceship(startLocation);

        if(id == 1) {
            spaceship.name = new Line("A Spaceship", "2W9CONT", "", true, true);
        }

        spaceship.nameKeyList = Entity.createNameKeyList(spaceship.name.label);
        spaceship.nameKeyList.add("spaceship");
        spaceship.nameKeyList.add("ship");

        return spaceship;
    }
}
