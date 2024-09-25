package com.jbs.textgameengine.gamedata.entity.mob.action;

import com.jbs.textgameengine.gamedata.entity.mob.action.general.Look;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;

import java.util.ArrayList;
import java.util.Arrays;

public class Action {
    public static ArrayList<Action> actionList = loadActionList();
    public static ArrayList<String> directionList = new ArrayList<>(Arrays.asList("north", "nort", "nor", "no", "n", "east", "eas", "ea", "e", "south", "sout", "sou", "so", "s", "west", "wes", "we", "w", "northeast", "northeas", "northea", "northe", "ne", "southeast", "southeas", "southea", "southe", "se", "southwest", "southwes", "southwe", "southw", "sw", "northwest", "northwes", "northwe", "northw", "nw"));

    public String actionType;
    public boolean interruptAction;

    public String targetEntityString;
    public String targetDirection;
    public int directionCount;
    public String targetContainerString;

    public Action() {
        actionType = "";
        interruptAction = false;

        targetEntityString = "";
        targetDirection = "";
        directionCount = -1;
        targetContainerString = "";
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        return null;
    }

    public void initiate(Mob parentEntity) {}

    public static ArrayList<Action> loadActionList() {
        ArrayList<Action> actionList = new ArrayList<>();

        actionList.add(new Look());

        return actionList;
    }

    public static String getDirectionFromSubstring(String directionSubstring) {
        directionSubstring = directionSubstring.toLowerCase();
        if(directionSubstring.substring(0, 1).equals("n")) {
            return "North";
        }
        else if(directionSubstring.substring(0, 1).equals("e")) {
            return "East";
        }
        else if(directionSubstring.substring(0, 1).equals("s")) {
            return "South";
        }
        else if(directionSubstring.substring(0, 1).equals("w")) {
            return "West";
        }

        return "";
    }
}
