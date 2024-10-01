package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Target extends Action {
    public Target(Mob parentEntity) {
        super(parentEntity);
    }

    public Target() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("target", "targe", "targ", "tar", "ta", "t").contains(inputList.get(0))) {
            Target targetAction = new Target(parentEntity);

            if(false) {

            }

            else {
                userInterface.console.writeToConsole(new Line("Target who?", "7CONT3CONT1DY", "", true, true));
                return null;
            }

            return targetAction;
        }

        return null;
    }

    public void initiate() {
//        if(actionType.equals("")) {
//        }
//
//        else if(actionType.equals("")) {
//        }
    }
}
