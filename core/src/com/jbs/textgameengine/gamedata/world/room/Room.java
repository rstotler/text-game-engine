package com.jbs.textgameengine.gamedata.world.room;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Room {
    public Line name;
    public Line description;
    public Location location;

    public HashMap<String, Room> exitList;

    public ArrayList<Spaceship> spaceshipList;
    public ArrayList<Mob> mobList;
    public ArrayList<Item> itemList;

    public Room(Line name, Line description, Location location) {
        this.name = name;
        this.description = description;
        this.location = location;

        exitList = new HashMap<>();
        for(String direction : Arrays.asList("North", "East", "South", "West", "Northeast", "Southeast", "Southwest", "Northwest")) {
            exitList.put(direction, null);
        }

        spaceshipList = new ArrayList<>();
        mobList = new ArrayList<>();
        itemList = new ArrayList<>();
    }

    public void display() {

        // Name & Underline //
        Line nameLine = new Line(name.label, name.colorCode, name.effectCode, false, true);
        GameScreen.userInterface.console.writeToConsole(nameLine);
        Line underlineLine = new Line(Utility.getUnderlineString(name.label), String.valueOf(name.label.length()) + "ALTADY", "", false, true);
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
            if(exitList.containsKey(direction) && exitList.get(direction) != null) {
                exitName = exitList.get(direction).name.label;
                exitNameColorCode = exitList.get(direction).name.colorCode;
            }
            String exitLabel = "( " + spaceString + direction + " ) - " + exitName;
            String exitColorCode = "2DR" + spaceColorCode + String.valueOf(direction.length()) + "CONT3DR2DY" + exitNameColorCode;

            Line exitLine = new Line(exitLabel, exitColorCode, "", isLastLine, true);
            GameScreen.userInterface.console.writeToConsole(exitLine);
        }

        // Spaceships //
        if(!spaceshipList.isEmpty()) {

        }

        // Mobs //
        if(!mobList.isEmpty()) {

        }

        // Items //
        if(!itemList.isEmpty()) {

        }
    }
}
