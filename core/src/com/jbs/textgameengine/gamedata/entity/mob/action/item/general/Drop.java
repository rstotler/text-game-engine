package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.action.menu.Inventory;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Drop extends Action {
    public String targetPocket;

    public Drop(Mob parentEntity) {
        super(parentEntity);

        targetPocket = "";
    }

    public Drop() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("drop", "dro", "dr").contains(inputList.get(0))) {
            Drop dropAction = new Drop(parentEntity);

            String targetPocketString = "";
            if(inputList.size() == 3
            && inputList.get(1).equals("all")) {
                List<String> targetPocketStringList = inputList.subList(2, inputList.size());
                targetPocketString = targetPocketStringList.stream().collect(Collectors.joining(" "));
                targetPocketString = targetPocketString.substring(0, 1).toUpperCase() + targetPocketString.substring(1, targetPocketString.length()).toLowerCase();
            }

            // Drop All //
            if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                dropAction.allCheck = true;
            }

            // Drop All Pocket //
            else if(inputList.size() == 3
            && inputList.get(1).equals("all")
            && Inventory.inventoryNameKeyMap.containsKey(targetPocketString)) {
                dropAction.allCheck = true;
                dropAction.actionType = "Drop All Pocket";
                dropAction.targetPocket = targetPocketString;
            }

            // Drop All Item //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                dropAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                dropAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Drop # Item //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                dropAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                dropAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Drop Item //
            else if(inputList.size() >= 2) {
                dropAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                dropAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Drop what?", "5CONT4CONT1DY", "", true, true));
                dropAction.parentEntity = null;
            }

            return dropAction;
        }

        return null;
    }

    public void initiate() {
        int dropCount = 0;
        int inventoryItemCount = 0;
        Entity targetItem = null;
        boolean multipleItemTypes = false;
        boolean breakCheck = false;
        HashMap<String, ArrayList<Integer>> deleteIndexMap = new HashMap<>();

        // Get Inventory Item Count //
        for(String pocket : parentEntity.inventory.keySet()) {
            for(int i = 0; i < parentEntity.inventory.get(pocket).size(); i++) {
                Entity item = parentEntity.inventory.get(pocket).get(i);
                if(((Item) item).isQuantity) {inventoryItemCount += ((Item) item).quantity;}
                else {inventoryItemCount += 1;}
            }
        }

        // Drop Item(s) //
        for(String pocket : parentEntity.inventory.keySet()) {
            deleteIndexMap.put(pocket, new ArrayList<Integer>());

            for(int i = 0; i < parentEntity.inventory.get(pocket).size(); i++) {
                Entity item = parentEntity.inventory.get(pocket).get(i);

                if((allCheck && targetEntityString.isEmpty() && actionType.isEmpty())
                || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))
                || (actionType.equals("Drop All Pocket") && pocket.equals(targetPocket))) {

                    // Non-Quantity Item //
                    if(!((Item) item).isQuantity) {
                        parentEntity.location.room.addItemToRoom(item);
                        deleteIndexMap.get(pocket).add(0, i);
                        dropCount += 1;
                    }

                    // Quantity Item //
                    else {
                        int itemQuantity = ((Item) item).quantity;
                        int quantityRemainder = 0;
                        if(targetCount != -1 && itemQuantity + dropCount > targetCount) {
                            quantityRemainder = (itemQuantity + dropCount) - targetCount;
                            itemQuantity -= quantityRemainder;
                        }
                        Entity quantityItem = Item.load(((Item) item).type, item.id, item.location, itemQuantity);
                        parentEntity.location.room.addItemToRoom(quantityItem);

                        if(quantityRemainder == 0) {deleteIndexMap.get(pocket).add(0, i);}
                        else {((Item) item).quantity = quantityRemainder;}
                        dropCount += itemQuantity;
                    }

                    if(targetItem == null) {targetItem = item;}
                    else if(targetItem.id != item.id) {multipleItemTypes = true;}

                    if(targetCount != -1 && dropCount >= targetCount) {
                        breakCheck = true;
                        break;
                    }
                }
            }
            if(breakCheck) {break;}
        }

        // Delete Items From Inventory //
        for(String pocket : deleteIndexMap.keySet()) {
            for(int deleteIndex : deleteIndexMap.get(pocket)) {
                parentEntity.inventory.get(pocket).remove(deleteIndex);
            }
        }

        // Message - You aren't carrying anything. //
        if(inventoryItemCount == 0) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You aren't carrying anything.", "4CONT4CONT1DY2DDW9CONT8CONT1DY", "", true, true));
            }
        }

        // Message - You can't find it. //
        else if(dropCount == 0) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You drop everything on the ground. //
        else if(dropCount > 1
        && dropCount == inventoryItemCount) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You drop everything on the ground.", "4CONT5CONT11CONT3CONT4CONT6CONT1DY", "", true, true));
            }
        }

        // Message - You drop some things on the ground. //
        else if(targetItem != null && multipleItemTypes) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You drop some things on the ground.", "4CONT5CONT5CONT7CONT3CONT4CONT6CONT1DY", "", true, true));
            }
        }

        // Message - You drop Entity on the ground. //
        else if(targetItem != null) {
            if(parentEntity.isPlayer) {
                String itemCountString = "";
                String itemCountColorCode = "";
                if(dropCount > 1) {
                    Line itemCountLine = Utility.insertCommas(dropCount);
                    itemCountString = " (" + itemCountLine.label + ")";
                    itemCountColorCode = "2DR" + itemCountLine.colorCode + "1DR";
                }
                userInterface.console.writeToConsole(new Line("You drop " + targetItem.prefix.toLowerCase() + targetItem.name.label + itemCountString + " on the ground.", "4CONT5CONT" + String.valueOf(targetItem.prefix.length()) + "CONT" + targetItem.name.colorCode + itemCountColorCode + "1W3CONT4CONT6CONT1DY", "", true, true));
            }
        }
    }
}
