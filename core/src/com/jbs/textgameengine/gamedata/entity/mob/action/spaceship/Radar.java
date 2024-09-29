package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Radar extends Action {
    public Radar() {
        super();
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("radar", "rada", "rad").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Radar();
        }

        return null;
    }

    public void initiate(Mob parentEntity) {

        // Message - You must be in a cockpit to do that. //
        if(parentEntity.location.spaceship == null
        || parentEntity.location.spaceship.cockpitRoom != parentEntity.location.room) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be in a cockpit to do that.", "4CONT5CONT3CONT3CONT2W8CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Radar //
        else {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You look at the ship's radar screen.", "4CONT5CONT3CONT4CONT4CONT1DY2DW6CONT6CONT1DY", "", true, true));
            }
        }
    }
}
