package com.jbs.textgameengine.gamedata.world.room;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;
import com.jbs.textgameengine.gamedata.world.room.door.Door;
import com.jbs.textgameengine.gamedata.world.room.hiddenexit.HiddenExit;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Room {
    public Line name;
    public Line description;
    public Location location;

    public HashMap<String, Room> exitMap;
    public HashMap<String, Door> doorMap;
    public HashMap<String, HiddenExit> hiddenExitMap;

    public ArrayList<Spaceship> spaceshipList;
    public ArrayList<Mob> mobList;
    public ArrayList<Item> itemList;

    public Room(Line name, Line description, Location location) {
        this.name = name;
        this.description = description;
        this.location = new Location(location.galaxy, location.solarSystem, location.planetoid, location.area, null, location.spaceship);
        this.location.room = this;

        exitMap = new HashMap<>();
        doorMap = new HashMap<>();
        hiddenExitMap = new HashMap<>();
        for(String direction : Arrays.asList("North", "East", "South", "West", "Northeast", "Southeast", "Southwest", "Northwest")) {
            exitMap.put(direction, null);
            doorMap.put(direction, null);
            hiddenExitMap.put(direction, null);
        }

        spaceshipList = new ArrayList<>();
        mobList = new ArrayList<>();
        itemList = new ArrayList<>();
    }

    public void display() {

        // Display Dark Room //
        if(!isLit()) {
            displayDark();
        }

        // Display Lit Room //
        else {

            // Name & Underline //
            Line nameLine = new Line(name.label, name.colorCode, name.effectCode, false, true);
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
                if(hiddenExitMap.containsKey(direction)
                && hiddenExitMap.get(direction) != null
                && !hiddenExitMap.get(direction).isOpen) {
                    // Do Nothing
                }
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

            // Spaceships //
            if(!spaceshipList.isEmpty()) {
                for(int i = 0; i < spaceshipList.size(); i++) {
                    Spaceship spaceship = spaceshipList.get(i);

                    if(i == spaceshipList.size() - 1
                    && mobList.isEmpty() && itemList.isEmpty()) {
                        isLastLine = true;
                    }

                    String spaceshipNameLabel = spaceship.name.label + " is sitting on the launch pad.";
                    String spaceshipNameColorCode = spaceship.name.colorCode + "1W3CONT8CONT3CONT4CONT7CONT3CONT1DY";
                    Line spaceshipLine = new Line(spaceshipNameLabel, spaceshipNameColorCode, spaceship.name.effectCode, isLastLine, true);
                    GameScreen.userInterface.console.writeToConsole(spaceshipLine);
                }
            }

            // Mobs //
            if(!mobList.isEmpty()) {
                HashMap<String, Integer> mobNameMap = new HashMap<>();
                for(Mob mob : mobList) {
                    if(mobNameMap.containsKey(mob.name.label)) {
                        int currentCount = mobNameMap.get(mob.name.label);
                        mobNameMap.put(mob.name.label, currentCount + 1);
                    }
                    else {
                        mobNameMap.put(mob.name.label, 1);
                    }
                }

                for(int i = 0; i < mobList.size(); i++) {
                    Mob mob = mobList.get(i);

                    if(mobNameMap.containsKey(mob.name.label)) {
                        if(mobNameMap.size() == 1) {
                            isLastLine = true;
                        }

                        String countString = "";
                        String countColorCode = "";
                        if(mobNameMap.get(mob.name.label) > 1) {
                            countString = " (" + String.valueOf(mobNameMap.get(mob.name.label)) + ")";
                            countColorCode = "2DR" + String.valueOf(mobNameMap.get(mob.name.label)).length() + "W1DR";
                        }

                        String mobNameLabel = mob.name.label + " is here, milling about." + countString;
                        String mobNameColorCode = mob.name.colorCode + "1W3CONT4CONT2DY8CONT5CONT1DY" + countColorCode;
                        Line mobLine = new Line(mobNameLabel, mobNameColorCode, mob.name.effectCode, isLastLine, true);
                        GameScreen.userInterface.console.writeToConsole(mobLine);

                        mobNameMap.remove(mob.name.label);
                    }
                }
            }

            // Items //
            if(!itemList.isEmpty()) {
                HashMap<String, Integer> itemNameMap = new HashMap<>();
                for(Item item : itemList) {
                    if(itemNameMap.containsKey(item.name.label)) {
                        int currentCount = itemNameMap.get(item.name.label);
                        itemNameMap.put(item.name.label, currentCount + 1);
                    }
                    else {
                        itemNameMap.put(item.name.label, 1);
                    }
                }

                for(int i = 0; i < itemList.size(); i++) {
                    Item item = itemList.get(i);

                    if(itemNameMap.containsKey(item.name.label)) {
                        if(itemNameMap.size() == 1) {
                            isLastLine = true;
                        }

                        String countString = "";
                        String countColorCode = "";
                        if(itemNameMap.get(item.name.label) > 1) {
                            countString = " (" + String.valueOf(itemNameMap.get(item.name.label)) + ")";
                            countColorCode = "2DR" + String.valueOf(itemNameMap.get(item.name.label)).length() + "W1DR";
                        }

                        String itemNameLabel = item.name.label + " is laying on the ground." + countString;
                        String itemNameColorCode = item.name.colorCode + "1W3CONT7CONT3CONT4CONT6CONT1DY" + countColorCode;
                        Line itemLine = new Line(itemNameLabel, itemNameColorCode, item.name.effectCode, isLastLine, true);
                        GameScreen.userInterface.console.writeToConsole(itemLine);

                        itemNameMap.remove(item.name.label);
                    }
                }
            }
        }
    }

    public void displayDark() {

        // Name & Underline //
        Line nameLine = new Line("Darkness..", "1DW1DW1DDW1DDW1DDDW1DDDW1DDDGR1DDDGR1DDDGR1DDDGR", "", false, true);
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
            && exitMap.get(direction).isLit()) {
                exitName = exitMap.get(direction).name.label;
                exitNameColorCode = exitMap.get(direction).name.colorCode;
            }
            String exitLabel = "( " + spaceString + direction + " ) - " + exitName;
            String exitColorCode = "2DR" + spaceColorCode + String.valueOf(direction.length()) + "CONT3DR2DY" + exitNameColorCode;

            Line exitLine = new Line(exitLabel, exitColorCode, "", isLastLine, true);
            GameScreen.userInterface.console.writeToConsole(exitLine);
        }

        // Spaceships //
        if(!spaceshipList.isEmpty()) {
            if(mobList.isEmpty() && itemList.isEmpty()) {
                isLastLine = true;
            }

            String countString = "";
            String countColorCode = "";
            if(spaceshipList.size() > 1) {
                countString = " (" + String.valueOf(spaceshipList.size()) + ")";
                countColorCode = "2DR" + String.valueOf(spaceshipList.size()).length() + "W1DR";
            }
            Line spaceshipsLine = new Line("A spaceship is sitting on the launch pad." + countString, "2W10CONT3CONT8CONT3CONT4CONT7CONT3CONT1DY" + countColorCode, "", isLastLine, true);
            GameScreen.userInterface.console.writeToConsole(spaceshipsLine);
        }

        // Mobs //
        if(!mobList.isEmpty()) {
            if(itemList.isEmpty()) {
                isLastLine = true;
            }

            String countString = "";
            String countColorCode = "";
            if(mobList.size() > 1) {
                countString = " (" + String.valueOf(mobList.size()) + ")";
                countColorCode = "2DR" + String.valueOf(mobList.size()).length() + "W1DR";
            }
            Line mobsLine = new Line("There is someone here." + countString, "6CONT3CONT8CONT4CONT1DY" + countColorCode, "", isLastLine, true);
            GameScreen.userInterface.console.writeToConsole(mobsLine);
        }

        // Items //
        if(!itemList.isEmpty()) {
            String countString = "";
            String countColorCode = "";
            if(itemList.size() > 1) {
                countString = " (" + String.valueOf(itemList.size()) + ")";
                countColorCode = "2DR" + String.valueOf(itemList.size()).length() + "W1DR";
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

        String oppositeDirection = Location.getOppositeDirection(targetDirection);
        targetRoom.doorMap.put(oppositeDirection, door);
    }

    public void createHiddenExit(String targetDirection, Room targetRoom) {
        createExit(targetDirection, targetRoom);

        HiddenExit hiddenExit = new HiddenExit();
        hiddenExitMap.put(targetDirection, hiddenExit);

        String oppositeDirection = Location.getOppositeDirection(targetDirection);
        targetRoom.hiddenExitMap.put(oppositeDirection, hiddenExit);
    }

    public void createEntity(String entityType, int id) {
        if(entityType.equals("Mob")) {
            mobList.add(Mob.load(id, this.location));
        }
        else if(entityType.equals("Item")) {
            itemList.add(Item.load(id, this.location));
        }
        else if(entityType.equals("Spaceship")) {
            spaceshipList.add(Spaceship.load(id, this.location));
        }
    }

    public boolean isLit() {
        return false;
    }
}
