package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.item.type.Gear;
import com.jbs.textgameengine.gamedata.entity.item.type.weapon.Weapon;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Wear extends Action {
    public String targetGearSlot;
    public int targetGearSlotIndex;

    public Wear(Mob parentEntity) {
        super(parentEntity);

        targetGearSlot = "";
        targetGearSlotIndex = -1;
    }

    public Wear() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("wear", "wea", "wield", "wiel", "wie", "wi").contains(inputList.get(0))) {
            Wear wearAction = new Wear(parentEntity);
            wearAction.actionType = "Wear";
            if(inputList.get(0).startsWith("wi")) {wearAction.actionType = "Wield";}

            // Get Target GearSlot Data //
            String targetGearSlot = "";
            String targetGearSlotRaw = "";
            if(inputList.size() >= 4 && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                targetGearSlotRaw = inputList.get(inputList.size() - 2).substring(0, 1).toUpperCase() + inputList.get(inputList.size() - 2).substring(1);
            }
            else if(inputList.size() >= 3) {
                targetGearSlotRaw = inputList.get(inputList.size() - 1).substring(0, 1).toUpperCase() + inputList.get(inputList.size() - 1).substring(1);
            }
            if(Mob.gearSlotList.contains(targetGearSlotRaw)) {targetGearSlot = targetGearSlotRaw;}

            // Wear/Wield Item GearSlot # //
            if(inputList.size() >= 4
            && !targetGearSlot.isEmpty()
            && Utility.isInteger(inputList.get(inputList.size() - 1))) {
                wearAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 2);
                wearAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                wearAction.targetGearSlot = targetGearSlot;
                wearAction.targetGearSlotIndex = Integer.valueOf(inputList.get(inputList.size() - 1));
            }

            // Wear/Wield Item GearSlot //
            else if(inputList.size() >= 3
            && !targetGearSlot.isEmpty()) {
                wearAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size() - 1);
                wearAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
                wearAction.targetGearSlot = targetGearSlot;
            }

            // Wear/Wield All //
            else if(inputList.size() == 2
            && inputList.get(1).equals("all")) {
                wearAction.allCheck = true;
            }

            // Wear/Wield All Item //
            else if(inputList.size() >= 3
            && inputList.get(1).equals("all")) {
                wearAction.allCheck = true;
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                wearAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Wear/Wield # Item //
            else if(inputList.size() >= 3
            && Utility.isInteger(inputList.get(1))) {
                wearAction.targetCount = Integer.valueOf(inputList.get(1));
                List<String> targetEntityStringList = inputList.subList(2, inputList.size());
                wearAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            // Wear/Wield Item //
            else if(inputList.size() >= 2) {
                wearAction.targetCount = 1;
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                wearAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line(wearAction.actionType + " what?", String.valueOf(wearAction.actionType.length() + 1) + "CONT4CONT1DY", "", true, true));
                wearAction.parentEntity = null;
            }

            return wearAction;
        }

        return null;
    }

    public void initiate() {
        boolean cantWearCheck = false;
        boolean wearGearCheck = false;
        boolean breakCheck = false;
        String wearGearSlot = "";
        ArrayList<Item> wearGearList = new ArrayList<>();
        ArrayList<Item> removeGearList = new ArrayList<>();

        // Wear/Wield Item(s) //
        ArrayList<ArrayList<Item>> itemListArray = new ArrayList<>();
        itemListArray.add(parentEntity.inventory.get("Gear"));
        itemListArray.add(parentEntity.inventory.get("Weapons"));

        for(ArrayList<Item> targetInventoryItemList : itemListArray) {
            ArrayList<Integer> deleteIndexList = new ArrayList<>();
            for(int i = 0; i < targetInventoryItemList.size(); i++) {
                Item item = targetInventoryItemList.get(i);

                if((targetEntityString.isEmpty() && allCheck)
                || (!targetEntityString.isEmpty() && item.nameKeyList.contains(targetEntityString))) {

                    // Get Target Gear Slot //
                    String gearSlot = "";
                    if(item.type.equals("Gear")) {
                        String gearSlotPrefix = ((Gear) item).gearSlot;
                        if(Arrays.asList("Neck", "Ring").contains(gearSlotPrefix)) {
                            if(targetGearSlotIndex == 1 || targetGearSlotIndex == 2) {
                                gearSlot = gearSlotPrefix + " " + String.valueOf(targetGearSlotIndex);
                            } else if(parentEntity.gear.containsKey(gearSlotPrefix + " 1")
                            && parentEntity.gear.containsKey(gearSlotPrefix + " 2")) {
                                if(parentEntity.gear.get(gearSlotPrefix + " 1") == null
                                || parentEntity.gear.get(gearSlotPrefix + " 2") != null) {
                                    gearSlot = gearSlotPrefix + " 1";
                                } else {
                                    gearSlot = gearSlotPrefix + " 2";
                                }
                            }
                        }
                        else {
                            gearSlot = ((Gear) item).gearSlot;
                        }
                    }
                    else if(item.type.equals("Weapon")) {
                        if(targetGearSlot.isEmpty()) {
                            if(parentEntity.gear.get("Main Hand") == null || parentEntity.gear.get("Off Hand") != null) {
                                gearSlot = "Main Hand";
                            } else if(parentEntity.gear.get("Off Hand") == null) {
                                gearSlot = "Off Hand";
                            }
                        }
                        else {
                            if(Arrays.asList("Main", "Off").contains(targetGearSlot)) {
                                gearSlot = targetGearSlot + " Hand";
                            }
                        }

                        // Target Weapon Is Two-Handed //
                        if(((Weapon) item).twoHanded && targetCount == 1) {
                            if(((Weapon) item).noDualWield
                            || !parentEntity.canDualWield()
                            || (parentEntity.gear.get("Main Hand") != null && ((Weapon) parentEntity.gear.get("Main Hand")).noDualWield && targetGearSlot.isEmpty())) {
                                gearSlot = "Main Hand";
                            }
                        }

                        // Target Weapon Is One-Handed (And Current Weapon Is Two-Handed) //
                        else if(!((Weapon) item).twoHanded && targetCount == 1) {
                            if(targetGearSlot.isEmpty()
                            && parentEntity.gear.get("Main Hand") != null
                            && (((Weapon) parentEntity.gear.get("Main Hand")).noDualWield || (((Weapon) parentEntity.gear.get("Main Hand")).twoHanded && !parentEntity.canDualWield()))) {
                                gearSlot = "Main Hand";
                            }
                        }
                    }

                    if((!item.type.equals("Gear") && !item.type.equals("Weapon"))
                    || (!item.type.equals("Weapon") && actionType.equals("Wield"))
                    || !parentEntity.gear.containsKey(gearSlot)) {
                        cantWearCheck = true;
                    }
                    else if(item.type.equals("Weapon")
                    && ((Weapon) item).twoHanded
                    && (allCheck || targetCount > 1)
                    && ((((Weapon) item).noDualWield && (parentEntity.gear.get("Main Hand") != null || parentEntity.gear.get("Off Hand") != null))
                    || (parentEntity.gear.get("Main Hand") != null && ((Weapon) parentEntity.gear.get("Main Hand")).noDualWield)
                    || ((parentEntity.gear.get("Main Hand") != null && ((Weapon) parentEntity.gear.get("Main Hand")).twoHanded && !parentEntity.canDualWield()))
                    || (parentEntity.gear.get("Off Hand") != null && !parentEntity.canDualWield()))) {
                        // Do Nothing
                    }
                    else if(item.type.equals("Weapon")
                    && !((Weapon) item).twoHanded
                    && (allCheck || targetCount > 1)
                    && ((parentEntity.gear.get("Main Hand") != null && ((Weapon) parentEntity.gear.get("Main Hand")).twoHanded && ((Weapon) parentEntity.gear.get("Main Hand")).noDualWield)
                    || (parentEntity.gear.get("Main Hand") != null && ((Weapon) parentEntity.gear.get("Main Hand")).twoHanded && parentEntity.canDualWield()))) {
                        // Do Nothing
                    }
                    else if((((allCheck || targetCount > 1) && parentEntity.gear.get(gearSlot) == null)
                    || targetCount == 1)) {

                        // Remove Previous Item //
                        Item previousItem = null;
                        if(parentEntity.gear.containsKey(gearSlot)
                        && parentEntity.gear.get(gearSlot) != null) {
                            previousItem = parentEntity.gear.get(gearSlot);
                            parentEntity.addItemToInventory(previousItem);
                            removeGearList.add(previousItem);
                        }

                        // NoDualWield Removals //
                        if(item.type.equals("Weapon")) {

                            // Remove NoDualWield OR CantDualWield Main Hand Weapon If Target Gear Slot Is Off-Hand //
                            if(gearSlot.equals("Off Hand")
                            && parentEntity.gear.get("Main Hand") != null
                            && (((Weapon) parentEntity.gear.get("Main Hand")).noDualWield || (((Weapon) parentEntity.gear.get("Main Hand")).twoHanded && !parentEntity.canDualWield()))) {
                                parentEntity.addItemToInventory(parentEntity.gear.get("Main Hand"));
                                removeGearList.add(parentEntity.gear.get("Main Hand"));
                                parentEntity.gear.put("Main Hand", null);
                            }

                            // Remove Off-Hand When Wearing A NoDualWield Weapon OR ParentEntity CAN'T Dual Wield //
                            else if(gearSlot.equals("Main Hand")
                            && (((Weapon) item).noDualWield || (((Weapon) item).twoHanded && !parentEntity.canDualWield()))
                            && parentEntity.gear.get("Off Hand") != null) {
                                parentEntity.addItemToInventory(parentEntity.gear.get("Off Hand"));
                                removeGearList.add(parentEntity.gear.get("Off Hand"));
                                parentEntity.gear.put("Off Hand", null);
                            }
                        }

                        // Wear Target Item //
                        parentEntity.gear.put(gearSlot, item);
                        if(wearGearSlot.isEmpty()) {wearGearSlot = gearSlot;}
                        deleteIndexList.add(0, i);
                        wearGearList.add(item);
                        if(item.type.equals("Gear")) {wearGearCheck = true;}

                        if(targetCount >= 1 && wearGearList.size() >= targetCount) {
                            breakCheck = true;
                            break;
                        }
                    }
                }
            }

            for(int i : deleteIndexList) {
                targetInventoryItemList.remove(i);
            }

            if(breakCheck) {break;}
        }

        // Message - You can't wear that. (Non-Gear, Non-Weapon Items) //
        if(wearGearList.isEmpty()
        && cantWearCheck
        && actionType.equals("Wear")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't wear that.", "4CONT3CONT1DY2DDW5CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You can't wield that there. (Wrong Gear Slot) //
        else if(wearGearList.isEmpty()
        && cantWearCheck
        && actionType.equals("Wield")
        && !targetGearSlot.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't wield that there.", "4CONT3CONT1DY2DDW6CONT5CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You can't wield that. (Non-Weapon Items) //
        else if(wearGearList.isEmpty()
        && cantWearCheck
        && actionType.equals("Wield")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't wield that.", "4CONT3CONT1DY2DDW6CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You are already wearing something. (Wear All, Wear All Item) //
        else if(wearGearList.isEmpty()
        && allCheck
        && !parentEntity.inventory.get("Gear").isEmpty()
        && actionType.equals("Wear")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You are already wearing something.", "4CONT4CONT8CONT8CONT9CONT1DY", "", true, true));
            }
        }

        // Message - You are already holding something. (Wield All, Wield All Item) //
        else if(wearGearList.isEmpty()
        && allCheck
        && !parentEntity.inventory.get("Weapons").isEmpty()
        && actionType.equals("Wield")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You are already holding something.", "4CONT4CONT8CONT8CONT9CONT1DY", "", true, true));
            }
        }

        // Message - You don't have anything to wear. //
        else if(wearGearList.isEmpty()
        && parentEntity.inventory.get("Gear").isEmpty()
        && allCheck
        && actionType.equals("Wear")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You don't have anything to wear.", "4CONT3CONT1DY2DDW5CONT9CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You don't have anything to wield. //
        else if(wearGearList.isEmpty()
        && parentEntity.inventory.get("Gear").isEmpty()
        && allCheck
        && actionType.equals("Wield")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You don't have anything to wield.", "4CONT3CONT1DY2DDW5CONT9CONT3CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You can't find it. //
        else if(wearGearList.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't find it.", "4CONT3CONT1DY2DDW5CONT2CONT1DY", "", true, true));
            }
        }

        // Message - You throw on some gear. (Wear All, Wear All Item) //
        else if(wearGearList.size() > 1
        && allCheck
        && wearGearCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You throw on some gear.", "4CONT6CONT3CONT5CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You remove Item and wear/wield Item on/in your GearSlot. //
        else if(wearGearList.size() == 1
        && removeGearList.size() == 1) {
            if(parentEntity.isPlayer) {
                Item wearItem = wearGearList.get(0);
                Item removeItem = removeGearList.get(0);
                String inOnString = " on";
                if(wearItem.type.equals("Weapon")) {inOnString = " in";}

                String wearGearSlotRaw = " your " + wearGearSlot.toLowerCase();
                String wearGearSlotRawColorCode = "1W5CONT" + String.valueOf(wearGearSlot.length()) + "CONT";
                if(wearItem.type.equals("Weapon")
                && (((Weapon) wearItem).noDualWield || (((Weapon) wearItem).twoHanded && !parentEntity.canDualWield()))) {
                    wearGearSlotRaw = " both hands";
                    wearGearSlotRawColorCode = "1W5CONT5CONT";
                }
                else if(wearItem.type.equals("Gear")) {
                    if(Arrays.asList("Neck 1", "Neck 2").contains(wearGearSlot)) {
                        wearGearSlotRaw = " your " + wearGearSlot.substring(0, 4).toLowerCase();
                        wearGearSlotRawColorCode = "1W5CONT4CONT";
                    } else if(Arrays.asList("Ring 1", "Ring 2").contains(wearGearSlot)) {
                        wearGearSlotRaw = " your finger";
                        wearGearSlotRawColorCode = "1W5CONT6CONT";
                    }
                }

                String removeString = "You remove " + removeItem.prefix.toLowerCase() + removeItem.name.label + " and " + actionType.toLowerCase() + " " + wearItem.prefix.toLowerCase() + wearItem.name.label + inOnString + wearGearSlotRaw + ".";
                String removeColorCode = "4CONT7CONT" + String.valueOf(removeItem.prefix.length()) + "CONT" + removeItem.name.colorCode + "1W4CONT" + String.valueOf(actionType.length()) + "CONT1W" + String.valueOf(wearItem.prefix.length()) + "CONT" + wearItem.name.colorCode + "1W2CONT" + wearGearSlotRawColorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(removeString, removeColorCode, "", true, true));
            }
        }

        // Message - You remove your weapons and wield Item in your GearSlot. (Two-Handed Weapons) //
        else if(wearGearList.size() == 1
        && removeGearList.size() == 2
        && !wearGearCheck) {
            if(parentEntity.isPlayer) {
                Item wearItem = wearGearList.get(0);

                String wearGearSlotRaw = " your " + wearGearSlot.toLowerCase();
                String wearGearSlotRawColorCode = "1W5CONT" + String.valueOf(wearGearSlot.length()) + "CONT";
                if(wearItem.type.equals("Weapon")
                && (((Weapon) wearItem).noDualWield || (((Weapon) wearItem).twoHanded && !parentEntity.canDualWield()))) {
                    wearGearSlotRaw = " both hands";
                    wearGearSlotRawColorCode = "1W5CONT5CONT";
                }

                String removeString = "You remove your weapons and wield " + wearItem.prefix.toLowerCase() + wearItem.name.label + " in" + wearGearSlotRaw + ".";
                String removeColorCode = "4CONT7CONT5CONT8CONT4CONT6CONT" + String.valueOf(wearItem.prefix.length()) + "CONT" + wearItem.name.colorCode + "1W2CONT" + wearGearSlotRawColorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(removeString, removeColorCode, "", true, true));
            }
        }

        // Message - You wield Item in your main hand and Item in your off-hand. //
        else if(wearGearList.size() == 2
        && !wearGearCheck) {
            if(parentEntity.isPlayer) {
                Item wearItem = wearGearList.get(0);
                Item secondWearItem = wearGearList.get(1);
                String removeString = "You wield " + wearItem.prefix.toLowerCase() + wearItem.name.label + " in your main hand and " + secondWearItem.prefix.toLowerCase() + secondWearItem.name.label + " in your off-hand.";
                String removeColorCode = "4CONT6CONT" + String.valueOf(wearItem.prefix.length()) + "CONT" + wearItem.name.colorCode + "1W3CONT5CONT5CONT5CONT4CONT" + String.valueOf(secondWearItem.prefix.length()) + "CONT" + secondWearItem.name.colorCode + "1W3CONT5CONT3CONT1DY4CONT1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(removeString, removeColorCode, "", true, true));
            }
        }

        // Message - You wear/wield Item on/in your GearSlot. //
        else if(wearGearList.size() == 1) {
            if(parentEntity.isPlayer) {
                Item wearItem = wearGearList.get(0);
                String inOnString = " on";
                if(wearItem.type.equals("Weapon")) {inOnString = " in";}

                String wearGearSlotRaw = " your " + wearGearSlot.toLowerCase();
                String wearGearSlotRawColorCode = "1W5CONT" + String.valueOf(wearGearSlot.length()) + "CONT";
                if(wearItem.type.equals("Weapon")
                && (((Weapon) wearItem).noDualWield || (((Weapon) wearItem).twoHanded && !parentEntity.canDualWield()))) {
                    wearGearSlotRaw = " both hands";
                    wearGearSlotRawColorCode = "1W5CONT5CONT";
                }
                else if(wearItem.type.equals("Gear")) {
                    if(Arrays.asList("Neck 1", "Neck 2").contains(wearGearSlot)) {
                        wearGearSlotRaw = " your " + wearGearSlot.substring(0, 4).toLowerCase();
                        wearGearSlotRawColorCode = "1W5CONT4CONT";
                    } else if(Arrays.asList("Ring 1", "Ring 2").contains(wearGearSlot)) {
                        wearGearSlotRaw = " your finger";
                        wearGearSlotRawColorCode = "1W5CONT6CONT";
                    }
                }

                String removeString = "You " + actionType.toLowerCase() + " " + wearItem.prefix.toLowerCase() + wearItem.name.label + inOnString + wearGearSlotRaw + ".";
                String removeColorCode = "4CONT" + String.valueOf(actionType.length()) + "CONT1W" + String.valueOf(wearItem.prefix.length()) + "CONT" + wearItem.name.colorCode + "1W3CONT" + wearGearSlotRawColorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(removeString, removeColorCode, "", true, true));
            }
        }
    }
}
