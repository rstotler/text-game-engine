package com.jbs.textgameengine.gamedata.entity.mob.action.other;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Time extends Action {
    public Time() {
        super();
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("time", "tim", "ti").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Time();
        }

        return null;
    }

    public void initiate(Mob parentEntity) {

        // Message - You must be on a planet to do that.
        if(parentEntity.location.planetoid == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be on a planet to do that.", "4CONT5CONT3CONT3CONT2W7CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Get Current Planet Time //
        else {
            if(parentEntity.isPlayer) {
                Line timeLine = ((Planet) parentEntity.location.planetoid).getTime();
                GameScreen.userInterface.console.writeToConsole(new Line(timeLine.label, timeLine.colorCode, "", true, true));
            }
        }
    }
}
