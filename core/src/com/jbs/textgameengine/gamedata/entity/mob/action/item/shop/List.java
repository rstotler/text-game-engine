package com.jbs.textgameengine.gamedata.entity.mob.action.item.shop;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class List extends Action {
    public List(Mob parentEntity) {
        super(parentEntity);
    }

    public List() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("list", "lis", "li").contains(inputList.get(0))) {
            List listAction = new List(parentEntity);

            if(inputList.size() >= 2) {
                java.util.List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                listAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            return listAction;
        }

        return null;
    }

    public void initiate() {

        // Message - There is no shop around. //
        if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("There is no shop around.", "6CONT3CONT3CONT5CONT6CONT1DY", "", true, true));
            }
        }

        // Display Shop //
        else {

            // Display Shop Inventory Type List //

            // Display Target Shop Inventory //
        }
    }
}
