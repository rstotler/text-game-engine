package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

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
                boolean isLastLine = parentEntity.location.spaceship.status.equals("Flight");
                String statusString = parentEntity.location.spaceship.status + "ing";
                String statusColorCode = String.valueOf(parentEntity.location.spaceship.status.length() + 3) + "CONT";
                if(parentEntity.location.spaceship.status.equals("Landed")
                || parentEntity.location.spaceship.status.equals("Flight")) {
                    statusString = parentEntity.location.spaceship.status;
                    statusColorCode = String.valueOf(parentEntity.location.spaceship.status.length()) + "CONT";
                }

                String planetStatusString = "";
                String planetStatusColorCode = "";
                if(!parentEntity.location.spaceship.status.equals("Flight")
                && parentEntity.location.spaceship.location.planetoid != null) {
                    planetStatusString = " [Planet " + parentEntity.location.spaceship.location.planetoid.name.label + "]";
                    planetStatusColorCode = "2DR7CONT" + parentEntity.location.spaceship.location.planetoid.name.colorCode + "1DR";
                }
                GameScreen.userInterface.console.writeToConsole(new Line("Ship Status: " + statusString + planetStatusString, "5CONT6CONT2DY" + statusColorCode + planetStatusColorCode, "", isLastLine, true));

                if(!parentEntity.location.spaceship.status.equals("Flight")
                && parentEntity.location.spaceship.location.planetoid != null) {
                    Planetoid targetPlanet = parentEntity.location.spaceship.location.planetoid;
                    isLastLine = true;
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(0);
                    numberFormat.setGroupingUsed(false);
                    int targetX = Integer.valueOf(numberFormat.format(targetPlanet.coordinates.x));
                    int targetY = Integer.valueOf(numberFormat.format(targetPlanet.coordinates.y));

                    String planetLocationString = "X: " + String.format("%,d", targetX) + ", Y: " + String.format("%,d", targetY);
                    String planetLocationColorCode = "1W1DY" + (String.valueOf(String.format("%,d", targetX)).length() + 1) + "CONT2DY1W1DY" + (String.valueOf(String.format("%,d", targetY)).length() + 1) + "CONT";
                    GameScreen.userInterface.console.writeToConsole(new Line("Location " + planetLocationString, "9CONT" + planetLocationColorCode, "", isLastLine, true));
                }

                // Display Solar System Planetoids //
                GameScreen.userInterface.console.writeToConsole(new Line("Detected Planets:", "9CONT7CONT1DY", "", false, true));

                isLastLine = false;
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
