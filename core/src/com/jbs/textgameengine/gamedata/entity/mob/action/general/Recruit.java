package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class Recruit extends Action {
    public boolean targetCheck;

    public Recruit(Mob parentEntity) {
        super(parentEntity);

        targetCheck = false;
    }

    public Recruit() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("recruit", "recrui", "recru", "recr", "rec", "re", "tame", "tam", "ta").contains(inputList.get(0))) {
            Recruit recruitAction = new Recruit(parentEntity);

            // Recruit All Mob //
            if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                recruitAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                recruitAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Recruit # Mob //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                recruitAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                recruitAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Recruit Targets //
            else if(inputList.size() == 2
            && (inputList.get(1).equals("target") || inputList.get(1).equals("targets"))) {
                recruitAction.allCheck = true;
                recruitAction.targetCheck = true;
            }

            // Recruit All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                recruitAction.allCheck = true;
            }

            // Recruit Mob //
            else if(inputList.size() >= 2) {
                recruitAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                recruitAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Recruit //
            else {
                recruitAction.targetCount = 1;
                recruitAction.targetCheck = true;
            }

            return recruitAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<Mob> recruitList = new ArrayList<>();
        boolean multipleMobTypes = false;
        boolean alreadyInGroupCheck = false;
        boolean inCombatWithTargetCheck = false;

        // Get Target Mob Out Of Range Data //
        boolean outOfRangeCheck = false;
        for(Mob mob : parentEntity.targetList) {
            if(parentEntity.location.room != mob.location.room) {
                outOfRangeCheck = true;
                break;
            }
        }

        // Recruit/Tame Mob //
        if(parentEntity.location.room.isLit()) {
            for(Entity mob : parentEntity.location.room.mobList) {
                if((allCheck && targetEntityString.isEmpty())
                || (!targetEntityString.isEmpty() && mob.nameKeyList.contains(targetEntityString))
                || (targetCheck && parentEntity.targetList.contains(mob))) {
                    if(parentEntity.groupList.contains(mob)) {
                        alreadyInGroupCheck = true;
                    }
                    else if(parentEntity.combatList.contains(mob)) {
                        inCombatWithTargetCheck = true;
                    }

                    // Recruit //
                    else {

                        // Multiple Mob Type Check //
                        if(!recruitList.isEmpty()
                        && (recruitList.get(0).id != mob.id
                        || !recruitList.get(0).type.equals(((Mob) mob).type))) {
                            multipleMobTypes = true;
                        }

                        parentEntity.groupList.add((Mob) mob);
                        recruitList.add((Mob) mob);

                        if(parentEntity.targetList.contains(mob)) {
                            parentEntity.targetList.remove(mob);
                        }

                        if(targetCount != -1
                        && recruitList.size() >= targetCount) {
                            break;
                        }
                    }
                }
            }
        }

        // Message - It's too dark to see. //
        if(!parentEntity.location.room.isLit()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("It's too dark to see.", "2CONT1DY2DDW4CONT5CONT3CONT3CONT1DY", "", true, true));
            }
        }

        // Message - You aren't targeting anyone. //
        else if(targetCheck
        && recruitList.isEmpty()
        && parentEntity.targetList.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You aren't targeting anyone.", "4CONT4CONT1DY2DDW10CONT6CONT1DY", "", true, true));
            }
        }

        // Message - Try moving a bit closer. //
        else if(targetCheck
        && recruitList.isEmpty()
        && outOfRangeCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Try moving a bit closer.", "4CONT7CONT2W4CONT6CONT1DY", "", true, true));
            }
        }

        // Message - There is no one here. //
        else if(!targetCheck
        && recruitList.isEmpty()
        && parentEntity.location.room.mobList.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("There is no one here.", "6CONT3CONT3CONT4CONT4CONT1DY", "", true, true));
            }
        }

        // Message - They don't seem too eager to join you. //
        else if(recruitList.isEmpty()
        && inCombatWithTargetCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("They don't seem too eager to join you.", "5CONT3CONT1DY2DDW5CONT4CONT6CONT3CONT5CONT3CONT1DY", "", true, true));
            }
        }

        // Message - They're already in your group. //
        else if(!targetCheck
        && recruitList.isEmpty()
        && alreadyInGroupCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("They're already in your group.", "4CONT1DY3DDW8CONT3CONT5CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You don't see them. //
        else if(!targetCheck
        && recruitList.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You don't see them.", "4CONT3CONT1DY2DDW4CONT4CONT1DY", "", true, true));
            }
        }

        // Message - Some new members join the group. //
        else if(!recruitList.isEmpty()
        && multipleMobTypes == true) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Some new members join the group.", "5CONT4CONT8CONT5CONT4CONT5CONT1DY", "", true, true));
            }
        }

        // Message - Mob joins the group. //
        else if(!recruitList.isEmpty()) {
            if(parentEntity.isPlayer) {
                Mob targetMob = recruitList.get(0);
                GameScreen.userInterface.console.writeToConsole(new Line(targetMob.prefix + targetMob.name.label + " joins the group.", String.valueOf(targetMob.prefix.length()) + "CONT" + targetMob.name.colorCode + "1W6CONT4CONT5CONT1DY", "", true, true));
            }
        }
    }
}
