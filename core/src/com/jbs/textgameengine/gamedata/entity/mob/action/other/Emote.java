package com.jbs.textgameengine.gamedata.entity.mob.action.other;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Emote extends Action {
    public Emote(String actionType) {
        super();

        this.actionType = actionType;
    }

    public Emote() {
        super();
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("hmm", "hm").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote("Hmm");
        }

        else if(Arrays.asList("nod").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote("Nod");
        }

        else if(Arrays.asList("nodnod").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote("Nodnod");
        }

        return null;
    }

    public void initiate(Mob parentEntity) {
        if(actionType.equals("Hmm")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You scratch your chin and go, 'Hmm..'", "4CONT8CONT5CONT5CONT4CONT2CONT3DY5CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Nod")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You nod your head in agreement.", "4CONT4CONT5CONT5CONT3CONT9CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Nodnod")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You nodnod.", "4CONT6CONT1DY", "", true, true));
            }
        }
    }
}
