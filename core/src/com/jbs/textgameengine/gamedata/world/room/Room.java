package com.jbs.textgameengine.gamedata.world.room;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.item.type.Seed;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.room.door.Door;
import com.jbs.textgameengine.gamedata.world.room.hiddenexit.HiddenExit;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Point;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Room {
    public int index;
    public Point coordinates;

    public Line name;
    public Line description;
    public Location location;
    public ArrayList<String> nameKeyList;

    public String tileType;
    public String mapColorTargetColor;
    public float mapColorMod;

    public boolean inside;
    public HashMap<String, Room> exitMap;
    public HashMap<String, Door> doorMap;
    public HashMap<String, HiddenExit> hiddenExitMap;

    public ArrayList<Entity> mobList;
    public ArrayList<Entity> spaceshipList;
    public ArrayList<Entity> itemList;

    public String groundType;
    public float groundSaturation;
    public ArrayList<Seed> plantedSeedList;

    public Room(int index, Line name, Line description, Location location) {
        this.index = index;
        coordinates = new Point(0, 0);

        this.name = name;
        this.description = description;
        this.location = new Location(location.galaxy, location.solarSystem, location.planetoid, location.area, null, location.spaceship);
        this.location.room = this;
        nameKeyList = Entity.createNameKeyList(name.label.toLowerCase());

        tileType = "";
        mapColorTargetColor = Arrays.asList("R", "G", "B").get(new Random().nextInt(3));
        mapColorMod = (new Random().nextFloat() * .25f);

        inside = false;
        exitMap = new HashMap<>();
        doorMap = new HashMap<>();
        hiddenExitMap = new HashMap<>();
        for(String direction : Arrays.asList("North", "East", "South", "West", "Northeast", "Southeast", "Southwest", "Northwest", "Up", "Down")) {
            exitMap.put(direction, null);
            doorMap.put(direction, null);
            hiddenExitMap.put(direction, null);
        }

        mobList = new ArrayList<>();
        spaceshipList = new ArrayList<>();
        itemList = new ArrayList<>();

        groundType = "Dirt";
        groundSaturation = 0.0f;
        plantedSeedList = new ArrayList<>();
    }

    public void update() {

        // Update Room Saturation Timers //
        if(Arrays.asList("Dirt", "Soil").contains(groundType)) {
            if(!inside
            && location.area.weatherSystem != null
            && location.area.weatherSystem.precipitationTimer > 0
            && location.area.currentTemperature > ((Planet) location.planetoid).getFreezingTemperature()) {
                groundSaturation += .01f;
                if(groundSaturation > 1.0) {groundSaturation = 1.0f;}
            }
            else {
                if(groundSaturation > 0) {
                    groundSaturation -= .005f;
                    if(groundSaturation < 0.0) {
                        groundSaturation = 0.0f;

                        // Display Message //
                        if(GameScreen.player.location.room == this) {
                            GameScreen.userInterface.console.writeToConsole(new Line("The ground dries up.", "4CONT7CONT6CONT2CONT1DY", "", true, true));
                        }
                    }
                }
            }
        }

        // Update Mobs //
        for(int i = mobList.size() - 1; i >= 0; i--) {
            Mob mob = (Mob) mobList.get(i);
            mob.update();
        }
    }

    public void display() {

        // Display Dark Room //
        if(!isLit()) {
            displayDark();
        }

        // Display Lit Room //
        else {

            // Name & Underline //
            String insideString = "";
            String insideColorCode = "";
            String insideEffectCode = "";
            if(inside) {
                insideString = "(Inside) ";
                insideColorCode = "1DR6CONT2DR";
                insideEffectCode = String.valueOf(insideString.length()) + "X";
            }
            else if(location.area.weatherSystem != null
            && location.area.weatherSystem.precipitationTimer > 0) {
                if(location.area.currentTemperature > ((Planet) location.planetoid).getFreezingTemperature()) {
                    insideString = "(Raining) ";
                    insideColorCode = "1DR7SHIAB2DR";
                    insideEffectCode = "10X";
                }
                else {
                    insideString = "(Snowing) ";
                    insideColorCode = "1DR7SHIA2DR";
                    insideEffectCode = "10X";
                }
            }

            Line nameLine = new Line(insideString + name.label, insideColorCode + name.colorCode, insideEffectCode + name.effectCode, false, true);
            GameScreen.userInterface.console.writeToConsole(nameLine);
            Line underlineLine = new Line(Line.getUnderlineString(name.label), String.valueOf(name.label.length()) + "ALTADY", "", false, true);
            GameScreen.userInterface.console.writeToConsole(underlineLine);

            // Description //
            if(description != null) {
                Line descriptionLine = new Line(description.label, description.colorCode, description.effectCode, false, true);
                GameScreen.userInterface.console.writeToConsole(descriptionLine);
            }

            // Exits //
            boolean isLastLine = false;
            for(String direction : Arrays.asList("North", "East", "South", "West")) {
                if(direction.equals("West")
                && spaceshipList.isEmpty() && mobList.isEmpty() && itemList.isEmpty()) {
                    isLastLine = true;
                }

                String spaceString = "";
                String spaceColorCode = "";
                if(direction.length() == 4) {
                    spaceString = " ";
                    spaceColorCode = "1W";
                }
                String exitName = "( Nothing )";
                String exitNameColorCode = "2DR8GRAD1DR";

                // Hidden Exit (Do Not Show) //
                if(hiddenExitMap.containsKey(direction)
                && hiddenExitMap.get(direction) != null
                && !hiddenExitMap.get(direction).isOpen) {
                    // Do Nothing
                }

                // Spaceship Exit //
                else if(location.spaceship != null
                && location.spaceship.boardingRoom == this
                && location.spaceship.boardingRoomExitDirection.equals(direction)) {
                    exitName = "( Exit Door )";
                    exitNameColorCode = "2DR5CONT4CONT2DR";
                }

                // Normal Exit //
                else if(exitMap.containsKey(direction) && exitMap.get(direction) != null) {
                    if(doorMap.containsKey(direction)
                    && doorMap.get(direction) != null
                    && Arrays.asList("Closed", "Locked").contains(doorMap.get(direction).status)) {
                        exitName = "( Closed )";
                        exitNameColorCode = "2DR7CONT1DR";
                    }
                    else if(!exitMap.get(direction).isLit()) {
                        exitName = "( Darkness )";
                        exitNameColorCode = "2DR1DW1DW1DDW1DDW1DDDW1DDDW1DDDGR1DDDGR2DR";
                    }
                    else {
                        exitName = exitMap.get(direction).name.label;
                        exitNameColorCode = exitMap.get(direction).name.colorCode;
                    }
                }
                String exitLabel = "( " + spaceString + direction + " ) - " + exitName;
                String exitColorCode = "2DR" + spaceColorCode + String.valueOf(direction.length()) + "CONT3DR2DY" + exitNameColorCode;

                Line exitLine = new Line(exitLabel, exitColorCode, "", isLastLine, true);
                GameScreen.userInterface.console.writeToConsole(exitLine);
            }

            // Mobs //
            if(!mobList.isEmpty()) {
                HashMap<String, Integer> mobNameMap = new HashMap<>();
                Mob lastLineMob = null;

                for(Entity entity : mobList) {
                    Mob mob = (Mob) entity;
                    Line mobNameMod = mob.getNameMod();

                    // Get Group/Target Display Mods //
                    String targetLabel = mob.prefix + mob.name.label + mobNameMod.label;
                    if(GameScreen.player.groupList.contains(mob)) {
                        targetLabel = "[+]" + targetLabel;
                    }
                    else if(GameScreen.player.targetList.contains(mob)) {
                        targetLabel = "[-]" + targetLabel;
                    }

                    if(mobNameMap.containsKey(targetLabel)) {
                        int currentCount = mobNameMap.get(targetLabel);
                        mobNameMap.put(targetLabel, currentCount + 1);
                    }
                    else {
                        mobNameMap.put(targetLabel, 1);
                        lastLineMob = mob;
                    }
                }

                for(int i = 0; i < mobList.size(); i++) {
                    Mob mob = (Mob) mobList.get(i);
                    Line mobNameMod = mob.getNameMod();

                    // Get Group/Target Display Mods //
                    String targetLabel = mob.prefix + mob.name.label + mobNameMod.label;
                    if(GameScreen.player.groupList.contains(mob)) {
                        targetLabel = "[+]" + targetLabel;
                    }
                    else if(GameScreen.player.targetList.contains(mob)) {
                        targetLabel = "[-]" + targetLabel;
                    }

                    if(mobNameMap.containsKey(targetLabel)) {
                        if(spaceshipList.isEmpty()
                        && itemList.isEmpty()
                        && mob == lastLineMob) {
                            isLastLine = true;
                        }

                        String countString = "";
                        String countColorCode = "";
                        if(mobNameMap.get(targetLabel) > 1) {
                            Line countLine = Utility.insertCommas(mobNameMap.get(targetLabel));
                            countString = " (" + countLine.label + ")";
                            countColorCode = "2DR" + countLine.colorCode + "1DR";
                        }

                        String mobRoomDescriptionLabel = "is here, milling about.";
                        String mobRoomDescriptionColorCode = "3CONT4CONT2DY8CONT5CONT1DY";
                        if(mob.roomDescription != null) {
                            mobRoomDescriptionLabel = mob.roomDescription.label;
                            mobRoomDescriptionColorCode = mob.roomDescription.colorCode;
                        }

                        String groupTargetString = "";
                        String groupTargetColorCode = "";
                        String groupTargetEffectCode = "";
                        if(GameScreen.player.groupList.contains(mob)) {
                            groupTargetString = "[+]";
                            groupTargetColorCode = "3G";
                            groupTargetEffectCode = "3X";
                        }
                        else if(GameScreen.player.targetList.contains(mob)) {
                            groupTargetString = "[-]";
                            groupTargetColorCode = "3V";
                            groupTargetEffectCode = "3X";
                        }

                        String mobNameLabel = groupTargetString + mob.prefix + mob.name.label + mobNameMod.label + " " + mobRoomDescriptionLabel + countString;
                        String mobNameColorCode = groupTargetColorCode + String.valueOf(mob.prefix.length()) + "W" + mob.name.colorCode + mobNameMod.colorCode + "1W" + mobRoomDescriptionColorCode + countColorCode;
                        String mobNameEffectCode = groupTargetEffectCode + String.valueOf(mob.prefix.length()) + "X" + mob.name.effectCode;

                        Line mobLine = new Line(mobNameLabel, mobNameColorCode, mobNameEffectCode, isLastLine, true);
                        GameScreen.userInterface.console.writeToConsole(mobLine);

                        mobNameMap.remove(targetLabel);
                    }
                }
            }

            // Spaceships //
            if(!spaceshipList.isEmpty()) {
                for(int i = 0; i < spaceshipList.size(); i++) {
                    Spaceship spaceship = (Spaceship) spaceshipList.get(i);

                    if(i == spaceshipList.size() - 1
                    && itemList.isEmpty()) {
                        isLastLine = true;
                    }

                    String spaceshipNameLabel = spaceship.name.label + " is sitting on the launch pad.";
                    String spaceshipNameColorCode = spaceship.name.colorCode + "1W3CONT8CONT3CONT4CONT7CONT3CONT1DY";
                    Line spaceshipLine = new Line(spaceshipNameLabel, spaceshipNameColorCode, spaceship.name.effectCode, isLastLine, true);
                    GameScreen.userInterface.console.writeToConsole(spaceshipLine);
                }
            }

            // Items //
            if(!itemList.isEmpty()) {
                HashMap<String, Integer> itemNameMap = new HashMap<>();
                Item lastLineItem = null;

                for(Entity entity : itemList) {
                    Item item = (Item) entity;
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

                for(int i = 0; i < itemList.size(); i++) {
                    Item item = (Item) itemList.get(i);
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
                        else if(item.isQuantity && item.quantity > 1) {
                            Line countLine = Utility.insertCommas(item.quantity);
                            countString = " (" + countLine.label + ")";
                            countColorCode = "2DR" + countLine.colorCode + "1DR";
                        }

                        String itemNameLabel = item.prefix + item.name.label + itemNameMod.label + " is lying on the ground." + countString;
                        String itemNameColorCode = String.valueOf(item.prefix.length()) + "CONT" + item.name.colorCode + itemNameMod.colorCode + "1W3CONT6CONT3CONT4CONT6CONT1DY" + countColorCode;
                        if(item.roomDescription != null) {
                            itemNameLabel = item.prefix + item.name.label + itemNameMod.label + item.roomDescription.label + countString;
                            itemNameColorCode = String.valueOf(item.prefix.length()) + "CONT" + item.name.colorCode + itemNameMod.colorCode + item.roomDescription.colorCode + countColorCode;
                        }
                        Line itemLine = new Line(itemNameLabel, itemNameColorCode, "", isLastLine, true);
                        GameScreen.userInterface.console.writeToConsole(itemLine);

                        itemNameMap.remove(item.name.label + itemNameMod.label);
                    }
                }
            }
        }
    }

    public void displayDark() {

        // Name & Underline //
        String insideString = "";
        String insideColorCode = "";
        String insideEffectCode = "";
        if(inside) {
            insideString = "(Inside) ";
            insideColorCode = "1DR6CONT2DR";
            insideEffectCode = String.valueOf(insideString.length()) + "X";
        }
        else if(location.area.weatherSystem != null
        && location.area.weatherSystem.precipitationTimer > 0) {
            if(location.area.currentTemperature > ((Planet) location.planetoid).getFreezingTemperature()) {
                insideString = "(Raining) ";
                insideColorCode = "1DR7SHIAB2DR";
                insideEffectCode = "10X";
            }
            else {
                insideString = "(Snowing) ";
                insideColorCode = "1DR7SHIA2DR";
                insideEffectCode = "10X";
            }
        }

        Line nameLine = new Line(insideString + "Darkness..", insideColorCode + "1DW1DW1DDW1DDW1DDDW1DDDW1DDDGR1DDDGR1DDDGR1DDDGR", insideEffectCode + "10FADA", false, true);
        GameScreen.userInterface.console.writeToConsole(nameLine);
        Line underlineLine = new Line(Line.getUnderlineString("Darkness.."), "10ALTADY", "", false, true);
        GameScreen.userInterface.console.writeToConsole(underlineLine);

        // Exits //
        boolean isLastLine = false;
        for(String direction : Arrays.asList("North", "East", "South", "West")) {
            if(direction.equals("West")
            && spaceshipList.isEmpty() && mobList.isEmpty() && itemList.isEmpty()) {
                isLastLine = true;
            }

            String spaceString = "";
            String spaceColorCode = "";
            if(direction.length() == 4) {
                spaceString = " ";
                spaceColorCode = "1W";
            }
            String exitName = "( Darkness )";
            String exitNameColorCode = "2DR1DW1DW1DDW1DDW1DDDW1DDDW1DDDGR1DDDGR2DR";

            if(exitMap.containsKey(direction)
            && exitMap.get(direction) != null
            && exitMap.get(direction).isLit()
            && !(doorMap.containsKey(direction) && doorMap.get(direction) != null && Arrays.asList("Closed", "Locked").contains(doorMap.get(direction).status))) {
                exitName = exitMap.get(direction).name.label;
                exitNameColorCode = exitMap.get(direction).name.colorCode;
            }
            String exitLabel = "( " + spaceString + direction + " ) - " + exitName;
            String exitColorCode = "2DR" + spaceColorCode + String.valueOf(direction.length()) + "CONT3DR2DY" + exitNameColorCode;

            Line exitLine = new Line(exitLabel, exitColorCode, "", isLastLine, true);
            GameScreen.userInterface.console.writeToConsole(exitLine);
        }

        // Mobs //
        if(!mobList.isEmpty()) {
            if(spaceshipList.isEmpty() && itemList.isEmpty()) {
                isLastLine = true;
            }

            String countString = "";
            String countColorCode = "";
            if(mobList.size() > 1) {
                Line countLine = Utility.insertCommas(mobList.size());
                countString = " (" + countLine.label + ")";
                countColorCode = "2DR" + countLine.colorCode + "W1DR";
            }
            Line mobsLine = new Line("There is someone here." + countString, "6CONT3CONT8CONT4CONT1DY" + countColorCode, "", isLastLine, true);
            GameScreen.userInterface.console.writeToConsole(mobsLine);
        }

        // Spaceships //
        if(!spaceshipList.isEmpty()) {
            if(itemList.isEmpty()) {
                isLastLine = true;
            }

            String countString = "";
            String countColorCode = "";
            if(spaceshipList.size() > 1) {
                Line countLine = Utility.insertCommas(spaceshipList.size());
                countString = " (" + countLine.label + ")";
                countColorCode = "2DR" + countLine.colorCode + "W1DR";
            }
            Line spaceshipsLine = new Line("A spaceship is sitting on the launch pad." + countString, "2W10CONT3CONT8CONT3CONT4CONT7CONT3CONT1DY" + countColorCode, "", isLastLine, true);
            GameScreen.userInterface.console.writeToConsole(spaceshipsLine);
        }

        // Items //
        if(!itemList.isEmpty()) {
            String countString = "";
            String countColorCode = "";
            if(itemList.size() > 1) {
                Line countLine = Utility.insertCommas(itemList.size());
                countString = " (" + countLine.label + ")";
                countColorCode = "2DR" + countLine.colorCode + "W1DR";
            }
            Line itemsLine = new Line("There is something on the ground." + countString, "6CONT3CONT10CONT3CONT4CONT6CONT1DY" + countColorCode, "", true, true);
            GameScreen.userInterface.console.writeToConsole(itemsLine);
        }
    }

    public void createExit(String targetDirection, Room targetRoom, String doorType, int doorKeyNum) {
        createExit(targetDirection, targetRoom);
        createDoor(targetDirection, targetRoom, doorType, doorKeyNum);
    }

    public void createExit(String targetDirection, Room targetRoom, String doorType) {
        createExit(targetDirection, targetRoom, doorType, -9999);
    }

    public void createExit(String targetDirection, Room targetRoom) {
        exitMap.put(targetDirection, targetRoom);

        String oppositeDirection = Location.getOppositeDirection(targetDirection);
        targetRoom.exitMap.put(oppositeDirection, this);
    }

    public void createDoor(String targetDirection, Room targetRoom, String doorType, int doorKeyNum) {
        Door door = new Door(doorType, doorKeyNum);
        doorMap.put(targetDirection, door);

        if(targetRoom != null) {
            String oppositeDirection = Location.getOppositeDirection(targetDirection);
            targetRoom.doorMap.put(oppositeDirection, door);
        }
    }

    public void createDoor(String targetDirection, Room targetRoom, String doorType) {
        createDoor(targetDirection, targetRoom, doorType, -9999);
    }

    public void createHiddenExit(String targetDirection, Room targetRoom) {
        createExit(targetDirection, targetRoom);

        HiddenExit hiddenExit = new HiddenExit();
        hiddenExitMap.put(targetDirection, hiddenExit);

        String oppositeDirection = Location.getOppositeDirection(targetDirection);
        targetRoom.hiddenExitMap.put(oppositeDirection, hiddenExit);
    }

    public void createEntity(String entityType, String entitySubType, int id) {
        if(entityType.equals("Mob")) {
            mobList.add(Mob.load(id, this.location));
        }
        else if(entityType.equals("Item")) {
            addItemToRoom(Item.load(entitySubType, id, this.location));
        }
        else if(entityType.equals("Spaceship")) {
            spaceshipList.add(Spaceship.load(id, this.location));
        }
    }

    public void addItemToRoom(Entity targetItem) {
        targetItem.location = new Location(location);

        // Quantity Item Already In Room Check //
        boolean quantityItemInRoom = false;
        if(((Item) targetItem).isQuantity) {
            for(Entity item : itemList) {
                if(targetItem.id == item.id
                && ((Item) targetItem).type.equals(((Item) item).type)) {
                    ((Item) item).quantity += ((Item) targetItem).quantity;
                    quantityItemInRoom = true;
                    break;
                }
            }
        }

        // Default Add Item To Room //
        if(!quantityItemInRoom) {
            itemList.add(targetItem);
        }
    }

    public Entity getEntityFromNameKey(String key, String entityType) {
        ArrayList<String> listsToSearch = new ArrayList<>();
        if(entityType != null && !entityType.isEmpty()) {
            listsToSearch.add(entityType);
        }

        if(!listsToSearch.contains("Mob")) {listsToSearch.add("Mob");}
        if(!listsToSearch.contains("Spaceship")) {listsToSearch.add("Spaceship");}
        if(!listsToSearch.contains("Item")) {listsToSearch.add("Item");}

        for(String listType : listsToSearch) {
            ArrayList<Entity> targetEntityList = null;
            if(listType.equals("Mob")) {targetEntityList = mobList;}
            else if(listType.equals("Spaceship")) {targetEntityList = spaceshipList;}
            else if(listType.equals("Item")) {targetEntityList = itemList;}

            if(targetEntityList != null) {
                for(Entity entity : targetEntityList) {
                    if(key.equals("ANY")
                    || entity.nameKeyList.contains(key)) {
                        return entity;
                    }
                }
            }
        }

        return null;
    }

    public boolean isLit() {
        if(location.spaceship == null
        && location.planetoid != null) {

            // Glowing Item On Ground Check //
            for(Entity item : itemList) {
                if(item.glowing) {
                    return true;
                }
            }

            // Glowing Entity Or Entity Gear Check //
            for(Entity mob : mobList) {
                if(((Mob) mob).isGlowing()) {
                    return true;
                }
            }
            if(GameScreen.player.location.room == this
            && GameScreen.player.isGlowing()) {
                return true;
            }

            if(inside) {
                return false;
            }
            else {
                return location.planetoid.isDay();
            }
        }

        else if(location.spaceship != null) {
            return true;
        }

        return false;
    }
}
