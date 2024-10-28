package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Board extends Action {
    public Board(Mob parentEntity) {
        super(parentEntity);
    }

    public Board() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("board", "boar", "boa", "bo", "enter", "ente", "ent", "en").contains(inputList.get(0))) {
            Board boardAction = new Board(parentEntity);

            // Board Target //
            if(inputList.size() > 1) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                boardAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                boardAction.actionType = "Board Target";
            }

            // Board //
            else {
                boardAction.actionType = "Board";
            }

            return boardAction;
        }

        return null;
    }

    public void initiate() {
        Spaceship targetSpaceship = null;

        if(actionType.equals("Board Target")) {
            if(parentEntity.location != null
            && parentEntity.location.room != null) {
                targetSpaceship = (Spaceship) parentEntity.location.room.getEntityFromNameKey(targetEntityString, "Spaceship");
            }
        }

        else if(actionType.equals("Board")) {
            if(parentEntity.location != null
            && parentEntity.location.room != null) {
                targetSpaceship = (Spaceship) parentEntity.location.room.getEntityFromNameKey("ANY", "Spaceship");
            }
        }

        // Message - You are already inside. //
        if(parentEntity.location != null
        && parentEntity.location.spaceship != null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You are already inside.", "4CONT4CONT8CONT6CONT1DY", "", true, true));
            }
        }

        // Message - You see no such ship. //
        else if(targetSpaceship == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You see no such ship.", "4CONT4CONT3CONT5CONT4CONT1DY", "", true, true));
            }
        }

        // Message - The door is locked. //
        else if(targetSpaceship.hatchStatus.equals("Locked")
        && !parentEntity.hasKey(targetSpaceship.keyNum)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The door is locked.", "4CONT5CONT3CONT6CONT1DY", "", true, true));
            }
        }

        // Board Ship //
        else {
            parentEntity.interruptAction();

            parentEntity.location = new Location(targetSpaceship.boardingRoom.location);

            // Update Map (Player Only) //
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.map.buffer(parentEntity.location);
                GameScreen.userInterface.map.updateOffset(parentEntity.location.room);
            }

            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The door opens and closes as you board the ship.", "4CONT5CONT6CONT4CONT7CONT3CONT4CONT6CONT4CONT4CONT1DY", "", true, true));

                if(parentEntity.location.room != null) {
                    parentEntity.location.room.display();
                }
            }
        }
    }
}
