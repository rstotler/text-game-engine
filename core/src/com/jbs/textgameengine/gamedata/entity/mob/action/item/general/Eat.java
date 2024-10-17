package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Eat extends Action {
    public Eat(Mob parentEntity) {
        super(parentEntity);
    }

    public Eat() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("eat", "drink", "drin", "dri").contains(inputList.get(0))) {
            Eat eatAction = new Eat(parentEntity);
            if(inputList.get(0).startsWith("e")) {eatAction.actionType = "Eat";}
            else {eatAction.actionType = "Drink";}

            // Eat/Drink TargetItem //
            if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                eatAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line(eatAction.actionType + " what?", String.valueOf(eatAction.actionType.length()) + "CONT1W4CONT1DY", "", true, true));
                eatAction.parentEntity = null;
            }

            return eatAction;
        }

        return null;
    }

    public void initiate() {
    }
}
