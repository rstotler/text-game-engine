package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.action.menu.Inventory;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Drop extends Action {
    public Drop(Mob parentEntity) {
        super(parentEntity);
    }

    public Drop() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("drop", "dro", "dr").contains(inputList.get(0))) {
            Drop dropAction = new Drop(parentEntity);

            String targetPocketString = "";
            if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                List<String> targetPocketStringList = inputList.subList(2, inputList.size());
                targetPocketString = targetPocketStringList.stream().collect(Collectors.joining(" "));
                targetPocketString = targetPocketString.substring(0, 1).toUpperCase() + targetPocketString.substring(1, targetPocketString.length()).toLowerCase();
            }

            // Drop All //
            if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
            }

            // Drop All Pocket //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")
            && Inventory.inventoryNameKeyMap.containsKey(targetPocketString)) {
            }

            // Drop All Item //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
            }

            // Drop # Item //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
            }

            // Drop Item //
            else if(inputList.size() >= 2) {
            }

            else {
                userInterface.console.writeToConsole(new Line("Drop what?", "5CONT4CONT1DY", "", true, true));
                dropAction.parentEntity = null;
            }

            return dropAction;
        }

        return null;
    }

    public void initiate() {
    }
}
