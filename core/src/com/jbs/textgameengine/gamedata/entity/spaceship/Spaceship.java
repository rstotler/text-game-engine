package com.jbs.textgameengine.gamedata.entity.spaceship;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.HashMap;

public class Spaceship extends Entity {
    public HashMap<String, Area> areaMap;

    public String status; // Status: Landed, Launch, Orbit, Flight, Land
    public int currentPhase;
    public int phaseTimer;

    public int keyNum;
    public String hatchStatus;
    public String boardingRoomExitDirection;
    public Room boardingRoom;
    public Room cockpitRoom;

    public Spaceship(Location startLocation) {
        super(-1, startLocation);
        location.spaceship = this;
        isSpaceship = true;

        areaMap = new HashMap<>();

        currentPhase = 0;
        phaseTimer = 0;

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
        room01.createExit("South", room00, "Automatic");
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
            spaceship.keyNum = 7777;
        }

        spaceship.nameKeyList = Entity.createNameKeyList(spaceship.name.label);
        spaceship.nameKeyList.add("spaceship");
        spaceship.nameKeyList.add("ship");

        return spaceship;
    }

    public boolean update() {
        if(status.equals("Launch")) {updateLaunch();}
        else if(status.equals("Land")) {updateLand();}

        return status.equals("Landed");
    }

    public void updateLaunch() {
        phaseTimer += 1;

        if(phaseTimer >= 4) {
            phaseTimer = 0;
            currentPhase += 1;

            if(GameScreen.player.location.spaceship == this) {
                if(currentPhase == 1) {
                    GameScreen.userInterface.console.writeToConsole(new Line("A computerized voice says, 'Commencing launch.'", "2W13CONT6CONT4CONT3DY11SHIA6SHIA2DY", "", true, true));
                }

                else if(currentPhase == 2) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The ship rumbles as the engines begin to hum.", "4CONT5CONT8CONT3CONT4CONT8CONT6CONT3CONT3CONT1DY", "", true, true));
                }

                else if(currentPhase == 3) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The engines roar to life as you *BLAST* off!", "4CONT8CONT5CONT3CONT5CONT3CONT4CONT1DY5SHIAC2DY3CONT1DY", "", true, true));
                }

                else if(currentPhase == 4) {
                    status = "Orbit";
                    currentPhase = 0;

                    GameScreen.userInterface.console.writeToConsole(new Line("You being orbiting " + location.planetoid.name.label + ".", "4CONT6CONT9CONT" + location.planetoid.name.colorCode + "1DY", "", true, true));
                }
            }
        }
    }

    public void updateLand() {
        phaseTimer += 1;

        if(phaseTimer >= 4) {
            phaseTimer = 0;
            currentPhase += 1;

            if(GameScreen.player.location.spaceship == this) {
                if(currentPhase == 1) {
                    GameScreen.userInterface.console.writeToConsole(new Line("A computerized voice says, 'Initiating landing sequence.'", "2W13CONT6CONT4CONT3DY11SHIA8SHIA8SHIA2DY", "", true, true));
                }

                else if(currentPhase == 2) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The ship rumbles as it makes its fiery descent.", "4CONT5CONT8CONT3CONT3CONT6CONT4CONT6SHIAR7CONT1DY", "", true, true));
                }

                else if(currentPhase == 3) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The engines begin powering down as you near your destination.", "4CONT8CONT6CONT9CONT5CONT3CONT4CONT5CONT5CONT11CONT1DY", "", true, true));
                }

                else if(currentPhase == 4) {
                    status = "Landed";
                    currentPhase = 0;

                    if(location.solarSystem.spaceshipList.contains(this)) {
                        location.solarSystem.spaceshipList.remove(this);
                    }

                    GameScreen.userInterface.console.writeToConsole(new Line("You feel a soft *thud* as you land.", "4CONT5CONT2W5CONT1DY4CONT2DY3CONT4CONT4CONT1DY", "", true, true));
                }
            }
        }
    }
}
