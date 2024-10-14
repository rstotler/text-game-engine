package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Remove extends Action {
    public String targetGearSlot;
    public int targetGearSlotIndex;

    public Remove(Mob parentEntity) {
        super(parentEntity);

        targetGearSlot = "";
        targetGearSlotIndex = -1;
    }

    public Remove() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("remove", "remov", "remo", "rem", "re").contains(inputList.get(0))) {
            Remove removeAction = new Remove(parentEntity);

            // Get Target GearSlot Data //
            String targetGearSlot = "";
            String targetGearSlotRaw = "";
            if(inputList.size() == 3 && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                targetGearSlotRaw = inputList.get(inputList.size() - 2).substring(0, 1).toUpperCase() + inputList.get(inputList.size() - 2).substring(1);
            }
            else if(inputList.size() == 2) {
                targetGearSlotRaw = inputList.get(inputList.size() - 1).substring(0, 1).toUpperCase() + inputList.get(inputList.size() - 1).substring(1);
            }
            if(Mob.gearSlotList.contains(targetGearSlotRaw)) {targetGearSlot = targetGearSlotRaw;}

            // Remove All //
            if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                removeAction.allCheck = true;
            }

            // Remove All Entity //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                removeAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                removeAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Remove GearSlot # //
            else if(inputList.size() == 3
            && !targetGearSlot.isEmpty()
            && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                removeAction.targetCount = 1;
                removeAction.targetGearSlot = targetGearSlot;
                removeAction.targetGearSlotIndex = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Remove GearSlot //
            else if(inputList.size() == 2
            && !targetGearSlot.isEmpty()) {
                removeAction.targetCount = 1;
                removeAction.targetGearSlot = targetGearSlot;
            }

            // Remove # Entity //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                removeAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                removeAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Remove Entity //
            else if(inputList.size() >= 2) {
                removeAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                removeAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line("Remove what?", "7CONT4CONT1DY", "", true, true));
                removeAction.parentEntity = null;
            }

            return removeAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<Item> removeGearList = new ArrayList<>();
        boolean removeWeaponCheck = false;

        // Get Target GearSlot Data //
        if(!targetGearSlot.isEmpty()) {
            if(Arrays.asList("Neck", "Ring").contains(targetGearSlot)) {
                if(targetGearSlotIndex == 1 || targetGearSlotIndex == 2) {
                    targetGearSlot = targetGearSlot + " " + String.valueOf(targetGearSlotIndex);
                } else if(parentEntity.gear.containsKey(targetGearSlot + " 1")
                && parentEntity.gear.containsKey(targetGearSlot + " 2")) {
                    if(parentEntity.gear.get(targetGearSlot + " 1") != null
                    || parentEntity.gear.get(targetGearSlot + " 2") == null) {
                        targetGearSlot = targetGearSlot + " 1";
                    } else if(parentEntity.gear.get(targetGearSlot + " 2") != null) {
                        targetGearSlot = targetGearSlot + " 2";
                    }
                }
            }
            else if(Arrays.asList("Main", "Off").contains(targetGearSlot)) {
                targetGearSlot = targetGearSlot + " Hand";
            }
        }

        // Remove Item(s) //
        for(String gearSlot : parentEntity.gear.keySet()) {
            Item gearItem = parentEntity.gear.get(gearSlot);

            if(gearItem != null) {
                if(((targetEntityString.isEmpty() && allCheck)
                || (!targetEntityString.isEmpty() && gearItem.nameKeyList.contains(targetEntityString))
                || (!targetGearSlot.isEmpty() && parentEntity.gear.containsKey(targetGearSlot) && gearSlot.equals(targetGearSlot)))) {
                    parentEntity.addItemToInventory(gearItem);
                    removeGearList.add(gearItem);
                    parentEntity.gear.put(gearSlot, null);

                    if(gearItem.type.equals("Weapon")) {
                        removeWeaponCheck = true;
                    }

                    if(targetCount >= 1 && removeGearList.size() >= targetCount) {
                        break;
                    }
                }
            }
        }

        // Message - You aren't wearing anything. //
        if(removeGearList.isEmpty()
        && ((allCheck && targetEntityString.isEmpty())
        || !targetGearSlot.isEmpty())) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You aren't wearing anything.", "4CONT4CONT1DY2DDW8CONT8CONT1DY", "", true, true));
            }
        }

        // Message - You remove all of your gear. //
        else if(removeGearList.size() > 1
        && allCheck
        && targetEntityString.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You remove all of your gear.", "4CONT7CONT4CONT3CONT5CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You can't find it. //
        else if(removeGearList.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You remove some gear. //
        else if(removeGearList.size() > 1) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You remove some gear.", "4CONT7CONT5CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You remove/stop wielding Item. //
        else if(removeGearList.size() == 1) {
            if(parentEntity.isPlayer) {
                Item removeItem = removeGearList.get(0);
                String removeWieldString = "remove ";
                if(removeWeaponCheck) {removeWieldString = "stop wielding ";}
                String removeString = "You " + removeWieldString + removeItem.prefix.toLowerCase() + removeItem.name.label + ".";
                String removeColorCode = "4CONT" + String.valueOf(removeWieldString.length()) + "CONT" + String.valueOf(removeItem.prefix.length()) + "CONT" + removeItem.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(removeString, removeColorCode, "", true, true));
            }
        }
    }
}
