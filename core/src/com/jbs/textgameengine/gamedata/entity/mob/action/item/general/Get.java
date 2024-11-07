package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Get extends Action {
    public String targetContainerString;
    public boolean containerAllCheck;

    public Get(Mob parentEntity) {
        super(parentEntity);

        targetContainerString = "";
        containerAllCheck = false;
    }

    public Get() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("get", "ge", "g").contains(inputList.get(0))) {
            Get getAction = new Get(parentEntity);

            // Get All Item From All (Loot Item From All) //
            if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && inputList.get(inputList.size() - 2).equals("from")
            && inputList.get(inputList.size() - 1).equals("all")) {
                getAction.allCheck = true;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                getAction.containerAllCheck = true;
            }

            // Get All Item From Container (Loot Item From Container) //
            else if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && inputList.contains("from")
            && !inputList.get(2).equals("from")
            && !inputList.get(inputList.size() - 1).equals("from")) {
                getAction.allCheck = true;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get All From All (Loot All) //
            else if(inputList.size() == 4
            && inputList.get(1).equals("all")
            && inputList.get(2).equals("from")
            && inputList.get(3).equals("all")) {
                getAction.allCheck = true;
                getAction.containerAllCheck = true;
            }

            // Get All From Container (Loot Container) //
            else if(inputList.size() >= 4
            && inputList.get(1).equals("all")
            && inputList.get(2).equals("from")) {
                getAction.allCheck = true;
                int fromIndex = inputList.indexOf("from");
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get # Item From All //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && inputList.get(inputList.size() - 2).equals("from")
            && inputList.get(inputList.size() - 1).equals("all")) {
                getAction.targetCount = Integer.valueOf(inputList.get(1));
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                getAction.containerAllCheck = true;
            }

            // Get # Item From Container //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && inputList.contains("from")
            && !inputList.get(2).equals("from")
            && !inputList.get(inputList.size() - 1).equals("from")) {
                getAction.targetCount = Integer.valueOf(inputList.get(1));
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get Item From All //
            else if(inputList.size() >= 4
            && inputList.get(inputList.size() - 2).equals("from")
            && inputList.get(inputList.size() - 1).equals("all")) {
                getAction.targetCount = 1;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(1, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                getAction.containerAllCheck = true;
            }

            // Get Item From Container //
            else if(inputList.size() >= 4
            && inputList.contains("from")
            && !inputList.get(1).equals("from")
            && !inputList.get(inputList.size() - 1).equals("from")) {
                getAction.targetCount = 1;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(1, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                getAction.allCheck = true;
            }

            // Get All Item //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                getAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Get # Item //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                getAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Get Item //
            else if(inputList.size() >= 2) {
                getAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Get what?", "4CONT4CONT1DY", "", true, true));
                getAction.parentEntity = null;
            }

            return getAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<ItemListData> itemListDataArray = new ArrayList<>();
        Entity targetContainer = null;
        boolean lockedCheck = false;

        // Get Target Container & Item List Data //
        if(true) {

            // Get Item From Room //
            if(targetContainerString.isEmpty() && !containerAllCheck) {
                itemListDataArray.add(new ItemListData("Get Item From Room", null, null));
            }

            // Get Item From Container //
            else if(!targetContainerString.isEmpty()) {
                targetContainer = parentEntity.location.room.getEntityFromNameKey(targetContainerString, "Item");
                String containerLocation = "Room";

                if(targetContainer == null || !targetContainer.isItem || ((Item) targetContainer).containerItemList == null) {
                    Item parentEntityItem = parentEntity.getItemFromInventory(targetContainerString);
                    containerLocation = "Inventory";
                    if(parentEntityItem == null || parentEntityItem.containerItemList == null) {
                        parentEntityItem = parentEntity.getItemFromGear(targetContainerString);
                        containerLocation = "Gear";
                    }

                    if(parentEntityItem != null
                    && parentEntityItem.containerItemList != null) {
                        targetContainer = parentEntityItem;
                    }
                }

                if(targetContainer != null
                && targetContainer.isItem
                && ((Item) targetContainer).containerItemList != null) {
                    if(((Item) targetContainer).status.equals("Locked")
                    && !parentEntity.hasKey(((Item) targetContainer).keyNum)) {
                        lockedCheck = true;
                    }
                    else {
                        ArrayList<Item> targetContainerItemList = ((Item) targetContainer).containerItemList;
                        itemListDataArray.add(new ItemListData(containerLocation, (Item) targetContainer, targetContainerItemList));
                    }
                }
            }

            // Get Item From All //
            else if(containerAllCheck) {
                for(Entity item : parentEntity.location.room.itemList) {
                    if(((Item) item).containerItemList != null
                    && ((Item) item).status.equals("Locked")
                    && !parentEntity.hasKey(((Item) item).keyNum)) {
                        lockedCheck = true;
                    }
                    else if(((Item) item).containerItemList != null) {
                        itemListDataArray.add(new ItemListData("Room", (Item) item, ((Item) item).containerItemList));
                    }
                }
                for(String pocket : parentEntity.inventory.keySet()) {
                    for(Item item : parentEntity.inventory.get(pocket)) {
                        if(item.containerItemList != null
                        && item.status.equals("Locked")
                        && !parentEntity.hasKey(item.keyNum)) {
                            lockedCheck = true;
                        }
                        else if(item.containerItemList != null) {
                            itemListDataArray.add(new ItemListData("Inventory", item, item.containerItemList));
                        }
                    }
                }
                for(String gearSlot : parentEntity.gear.keySet()) {
                    Item item = parentEntity.gear.get(gearSlot);
                    if(item != null) {
                        if(item.containerItemList != null
                        && item.status.equals("Locked")
                        && !parentEntity.hasKey(item.keyNum)) {
                            lockedCheck = true;
                        }
                        else if(item.containerItemList != null) {
                            itemListDataArray.add(new ItemListData("Gear", item, item.containerItemList));
                        }
                    }
                }
            }
        }

        // Message - That's not a container. //
        if(targetContainer != null
        && (!targetContainer.isItem || ((Item) targetContainer).containerItemList == null)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("That's not a container.", "4CONT1DY2DDW4CONT2W9CONT1DY", "", true, true));
            }
        }

        // Get //
        else {
            int getCount = 0;
            Entity targetItem = null;
            boolean multipleItemTypes = false;
            ArrayList<ArrayList<Item>> usedContainerList = new ArrayList<>();
            boolean overweightCheck = false;
            boolean cantGetCheck = false;
            boolean breakCheck = false;
            boolean unlockContainerCheck = false;
            boolean openContainerCheck = false;
            float parentEntityCurrentWeight = parentEntity.getWeight();

            // Get Target Item(s) //
            for(ItemListData itemListData : itemListDataArray) {
                ArrayList<Integer> getIndexList = new ArrayList<>();

                int itemListSize = 0;
                if(itemListData.itemListLocation.equals("Get Item From Room")) {
                    itemListSize = parentEntity.location.room.itemList.size();
                } else {
                    itemListSize = itemListData.itemList.size();
                }

                for(int i = 0; i < itemListSize; i++) {
                    Item item = null;
                    if(!itemListData.itemListLocation.equals("Get Item From Room")) {item = itemListData.itemList.get(i);}
                    else {item = (Item) parentEntity.location.room.itemList.get(i);}

                    if((targetEntityString.isEmpty() && allCheck)
                    || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))) {
                        if((itemListData.itemListLocation.equals("Room") || itemListData.itemListLocation.equals("Get Item From Room"))
                        && !item.isQuantity
                        && parentEntityCurrentWeight + item.getWeight() > parentEntity.getMaxWeight()) {
                            overweightCheck = true;
                        }
                        else if((itemListData.itemListLocation.equals("Room") || itemListData.itemListLocation.equals("Get Item From Room"))
                        && item.isQuantity
                        && parentEntityCurrentWeight + item.weight > parentEntity.getMaxWeight()) {
                            overweightCheck = true;
                        }
                        else if(itemListData.itemListLocation.equals("Get Item From Room")
                        && item.noGet) {
                            cantGetCheck = true;
                        }
                        else {

                            // Non-Quantity Items //
                            if(!item.isQuantity) {
                                parentEntity.addItemToInventory(item);
                                getIndexList.add(0, i);
                                getCount += 1;

                                if((itemListData.itemListLocation.equals("Room") || itemListData.itemListLocation.equals("Get Item From Room"))) {
                                    parentEntityCurrentWeight += item.getWeight();
                                }
                            }

                            // Quantity Items //
                            else {
                                int itemQuantity = item.quantity;
                                int quantityRemainder = 0;
                                if(parentEntityCurrentWeight + item.getWeight() > parentEntity.getMaxWeight()) {
                                    itemQuantity = ((int) (parentEntity.getMaxWeight() - parentEntityCurrentWeight)) / (int) item.weight;
                                    quantityRemainder = item.quantity - itemQuantity;
                                }

                                Item quantityItem = Item.load(item.type, item.id, item.location, itemQuantity);
                                parentEntity.addItemToInventory(quantityItem);

                                if(quantityRemainder == 0) {getIndexList.add(0, i);}
                                else {item.quantity = quantityRemainder;}
                                getCount += itemQuantity;

                                if((itemListData.itemListLocation.equals("Room") || itemListData.itemListLocation.equals("Get Item From Room"))) {
                                    parentEntityCurrentWeight += quantityItem.getWeight();
                                }
                            }

                            if(targetItem == null) {targetItem = item;}
                            else if(targetItem.id != item.id || !((Item) targetItem).type.equals(item.type)) {multipleItemTypes = true;}

                            if(targetCount != -1 && getCount >= targetCount) {
                                breakCheck = true;
                                break;
                            }
                        }
                    }
                }

                // Update Used Container List //
                if(itemListData.itemList != null
                && itemListData.parentItem != null && itemListData.parentItem.containerItemList != null) {
                    usedContainerList.add(itemListData.itemList);
                }

                // Remove Items From Room/Target Containers //
                for(int deleteIndex : getIndexList) {
                    if(itemListData.itemListLocation.equals("Get Item From Room")) {
                        parentEntity.location.room.itemList.remove(deleteIndex);
                    } else {
                        itemListData.itemList.remove(deleteIndex);
                    }
                }

                // Set Unlock/Open Checks //
                if(!getIndexList.isEmpty() && itemListData.parentItem != null) {
                    if(itemListData.parentItem.status.equals("Locked")) {
                        itemListData.parentItem.status = "Open";
                        unlockContainerCheck = true;
                    }
                    else if(itemListData.parentItem.status.equals("Closed")) {
                        itemListData.parentItem.status = "Open";
                        openContainerCheck = true;
                    }
                }

                if(breakCheck) {break;}
            }

            // Message - It's locked. //
            if((getCount == 0 && lockedCheck)
            || (targetContainer != null
            && ((Item) targetContainer).status.equals("Locked")
            && !parentEntity.hasKey(((Item) targetContainer).keyNum))) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("It's locked.", "2CONT1DY2DDW6CONT1DY", "", true, true));
                }
            }

            // Message - It's empty. //
            else if(getCount == 0
            && targetContainer != null
            && ((Item) targetContainer).containerItemList.isEmpty()) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("It's empty.", "2CONT1DY2DDW5CONT1DY", "", true, true));
                }
            }

            // Message - There is nothing to loot. //
            else if(getCount == 0
            && containerAllCheck) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("There is nothing to loot.", "6CONT3CONT8CONT3CONT4CONT1DY", "", true, true));
                }
            }

            // Message - You can't carry that much weight. //
            else if(getCount == 0
            && overweightCheck) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You can't carry that much weight.", "4CONT3CONT1DY2DDW6CONT5CONT5CONT6CONT1DY", "", true, true));
                }
            }

            // Message - You can't pick that up. //
            else if(getCount == 0
            && cantGetCheck) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You can't pick that up.", "4CONT3CONT1DY2DDW5CONT5CONT2CONT1DY", "", true, true));
                }
            }

            // Message - You don't see it. (Don't see TargetContainer OR TargetItem in Room) //
            else if((!targetContainerString.isEmpty() && targetContainer == null)
            || (targetContainerString.isEmpty() && !containerAllCheck && !targetEntityString.isEmpty() && targetItem == null)) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You don't see it.", "4CONT3CONT1DY2DDW4CONT2CONT1DY", "", true, true));
                }
            }

            // Message - You can't find it. (Can't find TargetItem in TargetContainer) //
            else if(!targetEntityString.isEmpty()
            && targetItem == null) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
                }
            }

            // Message - There is nothing to get. //
            else if(getCount == 0
            && parentEntity.location.room.itemList.isEmpty()) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("There is nothing to get.", "6CONT3CONT8CONT3CONT3CONT1DY", "", true, true));
                }
            }

            // Message - You loot every corner of the place. //
            else if(getCount > 1
            && containerAllCheck
            && (allCheck || usedContainerList.size() > 1)
            && !overweightCheck
            && targetEntityString.isEmpty()) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You loot every corner of the place.", "4CONT5CONT6CONT7CONT3CONT4CONT5CONT1DY", "", true, true));
                }
            }

            // Message - You loot the place. //
            else if(getCount > 1
            && containerAllCheck
            && (allCheck || usedContainerList.size() > 1)) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You loot the place.", "4CONT5CONT4CONT5CONT1DY", "", true, true));
                }
            }

            // Message - You pick everything up. //
            else if(targetContainerString.isEmpty()
            && !containerAllCheck
            && multipleItemTypes
            && parentEntity.location.room.itemList.isEmpty()) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You pick everything up from the ground.", "4CONT5CONT11CONT3CONT5CONT4CONT6CONT1DY", "", true, true));
                }
            }

            // Message - You pick some things up. //
            else if(targetContainerString.isEmpty()
            && !containerAllCheck
            && multipleItemTypes) {
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You pick some things up.", "4CONT5CONT5CONT7CONT2CONT1DY", "", true, true));
                }
            }

            // Message - You pick up Entity. //
            else if(targetContainerString.isEmpty()
            && !containerAllCheck
            && getCount > 0
            && targetItem != null
            && !multipleItemTypes) {
                if(parentEntity.isPlayer) {
                    String countString = "";
                    String countColorCode = "";
                    if(getCount > 1) {
                        Line countLine = Utility.insertCommas(getCount);
                        countString = " (" + countLine.label + ")";
                        countColorCode = "2DR" + countLine.colorCode + "1DR";
                    }
                    String getString = "You pick up " + targetItem.prefix.toLowerCase() + targetItem.name.label + countString + ".";
                    String getColorCode = "4CONT5CONT3CONT" + String.valueOf(targetItem.prefix.length()) + "CONT" + targetItem.name.colorCode + countColorCode + "1DY";
                    GameScreen.userInterface.console.writeToConsole(new Line(getString, getColorCode, "", true, true));
                }
            }

            // Message - You get everything/some things out of Entity. //
            else if(targetContainer != null
            && usedContainerList.size() == 1
            && multipleItemTypes) {
                if(parentEntity.isPlayer) {
                    if(unlockContainerCheck) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You unlock and open " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".", "4CONT7CONT4CONT5CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY", "", false, true));
                    } else if(openContainerCheck) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You open " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".", "4CONT5CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY", "", false, true));
                    }

                    String someThingsString = "some things ";
                    if(((Item) targetContainer).containerItemList.isEmpty()) {
                        someThingsString = "everything ";
                    }
                    String getString = "You get " + someThingsString + "out of " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                    String getColorCode = "4CONT4CONT" + String.valueOf(someThingsString.length()) + "CONT4CONT3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                    GameScreen.userInterface.console.writeToConsole(new Line(getString, getColorCode, "", true, true));
                }
            }

            // Message - You get Entity from Entity. //
            else if(targetItem != null
            && targetContainer != null
            && usedContainerList.size() == 1) {
                if(parentEntity.isPlayer) {
                    if(unlockContainerCheck) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You unlock and open " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".", "4CONT7CONT4CONT5CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY", "", true, true));
                    } else if(openContainerCheck) {
                        GameScreen.userInterface.console.writeToConsole(new Line("You open " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".", "4CONT5CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY", "", true, true));
                    }

                    String countString = "";
                    String countColorCode = "";
                    if(getCount > 1) {
                        Line countLine = Utility.insertCommas(getCount);
                        countString = " (" + countLine.label + ")";
                        countColorCode = "2DR" + countLine.colorCode + "1DR";
                    }
                    String getString = "You get " + targetItem.prefix.toLowerCase() + targetItem.name.label + countString + " from " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                    String getColorCode = "4CONT4CONT" + String.valueOf(targetItem.prefix.length()) + "CONT" + targetItem.name.colorCode + countColorCode + "1W5CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                    GameScreen.userInterface.console.writeToConsole(new Line(getString, getColorCode, "", true, true));
                }
            }
        }
    }
}

class ItemListData {
    public String itemListLocation;
    public Item parentItem;
    public ArrayList<Item> itemList;

    public ItemListData(String itemListLocation, Item parentItem, ArrayList<Item> itemList) {
        this.itemListLocation = itemListLocation;
        this.parentItem = parentItem;
        this.itemList = itemList;
    }
}
