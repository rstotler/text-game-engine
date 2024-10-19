package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Search extends Action {
    public Search(Mob parentEntity) {
        super(parentEntity);
    }

    public Search() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("search", "searc", "sear", "sea").contains(inputList.get(0))) {
            Search searchAction = new Search(parentEntity);

            // Search Direction //
            if(inputList.size() == 2
            && !Location.getDirectionFromSubstring(inputList.get(1)).isEmpty()) {
                searchAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
            }

            // Search Target //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                searchAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                GameScreen.userInterface.console.writeToConsole(new Line("Search where?", "7CONT5CONT1DY", "", true, true));
                searchAction.parentEntity = null;
            }

            return searchAction;
        }

        return null;
    }

    public void initiate() {
        if(!targetDirection.isEmpty()) {initiateSearchDirection();}
        else {initiateSearchTarget();}
    }

    public void initiateSearchDirection() {
        boolean searchCheck = false;

        // Search Direction //
        if(parentEntity.location.room.exitMap.containsKey(targetDirection)
        && parentEntity.location.room.exitMap.get(targetDirection) != null
        && parentEntity.location.room.hiddenExitMap.containsKey(targetDirection)
        && parentEntity.location.room.hiddenExitMap.get(targetDirection) != null
        && !parentEntity.location.room.hiddenExitMap.get(targetDirection).isOpen) {
            parentEntity.location.room.hiddenExitMap.get(targetDirection).isOpen = true;
            searchCheck = true;
        }

        // Message - Your search reveals nothing unusual. //
        if(!searchCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Your search reveals nothing unusual.", "5CONT7CONT8CONT8CONT7CONT1DY", "", true, true));
            }
        }

        // Message - Your search reveals a hidden exit to the Direction! //
        else {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Your search reveals a hidden exit to the " + targetDirection.toLowerCase() + "!", "5CONT7CONT8CONT2W7CONT5CONT3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT1DY", "", true, true));
            }
        }
    }

    public void initiateSearchTarget() {

        // Message - Your search reveals nothing unusual. //
        if(parentEntity.isPlayer) {
            GameScreen.userInterface.console.writeToConsole(new Line("Your search reveals nothing unusual.", "5CONT7CONT8CONT8CONT7CONT1DY", "", true, true));
        }
    }
}
