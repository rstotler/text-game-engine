package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

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
                    if(parentEntity.currentAction != null
                    && !parentEntity.currentAction.isCombatAction) {
                        parentEntity.interruptAction();
                    }

                    Room previousRoom = parentEntity.location.room;
                    parentEntity.location = new Location(newRoom.location);

                    // Move Mob To Other Room //
                    if(!parentEntity.isPlayer) {
                        if(!newRoom.mobList.contains(parentEntity)) {
                            newRoom.mobList.add(parentEntity);
                        }
                        previousRoom.mobList.remove(parentEntity);
                    }

                    // Update Current Action Movement List //
                    if(parentEntity.currentAction != null) {
                        if(!parentEntity.currentAction.movementList.isEmpty()
                        && parentEntity.currentAction.movementList.get(0).equals(targetDirection)) {
                            parentEntity.currentAction.movementList.remove(0);
                        } else {
                            parentEntity.currentAction.movementList.add(0, Location.getOppositeDirection(targetDirection));
                        }
                    }

                    // Combat Action Distance Cancel Check //
                    if(parentEntity.currentAction != null
                    && parentEntity.currentAction.isCombatAction) {
                        parentEntity.combatActionDistanceCancelCheck();
                    }

                    // Reset Mob Dialogue Timers //
                    for(Entity mob : newRoom.mobList) {
                        if(((Mob) mob).dialogue != null
                        && ((Mob) mob).dialogue.resetOnPlayerEntrance) {
                            ((Mob) mob).dialogue.speakTimer = ((Mob) mob).dialogue.speakTimerMax - 5;
                        }
                    }

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
                    if(true) {

                        // Player Display Data //
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

                        // Mob Display Data (Leaving Room) //
                        else if(previousRoom == GameScreen.player.location.room) {
                            if(closedCheck) {
                                GameScreen.userInterface.console.writeToConsole(new Line(parentEntity.prefix + parentEntity.name.label + " opens the door to the " + targetDirection.toLowerCase() + ".", String.valueOf(parentEntity.prefix.length()) + "CONT" + parentEntity.name.colorCode + "1W6CONT4CONT5CONT3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT1DY", "", false, true));
                            } else if(lockedCheck) {
                                GameScreen.userInterface.console.writeToConsole(new Line(parentEntity.prefix + parentEntity.name.label + " opens and unlocks the door to the " + targetDirection.toLowerCase() + ".", String.valueOf(parentEntity.prefix.length()) + "CONT" + parentEntity.name.colorCode + "1W6CONT4CONT8CONT4CONT5CONT3CONT5CONT" + String.valueOf(targetDirection.length()) + "CONT1DY", "", false, true));
                            } else if(automaticCheck) {
                                GameScreen.userInterface.console.writeToConsole(new Line("The door to the " + targetDirection.toLowerCase() + " opens and closes as " + parentEntity.prefix.toLowerCase() + parentEntity.name.label + " walks through.", "4CONT5CONT3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT1W6CONT4CONT7CONT3CONT" + String.valueOf(parentEntity.prefix.length()) + "CONT" + parentEntity.name.colorCode + "1W6CONT7CONT1DY", "", false, true));
                            }

                            GameScreen.userInterface.console.writeToConsole(new Line(parentEntity.prefix + parentEntity.name.label + " leaves to the " + targetDirection.toLowerCase() + ".", String.valueOf(parentEntity.prefix.length()) + "CONT" + parentEntity.name.colorCode + "1W7CONT3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT1DY", "", true, true));
                        }

                        // Mob Display Data (Entering Room) //
                        else if(parentEntity.location.room == GameScreen.player.location.room) {
                            String displayDirection = Location.getOppositeDirection(targetDirection);

                            if(closedCheck) {
                                GameScreen.userInterface.console.writeToConsole(new Line("The door to the " + displayDirection.toLowerCase() + " opens.", "4CONT5CONT3CONT4CONT" + String.valueOf(displayDirection.length()) + "CONT1W5CONT1DY", "", false, true));
                            } else if(lockedCheck) {
                                GameScreen.userInterface.console.writeToConsole(new Line("The door to the " + displayDirection.toLowerCase() + " unlocks and opens.", "4CONT5CONT3CONT4CONT" + String.valueOf(displayDirection.length()) + "CONT1W8CONT4CONT5CONT1DY", "", false, true));
                            } else if(automaticCheck) {
                                GameScreen.userInterface.console.writeToConsole(new Line("The door to the " + displayDirection.toLowerCase() + " opens and closes as " + parentEntity.prefix.toLowerCase() + parentEntity.name.label + " walks through.", "4CONT5CONT3CONT4CONT" + String.valueOf(displayDirection.length()) + "CONT1W6CONT4CONT7CONT3CONT" + String.valueOf(parentEntity.prefix.length()) + "CONT" + parentEntity.name.colorCode + "1W6CONT7CONT1DY", "", false, true));
                            }

                            GameScreen.userInterface.console.writeToConsole(new Line(parentEntity.prefix + parentEntity.name.label + " arrives from the " + displayDirection.toLowerCase() + ".", String.valueOf(parentEntity.prefix.length()) + "CONT" + parentEntity.name.colorCode + "1W8CONT5CONT4CONT" + String.valueOf(displayDirection.length()) + "CONT1DY", "", true, true));
                        }
                    }
                }
            }
        }
    }
}
