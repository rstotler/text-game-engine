package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Move extends Action {
    public Move() {
        super();
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Location.directionList.contains(inputList.get(0))) {
            Move moveAction = new Move();

            // Direction # //
            if(inputList.size() == 2 && Utility.isInteger(inputList.get(1))) {
                moveAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                moveAction.directionCount = Integer.valueOf(inputList.get(1));
                moveAction.actionType = "Direction #";
            }

            // Direction //
            else if(inputList.size() == 1) {
                moveAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                moveAction.actionType = "Direction";
            }

            else {
                userInterface.console.writeToConsole(new Line("Move where?", "5CONT5CONT1DY", "", true, true));
                return null;
            }

            return moveAction;
        }

        return null;
    }

    public void initiate(Mob parentEntity) {
        if(actionType.equals("Direction #")) {
        }

        else if(actionType.equals("Direction")) {
            Room parentRoom = parentEntity.room;

            // No Such Exit Exists //
            if(parentRoom.exitMap.containsKey(targetDirection)
            && parentRoom.exitMap.get(targetDirection) == null) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You can't go that way.", "4CONT3CONT1DY2DDW3CONT5CONT3CONT1DY", "", true, true));
                }
            }

            // Door Is Locked //
            else if(parentRoom.doorMap.containsKey(targetDirection)
            && parentRoom.doorMap.get(targetDirection) != null
            && parentRoom.doorMap.get(targetDirection).status.equals("Locked")
            && !parentEntity.hasKey(parentRoom.doorMap.get(targetDirection).keyNum)) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("It's locked.", "2CONT1DY2DDW6CONT1DY", "", true, true));
                }
            }

            // Move //
            else {
                Room newRoom = parentRoom.exitMap.get(targetDirection);
                parentEntity.room = newRoom;

                boolean closedCheck = false;
                boolean lockedCheck = false;
                boolean automaticCheck = false;

                if(parentRoom.doorMap.containsKey(targetDirection)
                && parentRoom.doorMap.get(targetDirection) != null
                && !parentRoom.doorMap.get(targetDirection).status.equals("Open")) {
                    if(parentRoom.doorMap.get(targetDirection).type.equals("Manual")) {
                        if(parentRoom.doorMap.get(targetDirection).status.equals("Closed")) {
                            closedCheck = true;
                        }
                        else if(parentRoom.doorMap.get(targetDirection).status.equals("Locked")) {
                            lockedCheck = true;
                        }
                        parentRoom.doorMap.get(targetDirection).status = "Open";
                    }
                    else if(parentRoom.doorMap.get(targetDirection).type.equals("Automatic")) {
                        automaticCheck = true;
                    }
                }

                if(parentEntity.isPlayer) {
                    if(closedCheck) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You open the door.", "4CONT5CONT4CONT4CONT1DY", "", true, true));
                    } else if(lockedCheck) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You unlock and open the door.", "4CONT7CONT4CONT5CONT4CONT4CONT1DY", "", true, true));
                    } else if(automaticCheck) {
                        GameScreen.userInterface.console.writeToConsole(new Line("The door opens and closes as you pass through.", "4CONT5CONT6CONT4CONT7CONT3CONT4CONT5CONT7CONT1DY", "", true, true));
                    }

                    parentEntity.room.display();
                }
            }
        }
    }
}
