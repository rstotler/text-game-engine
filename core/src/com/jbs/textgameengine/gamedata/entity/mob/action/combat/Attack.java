package com.jbs.textgameengine.gamedata.entity.mob.action.combat;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Attack extends Action {
    public Attack() {
        super();
    }

    public Attack getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("attack", "attac", "atta", "att", "at", "a", "kill", "kil", "ki", "k").contains(inputList.get(0))) {
            Attack attackAction = new Attack();

            // Attack Mob //
            if(inputList.size() > 1) {
                attackAction.actionType = "Attack Mob";
            }

            // Attack //
            else {
                attackAction.actionType = "Attack";
            }

            return attackAction;
        }

        return null;
    }

    public void initiate(Mob parentEntity) {

    }
}
