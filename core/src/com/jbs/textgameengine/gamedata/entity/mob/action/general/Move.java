package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Move extends Action {
    public Move() {
        super();
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Location.directionList.contains(inputList.get(0))) {
            Move moveAction = new Move();

            // Direction # //
            if(inputList.size() == 2 && Utility.isInteger(inputList.get(1))) {
                moveAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                moveAction.directionCount = Integer.valueOf(inputList.get(1));
                moveAction.actionType = "Direction #";
            }

            // Direction //
            else if(inputList.size() == 1) {
                moveAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(0));
                moveAction.actionType = "Direction";
            }

            else {
                userInterface.console.writeToConsole(new Line("Move where?", "5CONT5CONT1DY", "", true, true));
                return null;
            }

            return moveAction;
        }

        return null;
    }

    public void initiate(Mob parentEntity) {
        if(actionType.equals("Direction #")) {
        }

        else if(actionType.equals("Direction")) {

        }
    }
}
