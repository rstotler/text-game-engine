package com.jbs.textgameengine.gamedata.entity.mob.action.combat;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Attack extends Action {
    public Attack(Mob parentEntity) {
        super(parentEntity);
    }

    public Attack() {
        this(null);
    }

    public Attack getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("attack", "attac", "atta", "att", "at", "a", "kill", "kil", "ki", "k").contains(inputList.get(0))) {
            Attack attackAction = new Attack(parentEntity);

            // Attack All //
            if(inputList.size() == 2 && inputList.get(1).equals("all")) {
                attackAction.allCheck = true;
                attackAction.actionType = "Attack All";
            }

            // Attack Target //
            else if(inputList.size() > 1) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                attackAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                attackAction.actionType = "Attack Target";
            }

            // Attack //
            else {
                attackAction.actionType = "Attack";
            }

            return attackAction;
        }

        return null;
    }

    public void initiate() {
    }
}
