package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class Recruit extends Action {
    public Recruit(Mob parentEntity) {
        super(parentEntity);
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

            // Recruit Targets/List //
            else if(inputList.size() == 2
            && (inputList.get(1).equals("target") || inputList.get(1).equals("targets") || inputList.get(1).equals("list"))) {
                recruitAction.allCheck = true;
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
            }

            return recruitAction;
        }

        return null;
    }

    public void initiate() {

        // Recruit/Tame Mob //

        // Message - It's too dark to see. //
        if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - There is no one here. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - You aren't targeting anyone. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - You don't see them. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - You can't recruit while you are fighting! //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - They have already been recruited. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - You welcome some new members into the group. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - You tame some wild beasts into following you. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - You welcome Mob to the group. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }

        // Message - You tame Mob into following you. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("", "", "", true, true));
            }
        }
    }
}
