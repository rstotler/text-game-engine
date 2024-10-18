package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Point;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Radar extends Action {
    public Radar(Mob parentEntity) {
        super(parentEntity);
    }

    public Radar() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("radar", "rada", "rad", "ra").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Radar(parentEntity);
        }

        return null;
    }

    public void initiate() {

        // Message - You must be in a cockpit to do that. //
        if(parentEntity.location.spaceship == null
        || parentEntity.location.spaceship.cockpitRoom != parentEntity.location.room) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be in a cockpit to do that.", "4CONT5CONT3CONT3CONT2W8CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Radar //
        else {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You look at the ship's radar screen.", "4CONT5CONT3CONT4CONT4CONT1DY2DW6CONT6CONT1DY", "", true, true));

                // Display Ship Status //
                if(true) {
                    String statusString = "Ship Status: " + parentEntity.location.spaceship.status + "ing";
                    String statusColorCode = "5CONT6CONT2DY" + String.valueOf(parentEntity.location.spaceship.status.length() + 3) + "CONT";
                    if(parentEntity.location.spaceship.status.equals("Landed")) {
                        statusString = "Ship Status: Landed";
                        statusColorCode = "5CONT6CONT2DY6CONT";
                    }
                    else if(parentEntity.location.spaceship.status.equals("Flight")) {
                        statusString = "Ship Status: In Flight";
                        statusColorCode = "5CONT6CONT2DY3CONT6CONT";
                    }

                    String planetStatusString = "";
                    String planetStatusColorCode = "";
                    if(!parentEntity.location.spaceship.status.equals("Flight")
                    && parentEntity.location.spaceship.location.planetoid != null) {
                        planetStatusString = " [Planet " + parentEntity.location.spaceship.location.planetoid.name.label + "]";
                        planetStatusColorCode = "2DR7CONT" + parentEntity.location.spaceship.location.planetoid.name.colorCode + "1DR";
                    }

                    GameScreen.userInterface.console.writeToConsole(new Line(statusString + planetStatusString, statusColorCode + planetStatusColorCode, "", false, true));
                }

                // Display Spaceship Heading String //
                if(true) {
                    Point targetPoint = null;
                    String headingString = "Ship Heading: [None]";
                    String headingColorCode = "5CONT7CONT2DY1DR4CONT1DR";
                    if(parentEntity.location.spaceship.headingPlanetoid != null) {
                        targetPoint = parentEntity.location.spaceship.headingPlanetoid.coordinates;
                        headingString = "Ship Heading: " + parentEntity.location.spaceship.headingPlanetoid.name.label;
                        headingColorCode = "5CONT7CONT2DY" + parentEntity.location.spaceship.headingPlanetoid.name.colorCode;
                    }
                    else if(parentEntity.location.spaceship.headingXY != null) {
                        targetPoint = parentEntity.location.spaceship.headingXY;
                        Line headingXLine = Utility.insertCommas((int) parentEntity.location.spaceship.headingXY.x);
                        Line headingYLine = Utility.insertCommas((int) parentEntity.location.spaceship.headingXY.y);
                        headingString = "Ship Heading: X: " + headingXLine.label + ", Y: " + headingYLine.label;
                        headingColorCode = "5CONT7CONT2DY1W2DY" + headingXLine.colorCode + "2DY1W2DY" + headingYLine.colorCode;
                    }

                    if(parentEntity.location.spaceship.headingPlanetoid != null
                    || parentEntity.location.spaceship.headingXY != null) {
                        Point spaceshipPoint = null;
                        if(parentEntity.location.spaceship.location.planetoid != null) {
                            spaceshipPoint = parentEntity.location.spaceship.location.planetoid.coordinates;
                        } else if(parentEntity.location.spaceship.coordinates != null) {
                            spaceshipPoint = parentEntity.location.spaceship.coordinates;
                        }
                        if(spaceshipPoint != null) {
                            int distance = (int) Utility.distanceBetweenPoints(spaceshipPoint, targetPoint);
                            Line distanceLine = Utility.insertCommas(distance);
                            headingString += " (Distance: " + distanceLine.label + " Miles)";
                            headingColorCode += "2DR8CONT2DY" + distanceLine.colorCode + "1W5CONT1DR";
                        }
                    }

                    GameScreen.userInterface.console.writeToConsole(new Line(headingString, headingColorCode, "", false, true));
                }

                // Display Speed String //
                if(true) {
                    String speedPercentString = String.valueOf((int) (parentEntity.location.spaceship.speedPercent * 100));
                    String speedString = "Ship Speed: " + speedPercentString + "%";
                    String speedColorCode = "5CONT5CONT2DY" + String.valueOf(speedPercentString).length() + "CONT1DY";
                    GameScreen.userInterface.console.writeToConsole(new Line(speedString, speedColorCode, "", true, true));
                }

                // Display Solar System Planetoids //
                GameScreen.userInterface.console.writeToConsole(new Line("Detected Planets:", "9CONT7CONT1DY", "", false, true));

                boolean isLastLine = false;
                ArrayList<Planetoid> planetoidList = parentEntity.location.spaceship.location.solarSystem.planetoidList;
                for(int i = 0; i < planetoidList.size(); i++) {
                    Planetoid planetoid = planetoidList.get(i);

                    String planetTypeLabel = "[Pl] ";
                    String planetTypeColodeCode = "1DR2CONT2DR";
                    if(!planetoid.isPlanet) {planetTypeLabel = "[St] ";}

                    String planetoidLabel = planetTypeLabel + planetoid.name.label;
                    String planetoidColorCode = planetTypeColodeCode + planetoid.name.colorCode;
                    if(!parentEntity.location.spaceship.status.equals("Flight")
                    && parentEntity.location.spaceship.location.planetoid == planetoid) {
                        String spaceshipStatus = parentEntity.location.spaceship.status;
                        if(Arrays.asList("Launch", "Land", "Orbit").contains(spaceshipStatus)) {
                            spaceshipStatus += "ing";
                        }
                        planetoidLabel += " [" + spaceshipStatus + "]";
                        planetoidColorCode += "2DR" + String.valueOf(spaceshipStatus.length()) + "CONT1DR";
                    }

                    if(i == planetoidList.size() - 1) {isLastLine = true;}
                    GameScreen.userInterface.console.writeToConsole(new Line(planetoidLabel, planetoidColorCode, "", isLastLine, true));
                }

                // Display Landing Pads (If In Orbit) //
                isLastLine = false;
                if(parentEntity.location.spaceship.status.equals("Orbit")) {
                    ArrayList<Room> landingPadList = ((Planet) parentEntity.location.spaceship.location.planetoid).landingPadList;

                    for(int i = 0; i < landingPadList.size(); i++) {
                        Room landingPadRoom = landingPadList.get(i);

                        String zeroString = "0";
                        String zeroColorCode = "1W";
                        if(i >= 9) {zeroString = ""; zeroColorCode = "";}
                        String landingPadString = "[" + zeroString + String.valueOf(i + 1) + "] - " + landingPadRoom.name.label;
                        String landingPadColorCode = "1DR" + zeroColorCode + String.valueOf(String.valueOf(i + 1).length()) + "W2DR2DY" + landingPadRoom.name.colorCode;

                        if(i == landingPadList.size() - 1) {isLastLine = true;}
                        GameScreen.userInterface.console.writeToConsole(new Line(landingPadString, landingPadColorCode, "", isLastLine, true));
                    }
                }
            }
        }
    }
}
