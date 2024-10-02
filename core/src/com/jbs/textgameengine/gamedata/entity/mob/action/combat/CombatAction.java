package com.jbs.textgameengine.gamedata.entity.mob.action.combat;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.combateffect.Fumble;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.combateffect.Stumble;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class CombatAction extends Action {
    public CombatAction(Mob parentEntity, Skill combatSkill) {
        super(parentEntity, combatSkill);
    }

    public CombatAction() {
        this(null, null);
    }

    public CombatAction getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        String twoWordKey = "";
        if(inputList.size() > 1) {
            List<String> twoWordKeyList = inputList.subList(0, 2);
            twoWordKey = twoWordKeyList.stream().collect(Collectors.joining(" "));
        }

        // Get CombatAction & Remove Name Key From Front Of InputList //
        CombatAction targetCombatAction = null;
        for(Skill skill : parentEntity.skillMap.get("Combat")) {
            if(!twoWordKey.isEmpty() && skill.nameKeyList.contains(twoWordKey)) {
                targetCombatAction = new CombatAction(parentEntity, skill);
                inputList = new ArrayList<>(inputList.subList(2, inputList.size()));
                break;
            }
            else if(skill.nameKeyList.contains(inputList.get(0))) {
                targetCombatAction = new CombatAction(parentEntity, skill);
                inputList = new ArrayList<>(inputList.subList(1, inputList.size()));
                break;
            }
        }

        if(targetCombatAction != null) {

            // CombatAction All/Group Direction # //
            if(inputList.size() == 3
            && (inputList.get(0).equals("all") || inputList.get(0).equals("group"))
            && Location.directionList.contains(inputList.get(1))
            && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                if(inputList.get(0).equals("all")) {
                    targetCombatAction.allCheck = true;
                } else {
                    targetCombatAction.groupCheck = true;
                }
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                targetCombatAction.targetCount = Integer.valueOf(inputList.get(inputList.size() - 1));
                targetCombatAction.actionType = "CombatAction All/Group Direction #";
            }

            // CombatAction Entity Direction # //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                List<String> targetEntityStringList = inputList.subList(0, inputList.size() - 2);
                targetCombatAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetCombatAction.targetCount = Integer.valueOf(inputList.get(inputList.size() - 1));
                targetCombatAction.actionType = "CombatAction Entity Direction #";
            }

            // CombatAction All/Group Direction //
            else if(inputList.size() == 2
            && (inputList.get(0).equals("all") || inputList.get(0).equals("group"))
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                if(inputList.get(0).equals("all")) {
                    targetCombatAction.allCheck = true;
                } else {
                    targetCombatAction.groupCheck = true;
                }
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                targetCombatAction.targetCount = 1;
                targetCombatAction.actionType = "CombatAction All/Group Direction";
            }

            // CombatAction Entity Direction //
            else if(inputList.size() >= 2
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                List<String> targetEntityStringList = inputList.subList(0, inputList.size() - 1);
                targetCombatAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetCombatAction.targetCount = 1;
                targetCombatAction.actionType = "CombatAction Entity Direction";
            }

            // CombatAction Direction # //
            else if(inputList.size() == 2
            && Location.directionList.contains(inputList.get(0))
            && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                targetCombatAction.targetCount = Integer.valueOf(inputList.get(1));
                targetCombatAction.actionType = "CombatAction Direction #";
            }

            // CombatAction All/Group //
            else if(inputList.size() == 1
            && (inputList.get(0).equals("all") || inputList.get(0).equals("group"))) {
                if(inputList.get(0).equals("all")) {
                    targetCombatAction.allCheck = true;
                } else {
                    targetCombatAction.groupCheck = true;
                }
                targetCombatAction.targetCount = 0;
                targetCombatAction.actionType = "CombatAction All/Group";
            }

            // CombatAction Direction //
            else if(inputList.size() == 1
            && Location.directionList.contains(inputList.get(0))) {
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                targetCombatAction.targetCount = 1;
                targetCombatAction.actionType = "CombatAction Direction";
            }

            // CombatAction Entity/Self //
            else if(!inputList.isEmpty()) {
                if(inputList.get(0).equals("self")) {
                    targetCombatAction.selfCheck = true;
                } else {
                    List<String> targetEntityStringList = inputList.subList(0, inputList.size());
                    targetCombatAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                }
                targetCombatAction.targetCount = 0;
                targetCombatAction.actionType = "CombatAction Entity/Self";
            }

            // CombatAction //
            else {
                targetCombatAction.actionType = "CombatAction";
            }

            if(!targetCombatAction.targetDirection.isEmpty()
            && targetCombatAction.targetCount == 0) {
                targetCombatAction.targetCount = 1;
            }

            return targetCombatAction;
        }

        return null;
    }

    public void initiate() {

        // Message - You lose balance and trip over yourself./You fumble with your equipment. //
        if(parentEntity.currentAction != null
        && parentEntity.currentAction.skill != null) {
            if(parentEntity.currentAction.skill.toString().equals("Dodge")) {
                parentEntity.currentAction = new Stumble();

                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You lose balance and trip over yourself.", "4CONT5CONT8CONT4CONT5CONT5CONT8CONT1DY", "", true, true));
                }
            }

            else if(parentEntity.currentAction.skill.toString().equals("Block")) {
                parentEntity.currentAction = new Fumble();

                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You fumble with your equipment.", "4CONT7CONT5CONT5CONT9CONT1DW", "", true, true));
                }
            }
        }

        // Message - You aren't holding the proper weapon type. //
        else if(skill != null
        && !skill.weaponCheck(parentEntity)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You aren't holding the proper weapon type.", "4CONT4CONT1DY2DW8CONT4CONT7CONT7CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You can't use that on yourself/your group. //
        else if(skill != null
        && !skill.isHealing
        && (selfCheck || groupCheck)) {
            if(parentEntity.isPlayer) {
                String targetString = "yourself";
                String targetColorCode = "8CONT";
                if(groupCheck) {
                    targetString = "your group";
                    targetColorCode = "5CONT5CONT";
                }
                GameScreen.userInterface.console.writeToConsole(new Line("You can't use that on " + targetString + ".", "4CONT3CONT1DY2DW4CONT5CONT3CONT" + targetColorCode + "1DY", "", true, true));
            }
        }

        // Message - You need a target. //
        else if(targetEntityString.isEmpty()
        && !targetDirection.isEmpty()
        && skill.maxDistance > 0
        && !skill.allOnly
        && !allCheck
        && !(skill.isHealing && groupCheck)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You need a target.", "4CONT5CONT2W6CONT1DY", "", true, true));
            }
        }

        // Message - You aren't targeting anyone. //
        else if(parentEntity.targetList.isEmpty()
        && targetEntityString.isEmpty()
        && targetDirection.isEmpty()
        && !skill.allOnly
        && !allCheck
        && !skill.isHealing) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You aren't targeting anyone.", "4CONT4CONT1DY2DDW10CONT6CONT1DY", "", true, true));
            }
        }

        // Message - You're out of ammo. //
        else if(skill != null
        && !skill.ammoCheck(parentEntity)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You're out of ammo.", "3CONT1DY3DW4CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Get Target Room //
        else {

            // Get Data //
            boolean targetOutOfRange = false;
            if(targetCount == -1 && !parentEntity.targetList.isEmpty() && targetEntityString.isEmpty() && targetDirection.isEmpty()) {
                TargetRoomData targetRoomData = TargetRoomData.getTargetEntityRoomFromStartRoom(parentEntity.location.room, parentEntity.targetList.get(0), skill.getMaxDistance(parentEntity));
                if(targetRoomData != null) {
                    targetCount = targetRoomData.distance;
                    targetDirection = targetRoomData.targetDirection;
                } else {
                    targetOutOfRange = true;
                }
            }
            Room targetRoom = parentEntity.location.room;
            TargetRoomData targetRoomData = null;
            if(targetCount > 0 && !targetDirection.isEmpty()) {
                ArrayList<String> directionList = new ArrayList<>();
                for(int i = 0; i < targetCount; i++) {directionList.add(targetDirection);}
                targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(parentEntity.location.room, directionList, false, false);
                targetRoom = targetRoomData.targetRoom;
            }

            // Group Entity In Target Room Check //
            boolean groupEntityInTargetRoom = false;
            for(Entity mob : parentEntity.groupList) {
                if(targetRoom.mobList.contains(mob)) {
                    groupEntityInTargetRoom = true;
                    break;
                }
            }

            // Message - That's too far away. //
            if((targetCount != -1 && targetCount > skill.getMaxDistance(parentEntity)) || targetOutOfRange) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("That's too far away.", "4CONT1DY2DW4CONT4CONT4CONT1DY", "", true, true));
                }
            }

            // Message - Your group isn't there. //
            else if(!targetDirection.isEmpty()
            && groupCheck
            && !groupEntityInTargetRoom) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("Your group isn't there.", "5CONT6CONT3CONT1DY2DDW5CONT1DY", "", true, true));
                }
            }

            // Message - There is nothing there. //
            else if(targetCount > 0
            && targetRoomData != null
            && targetRoomData.message.equals("No Exit")) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("There is nothing there.", "6CONT3CONT8CONT5CONT1DY", "", true, true));
                }
            }

            // Message - You're too close. //
            else if(skill.getMinDistance(parentEntity) > 0 && targetCount < skill.getMinDistance(parentEntity)) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You're too close.", "3CONT1DY1DW2DDW4CONT5CONT1DY", "", true, true));
                }
            }

            // Message - The door is closed. //
            else if(targetRoomData != null
            && targetRoomData.message.equals("Door Is Closed")) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("The door is closed.", "4CONT5CONT3CONT6CONT1DY", "", true, true));
                }
            }

            // Get Target Data //
            else {

                // Get Data //
                ArrayList<Entity> targetList = skill.getTargetList(targetRoom, this);

                // Message - You don't see them. //
                if(targetList.isEmpty()
                && !selfCheck
                && !allCheck
                && !targetEntityString.isEmpty()) {
                    if(parentEntity.isPlayer) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You don't see them.", "4CONT3CONT1DY2DW4CONT4CONT1DY", "", true, true));
                    }
                }

                // Message - You don't see anyone/anyone there. //
                else if(targetList.isEmpty()
                && allCheck
                && !skill.allOnly) {
                    if(parentEntity.isPlayer) {
                        if(!targetDirection.isEmpty()) {
                            GameScreen.userInterface.console.writeToConsole(new Line("You don't see anyone there.", "4CONT3CONT1DY2DW4CONT7CONT5CONT1DY", "", true, true));
                        } else {
                            GameScreen.userInterface.console.writeToConsole(new Line("You don't see anyone.", "4CONT3CONT1DY2DW4CONT6CONT1DY", "", true, true));
                        }
                    }
                }

                // Initiate //
                else {
                    parentEntity.interruptAction();

                    // parentEntity.currentAction = this;

                    if(parentEntity.isPlayer) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You prepare to " + skill.toString().toLowerCase() + "..", "4CONT8CONT3CONT" + String.valueOf(skill.toString().length()) + "CONT2DY", "", true, true));
                    }
                }
            }
        }
    }

    public void performAction() {
    }
}
