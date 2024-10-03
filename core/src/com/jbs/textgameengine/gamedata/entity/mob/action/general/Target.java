package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.CombatAction;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Target extends Action {
    public Target(Mob parentEntity) {
        super(parentEntity);
    }

    public Target() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("target", "targe", "targ", "tar", "ta", "t", "untarget", "untarge", "untarg", "untarg", "untar", "unta", "unt", "un", "u").contains(inputList.get(0))) {
            Target targetAction = new Target(parentEntity);
            targetAction.actionType = "Target";
            if(inputList.get(0).charAt(0) == 'u') {targetAction.actionType = "Untarget";}

            // Target All Entity Direction # //
            if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                targetAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 2);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target All Entity Direction //
            else if(inputList.size() >= 4
            && inputList.get(1).equals("all")
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                targetAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 1);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target # Entity Direction # //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                targetAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 2);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target # Entity Direction //
            else if(inputList.size() >= 4
            && Utility.isInteger(inputList.get(1))
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                targetAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size() - 1);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target All Direction # //
            else if(inputList.size() == 4
            && inputList.get(1).equals("all")
            && Location.directionList.contains(inputList.get(2))
            && Utility.isInteger(inputList.get(3))) {
                targetAction.allCheck = true;
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target All Direction //
            else if(inputList.size() == 3
            && inputList.get(1).equals("all")
            && Location.directionList.contains(inputList.get(2))) {
                targetAction.allCheck = true;
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target Entity Direction # //
            else if(inputList.size() >= 4
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 2);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetAction.targetDirectionCount = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Target Entity Direction //
            else if(inputList.size() >= 3
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 1);
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetAction.targetDirectionCount = 1;
            }

            // Target # Entity //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                targetAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Target All Entity //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                targetAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Target All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                targetAction.allCheck = true;
            }

            // Target Entity //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                targetAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                String stringPrefix = "T";
                if(targetAction.actionType.equals("Untarget")) {stringPrefix = "Unt";}
                userInterface.console.writeToConsole(new Line(stringPrefix + "arget who?", String.valueOf(stringPrefix.length()) + "CONT1DDW1DDW1DDW1DDW1DDW1DDW3CONT1DY", "", true, true));
            }

            return targetAction;
        }

        return null;
    }

    public void initiate() {
    }
}
