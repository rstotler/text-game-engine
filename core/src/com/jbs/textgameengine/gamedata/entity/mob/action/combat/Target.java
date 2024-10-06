package com.jbs.textgameengine.gamedata.entity.mob.action.combat;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Target extends Action {
    public Target(Mob parentEntity) {
        super(parentEntity);
    }

    public Target() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("target", "targe", "targ", "tar", "ta", "t", "untarget", "untarge", "untarg", "untarg", "untar", "unta", "unt", "un", "u").contains(inputList.get(0))) {
            Target targetAction = new Target(parentEntity);
            targetAction.actionType = "Target";
            if(inputList.get(0).charAt(0) == 'u') {targetAction.actionType = "Untarget";}

            // Target/Untarget All Entity Direction # //
            if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                targetAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 2);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target/Untarget All Entity Direction //
            else if(inputList.size() >= 4
            && inputList.get(1).equals("all")
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                targetAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 1);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target/Untarget # Entity Direction # //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                targetAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 2);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target/Untarget # Entity Direction //
            else if(inputList.size() >= 4
            && Utility.isInteger(inputList.get(1))
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                targetAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 1);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target/Untarget All Direction # //
            else if(inputList.size() == 4
            && inputList.get(1).equals("all")
            && Location.directionList.contains(inputList.get(2))
            && Utility.isInteger(inputList.get(3))) {
                targetAction.allCheck = true;
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target/Untarget All Direction //
            else if(inputList.size() == 3
            && inputList.get(1).equals("all")
            && Location.directionList.contains(inputList.get(2))) {
                targetAction.allCheck = true;
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target/Untarget Entity Direction # //
            else if(inputList.size() >= 4
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 2);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target/Untarget Entity Direction //
            else if(inputList.size() >= 3
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 1);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target/Untarget # Entity //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                targetAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Target/Untarget All Entity //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                targetAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Target/Untarget All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                targetAction.allCheck = true;
            }

            // Target/Untarget Entity //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line(targetAction.actionType + " who?", String.valueOf(targetAction.actionType.length() + 1) + "CONT3CONT1DY", "", true, true));
                targetAction.parentEntity = null;
            }

            return targetAction;
        }

        return null;
    }

    public void initiate() {

        // Get Target Room Data //
        Room targetRoom = parentEntity.location.room;
        TargetRoomData targetRoomData = null;
        if(!targetDirection.isEmpty() && targetDirectionCount > 0) {
            ArrayList<String> directionList = new ArrayList<>();
            for(int i = 0; i < targetDirectionCount; i++) {directionList.add(targetDirection);}
            targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(parentEntity.location.room, directionList, false, false);
            targetRoom = targetRoomData.targetRoom;
        }

        // Get Target Entity Data //
        boolean targetIsInRoom = false;
        if(actionType.equals("Target")
        && !targetEntityString.isEmpty()) {
            for(Entity mob : targetRoom.mobList) {
                if(mob.nameKeyList.contains(targetEntityString)
                && !parentEntity.groupList.contains(mob)) {
                    targetIsInRoom = true;
                    break;
                }
            }
        }
        boolean targetFound = false;
        if(actionType.equals("Untarget")
        && !targetEntityString.isEmpty()) {
            for(Entity mob : parentEntity.targetList) {
                if(mob.nameKeyList.contains(targetEntityString)) {
                    targetFound = true;
                    break;
                }
            }
        }

        // Message - That's out of your range. //
        if(targetDirectionCount != -1
        && targetDirectionCount > parentEntity.getMaxViewDistance()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("That's out of your range.", "4CONT1DY2DDW4CONT3CONT5CONT5CONT1DY", "", true, true));
            }
        }

        // Message - There is nothing there. //
        else if(targetRoomData != null && targetRoomData.message.equals("No Exit")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("There is nothing there.", "6CONT3CONT8CONT5CONT1DY", "", true, true));
            }
        }

        // Message - Your view to the Direction is obstructed by a door. //
        else if(targetRoomData != null && targetRoomData.message.equals("Door Is Closed")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Your view to the " + targetDirection + " is obstructed by a door.", "5CONT5CONT3CONT4CONT" + String.valueOf(targetDirection.length()) + "1W3CONT11CONT3CONT2W4CONT1DY", "", true, true));
            }
        }

        // Message - You don't see them. (ActionType: Target) //
        else if(actionType.equals("Target") && !targetEntityString.isEmpty() && !targetIsInRoom) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You don't see them.", "4CONT3CONT1DY2DDW4CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You aren't targeting them. (ActionType: Untarget) //
        else if(actionType.equals("Untarget") && !targetEntityString.isEmpty() && !targetFound) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You aren't targeting them.", "4CONT4CONT1DY2DDW10CONT4CONT1DY", "", true, true));
            }
        }

        // Target/Untarget Mobs //
        else {

            // Get Target/Untarget Data //
            int count = 0;
            boolean alreadyTargetingCheck = false;
            ArrayList<Entity> targetMobList = new ArrayList<>();
            ArrayList<Entity> checkedList = new ArrayList<>();
            if(actionType.equals("Untarget")
            && targetDirection.isEmpty()) {
                targetMobList.addAll(parentEntity.targetList);
            }
            targetMobList.addAll(targetRoom.mobList);

            Mob targetEntity = null;
            for(Entity mob : targetMobList) {
                if(!checkedList.contains(mob)) {
                    checkedList.add(mob);
                    boolean countCheck = false;

                    if((allCheck && targetEntityString.isEmpty())
                    || (!targetEntityString.isEmpty() && mob.nameKeyList.contains(targetEntityString))) {
                        if(actionType.equals("Target")
                        && !parentEntity.groupList.contains(mob)
                        && !parentEntity.targetList.contains(mob)) {
                            parentEntity.targetList.add((Mob) mob);
                            count += 1;
                            countCheck = true;
                            if(targetEntity == null) {targetEntity = (Mob) mob;}
                        }
                        else if(actionType.equals("Untarget")
                        && parentEntity.targetList.contains(mob)) {
                            parentEntity.targetList.remove(mob);
                            count += 1;
                            countCheck = true;
                            if(targetEntity == null) {targetEntity = (Mob) mob;}
                        }

                        if(countCheck && count == 1
                        && actionType.equals("Untarget")
                        && targetDirection.isEmpty()) {
                            if(!targetRoom.mobList.contains(mob)) {
                                TargetRoomData combatTargetRoomData = TargetRoomData.getTargetEntityRoomFromStartRoom(parentEntity.location.room, mob, parentEntity.getMaxViewDistance());
                                if(combatTargetRoomData.message.isEmpty()
                                && !combatTargetRoomData.targetDirection.isEmpty()) {
                                    targetDirection = combatTargetRoomData.targetDirection;
                                }
                            }
                        }

                        if(actionType.equals("Target") && parentEntity.targetList.contains(mob)) {
                            alreadyTargetingCheck = true;
                        }

                        if(countCheck) {
                            if((targetCount != -1 && count >= targetCount)
                            || (targetCount == -1 && !targetEntityString.isEmpty() && !allCheck)) {
                                break;
                            }
                        }
                    }
                }
            }

            // Message - You aren't targeting anyone. (ActionType: Untarget) //
            if(actionType.equals("Untarget")
            && parentEntity.targetList.isEmpty()
            && count == 0) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You aren't targeting anyone.", "4CONT4CONT1DY2DDW10CONT6CONT1DY", "", true, true));
                }
            }

            // Message - You are already targeting them. (ActionType: Target) //
            else if(actionType.equals("Target")
            && count == 0
            && alreadyTargetingCheck) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You are already targeting them.", "4CONT4CONT8CONT10CONT4CONT1DY", "", true, true));
                }
            }

            // Message - There is no one here/there to target/untarget. //
            else if(count == 0) {
                if(parentEntity.isPlayer) {
                    String hereThereString = "here";
                    if(targetDirectionCount > 0) {hereThereString = "there";}
                    GameScreen.userInterface.console.writeToConsole(new Line("There is no one " + hereThereString + " to " + actionType.toLowerCase() + ".", "6CONT3CONT3CONT4CONT" + String.valueOf(hereThereString.length()) + "CONT1W3CONT" + String.valueOf(actionType.length()) + "CONT1Y", "", true, true));
                }
            }

            // Target/Untarget //
            else {

                // Message - You narrow your focus on Entity to the Direction. //
                if(actionType.equals("Target") && count == 1) {
                    if(parentEntity.isPlayer) {
                        if(targetEntity != null) {
                            String directionString = "";
                            String directionColorCode = "";
                            if(!targetDirection.isEmpty()) {
                                directionString = " to the " + targetDirection.toLowerCase();
                                directionColorCode = "1W3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT";
                            }
                            GameScreen.userInterface.console.writeToConsole(new Line("You narrow your focus on " + targetEntity.prefix + targetEntity.name.label + directionString + ".", "4CONT7CONT5CONT6CONT3CONT" + String.valueOf(targetEntity.prefix.length()) + "CONT" + targetEntity.name.colorCode + directionColorCode + "1DY", "", true, true));
                        }
                    }
                }

                // Message - You focus your attention on the group to the Direction. //
                else if(actionType.equals("Target") && count > 1) {
                    if(parentEntity.isPlayer) {
                        String directionString = "";
                        String directionColorCode = "";
                        if(!targetDirection.isEmpty()) {
                            directionString = " to the " + targetDirection.toLowerCase();
                            directionColorCode = "1W3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT";
                        }
                        GameScreen.userInterface.console.writeToConsole(new Line("You focus your attention on the group" + directionString + ".", "4CONT6CONT5CONT10CONT3CONT4CONT5CONT" + directionColorCode + "1DY", "", true, true));
                    }
                }

                // Message - You stop focusing on Entity to the Direction. //
                else if(actionType.equals("Untarget") && count == 1) {
                    if(parentEntity.isPlayer) {
                        if(targetEntity != null) {
                            String directionString = "";
                            String directionColorCode = "";
                            if(!targetDirection.isEmpty()) {
                                directionString = " to the " + targetDirection.toLowerCase();
                                directionColorCode = "1W3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT";
                            }
                            GameScreen.userInterface.console.writeToConsole(new Line("You stop focusing on " + targetEntity.prefix + targetEntity.name.label + directionString + ".", "4CONT5CONT9CONT3CONT" + String.valueOf(targetEntity.prefix.length()) + "CONT" + targetEntity.name.colorCode + directionColorCode + "1DY", "", true, true));
                        }
                    }
                }

                // Message - You take a deep breath and relax your mind. //
                else if(actionType.equals("Untarget") && count > 1) {
                    if(parentEntity.isPlayer) {
                        String directionString = "";
                        String directionColorCode = "";
                        if(!targetDirection.isEmpty()) {
                            directionString = " to the " + targetDirection.toLowerCase();
                            directionColorCode = "1W3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT";
                        }
                        GameScreen.userInterface.console.writeToConsole(new Line("You take a deep breath and relax your mind.", "4CONT5CONT2W5CONT7CONT4CONT6CONT5CONT4CONT1DY", "", true, true));
                    }
                }
            }
        }
    }
}
