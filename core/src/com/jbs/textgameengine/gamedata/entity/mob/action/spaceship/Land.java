package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class Land extends Action {
    public Land(Mob parentEntity) {
        super(parentEntity);
    }

    public Land() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("land", "lan", "la").contains(inputList.get(0))) {
            Land landAction = new Land(parentEntity);

            // Land # //
            if(inputList.size() == 2
            && Utility.isInteger(inputList.get(1))) {
                landAction.targetCount = Integer.valueOf(inputList.get(1));
                landAction.actionType = "Land #";
            }

            // Land Target //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                landAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                landAction.actionType = "Land Target";
            }

            // Land //
            else {
                landAction.actionType = "Land";
            }

            return landAction;
        }

        return null;
    }

    public void initiate() {

        // Message - You must be in a cockpit to do that. //
        if(parentEntity.location == null
        || parentEntity.location.spaceship == null
        || parentEntity.location.spaceship.cockpitRoom != parentEntity.location.room) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be in a cockpit to do that.", "4CONT5CONT3CONT3CONT2W8CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Message - The ship must be in orbit first. //
        else if(!parentEntity.location.spaceship.status.equals("Orbit")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The ship must be in orbit first.", "4CONT5CONT5CONT3CONT3CONT6CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You lack the proper key. //
        else if(parentEntity.location.spaceship.keyNum != -9999
        && !parentEntity.hasKey(parentEntity.location.spaceship.keyNum)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You lack the proper key.", "4CONT5CONT4CONT7CONT3CONT1DY", "", true, true));
            }
        }

        // Get Target Landing Room //
        else {
            Room targetLandingRoom = null;

            if(actionType.equals("Land #")) {
                if(parentEntity.location.spaceship.location.planetoid.isPlanet
                && !((Planet) parentEntity.location.spaceship.location.planetoid).landingPadList.isEmpty()
                && targetCount <= ((Planet) parentEntity.location.spaceship.location.planetoid).landingPadList.size()) {
                    int targetLandingRoomIndex = targetCount;
                    if(targetCount > 0) {targetLandingRoomIndex -= 1;}
                    targetLandingRoom = ((Planet) parentEntity.location.spaceship.location.planetoid).landingPadList.get(targetLandingRoomIndex);
                }
            }

            else if(actionType.equals("Land Target")) {
                if(parentEntity.location.spaceship.location.planetoid.isPlanet) {
                    for(Room landingPadRoom : ((Planet) parentEntity.location.spaceship.location.planetoid).landingPadList) {
                        if(landingPadRoom.nameKeyList.contains(targetEntityString)) {
                            targetLandingRoom = landingPadRoom;
                            break;
                        }
                    }
                }
            }

            else if(actionType.equals("Land")) {
                if(parentEntity.location.spaceship.location.planetoid.isPlanet
                && !((Planet) parentEntity.location.spaceship.location.planetoid).landingPadList.isEmpty()) {
                    targetLandingRoom = ((Planet) parentEntity.location.spaceship.location.planetoid).landingPadList.get(0);
                }
            }

            // Message - The ship's radar doesn't detect any landing zones. //
            if(targetLandingRoom == null) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The ship's radar doesn't detect any landing zones.", "4CONT4CONT1DY2DDW6CONT5CONT1DY2DDW7CONT4CONT8CONT5CONT1DY", "", true, true));
                }
            }

            // Land //
            else {
                parentEntity.interruptAction();

                parentEntity.location.spaceship.status = "Land";
                parentEntity.location.spaceship.currentPhase = 0;
                parentEntity.location.spaceship.phaseTimer = 3;
                parentEntity.location.spaceship.location.area = targetLandingRoom.location.area;
                parentEntity.location.spaceship.location.room = targetLandingRoom;

                if(parentEntity.location.spaceship.location.solarSystem.spaceshipList.contains(this)) {
                    parentEntity.location.spaceship.location.solarSystem.spaceshipList.remove(this);
                }

                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You press a button on the dashboard.", "4CONT6CONT2W7CONT3CONT4CONT4CONT9CONT1DY", "", true, true));
                }
            }
        }
    }
}
