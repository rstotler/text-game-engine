package com.jbs.textgameengine.gamedata.entity.mob.action.menu;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.Arrays;

public class Group extends Action {
    public Group(Mob parentEntity) {
        super(parentEntity);
    }

    public Group() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("group", "grou", "gro", "gr").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Group(parentEntity);
        }

        return null;
    }

    public void initiate() {

        // Message - You don't have any followers. //
        if(parentEntity.groupList.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You don't have any followers.", "4CONT3CONT1DY2DDW5CONT4CONT9CONT1DY", "", true, true));
            }
        }

        else {
            GameScreen.userInterface.console.writeToConsole(new Line("Group Members:", "6CONT7CONT1DY", "", false, true));

            boolean isLastLine = false;
            for(int i = 0; i < parentEntity.groupList.size(); i++) {
                Entity groupMob = parentEntity.groupList.get(i);
                if(i == parentEntity.groupList.size() - 1) {isLastLine = true;}

                String groupMobString = groupMob.prefix + groupMob.name.label + ", " + groupMob.location.room.name.label;
                String groupMobColorCode = String.valueOf(groupMob.prefix).length() + "CONT" + groupMob.name.colorCode + "2DY" + groupMob.location.room.name.colorCode;
                if(groupMob.location.room == parentEntity.location.room) {
                    groupMobString += " [Here]";
                    groupMobColorCode += "2DR4DDR1DR";
                }
                GameScreen.userInterface.console.writeToConsole(new Line(groupMobString, groupMobColorCode, "", isLastLine, true));
            }
        }
    }
}
