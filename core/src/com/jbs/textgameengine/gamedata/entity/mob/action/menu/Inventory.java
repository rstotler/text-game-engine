package com.jbs.textgameengine.gamedata.entity.mob.action.menu;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Inventory extends Action {
    public Inventory(Mob parentEntity) {
        super(parentEntity);
    }

    public Inventory() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));
        HashMap<String, ArrayList<String>> inventoryNameKeyMap = new HashMap<>();
        inventoryNameKeyMap.put("General", new ArrayList<>(Arrays.asList("inventory", "inventor", "invento", "invent", "inven", "inve", "inv", "in", "i", "general", "genera", "gener", "gene", "gen", "ge", "g")));

        Inventory actionInventory = new Inventory(parentEntity);

        String targetInventoryPocket = "";
        for(String key : inventoryNameKeyMap.keySet()) {
            if((inputList.size() == 1 && inventoryNameKeyMap.get(key).contains(inputList.get(0)) && (inputList.get(0).length() >= 3 || inputList.get(0).substring(0, 1).equals("i")))
            || (inputList.size() >= 2 && inventoryNameKeyMap.get(key).contains(inputList.get(1)))) {
                targetInventoryPocket = key;
                break;
            }
        }

        if(!targetInventoryPocket.isEmpty()) {
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

                if(itemLineMap.containsKey(item.name.label)) {
                    int count = itemLineMap.get(item.name.label);
                    itemLineMap.put(item.name.label, count + 1);
                }
                else {
                    itemLineMap.put(item.name.label, 1);
                }

                if(i == parentEntity.inventory.get(actionType).size() - 1) {
                    lastLineItem = item;
                }
            }

            GameScreen.userInterface.console.writeToConsole(new Line( actionType+ " Item Bag:", String.valueOf(actionType.length()) + "CONT1W5CONT3CONT1DY", "", false, true));

            boolean isLastLine = false;
            ArrayList<String> displayedLines = new ArrayList<>();
            for(Entity item : parentEntity.inventory.get(actionType)) {
                if(!displayedLines.contains(item.name.label)) {
                    displayedLines.add(item.name.label);

                    if(item.name.label.equals(lastLineItem.name.label)) {
                        isLastLine = true;
                    }

                    String countString = "";
                    String countColorCode = "";
                    if(itemLineMap.containsKey(item.name.label)
                    && itemLineMap.get(item.name.label) > 1) {
                        countString = " (" + String.valueOf(itemLineMap.get(item.name.label)) + ")";
                        countColorCode = "2DR" + String.valueOf(itemLineMap.get(item.name.label)).length() + "DW1DR";
                    }
                    String itemString = item.prefix + item.name.label + countString;
                    String itemColorCode = String.valueOf(item.prefix).length() + "CONT" + item.name.colorCode + countColorCode;
                    GameScreen.userInterface.console.writeToConsole(new Line(itemString, itemColorCode, "", isLastLine, true));
                }
            }
        }
    }
}
