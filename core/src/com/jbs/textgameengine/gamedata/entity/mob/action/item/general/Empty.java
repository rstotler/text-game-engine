package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Empty extends Action {
    public boolean roomCheck;

    public Empty(Mob parentEntity) {
        super(parentEntity);

        roomCheck = false;
    }

    public Empty() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("empty", "empt", "emp", "em").contains(inputList.get(0))) {
            Empty emptyAction = new Empty(parentEntity);

            // Empty All Self //
            if(inputList.size() == 3
            && inputList.get(1).equals("all")
            && inputList.get(2).equals("self")) {
                emptyAction.allCheck = true;
                emptyAction.selfCheck = true;
            }

            // Empty All Container //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                emptyAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                emptyAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Empty All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                emptyAction.allCheck = true;
            }

            // Empty Container //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                emptyAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Empty what?", "6CONT4CONT1DY", "", true, true));
                emptyAction.parentEntity = null;
            }

            return emptyAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<Item> emptyList = new ArrayList<>();
        Entity targetEntity = null;
        boolean isLockedCheck = false;
        boolean isEmptyCheck = false;

        // Get Target Item Data //
        if(!targetEntityString.isEmpty()) {
            targetEntity = parentEntity.location.room.getEntityFromNameKey(targetEntityString, "Item");
            if(targetEntity == null) {
                targetEntity = parentEntity.getItemFromInventory(targetEntityString);
            }
            if(targetEntity == null) {
                targetEntity = parentEntity.getItemFromGear(targetEntityString);
            }
        }

        // Get TargetContainer(s) //
        if(true) {

            // Room //
            if(!selfCheck) {
                for(Entity item : parentEntity.location.room.itemList) {
                    if((targetEntityString.isEmpty() && allCheck)
                    || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))) {
                        targetEntity = item;
                        if(((Item) item).containerItemList != null) {
                            if(((Item) item).keyNum != -9999
                            && !parentEntity.hasKey(((Item) item).keyNum)) {
                                isLockedCheck = true;
                            }
                            else if(((Item) item).containerItemList.isEmpty()) {
                                isEmptyCheck = true;
                            }
                            else {
                                emptyList.add((Item) item);
                            }
                        }
                    }
                }
            }

            // ParentEntity Inventory //
            if(!(allCheck && targetEntityString.isEmpty())
            && !(!allCheck && !emptyList.isEmpty())) {
                for(String pocket : parentEntity.inventory.keySet()) {
                    for(Item item : parentEntity.inventory.get(pocket)) {
                        if((targetEntityString.isEmpty() && allCheck)
                        || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))) {
                            targetEntity = item;
                            if(item.containerItemList != null) {
                                if(item.keyNum != -9999
                                && !parentEntity.hasKey(item.keyNum)) {
                                    isLockedCheck = true;
                                }
                                else if(item.containerItemList.isEmpty()) {
                                    isEmptyCheck = true;
                                }
                                else {
                                    emptyList.add((Item) item);
                                }
                            }
                        }
                    }
                }
            }

            // ParentEntity Gear //
            if(!(allCheck && targetEntityString.isEmpty())
            && !(!allCheck && !emptyList.isEmpty())) {
                for(String gearSlot : parentEntity.gear.keySet()) {
                    Item item = parentEntity.gear.get(gearSlot);
                    if(item != null) {
                        if((targetEntityString.isEmpty() && allCheck)
                        || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))) {
                            targetEntity = item;
                            if(item.containerItemList != null) {
                                if(item.keyNum != -9999
                                && !parentEntity.hasKey(item.keyNum)) {
                                    isLockedCheck = true;
                                }
                                else if(item.containerItemList.isEmpty()) {
                                    isEmptyCheck = true;
                                }
                                else {
                                    emptyList.add((Item) item);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Empty TargetContainer(s) //
        for(Item container : emptyList) {
            for(Item containerItem : container.containerItemList) {
                parentEntity.location.room.addItemToRoom(containerItem);
            }
            container.containerItemList.clear();
        }

        // Message - You can't empty that. //
        if(!targetEntityString.isEmpty()
        && targetEntity != null
        && emptyList.isEmpty()
        && !isLockedCheck) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You can't empty that.", "4CONT3CONT1DY2DDW6CONT4CONT1DY", "", true, true));
            }
        }

        // Message - There is nothing to empty. //
        else if(allCheck
        && emptyList.isEmpty()
        && !isLockedCheck) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("There is nothing to empty.", "6CONT3CONT8CONT3CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You can't find it. //
        else if(!targetEntityString.isEmpty()
        && targetEntity == null
        && emptyList.isEmpty()) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - It's locked. //
        else if(!targetEntityString.isEmpty()
        && emptyList.isEmpty()
        && isLockedCheck) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("It's locked.", "2CONT1DY2DDW6CONT1DY", "", true, true));
            }
        }

        // Message - It's empty. //
        else if(!targetEntityString.isEmpty()
        && emptyList.isEmpty()
        && isEmptyCheck) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("It's empty.", "2CONT1DY2DDW5CONT1DY", "", true, true));
            }
        }

        // Message - You empty some containers onto the floor. //
        else if(emptyList.size() >= 2) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You empty some containers onto the floor.", "4CONT6CONT5CONT11CONT5CONT4CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You empty Item onto the floor. //
        else if(emptyList.size() == 1) {
            if(parentEntity.isPlayer) {
                Item targetContainer = emptyList.get(0);
                String emptyString = "You empty " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + " onto the floor.";
                String emptyColorCode = "4CONT6CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1W5CONT4CONT5CONT1DY";
                userInterface.console.writeToConsole(new Line(emptyString, emptyColorCode, "", true, true));
            }
        }
    }
}
