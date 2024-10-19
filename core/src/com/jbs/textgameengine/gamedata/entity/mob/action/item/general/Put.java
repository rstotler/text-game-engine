package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.item.type.ammo.Ammo;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Put extends Action {
    public String targetContainerString;

    public Put(Mob parentEntity) {
        super(parentEntity);

        targetContainerString = "";
    }

    public Put() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("put", "pu", "p").contains(inputList.get(0))) {
            Put putAction = new Put(parentEntity);

            // Put All Item In Container //
            if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && inputList.contains("in")
            && !inputList.get(2).equals("in")
            && !inputList.get(inputList.size() - 1).equals("in")) {
                putAction.allCheck = true;
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(2, inIndex);
                putAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Put All In Container //
            else if(inputList.size() >= 4
            && inputList.get(1).equals("all")
            && inputList.get(2).equals("in")) {
                putAction.allCheck = true;
                List<String> targetContainerStringList = inputList.subList(3, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Put # Item In Container //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && inputList.contains("in")
            && !inputList.get(2).equals("in")
            && !inputList.get(inputList.size() - 1).equals("in")) {
                putAction.targetCount = Integer.valueOf(inputList.get(1));
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(2, inIndex);
                putAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Put Item In Container //
            else if(inputList.size() >= 4
            && inputList.contains("in")
            && !inputList.get(1).equals("in")
            && !inputList.get(inputList.size() - 1).equals("in")) {
                putAction.targetCount = 1;
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(1, inIndex);
                putAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Put what in what?", "4CONT5CONT3CONT4CONT1DY", "", true, true));
                putAction.parentEntity = null;
            }

            return putAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<Item> putList = new ArrayList<>();
        Entity targetContainer = null;
        boolean multipleItemTypes = false;
        boolean wontFitCheck = false;
        boolean cantPutCheck = false;
        boolean cantPutInSelfCheck = false;
        boolean isLockedCheck = false;
        boolean openContainerCheck = false;
        boolean breakCheck = false;

        // Get Target Container //
        targetContainer = parentEntity.location.room.getEntityFromNameKey(targetContainerString, "Item");
        if(targetContainer == null || !targetContainer.isItem || ((Item) targetContainer).containerItemList == null) {
            targetContainer = parentEntity.getItemFromInventory(targetContainerString);
        }
        if(targetContainer != null && !targetContainer.isItem) {targetContainer = null;}

        // Put Item(s) In Container //
        if(targetContainer != null
        && ((Item) targetContainer).containerItemList != null) {
            for(String pocket : parentEntity.inventory.keySet()) {
                ArrayList<Integer> deleteItemIndexList = new ArrayList<>();
                for(int i = 0; i < parentEntity.inventory.get(pocket).size(); i++) {
                    Item item = parentEntity.inventory.get(pocket).get(i);

                    if((targetEntityString.isEmpty() && allCheck)
                    || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))) {
                        if(item == targetContainer) {
                            cantPutInSelfCheck = true;
                        }
                        else if(((Item) targetContainer).keyNum != -9999
                        && !parentEntity.hasKey(((Item) targetContainer).keyNum)) {
                            isLockedCheck = true;
                        }
                        else if(((Item) targetContainer).ammoTypeList != null
                        && (!item.type.equals("Ammo")
                        || !((Item) targetContainer).ammoTypeList.contains(((Ammo) item).ammoType))) {
                            cantPutCheck = true;
                        }
                        else if((((Item) targetContainer).containerCapacity != -1 && ((Item) targetContainer).containerItemList.size() >= ((Item) targetContainer).containerCapacity)
                        || (((Item) targetContainer).containerMaxWeight != -1.0f && !item.isQuantity && (item.getWeight() + ((Item) targetContainer).getWeightInContainer()) > ((Item) targetContainer).containerMaxWeight)
                        || (((Item) targetContainer).containerMaxWeight != -1.0f && item.isQuantity && (item.weight + ((Item) targetContainer).getWeightInContainer()) > ((Item) targetContainer).containerMaxWeight)) {
                            wontFitCheck = true;
                        }
                        else {

                            // Multiple Item Type Check //
                            if(!putList.isEmpty()
                            && (putList.get(putList.size() - 1).id != item.id
                            || !putList.get(putList.size() - 1).type.equals(item.type))) {
                                multipleItemTypes = true;
                            }

                            // Non-Quantity Item //
                            if(!item.isQuantity) {
                                ((Item) targetContainer).containerItemList.add(item);
                                deleteItemIndexList.add(0, i);
                                putList.add(item);
                            }

                            // Quantity Item //
                            else {
                                int itemQuantity = item.quantity;
                                if(((Item) targetContainer).containerMaxWeight != -1.0f
                                && (((Item) targetContainer).getWeightInContainer() + item.getWeight()) > ((Item) targetContainer).containerMaxWeight) {
                                    itemQuantity = (int) (((int) (((Item) targetContainer).containerMaxWeight - ((Item) targetContainer).getWeightInContainer())) / item.weight);
                                }
                                if(((Item) targetContainer).containerCapacity != -1
                                && itemQuantity > ((Item) targetContainer).containerCapacity) {
                                    itemQuantity = ((Item) targetContainer).containerCapacity;
                                }
                                int quantityRemainder = item.quantity - itemQuantity;
                                Item quantityItem = Item.load(item.type, item.id, item.location, itemQuantity);
                                ((Item) targetContainer).containerItemList.add(quantityItem);

                                if(quantityRemainder == 0) {deleteItemIndexList.add(0, i);}
                                else {item.quantity = quantityRemainder;}
                                putList.add(item);
                            }

                            if(!((Item) targetContainer).status.equals("Open")) {
                                ((Item) targetContainer).status = "Open";
                                openContainerCheck = true;
                            }

                            if(targetCount != -1
                            && putList.size() >= targetCount) {
                                breakCheck = true;
                                break;
                            }
                        }
                    }
                }

                // Remove Objects From Inventory //
                for(int deleteIndex : deleteItemIndexList) {
                    parentEntity.inventory.get(pocket).remove(deleteIndex);
                }

                if(breakCheck) {break;}
            }
        }

        // Message - That's not a container. //
        if(targetContainer != null
        && ((Item) targetContainer).containerItemList == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("That's not a container.", "4CONT1DY2DDW4CONT1W9CONT1DY", "", true, true));
            }
        }

        // Message - You can't put that in Container. //
        else if(putList.isEmpty()
        && cantPutCheck) {
            if(parentEntity.isPlayer) {
                String putString = "You can't put that in " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                String putColorCode = "4CONT3CONT1DY2DDW4CONT5CONT3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(putString, putColorCode, "", true, true));
            }
        }

        // Message - It won't fit. //
        else if(putList.isEmpty()
        && wontFitCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("It won't fit.", "3CONT3CONT1DY2DDW3CONT1DY", "", true, true));
            }
        }

        // Message - You can't put something inside itself. //
        else if(putList.isEmpty()
        && cantPutInSelfCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't put something inside itself.", "4CONT3CONT1DY2DDW4CONT10CONT7CONT6CONT1DY", "", true, true));
            }
        }

        // Message - It's locked. //
        else if(putList.isEmpty()
        && isLockedCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("It's locked.", "2CONT1DY2DDW6CONT1DY", "", true, true));
            }
        }

        // Message - You can't find it. //
        else if(targetContainer == null
        || (!targetEntityString.isEmpty() && putList.isEmpty() && !wontFitCheck && !cantPutCheck && !cantPutInSelfCheck)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You put some things in Container. //
        else if(putList.size() > 1
        && multipleItemTypes) {
            if(parentEntity.isPlayer) {
                String openString = "";
                String openColorCode = "";
                if(openContainerCheck) {
                    openString = "open and ";
                    openColorCode = "5CONT4CONT";
                }
                String putString = "You " + openString + "put some things in " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                String putColorCode = "4CONT" + openColorCode + "4CONT5CONT7CONT3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(putString, putColorCode, "", true, true));
            }
        }

        // Message - You put Item(s) in Container. //
        else if(putList.size() > 0) {
            if(parentEntity.isPlayer) {
                String openString = "";
                String openColorCode = "";
                if(openContainerCheck) {
                    openString = "open and ";
                    openColorCode = "5CONT4CONT";
                }
                String countString = "";
                String countColorCode = "";
                if(putList.size() > 1 && !multipleItemTypes) {
                    Line countLine = Utility.insertCommas(putList.size());
                    countString = " (" + countLine.label + ")";
                    countColorCode = "2DR" + countLine.colorCode + "1DR";
                }
                Item targetItem = putList.get(0);
                String putString = "You " + openString + "put " + targetItem.prefix.toLowerCase() + targetItem.name.label + countString + " in " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                String putColorCode = "4CONT" + openColorCode + "4CONT" + String.valueOf(targetItem.prefix.length()) + "CONT" + targetItem.name.colorCode + countColorCode + "1W3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(putString, putColorCode, "", true, true));
            }
        }
    }
}
