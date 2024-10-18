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

        // Search Direction //
        if(!targetDirection.isEmpty()) {
            // Message - There is nothing there. //
            // Message - Your search reveals a hidden exit to the Direction! //
        }

        // Search Target //
        else {
            // Message - You don't see it. //
            // Message - You search but don't find anything. //
            // Message - Your search reveals a hidden exit to the Direction! //
        }
    }
}
