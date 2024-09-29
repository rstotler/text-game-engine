package com.jbs.textgameengine.gamedata.entity.spaceship;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.HashMap;

public class Spaceship extends Entity {
    // close hatch door when launch
    public HashMap<String, Area> areaMap;

    public String status; // Status: Landed, Orbit, Traveling

    public int keyNum;
    public String hatchStatus;
    public String boardingRoomExitDirection;
    public Room boardingRoom;
    public Room cockpitRoom;

    public Spaceship(Location startLocation) {
        super(startLocation);
        location.spaceship = this;

        areaMap = new HashMap<>();

        keyNum = -9999;
        hatchStatus = "Closed";

        boardingRoomExitDirection = "West";
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
        room00.createDoor(boardingRoomExitDirection, null, "Automatic");
        areaMainCabin.roomList.add(room00);

        // Room 01 - Sitting In A Cockpit //
        Line room01Line = new Line("Sitting In A Cockpit", "8CONT3CONT2W7CONT", "", true, true);
        Location room01Location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, areaMainCabin, null, this);
        Room room01 = new Room(room01Line, null, room01Location);
        room01.createExit("South", room00, "Automatic", 1234);
        areaMainCabin.roomList.add(room01);

        boardingRoom = areaMap.get("Main Cabin").roomList.get(0);
        cockpitRoom = areaMap.get("Main Cabin").roomList.get(1);
    }

    public static Spaceship load(int id, Location startLocation) {
        Spaceship spaceship = new Spaceship(startLocation);

        if(startLocation.room != null) {
            spaceship.status = "Landed";
        }
        else if(startLocation.planetoid != null) {
            spaceship.status = "Orbit";
        }
        else {
            spaceship.status = "Traveling";
        }

        if(id == 1) {
            spaceship.name = new Line("Starship Heart of Gold", "9CONT6SHIAR3CONT4SHIAY", "", true, true);
        }

        spaceship.nameKeyList = Entity.createNameKeyList(spaceship.name.label);
        spaceship.nameKeyList.add("spaceship");
        spaceship.nameKeyList.add("ship");

        return spaceship;
    }
}
