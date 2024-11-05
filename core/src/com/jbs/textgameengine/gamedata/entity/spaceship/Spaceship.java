package com.jbs.textgameengine.gamedata.entity.spaceship;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.planetoid.planet.Planet;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Point;
import com.jbs.textgameengine.screen.utility.Rect;

import java.util.HashMap;

public class Spaceship extends Entity {
    public HashMap<String, Area> areaMap;

    public String status; // Status: Landed, Launch, Orbit, Flight, Land
    public Point coordinates;
    public float speedPercent;
    public float targetSpeedPercent;
    public int displaySpeedUpMessage;
    public int displaySpeedDownMessage;

    public int currentPhase;
    public int phaseTimer;

    public int keyNum;
    public String hatchStatus;
    public String boardingRoomExitDirection;
    public Room boardingRoom;
    public Room cockpitRoom;

    public Planetoid headingPlanetoid;
    public Point headingXY;

    public Spaceship(Location startLocation) {
        super(-1, startLocation);
        location.spaceship = this;
        isSpaceship = true;

        areaMap = new HashMap<>();

        coordinates = null;
        speedPercent = 0.0f;
        targetSpeedPercent = 0.0f;
        displaySpeedUpMessage = -1;
        displaySpeedDownMessage = -1;

        currentPhase = 0;
        phaseTimer = 0;

        keyNum = -9999;
        hatchStatus = "Closed";

        headingPlanetoid = null;
        headingXY = null;

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
        Room room00 = new Room(areaMainCabin.roomList.size(), room00Line, null, room00Location);
        room00.createDoor(boardingRoomExitDirection, null, "Automatic");
        areaMainCabin.roomList.add(room00);

        // Room 01 - Sitting In A Cockpit //
        Line room01Line = new Line("Sitting In A Cockpit", "8CONT3CONT2W7CONT", "", true, true);
        Location room01Location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, areaMainCabin, null, this);
        Room room01 = new Room(areaMainCabin.roomList.size(), room01Line, null, room01Location);
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
            spaceship.status = "Flight";
        }

        if(id == 1) {
            spaceship.name = new Line("Starship Heart of Gold", "9CONT6SHIAR3CONT4SHIAY", "", true, true);
            spaceship.keyNum = 7777;
        }

        // Load Ship Key //
        if(spaceship.keyNum != -9999) {
            Item spaceshipKey = Item.load("General", 2, startLocation);
            spaceshipKey.prefix = "The ";
            spaceshipKey.name.label = "Key to " + spaceship.name.label;
            spaceshipKey.name.colorCode = "4CONT3CONT" + spaceship.name.colorCode;
            spaceshipKey.nameKeyList = Entity.createNameKeyList(spaceshipKey.name.label);
            spaceshipKey.keyList.add(spaceship.keyNum);
            spaceship.cockpitRoom.addItemToRoom(spaceshipKey);
        }

        spaceship.prefix = "";
        spaceship.nameKeyList = Entity.createNameKeyList(spaceship.name.label);
        spaceship.nameKeyList.add("spaceship");
        spaceship.nameKeyList.add("ship");

        return spaceship;
    }

    public boolean update() {
        if(status.equals("Launch")) {updateLaunch();}
        else if(status.equals("Land")) {updateLand();}
        else if(status.equals("Orbit") || status.equals("Flight")) {updateInFlight();}

        return status.equals("Landed");
    }

    public void updateLaunch() {
        if(currentPhase >= 3) {
            speedPercent += .025f;
        }

        phaseTimer += 1;
        if(phaseTimer >= 4) {
            phaseTimer = 0;
            currentPhase += 1;

            if(GameScreen.player.location.spaceship == this) {
                if(currentPhase == 1) {
                    GameScreen.userInterface.console.writeToConsole(new Line("A computerized voice says, \"Commencing launch\".", "2W13CONT6CONT4CONT3DY11SHIA6SHIA2DY", "", true, true));
                }

                else if(currentPhase == 2) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The ship rumbles as the engines begin to hum.", "4CONT5CONT8CONT3CONT4CONT8CONT6CONT3CONT3CONT1DY", "", true, true));
                }

                else if(currentPhase == 3) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The engines roar to life as you *BLAST* off!", "4CONT8CONT5CONT3CONT5CONT3CONT4CONT1DY5SHIAC2DY3CONT1DY", "", true, true));
                    speedPercent = .50f;
                }

                else if(currentPhase == 4) {
                    status = "Orbit";
                    currentPhase = 0;
                    speedPercent = .60f;
                    targetSpeedPercent = speedPercent;

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
                    GameScreen.userInterface.console.writeToConsole(new Line("A computerized voice says, \"Initiating landing sequence\".", "2W13CONT6CONT4CONT3DY11SHIA8SHIA8SHIA2DY", "", true, true));
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
                    speedPercent = 0.0f;
                    targetSpeedPercent = 0.0f;

                    GameScreen.userInterface.console.writeToConsole(new Line("You feel a soft *thud* as the ship lands.", "4CONT5CONT2W5CONT1DY4CONT2DY3CONT4CONT5CONT5CONT1DY", "", true, true));
                }
            }
        }
    }

    public void updateInFlight() {

        // Leave Orbit From Setting Course Check //
        if((headingPlanetoid != null || headingXY != null)
        && status.equals("Orbit")) {
            status = "Flight";
            coordinates = new Point(location.planetoid.coordinates.x, location.planetoid.coordinates.y);
            location.planetoid = null;
            targetSpeedPercent = 1.0f;
        }

        else {
            if((status.equals("Flight") || status.equals("Orbit"))
            && targetSpeedPercent != speedPercent) {
                updateSpeed();
            }

            if(status.equals("Flight")
            && location.planetoid == null
            && coordinates != null) {
                updateLocation();
            }
        }
    }

    public void updateSpeed() {

        // Speed Up //
        if(speedPercent < targetSpeedPercent) {
            if(displaySpeedUpMessage != -1) {
                if(displaySpeedUpMessage < 2) {
                    displaySpeedUpMessage += 1;
                }
                else {
                    displaySpeedUpMessage = -1;
                    if(GameScreen.player.location.spaceship == this) {
                        GameScreen.userInterface.console.writeToConsole(new Line("The ship lurches slightly as it increases speed.", "4CONT5CONT8CONT9CONT3CONT3CONT10CONT5CONT1DY", "", true, true));
                    }
                }
            }

            speedPercent += .02f;
            if(speedPercent > targetSpeedPercent) {
                speedPercent = targetSpeedPercent;
            }

            if(GameScreen.player.location.spaceship == this && speedPercent == targetSpeedPercent) {
                GameScreen.userInterface.console.writeToConsole(new Line("The engine sounds normalize as the ship reaches its target speed.", "4CONT7CONT7CONT10CONT3CONT4CONT5CONT8CONT4CONT7CONT5CONT1DY", "", true, true));
            }
        }

        // Speed Down //
        else if(speedPercent > targetSpeedPercent) {
            if(displaySpeedDownMessage != -1) {
                if(displaySpeedDownMessage < 2) {
                    displaySpeedDownMessage += 1;
                }
                else {
                    displaySpeedDownMessage = -1;
                    if(GameScreen.player.location.spaceship == this) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You feel the ship slow down.", "4CONT5CONT4CONT5CONT5CONT4CONT1DY", "", true, true));
                    }
                }
            }

            speedPercent -= .04f;
            if(speedPercent < targetSpeedPercent) {
                speedPercent = targetSpeedPercent;
            }

            if(GameScreen.player.location.spaceship == this) {
                if(speedPercent == 0) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The electronic hum stops as the ships engines power down.", "4CONT11CONT4CONT6CONT3CONT4CONT6CONT8CONT6CONT4CONT1DY", "", true, true));
                }
                else if(speedPercent == targetSpeedPercent) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The engine sounds normalize as the ship reaches its target speed.", "4CONT7CONT7CONT10CONT3CONT4CONT5CONT8CONT4CONT7CONT5CONT1DY", "", true, true));
                }
            }
        }
    }

    public  void updateLocation() {
        float moveSpeed = getTopSpeed() * speedPercent;

        if((headingPlanetoid != null || headingXY != null)
        && speedPercent > 0) {
            Point targetPoint = null;
            if(headingXY != null) {targetPoint = headingXY;}
            else {targetPoint = headingPlanetoid.coordinates;}

            Rect distance = new Rect((int) (targetPoint.x - coordinates.x), (int) (targetPoint.y - coordinates.y));
            float slope = distance.width / (distance.height + 0.0f);
            float counterSlope = 1.0f - Math.abs(slope);
            if(Math.abs(slope) > 1.0) {
                counterSlope = 1.0f / slope;
                slope = 1.0f;
            }

            if(coordinates.x < targetPoint.x) {coordinates.x += (Math.abs(slope) * moveSpeed);}
            else {coordinates.x -= (Math.abs(slope) * moveSpeed);}
            if(coordinates.y < targetPoint.y) {coordinates.y += (Math.abs(counterSlope) * moveSpeed);}
            else {coordinates.y -= (Math.abs(counterSlope) * moveSpeed);}
        }

        for(Planetoid planet : location.solarSystem.planetoidList) {
            if(planet == headingPlanetoid
            || (headingPlanetoid == null && headingXY == null)
            || (headingXY != null
            && Math.abs(headingXY.x - planet.coordinates.x) <= 1.25 * moveSpeed
            && Math.abs(headingXY.y - planet.coordinates.y) <= 1.25 * moveSpeed)) {
                if(Math.abs(coordinates.x - planet.coordinates.x) <= 1.25 * moveSpeed) {
                    if(Math.abs(coordinates.y - planet.coordinates.y) <= 1.25 * moveSpeed) {
                        status = "Orbit";
                        coordinates = null;
                        targetSpeedPercent = 0;
                        if(speedPercent > 0) {
                            displaySpeedDownMessage = 1;
                            displaySpeedUpMessage = -1;
                        }
                        location.planetoid = planet;
                        headingPlanetoid = null;
                        headingXY = null;

                        if(planet.isPlanet) {
                            ((Planet) planet).updateDayNightTimers();
                        }

                        if(GameScreen.player.location.spaceship == this) {
                            GameScreen.userInterface.console.writeToConsole(new Line("You begin orbiting " + planet.name.label + ".", "4CONT6CONT9CONT" + planet.name.colorCode + "1DY", "", true, true));
                        }

                        break;
                    }
                }
            }
        }
    }

    public float getTopSpeed() {
        return 5000000.0f;
    }
}
