package com.jbs.textgameengine.gamedata.entity.mob.action.god;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Manifest extends Action {
    public Manifest(Mob parentEntity) {
        super(parentEntity);
    }

    public Manifest() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));
        ArrayList<String> entityTypeList = new ArrayList<>(Arrays.asList("mob", "item"));

        if(Arrays.asList("manifest", "manifes", "manife", "manif", "mani", "man").contains(inputList.get(0))) {
            Manifest lookAction = new Manifest(parentEntity);

            // Manifest EntityType # EntityNum //
            if(inputList.size() == 4
            && entityTypeList.contains(inputList.get(1))
            && Utility.isInteger(inputList.get(2))
            && Utility.isInteger(inputList.get(3))) {
                lookAction.actionType = inputList.get(1);
                lookAction.targetCount = Integer.valueOf(inputList.get(2));
                lookAction.targetNum = Integer.valueOf(inputList.get(3));
                lookAction.parentEntity = parentEntity;
            }

            // Manifest EntityType EntityNum //
            else if(inputList.size() == 3
            && entityTypeList.contains(inputList.get(1))
            && Utility.isInteger(inputList.get(2))) {
                lookAction.actionType = inputList.get(1);
                lookAction.targetCount = 1;
                lookAction.targetNum = Integer.valueOf(inputList.get(2));
                lookAction.parentEntity = parentEntity;
            }

            else {
                lookAction.parentEntity = null;
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("Manifest what?", "9CONT4CONT1DY", "", true, true));
                }
            }

            return lookAction;
        }

        return null;
    }

    public void initiate() {
        Entity targetEntity = null;
        if(actionType.equals("mob")) {
            for(int i = 0; i < targetCount; i++) {
                Entity mob = Mob.load(targetNum, parentEntity.location);
                if(!mob.name.label.equals("Default Mob")) {
                    if(targetEntity == null) {targetEntity = mob;}
                    parentEntity.location.room.mobList.add(mob);
                }
            }
        }
        else if(actionType.equals("item")) {
            for(int i = 0; i < targetCount; i++) {
                Entity item = Item.load(targetNum, parentEntity.location);
                if(!item.name.label.equals("Default Item")) {
                    if(targetEntity == null) {targetEntity = item;}
                    parentEntity.location.room.addItemToRoom(item);
                }
            }
        }

        // Message - You utter an incantation, but nothing happens. //
        if(targetEntity == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You utter an incantation, but nothing happens.", "4CONT6CONT3CONT11CONT2DY4CONT8CONT7CONT1DY", "", true, true));
            }
        }

        // Message - You utter an incantation and an Entity appears in a puff of smoke. //
        else if(targetCount == 1) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You utter an incantation and " + targetEntity.prefix.toLowerCase() + targetEntity.name.label + " appears in a puff of smoke.", "4CONT6CONT3CONT12CONT4CONT" + String.valueOf(targetEntity.prefix).length() + "CONT" + targetEntity.name.colorCode + "1W8CONT3CONT2CONT5CONT3CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You utter an incantation and some EntityTypes appear in a puff of smoke. //
        else if(targetCount > 1) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You utter an incantation and some " + actionType + "s appear in a puff of smoke.", "4CONT6CONT3CONT12CONT4CONT5CONT" + String.valueOf(actionType.length() + 2) + "CONT7CONT3CONT2W5CONT3CONT5CONT1DY", "", true, true));
            }
        }
    }
}
