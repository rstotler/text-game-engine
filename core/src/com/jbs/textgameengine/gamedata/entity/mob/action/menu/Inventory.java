package com.jbs.textgameengine.gamedata.entity.mob.action.menu;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Inventory extends Action {
    public static HashMap<String, ArrayList<String>> inventoryNameKeyMap = loadInventoryNameKeyMap();

    public static HashMap<String, ArrayList<String>> loadInventoryNameKeyMap() {
        HashMap<String, ArrayList<String>> inventoryNameKeyMap = new HashMap<>();
        inventoryNameKeyMap.put("General", new ArrayList<>(Arrays.asList("inventory", "inventor", "invento", "invent", "inven", "inve", "inv", "in", "i", "general", "genera", "gener", "gene", "gen", "ge", "g")));
        inventoryNameKeyMap.put("Gear", new ArrayList<>(Arrays.asList("gear", "gea")));
        inventoryNameKeyMap.put("Weapons", new ArrayList<>(Arrays.asList("weapons", "weapon", "weapo", "weap")));
        inventoryNameKeyMap.put("Ammo", new ArrayList<>(Arrays.asList("ammo", "amm")));
        inventoryNameKeyMap.put("Food", new ArrayList<>(Arrays.asList("food", "foo")));
        inventoryNameKeyMap.put("Organic", new ArrayList<>(Arrays.asList("organic", "organi", "organ", "orga", "org")));

        return inventoryNameKeyMap;
    }

    public Inventory(Mob parentEntity) {
        super(parentEntity);
    }

    public Inventory() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));
        Inventory actionInventory = new Inventory(parentEntity);

        String targetInventoryPocket = "";
        for(String key : inventoryNameKeyMap.keySet()) {
            if((inputList.size() == 1 && inventoryNameKeyMap.get(key).contains(inputList.get(0)) && (inputList.get(0).length() >= 3 || inputList.get(0).substring(0, 1).equals("i")))
            || (inputList.size() >= 2 && inventoryNameKeyMap.get(key).contains(inputList.get(1)))) {
                targetInventoryPocket = key;
                break;
            }
        }

        if(inputList.size() == 1 && !targetInventoryPocket.isEmpty()) {
            actionInventory.actionType = targetInventoryPocket;
        }

        // Message - That is not an inventory pocket type. //
        else if(inputList.size() == 2
        && inventoryNameKeyMap.get("General").contains(inputList.get(0))) {
            actionInventory.parentEntity = null;
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("That is not an inventory pocket type.", "5CONT3CONT4CONT3CONT10CONT7CONT4CONT1DY", "", true, true));
            }
        }

        else {
            actionInventory = null;
        }

        return actionInventory;
    }

    public void initiate() {
        if(parentEntity.inventory.containsKey(actionType)) {
            HashMap<String, Integer> itemLineMap = new HashMap<>();
            Entity lastLineItem = null;

            for(int i = 0; i < parentEntity.inventory.get(actionType).size(); i++) {
                Entity item = parentEntity.inventory.get(actionType).get(i);
                Line itemNameMod = item.getNameMod();

                if(itemLineMap.containsKey(item.name.label + itemNameMod.label)) {
                    int count = itemLineMap.get(item.name.label + itemNameMod.label);
                    itemLineMap.put(item.name.label + itemNameMod.label, count + 1);
                }
                else {
                    itemLineMap.put(item.name.label + itemNameMod.label, 1);
                    lastLineItem = item;
                }
            }

            GameScreen.userInterface.console.writeToConsole(new Line( actionType+ " Item Bag:", String.valueOf(actionType.length()) + "CONT1W5CONT3CONT1DY", "", false, true));

            int inventoryItemCount = 0;
            boolean isLastLine = false;
            ArrayList<String> displayedLines = new ArrayList<>();
            for(Entity item : parentEntity.inventory.get(actionType)) {
                inventoryItemCount += 1;
                Line itemNameMod = item.getNameMod();

                if(!displayedLines.contains(item.name.label + itemNameMod.label)) {
                    displayedLines.add(item.name.label + itemNameMod.label);

                    if(item == lastLineItem) {
                        isLastLine = true;
                    }

                    String countString = "";
                    String countColorCode = "";
                    if(itemLineMap.containsKey(item.name.label + itemNameMod.label)
                    && itemLineMap.get(item.name.label + itemNameMod.label) > 1) {
                        Line countLine = Utility.insertCommas(itemLineMap.get(item.name.label + itemNameMod.label));
                        countString = " (" + countLine.label + ")";
                        countColorCode = "2DR" + countLine.colorCode + "1DR";
                    }
                    else if(((Item) item).isQuantity && ((Item) item).quantity > 1) {
                        Line countLine = Utility.insertCommas(((Item) item).quantity);
                        countString = " (" + countLine.label + ")";
                        countColorCode = "2DR" + countLine.colorCode + "1DR";
                    }

                    String itemString = item.prefix + item.name.label + itemNameMod.label + countString;
                    String itemColorCode = String.valueOf(item.prefix).length() + "CONT" + item.name.colorCode + itemNameMod.colorCode + countColorCode;
                    GameScreen.userInterface.console.writeToConsole(new Line(itemString, itemColorCode, "", isLastLine, true));
                }
            }

            if(inventoryItemCount == 0) {
                GameScreen.userInterface.console.writeToConsole(new Line( "-Empty", "1DY5CONT", "", true, true));
            }
        }
    }
}
