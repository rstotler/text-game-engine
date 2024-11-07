package com.jbs.textgameengine.gamedata.entity.mob.action.item.skill;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.item.type.Seed;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Plant extends Action {
    public Plant(Mob parentEntity) {
        super(parentEntity);
    }

    public Plant() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("plant", "plan", "pla", "pl").contains(inputList.get(0))) {
            Plant plantAction = new Plant(parentEntity);

            // Plant All //
            if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                plantAction.allCheck = true;
            }

            // Plant All Item //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                plantAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                plantAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Plant # Item //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                plantAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                plantAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Plant Item //
            else if(inputList.size() >= 2) {
                plantAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                plantAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Plant what?", "6CONT4CONT1DY", "", true, true));
                plantAction.parentEntity = null;
            }

            return plantAction;
        }

        return null;
    }

    public void initiate() {
        Seed targetSeed = null;
        boolean multipleItemTypes = false;
        boolean cantPlantCheck = false;
        boolean breakCheck = false;
        int plantCount = 0;

        for(String pocket : parentEntity.inventory.keySet()) {
            ArrayList<Integer> deleteIndexList = new ArrayList<>();
            for(int i = 0; i < parentEntity.inventory.get(pocket).size(); i++) {
                Item item = parentEntity.inventory.get(pocket).get(i);

                if((targetEntityString.isEmpty() && allCheck)
                || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))) {
                    if(!item.type.equals("Seed")) {
                        cantPlantCheck = true;
                    }
                    else {
                        int itemQuantity = item.quantity;
                        int quantityRemainder = 0;
                        if(targetCount != -1 && itemQuantity + plantCount > targetCount) {
                            quantityRemainder = (itemQuantity + plantCount) - targetCount;
                            itemQuantity -= quantityRemainder;
                        }
                        for(int ii = 0; ii < itemQuantity; ii++) {
                            Item quantityItem = Item.load("Seed", item.id, item.location, 1);
                            parentEntity.location.room.plantedSeedList.add((Seed) quantityItem);
                        }

                        if(quantityRemainder == 0) {deleteIndexList.add(0, i);}
                        else {item.quantity = quantityRemainder;}
                        plantCount += itemQuantity;

                        if(targetSeed == null) {targetSeed = (Seed) item;}
                        else if(targetSeed.id != item.id || !targetSeed.type.equals(item.type)) {multipleItemTypes = true;}

                        if(targetCount != -1 && plantCount >= targetCount) {
                            breakCheck = true;
                            break;
                        }
                    }
                }
            }
            for(int deleteIndex : deleteIndexList) {
                parentEntity.inventory.get(pocket).remove(deleteIndex);
            }

            if(breakCheck) {break;}
        }

        // Message - The ground is too hard to plant anything here. //
        if(targetSeed == null
        && !Arrays.asList("Dirt", "Soil").contains(parentEntity.location.room.groundType)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The ground is too hard to plant anything here.", "4CONT7CONT3CONT4CONT5CONT3CONT6CONT9CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You can't plant that. //
        else if(targetSeed == null
        && cantPlantCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't plant that.", "4CONT3CONT1DY2DDW6CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You can't find it. //
        else if(targetSeed == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You plant some seeds in the ground. //
        else if(multipleItemTypes) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You plant some seeds in the ground.", "4CONT6CONT5CONT6CONT3CONT4CONT6CONT1DY", "", true, true));
            }
        }

        // Message - You plant Seed in the ground. //
        else if(targetSeed != null) {
            if(parentEntity.isPlayer) {
                String countString = "";
                String countColorCode = "";
                if(plantCount > 1) {
                    Line countLine = Utility.insertCommas(plantCount);
                    countString = " (" + countLine.label + ")";
                    countColorCode = "2DR" + countLine.colorCode + "1DR";
                }

                GameScreen.userInterface.console.writeToConsole(new Line("You plant " + targetSeed.prefix.toLowerCase() + targetSeed.name.label + countString + " in the ground.", "4CONT6CONT" + String.valueOf(targetSeed.prefix.length()) + "CONT" + targetSeed.name.colorCode + countColorCode + "1DW3CONT4CONT6CONT1DY", "", true, true));
            }
        }
    }
}
