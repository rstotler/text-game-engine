package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Radar extends Action {
    public Radar() {
        super();
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("radar", "rada", "rad").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Radar();
        }

        return null;
    }

    public void initiate(Mob parentEntity) {

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

                // Display Solar System Planetoids //
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
                        String statusString = parentEntity.location.spaceship.status;
                        if(Arrays.asList("Launch", "Land", "Orbit").contains(statusString)) {
                            statusString += "ing";
                        }
                        planetoidLabel += " [" + statusString + "]";
                        planetoidColorCode += "2DR" + String.valueOf(statusString.length()) + "CONT1DR";
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
