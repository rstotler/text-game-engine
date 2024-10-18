package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Put extends Action {
    public String targetContainerString;

    public Put(Mob parentEntity) {
        super(parentEntity);

        targetContainerString = "";
    }

    public Put() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("put", "pu", "p").contains(inputList.get(0))) {
            Put putAction = new Put(parentEntity);

            // Put All Item In Container //
            if(inputList.size() >= 5
            && inputList.get(1).equals("all")
            && inputList.contains("in")
            && !inputList.get(2).equals("in")
            && !inputList.get(inputList.size() - 1).equals("in")) {
                putAction.allCheck = true;
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(2, inIndex);
                putAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Put All In Container //
            else if(inputList.size() >= 4
            && inputList.get(1).equals("all")
            && inputList.get(2).equals("in")) {
                putAction.allCheck = true;
                List<String> targetContainerStringList = inputList.subList(3, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Put # Item In Container //
            else if(inputList.size() >= 5
            && Utility.isInteger(inputList.get(1))
            && inputList.contains("in")
            && !inputList.get(2).equals("in")
            && !inputList.get(inputList.size() - 1).equals("in")) {
                putAction.targetCount = Integer.valueOf(inputList.get(1));
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(2, inIndex);
                putAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            // Put Item In Container //
            else if(inputList.size() >= 4
            && inputList.contains("in")
            && !inputList.get(1).equals("in")
            && !inputList.get(inputList.size() - 1).equals("in")) {
                putAction.targetCount = 1;
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(1, inIndex);
                putAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                putAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Put what in what?", "4CONT5CONT3CONT4CONT1DY", "", true, true));
                putAction.parentEntity = null;
            }

            return putAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<Item> putList = new ArrayList<>();
        Item targetContainer = null;
        boolean multipleItemTypes = false;

        // Put Item(s) In Container //

        // Message - That's not a container. //
        if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("That's not a container.", "4CONT1DY2DDW4CONT1W9CONT1DY", "", true, true));
            }
        }

        // Message - You don't have anything to put in. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You don't have anything to put in.", "4CONT3CONT1DY2DDW5CONT9CONT3CONT4CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You can't find it. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You can't put something inside itself. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't put something inside itself.", "4CONT3CONT1DY2DDW4CONT10CONT7CONT6CONT1DY", "", true, true));
            }
        }

        // Message - You can't put that in Container. //
        else if(false) {
            if(parentEntity.isPlayer) {
                String putString = "You can't put that in " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                String putColorCode = "4CONT3CONT1DY2DDW4CONT5CONT3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(putString, putColorCode, "", true, true));
            }
        }

        // Message - It won't fit. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("It won't fit.", "3CONT3CONT1DY2DDW3CONT1DY", "", true, true));
            }
        }

        // Message - You put some things in Container. //
        else if(false) {
            if(parentEntity.isPlayer) {
                String putString = "You put some things in " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                String putColorCode = "4CONT4CONT5CONT7CONT3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(putString, putColorCode, "", true, true));
            }
        }

        // Message - You put Item(s) in Container. //
        else if(false) {
            if(parentEntity.isPlayer) {
                String countString = "";
                String countColorCode = "";
                if(putList.size() > 1 && !multipleItemTypes) {
                    Line countLine = Utility.insertCommas(putList.size());
                    countString = " (" + countLine.label + ")";
                    countColorCode = "2DR" + countLine.colorCode + "1DR";
                }
                Item targetItem = putList.get(0);
                String putString = "You put " + targetItem.prefix.toLowerCase() + targetItem.name.label + countString + " in " + targetContainer.prefix.toLowerCase() + targetContainer.name.label + ".";
                String putColorCode = "4CONT4CONT" + String.valueOf(targetItem.prefix.length()) + "CONT" + targetItem.name.colorCode + countColorCode + "1W3CONT" + String.valueOf(targetContainer.prefix.length()) + "CONT" + targetContainer.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(putString, putColorCode, "", true, true));
            }
        }
    }
}
