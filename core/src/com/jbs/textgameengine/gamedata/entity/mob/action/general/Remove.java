package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Remove extends Action {
    public String gearSlot;

    public Remove(Mob parentEntity) {
        super(parentEntity);

        gearSlot = "";
    }

    public Remove() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("remove", "remov", "remo", "rem", "re").contains(inputList.get(0))) {
            Remove removeAction = new Remove(parentEntity);

            // Remove All //
            if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                removeAction.allCheck = true;
            }

            // Remove All Entity //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                removeAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                removeAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Remove Entity GearSlot //
            else if(inputList.size() >= 3
            && inputList.get(inputList.size() - 1).length() > 1
            && Mob.gearSlotList.contains(inputList.get(inputList.size() - 1).substring(0, 1).toUpperCase() + inputList.get(inputList.size() - 1).substring(1))) {
                removeAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 1);
                removeAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                removeAction.gearSlot = inputList.get(inputList.size() - 1);
            }

            // Remove # Entity //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                removeAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                removeAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Remove Entity //
            else if(inputList.size() >= 2) {
                removeAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                removeAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Remove what?", "7CONT4CONT1DY", "", true, true));
                removeAction.parentEntity = null;
            }

            return removeAction;
        }

        return null;
    }

    public void initiate() {
    }
}
