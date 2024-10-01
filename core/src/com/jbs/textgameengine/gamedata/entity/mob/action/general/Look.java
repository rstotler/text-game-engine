package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Look extends Action {
    public Look(Mob parentEntity) {
        super(parentEntity);
    }

    public Look() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("look", "loo", "lo", "l", "examine", "examin", "exami", "exam", "exa", "ex").contains(inputList.get(0))) {
            Look lookAction = new Look(parentEntity);

            // Look Direction //
            if(inputList.size() == 2 && Location.directionList.contains(inputList.get(1))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                lookAction.actionType = "Look Direction";
            }

            // Look Direction # //
            else if(inputList.size() == 3 && Location.directionList.contains(inputList.get(1)) && Utility.isInteger(inputList.get(2))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                lookAction.targetCount = Integer.valueOf(inputList.get(2));
                lookAction.actionType = "Look Direction #";
            }

            // Look Direction # Entity //
            else if(inputList.size() >= 4 && Location.directionList.contains(inputList.get(1)) && Utility.isInteger(inputList.get(2))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                lookAction.targetCount = Integer.valueOf(inputList.get(2));
                List<String> targetEntityStringList = inputList.subList(3, inputList.size());
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Direction # Entity";
            }

            // Look Direction Entity //
            else if(inputList.size() >= 3 && Location.directionList.contains(inputList.get(1))) {
                lookAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Direction Entity";
            }

            // Look Entity In Container //
            else if(inputList.size() >= 4 && inputList.contains("in") && !inputList.get(1).equals("in") && !inputList.get(inputList.size() - 1).equals("in")) {
                int inIndex = inputList.indexOf("in");
                List<String> targetEntityStringList = inputList.subList(1, inIndex);
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                List<String> targetContainerStringList = inputList.subList(inIndex + 1, inputList.size());
                lookAction.targetContainerString = targetContainerStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Entity In Container";
            }

            // Look Entity //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                lookAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                lookAction.actionType = "Look Entity";
            }

            // Look //
            else if(inputList.size() == 1 && Arrays.asList("look", "loo", "lo", "l").contains(inputList.get(0))) {
                lookAction.actionType = "Look";
            }

            // Look Target //
            else if(inputList.size() == 1 && !parentEntity.targetList.isEmpty()) {
                lookAction.actionType = "Look Target";
            }

            else {
                userInterface.console.writeToConsole(new Line("Examine what?", "8CONT4CONT1DY", "", true, true));
                return null;
            }

            return lookAction;
        }

        return null;
    }

    public void initiate() {
        if(actionType.equals("Look Direction")) {
        }

        else if(actionType.equals("Look Direction #")) {
        }

        else if(actionType.equals("Look Direction # Entity")) {
        }

        else if(actionType.equals("Look Direction Entity")) {
        }

        else if(actionType.equals("Look Entity In Container")) {
        }

        else if(actionType.equals("Look Entity")) {
        }

        else if(actionType.equals("Look")) {
            if(parentEntity.location != null
            && parentEntity.location.room != null) {
                if(parentEntity.isPlayer) {
                    parentEntity.location.room.display();
                }
            }
        }

        else if(actionType.equals("Look Target")) {
        }
    }
}
