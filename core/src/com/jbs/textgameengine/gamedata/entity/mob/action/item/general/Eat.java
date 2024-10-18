package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
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
        Item targetItem = null;
        boolean cantConsumeCheck = false;
        int deleteIndex = -1;

        // Eat/Drink Item //
        for(int i = 0; i < parentEntity.inventory.get("Food").size(); i++) {
            Item item = parentEntity.inventory.get("Food").get(i);
            if(item.nameKeyList.contains(targetEntityString)) {
                targetItem = item;

                if(!item.type.equals("Food") && !item.type.equals("Drink")) {
                    cantConsumeCheck = true;
                }
                else {
                    if(item.type.equals("Food")) {
                        deleteIndex = i;
                    }
                    cantConsumeCheck = false;
                    break;
                }
            }
        }

        // Remove Item From Inventory //
        if(deleteIndex != -1) {
            parentEntity.inventory.get("Food").remove(deleteIndex);
        }

        // Message - You can't find it. //
        if(targetItem == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You can't eat/drink that. //
        else if(cantConsumeCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't " + actionType.toLowerCase() + " that.", "4CONT3CONT1DY2DDW" + String.valueOf(actionType.length()) + "CONT1W4CONT1DY", "", true, true));
            }
        }

        // Message - You eat/take a drink from Item. //
        else {
            if(parentEntity.isPlayer) {
                String eatDrinkString = "eat ";
                String eatDrinkColorCode = "4CONT";
                if(actionType.equals("Drink")) {
                    eatDrinkString = "take a drink from ";
                    eatDrinkColorCode = "5CONT2W6CONT5CONT";
                }
                GameScreen.userInterface.console.writeToConsole(new Line("You " + eatDrinkString + targetItem.prefix.toLowerCase() + targetItem.name.label + ".", "4CONT" + eatDrinkColorCode + String.valueOf(targetItem.prefix.length()) + "CONT" + targetItem.name.colorCode + "1DY", "", true, true));
            }
        }
    }
}
