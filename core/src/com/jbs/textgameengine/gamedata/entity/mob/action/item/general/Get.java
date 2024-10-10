package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Get extends Action {
    public String targetContainerString;
    public boolean containerAllCheck;

    public Get(Mob parentEntity) {
        super(parentEntity);

        targetContainerString = "";
        containerAllCheck = false;
    }

    public Get() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("get", "ge", "g").contains(inputList.get(0))) {
            Get getAction = new Get(parentEntity);

            // Get All Item From All (Loot Item From All) //
            if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && inputList.get(inputList.size() - 2).equals("from")
            && inputList.get(inputList.size() - 1).equals("all")) {
                getAction.allCheck = true;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                getAction.containerAllCheck = true;
            }

            // Get All Item From Container (Loot Item From Container) //
            else if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && inputList.contains("from")
            && !inputList.get(2).equals("from")
            && !inputList.get(inputList.size() - 1).equals("from")) {
                getAction.allCheck = true;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get All From All (Loot All) //
            else if(inputList.size() == 4
            && inputList.get(1).equals("all")
            && inputList.get(2).equals("from")
            && inputList.get(3).equals("all")) {
                getAction.allCheck = true;
                getAction.containerAllCheck = true;
            }

            // Get All From Container (Loot Container) //
            else if(inputList.size() >= 4
            && inputList.get(1).equals("all")
            && inputList.get(2).equals("from")) {
                getAction.allCheck = true;
                int fromIndex = inputList.indexOf("from");
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get # Item From All //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && inputList.get(inputList.size() - 2).equals("from")
            && inputList.get(inputList.size() - 1).equals("all")) {
                getAction.targetCount = Integer.valueOf(inputList.get(1));
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                getAction.containerAllCheck = true;
            }

            // Get # Item From Container //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && inputList.contains("from")
            && !inputList.get(2).equals("from")
            && !inputList.get(inputList.size() - 1).equals("from")) {
                getAction.targetCount = Integer.valueOf(inputList.get(1));
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(2, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get Item From All //
            else if(inputList.size() >= 4
            && inputList.get(inputList.size() - 2).equals("from")
            && inputList.get(inputList.size() - 1).equals("all")) {
                getAction.targetCount = 1;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(1, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                getAction.containerAllCheck = true;
            }

            // Get Item From Container //
            else if(inputList.size() >= 4
            && inputList.contains("from")
            && !inputList.get(1).equals("from")
            && !inputList.get(inputList.size() - 1).equals("from")) {
                getAction.targetCount = 1;
                int fromIndex = inputList.indexOf("from");
                List<String> targetEntityStringList = inputList.subList(1, fromIndex);
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(fromIndex + 1, inputList.size());
                getAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Get All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                getAction.allCheck = true;
            }

            // Get All Item //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                getAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Get # Item //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                getAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Get Item //
            else if(inputList.size() >= 2) {
                getAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                getAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Get what?", "4CONT4CONT1DY", "", true, true));
                getAction.parentEntity = null;
            }

            return getAction;
        }

        return null;
    }

    public void initiate() {

    }
}
