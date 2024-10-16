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
    public Move(Mob parentEntity) {
        super(parentEntity);
    }

    public Move() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Location.directionList.contains(inputList.get(0))
        && (inputList.size() == 1 || (inputList.size() == 2 && Utility.isInteger(inputList.get(1))))) {
            Move moveAction = new Move(parentEntity);

            // Direction # //
            if(inputList.size() == 2 && Utility.isInteger(inputList.get(1))) {
                moveAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                moveAction.targetCount = Integer.valueOf(inputList.get(1));
                moveAction.actionType = "Direction #";
            }

            // Direction //
            else if(inputList.size() == 1) {
                moveAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                moveAction.actionType = "Direction";
            }

            return moveAction;
        }

        return null;
    }

    public void initiate() {
        if(actionType.equals("Direction #")) {}

        else if(actionType.equals("Direction")) {
            Room parentRoom = parentEntity.location.room;

            // Message - You can't go that way. //
            if(((parentRoom.exitMap.containsKey(targetDirection)
            && parentRoom.exitMap.get(targetDirection) == null)

            || (parentRoom.hiddenExitMap.containsKey(targetDirection)
            && parentRoom.hiddenExitMap.get(targetDirection) != null
            && !parentRoom.hiddenExitMap.get(targetDirection).isOpen))

            && !(parentRoom.location.spaceship != null
            && parentRoom.location.spaceship.boardingRoom == parentRoom
            && parentRoom.location.spaceship.boardingRoomExitDirection == targetDirection)) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You can't go that way.", "4CONT3CONT1DY2DDW3CONT5CONT3CONT1DY", "", true, true));
                }
            }

            // Message - The door is locked. //
            else if(parentRoom.doorMap.containsKey(targetDirection)
            && parentRoom.doorMap.get(targetDirection) != null
            && parentRoom.doorMap.get(targetDirection).status.equals("Locked")
            && !parentEntity.hasKey(parentRoom.doorMap.get(targetDirection).keyNum)) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The door is locked.", "4CONT5CONT3CONT6CONT1DY", "", true, true));
                }
            }

            // Message On Ship - You can't leave while the ship is in use. //
            else if(parentRoom.location.spaceship != null
            && parentRoom.location.spaceship.boardingRoom == parentRoom
            && parentRoom.location.spaceship.boardingRoomExitDirection == targetDirection
            && !parentRoom.location.spaceship.status.equals("Landed")) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You can't leave while the ship is in flight.", "4CONT3CONT1DY2DW6CONT6CONT4CONT5CONT3CONT3CONT6CONT1DY", "", true, true));
                }
            }

            // Move //
            else {
                parentEntity.interruptAction();

                Room newRoom = null;
                if(parentRoom.location.spaceship != null
                && parentRoom.location.spaceship.boardingRoom == parentRoom
                && parentRoom.location.spaceship.boardingRoomExitDirection == targetDirection
                && parentRoom.location.spaceship.location.room != null) {
                    newRoom = parentRoom.location.spaceship.location.room;
                }
                else if(parentRoom.exitMap.containsKey(targetDirection)) {
                    newRoom = parentRoom.exitMap.get(targetDirection);
                }

                if(newRoom != null) {
                    parentEntity.location = new Location(newRoom.location);

                    boolean closedCheck = false;
                    boolean lockedCheck = false;
                    boolean automaticCheck = false;

                    // Door Type Checks //
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
                        else if(parentRoom.doorMap.get(targetDirection).type.equals("Automatic")
                        && !parentRoom.doorMap.get(targetDirection).status.equals("Open")) {
                            automaticCheck = true;
                        }
                    }

                    // Display New Room //
                    if(parentEntity.isPlayer) {
                        if(closedCheck) {
                            GameScreen.userInterface.console.writeToConsole(new Line("You open the door.", "4CONT5CONT4CONT4CONT1DY", "", true, true));
                        } else if(lockedCheck) {
                            GameScreen.userInterface.console.writeToConsole(new Line("You unlock and open the door.", "4CONT7CONT4CONT5CONT4CONT4CONT1DY", "", true, true));
                        } else if(automaticCheck) {
                            GameScreen.userInterface.console.writeToConsole(new Line("The door opens and closes as you walk through.", "4CONT5CONT6CONT4CONT7CONT3CONT4CONT5CONT7CONT1DY", "", true, true));
                        }

                        if(parentEntity.location != null
                        && parentEntity.location.room != null) {
                            parentEntity.location.room.display();
                        }
                    }
                }
            }
        }
    }
}
