package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Loot extends Action {
    public String targetContainerString;
    public boolean containerAllCheck;

    public Loot(Mob parentEntity) {
        super(parentEntity);

        targetContainerString = "";
        containerAllCheck = false;
    }

    public Loot() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("loot").contains(inputList.get(0))) {
            Loot lootAction = new Loot(parentEntity);

            // Loot Item From All //
            if(inputList.size() >= 4
            && inputList.get(inputList.size() - 2).equals("from")
            && inputList.get(inputList.size() - 1).equals("all")) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 2);
                lootAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                lootAction.containerAllCheck = true;
                lootAction.actionType = "Loot Item From All";
            }

            // Loot Item From Container //
            else if(inputList.size() >= 4
            && inputList.contains("from")
            && !inputList.get(1).equals("from")
            && !inputList.get(inputList.size() - 1).equals("from")) {
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(1, fromIndex);
                lootAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                lootAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
                lootAction.actionType = "Loot Item From Container";
            }

            // Loot All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                lootAction.allCheck = true;
                lootAction.actionType = "Loot All";
            }

            // Loot Container //
            else if(inputList.size() >= 2) {
                List<String> targetContainerStringList = inputList.subList(1, inputList.size());
                lootAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
                lootAction.actionType = "Loot Container";
            }

            else {
                userInterface.console.writeToConsole(new Line("Loot what?", "5CONT4CONT1DY", "", true, true));
                lootAction.parentEntity = null;
            }

            return lootAction;
        }

        return null;
    }

    public void initiate() {
        Get getAction = new Get(parentEntity);

        // Loot Item From All (Get All Item From All) //
        if(actionType.equals("Loot Item From All")) {
            getAction.allCheck = true;
            getAction.targetEntityString = targetEntityString;
            getAction.containerAllCheck = true;
        }

        // Loot Item From Container (Get All Item From Container) //
        else if(actionType.equals("Loot Item From Container")) {
            getAction.allCheck = true;
            getAction.targetEntityString = targetEntityString;
            getAction.targetContainerString = targetContainerString;
        }

        // Loot All (Get All From All) //
        else if(actionType.equals("Loot All")) {
            getAction.allCheck = true;
            getAction.containerAllCheck = true;
        }

        // Loot Container (Get All From Container) //
        else if(actionType.equals("Loot Container")) {
            getAction.allCheck = true;
            getAction.targetContainerString = targetContainerString;
        }

        getAction.initiate();
    }
}
