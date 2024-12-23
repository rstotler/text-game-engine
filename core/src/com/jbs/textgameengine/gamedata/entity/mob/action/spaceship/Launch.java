package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Launch extends Action {
    public Launch(Mob targetEntity) {
        super(targetEntity);
    }

    public Launch() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("launch", "launc", "laun", "lau").contains(inputList.get(0))
        && inputList.size() == 1) {
            Launch launchAction = new Launch(parentEntity);

            return launchAction;
        }

        return null;
    }

    public void initiate() {

        // Message - You must be in a cockpit to do that. //
        if(parentEntity.location == null
        ||parentEntity.location.spaceship == null
        || parentEntity.location.spaceship.cockpitRoom != parentEntity.location.room) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be in a cockpit to do that.", "4CONT5CONT3CONT3CONT2W8CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Message - The ship must be landed first. //
        else if(!parentEntity.location.spaceship.status.equals("Landed")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The ship must be landed first.", "4CONT5CONT5CONT3CONT7CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You lack the proper key. //
        else if(parentEntity.location.spaceship.keyNum != -9999
        && !parentEntity.hasKey(parentEntity.location.spaceship.keyNum)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You lack the proper key.", "4CONT5CONT4CONT7CONT3CONT1DY", "", true, true));
            }
        }

        // Launch //
        else {
            parentEntity.interruptAction();

            Room landedRoom = parentEntity.location.spaceship.location.room;
            parentEntity.location.spaceship.status = "Launch";
            parentEntity.location.spaceship.currentPhase = 0;
            parentEntity.location.spaceship.phaseTimer = 3;
            parentEntity.location.spaceship.location.room = null;
            parentEntity.location.spaceship.location.area = null;

            if(parentEntity.location.spaceship.hatchStatus.equals("Open")) {
                parentEntity.location.spaceship.hatchStatus = "Closed";
            }
            if(landedRoom != null
            && landedRoom.spaceshipList.contains(parentEntity.location.spaceship)) {
                landedRoom.spaceshipList.remove(parentEntity.location.spaceship);
            }
            if(!parentEntity.location.solarSystem.spaceshipList.contains(parentEntity.location.spaceship)) {
                parentEntity.location.solarSystem.spaceshipList.add(parentEntity.location.spaceship);
            }

            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You press a button on the dashboard.", "4CONT6CONT2W7CONT3CONT4CONT4CONT9CONT1DY", "", true, true));
            }
        }
    }
}
