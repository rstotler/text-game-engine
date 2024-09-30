package com.jbs.textgameengine.gamedata.entity.mob.combat.combataction;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.combat.combataction.general.Punch;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CombatAction extends Action {
    public static ArrayList<CombatAction> combatActionList = getCombatActionList();

    public CombatAction() {
        super();
    }

    public CombatAction getNewObject() {
        return new CombatAction();
    }

    public CombatAction getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        String twoWordKey = "";
        if(inputList.size() > 1) {
            List<String> twoWordKeyList = inputList.subList(0, 2);
            twoWordKey = twoWordKeyList.stream().collect(Collectors.joining(" "));
        }

        // Get CombatAction & Remove Name Key From Front Of InputList //
        CombatAction targetCombatAction = null;
        for(CombatAction combatAction : combatActionList) {
            if(!twoWordKey.isEmpty() && combatAction.nameKeyList.contains(twoWordKey)) {
                targetCombatAction = combatAction.getNewObject();
                inputList = new ArrayList<>(inputList.subList(2, inputList.size()));
                break;
            }
            else if(combatAction.nameKeyList.contains(inputList.get(0))) {
                targetCombatAction = combatAction.getNewObject();
                inputList = new ArrayList<>(inputList.subList(1, inputList.size()));
                break;
            }
        }

        if(targetCombatAction != null) {

            // CombatAction All/Group Direction # //
            if(inputList.size() == 3
            && (inputList.get(0).equals("all") || inputList.get(0).equals("group"))
            && Location.directionList.contains(inputList.get(1))
            && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                if(inputList.get(0).equals("all")) {
                    targetCombatAction.allCheck = true;
                } else {
                    targetCombatAction.groupCheck = true;
                }
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                targetCombatAction.targetCount = Integer.valueOf(inputList.get(inputList.size() - 1));
                targetCombatAction.actionType = "CombatAction All/Group Direction #";
            }

            // CombatAction Entity Direction # //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(inputList.size() - 1))
            && Location.directionList.contains(inputList.get(inputList.size() - 2))) {
                List<String> targetEntityStringList = inputList.subList(0, inputList.size() - 2);
                targetCombatAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 2));
                targetCombatAction.targetCount = Integer.valueOf(inputList.get(inputList.size() - 1));
                targetCombatAction.actionType = "CombatAction Entity Direction #";
            }

            // CombatAction All/Group Direction //
            else if(inputList.size() == 2
            && (inputList.get(0).equals("all") || inputList.get(0).equals("group"))
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                if(inputList.get(0).equals("all")) {
                    targetCombatAction.allCheck = true;
                } else {
                    targetCombatAction.groupCheck = true;
                }
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                targetCombatAction.actionType = "CombatAction All/Group Direction";
            }

            // CombatAction Entity Direction //
            else if(inputList.size() >= 2
            && Location.directionList.contains(inputList.get(inputList.size() - 1))) {
                List<String> targetEntityStringList = inputList.subList(0, inputList.size() - 1);
                targetCombatAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(inputList.size() - 1));
                targetCombatAction.actionType = "CombatAction Entity Direction";
            }

            // CombatAction Direction # //
            else if(inputList.size() == 2
            && Location.directionList.contains(inputList.get(0))
            && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                targetCombatAction.targetCount = Integer.valueOf(inputList.get(1));
                targetCombatAction.actionType = "CombatAction Direction #";
            }

            // CombatAction All/Group //
            else if(inputList.size() == 1
            && (inputList.get(0).equals("all") || inputList.get(0).equals("group"))) {
                if(inputList.get(0).equals("all")) {
                    targetCombatAction.allCheck = true;
                } else {
                    targetCombatAction.groupCheck = true;
                }
                targetCombatAction.actionType = "CombatAction All/Group";
            }

            // CombatAction Direction //
            else if(inputList.size() == 1
            && Location.directionList.contains(inputList.get(0))) {
                targetCombatAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                targetCombatAction.actionType = "CombatAction Direction";
            }

            // CombatAction Entity/Self //
            else if(!inputList.isEmpty()) {
                if(inputList.get(0).equals("self")) {
                    targetCombatAction.selfCheck = true;
                } else {
                    List<String> targetEntityStringList = inputList.subList(0, inputList.size());
                    targetCombatAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                }
                targetCombatAction.actionType = "CombatAction Entity/Self";
            }

            // CombatAction //
            else {
                targetCombatAction.actionType = "CombatAction";
            }

            return targetCombatAction;
        }

        return null;
    }

    public static ArrayList<CombatAction> getCombatActionList() {
        ArrayList<CombatAction> combatActionList = new ArrayList<>();

        combatActionList.add(new Punch());

        return combatActionList;
    }
}
