package com.jbs.textgameengine.gamedata.entity.item;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.type.Drink;
import com.jbs.textgameengine.gamedata.entity.item.type.Food;
import com.jbs.textgameengine.gamedata.entity.item.type.Gear;
import com.jbs.textgameengine.gamedata.entity.item.type.Plant;
import com.jbs.textgameengine.gamedata.entity.item.type.ammo.Ammo;
import com.jbs.textgameengine.gamedata.entity.item.type.weapon.Firearm;
import com.jbs.textgameengine.gamedata.entity.item.type.weapon.Throwable;
import com.jbs.textgameengine.gamedata.entity.item.type.weapon.Weapon;
import com.jbs.textgameengine.gamedata.entity.item.type.ammo.Magazine;
import com.jbs.textgameengine.gamedata.entity.item.type.ammo.Quiver;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Item extends Entity {
    public String type;
    public String pocket;
    public float weight;

    public String status;
    public int keyNum;

    public boolean isWeapon;

    public int quantity;
    public boolean isQuantity;
    public boolean noGet;

    public ArrayList<Integer> keyList;
    public ArrayList<Item> containerItemList;
    public ArrayList<String> ammoTypeList;
    public int containerCapacity;
    public float containerMaxWeight;

    public Item(int id, Location startLocation) {
        super(id, startLocation);
        isItem = true;

        type = "General";
        pocket = "General";
        weight = 1.0f;

        status = "";
        keyNum = -9999;

        isWeapon = false;

        quantity = -1;
        isQuantity = false;
        noGet = false;

        keyList = null;
        containerItemList = null;
        ammoTypeList = null;
        containerCapacity = -1;
        containerMaxWeight = -1.0f;
    }

    public float getWeight() {
        if(isQuantity) {
            return weight * quantity;
        }
        if(containerItemList != null) {
            float containerWeight = weight;
            for(Item item : containerItemList) {
                containerWeight += item.getWeight();
            }
            return containerWeight;
        }
        return weight;
    }

    public float getWeightInContainer() {
        float weightInContainer = 0.0f;
        if(containerItemList != null) {
            for(Item item : containerItemList) {
                weightInContainer += item.getWeight();
            }
        }

        return weightInContainer;
    }

    public void displayLookDescription(int lookFromDistance) {
        if(containerItemList == null
        || lookFromDistance > 0) {
            super.displayLookDescription(lookFromDistance);
        }

        // Container Item (Display Container Item List) //
        else {
            if(status.equals("Closed") || status.equals("Locked")) {
                GameScreen.userInterface.console.writeToConsole(new Line("It's closed.", "2CONT1DY2DDW6CONT1DY", "", true, true));
            }
            else if(containerItemList.isEmpty()) {
                GameScreen.userInterface.console.writeToConsole(new Line("It's empty.", "2CONT1DY2DDW5CONT1DY", "", true, true));
            }
            else {
                HashMap<String, Integer> itemNameMap = new HashMap<>();
                Item lastLineItem = null;

                for(Item item : containerItemList) {
                    Line itemNameMod = item.getNameMod();

                    if(itemNameMap.containsKey(item.name.label + itemNameMod.label)) {
                        int currentCount = itemNameMap.get(item.name.label + itemNameMod.label);
                        itemNameMap.put(item.name.label + itemNameMod.label, currentCount + 1);
                    }
                    else {
                        itemNameMap.put(item.name.label + itemNameMod.label, 1);
                        lastLineItem = item;
                    }
                }

                boolean isLastLine = false;
                for(int i = 0; i < containerItemList.size(); i++) {
                    Item item = containerItemList.get(i);
                    Line itemNameMod = item.getNameMod();

                    if(itemNameMap.containsKey(item.name.label + itemNameMod.label)) {
                        if(item == lastLineItem) {isLastLine = true;}

                        String countString = "";
                        String countColorCode = "";
                        if(itemNameMap.get(item.name.label + itemNameMod.label) > 1) {
                            Line countLine = Utility.insertCommas(itemNameMap.get(item.name.label + itemNameMod.label));
                            countString = " (" + countLine.label + ")";
                            countColorCode = "2DR" + countLine.colorCode + "1DR";
                        }
                        else if(item.isQuantity) {
                            Line countLine = Utility.insertCommas(item.quantity);
                            countString = " (" + countLine.label + ")";
                            countColorCode = "2DR" + countLine.colorCode + "1DR";
                        }

                        String itemNameLabel = item.prefix + item.name.label + itemNameMod.label + countString;
                        String itemNameColorCode = String.valueOf(item.prefix.length()) + "CONT" + item.name.colorCode + itemNameMod.colorCode + countColorCode;
                        Line itemLine = new Line(itemNameLabel, itemNameColorCode, "", isLastLine, true);
                        GameScreen.userInterface.console.writeToConsole(itemLine);

                        itemNameMap.remove(item.name.label + itemNameMod.label);
                    }
                }
            }
        }
    }

    public static Item load(String itemType, int id, Location startLocation) {
        Item item = null;

        if(itemType.equals("Gear")) {item = Gear.load(id, startLocation);}
        else if(itemType.equals("Weapon")) {item = Weapon.load(id, startLocation);}
        else if(itemType.equals("Firearm")) {item = Firearm.load(id, startLocation);}
        else if(itemType.equals("Throwable")) {item = Throwable.load(id, startLocation);}
        else if(itemType.equals("Ammo")) {item = Ammo.load(id, startLocation);}
        else if(itemType.equals("Quiver")) {item = Quiver.load(id, startLocation);}
        else if(itemType.equals("Magazine")) {item = Magazine.load(id, startLocation);}
        else if(itemType.equals("Food")) {item = Food.load(id, startLocation);}
        else if(itemType.equals("Drink")) {item = Drink.load(id, startLocation);}
        else if(itemType.equals("Plant")) {item = Plant.load(id, startLocation);}

        else {
            item = new Item(id, startLocation);

            // 001 - A Silver Key //
            if(id == 1) {
                item.name = new Line("Silver Key", "7SHIAGR3CONT", "", true, true);
                item.weight = 0;
                item.keyList = new ArrayList<>();
                item.keyList.add(1234);
            }

            // 002 - Generic Ship Key //
            else if(id == 2) {
                item.name = new Line("Generic Ship Key", "8CONT5CONT3CONT", "", true, true);
                item.weight = 0;
                item.keyList = new ArrayList<>();
            }

            // 003 - A piece of Gold //
            else if(id == 3) {
                item.prefix = "A piece of ";
                item.name = new Line("Gold", "4SHIAY", "", true, true);
                item.weight = 0;
                item.isQuantity = true;
            }

            // 004 - An Ornate Chest //
            else if(id == 4) {
                item.prefix = "An ";
                item.name = new Line("Ornate Chest", "7SHIAY5SHIAO", "", true, true);
                item.containerItemList = new ArrayList<>();
            }

            // 005 - A Weapon Cabinet //
            else if(id == 5) {
                item.name = new Line("Weapon Cabinet", "7SHIA7SHIAO", "", true, true);
                item.containerItemList = new ArrayList<>();
            }

            // 006 - A Gun Cabinet //
            else if(id == 6) {
                item.name = new Line("Gun Cabinet", "4SHIA7SHIAO", "", true, true);
                item.containerItemList = new ArrayList<>();
            }

            // 007 - Am Ammo Crate //
            else if(id == 7) {
                item.prefix = "An ";
                item.name = new Line("Ammo Crate", "5CONT5SHIAG", "", true, true);
                item.containerItemList = new ArrayList<>();
            }

            // Default Item //
            else {
                item.name = new Line("Default Item", "8CONT4CONT", "", true, true);
            }
        }

        item.nameKeyList = Entity.createNameKeyList(item.prefix + item.name.label);

        if(item.isQuantity
        && item.quantity == -1) {
            item.quantity = 1;
        }

        if(item.containerItemList != null
        && !(item.type.equals("Magazine") || item.type.equals("Quiver"))) {
            item.status = "Closed";
        }

        return item;
    }

    public static Item load(String itemType, int id, Location startLocation, int quantity) {
        Item quantityItem = load(itemType, id, startLocation);
        if(quantityItem.isQuantity) {
            quantityItem.quantity = quantity;
        }

        return quantityItem;
    }
}
