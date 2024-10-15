package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Look extends Action {
    public String targetContainerString;

    public Look(Mob parentEntity) {
        super(parentEntity);

        targetContainerString = "";
    }

    public Look() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("look", "loo", "lo", "l", "examine", "examin", "exami", "exam", "exa", "ex").contains(inputList.get(0))) {
            Look lookAction = new Look(parentEntity);

            // Look Direction //
            if(inputList.size() == 2 && Location.directionList.contains(inputList.get(1))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                lookAction.targetDirectionCount = 1;
                lookAction.actionType = "Look Direction";
            }

            // Look Direction # //
            else if(inputList.size() == 3 && Location.directionList.contains(inputList.get(1)) && Utility.isInteger(inputList.get(2))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                lookAction.targetDirectionCount = Integer.valueOf(inputList.get(2));
                lookAction.actionType = "Look Direction #";
            }

            // Look Entity Direction # //
            else if(inputList.size() >= 4 && Location.directionList.contains(inputList.get(inputList.size() - 2)) && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                lookAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 2);
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Entity Direction #";
            }

            // Look Entity Direction //
            else if(inputList.size() >= 3 && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                lookAction.targetDirectionCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 1);
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Entity Direction";
            }

            // Look Entity In Container //
            else if(inputList.size() >= 4 && inputList.contains("in") && !inputList.get(1).equals("in") && !inputList.get(inputList.size() - 1).equals("in")) {
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(1, inIndex);
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                lookAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Entity In Container";
            }

            // Look Entity //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Entity";
            }

            // Look //
            else if(inputList.size() == 1 && Arrays.asList("look", "loo", "lo", "l").contains(inputList.get(0))) {
                lookAction.actionType = "Look";
            }

            // Look Target //
            else if(inputList.size() == 1 && !parentEntity.targetList.isEmpty()) {
                lookAction.actionType = "Look Target";
            }

            else {
                userInterface.console.writeToConsole(new Line("Examine what?", "8CONT4CONT1DY", "", true, true));
                lookAction.parentEntity = null;
            }

            return lookAction;
        }

        return null;
    }

    public void initiate() {
        Room targetRoom = parentEntity.location.room;
        Entity targetEntity = null;
        Entity targetContainer = null;

        // Get Target Room And Target Entity Data //
        if(actionType.startsWith("Look Direction") || actionType.startsWith("Look Entity Direction")) {
            ArrayList<String> directionList = new ArrayList<>();
            for(int i = 0; i < targetDirectionCount; i++) {directionList.add(targetDirection);}
            TargetRoomData targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(targetRoom, directionList, false, false);
            targetRoom = targetRoomData.targetRoom;

            // Message - That's out of your range. //
            if(targetDirectionCount > parentEntity.getMaxViewDistance()) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("That's out of your range.", "4CONT1DY2DDW4CONT3CONT5CONT5CONT1DY", "", true, true));
                }
                return;
            }

            // Message - Your view to the Direction is obstructed by a door. //
            else if(targetRoomData.message.equals("Door Is Closed")) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("Your view to the " + targetRoomData.targetDirection.toLowerCase() + " is obstructed by a door.", "5CONT5CONT3CONT4CONT" + String.valueOf(targetRoomData.targetDirection.length()) + "CONT1W3CONT11CONT3CONT2W4CONT1DY", "", true, true));
                }
                return;
            }

            // Message - There is nothing there. //
            else if(targetRoomData.message.equals("No Exit")
            || targetRoomData.message.equals("Hidden Exit Is Closed")) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("There is nothing there.", "6CONT3CONT8CONT5CONT1DY", "", true, true));
                }
                return;
            }
        }

        // Look At TargetEntity In Room/Gear/Inv //
        if(targetContainerString.isEmpty()) {
            targetEntity = targetRoom.getEntityFromNameKey(targetEntityString, null);
            if(targetEntity == null) {targetEntity = parentEntity.getItemFromInventory(targetEntityString);}
            if(targetEntity == null) {targetEntity = parentEntity.getItemFromGear(targetEntityString);}
        }

        // Look At TargetEntity In TargetContainer //
        else {
            targetContainer = targetRoom.getEntityFromNameKey(targetContainerString, "Item");
            if(targetContainer == null) {targetContainer = parentEntity.getItemFromInventory(targetContainerString);}
            if(targetContainer == null) {targetContainer = parentEntity.getItemFromGear(targetContainerString);}

            if(targetContainer != null
            && targetContainer.isItem
            && ((Item) targetContainer).containerItemList != null) {
                for(Item item : ((Item) targetContainer).containerItemList) {
                    if(item.nameKeyList.contains(targetEntityString)) {
                        targetEntity = item;
                        break;
                    }
                }
            }
        }

        // Display Room //
        if(actionType.equals("Look")
        || actionType.equals("Look Direction")
        || actionType.equals("Look Direction #")) {
            if(!targetDirection.isEmpty()) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You look " + targetDirection.toLowerCase() + ".", "4CONT5CONT" + String.valueOf(targetDirection.length()) + "CONT1DY", "", true, true));
                }
            }

            targetRoom.display();
        }

        // OR Display Target Entity Description //
        else if(actionType.equals("Look Entity Direction #")
        || actionType.equals("Look Entity Direction")
        || actionType.equals("Look Entity In Container")
        || actionType.equals("Look Entity")
        || actionType.equals("Look Target")) {

            // Message - That isn't a container. //
            if(targetContainer != null
            && !(targetContainer.isItem && ((Item) targetContainer).containerItemList != null)) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("That isn't a container.", "5CONT3CONT1DY2DDW2W9CONT1DY", "", true, true));
                }
            }

            // Message - You don't see it. //
            else if(targetEntity == null) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You don't see it.", "4CONT3CONT1DY2DDW4CONT2CONT1DY", "", true, true));
                }
            }

            // Display Target Entity Look Description //
            else {
                if(parentEntity.isPlayer) {
                    String atInsideString = "at ";
                    String itContainsString = "";
                    String itContainsColorCode = "";
                    if(targetEntity.isItem
                    && ((Item) targetEntity).containerItemList != null
                    && ((Item) targetEntity).status.equals("Open")) {
                        atInsideString = "inside ";
                        itContainsString = " It contains:";
                        itContainsColorCode = "1W3CONT8CONT1DY";
                    }
                    String directionString = "";
                    String directionColorCode = "";
                    if(!targetDirection.isEmpty()) {
                        directionString = " to the " + targetDirection.toLowerCase();
                        directionColorCode = "1W3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT";
                    }

                    String inContainerString = "";
                    String inContainerColorCode = "";
                    if(targetContainer != null) {
                        inContainerString = " inside of " + targetContainer.prefix.toLowerCase() + targetContainer.name.label;
                        inContainerColorCode = "1W7CONT3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode;
                    }

                    String prefixColorCode = String.valueOf(targetEntity.prefix.length()) + "CONT";
                    if(targetEntity.isSpaceship) {prefixColorCode = "";}
                    GameScreen.userInterface.console.writeToConsole(new Line("You look " + atInsideString + targetEntity.prefix.toLowerCase() + targetEntity.name.label + inContainerString + directionString + "." + itContainsString, "4CONT5CONT" + String.valueOf(atInsideString.length()) + "CONT" + prefixColorCode + targetEntity.name.colorCode + inContainerColorCode + directionColorCode + "1DY" + itContainsColorCode, "", false, true));
                }
                targetEntity.displayLookDescription();
            }
        }
    }
}
