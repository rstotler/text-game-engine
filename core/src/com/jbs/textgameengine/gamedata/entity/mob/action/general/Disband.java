package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Disband extends Action {
    public Disband(Mob parentEntity) {
        super(parentEntity);
    }

    public Disband() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("disband", "disban", "disba", "disb", "dis").contains(inputList.get(0))) {
            Disband disbandAction = new Disband(parentEntity);

            // Disband All Entity //
            if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                disbandAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                disbandAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Disband # Entity //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                disbandAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                disbandAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Disband All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                disbandAction.allCheck = true;
            }

            // Disband Entity //
            else if(inputList.size() >= 2) {
                disbandAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                disbandAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Disband //
            else {
                disbandAction.allCheck = true;
            }

            return disbandAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<Integer> removeIndexList = new ArrayList<>();
        Entity targetMob = null;
        boolean multipleMobTypes = false;

        for(int i = 0; i < parentEntity.groupList.size(); i++) {
            Entity mob = parentEntity.groupList.get(i);
            if((targetEntityString.isEmpty() && allCheck)
            || (!targetEntityString.isEmpty() && mob.nameKeyList.contains(targetEntityString))) {
                removeIndexList.add(0, i);

                if(targetMob == null) {targetMob = mob;}
                else if(targetMob.id != mob.id) {multipleMobTypes = true;}
            }

            if(targetCount != -1
            && removeIndexList.size() >= targetCount) {
                break;
            }
        }

        for(int removeIndex : removeIndexList) {
            parentEntity.groupList.remove(removeIndex);
        }

        // Message - You don't have any followers. //
        if(removeIndexList.isEmpty()
        && parentEntity.groupList.isEmpty()) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You don't have any followers.", "4CONT3CONT1DY2DDW5CONT4CONT9CONT1DY", "", true, true));
            }
        }

        // Message - You disband your group. //
        else if(parentEntity.groupList.isEmpty()) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You disband your group.", "4CONT8CONT5CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You remove some members of your group. //
        else if(multipleMobTypes) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("You remove some members of your group.", "4CONT7CONT5CONT8CONT3CONT5CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You remove Entity from the group. //
        else if(targetMob != null) {
            if(parentEntity.isPlayer) {
                String removeCountString = "";
                String removeCountColorCode = "";
                if(removeIndexList.size() > 1) {
                    removeCountString = " (" + String.valueOf(removeIndexList.size()) + ")";
                    removeCountColorCode = "2DR" + String.valueOf(removeIndexList.size()).length() + "CONT1DR";
                }
                userInterface.console.writeToConsole(new Line("You remove " + targetMob.prefix + targetMob.name.label + removeCountString + " from the group.", "4CONT7CONT" + String.valueOf(targetMob.prefix).length() + "CONT" + targetMob.name.colorCode + removeCountColorCode + "1W5CONT4CONT5CONT1DY", "", true, true));
            }
        }
    }
}
