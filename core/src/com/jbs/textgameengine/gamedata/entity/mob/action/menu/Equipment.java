package com.jbs.textgameengine.gamedata.entity.mob.action.menu;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Equipment extends Action {
    public Equipment(Mob parentEntity) {
        super(parentEntity);
    }

    public Equipment() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("equipment", "equipmen", "equipme", "equipm", "equip", "equi", "equ", "eq").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Equipment(parentEntity);
        }

        return null;
    }

    public void initiate() {
        userInterface.console.writeToConsole(new Line("Worn Gear:", "5CONT4CONT1DY", "", false, true));

        int longestSlotSize = 0;
        String lastLineSlot = "Head";
        boolean nakedCheck = true;
        for(String gearSlot : Mob.gearSlotList) {
            int gearSlotCount = 1;
            if(gearSlot.startsWith("Neck") || gearSlot.startsWith("Ring")) {
                gearSlotCount = 2;
            }

            for(int i = 0; i < gearSlotCount; i++) {
                if(gearSlot.startsWith("Neck") || gearSlot.startsWith("Ring")) {
                    gearSlot = gearSlot.substring(0, 4) + " " + String.valueOf(i + 1);
                }
                else if(gearSlot.equals("Main") || gearSlot.equals("Off")) {
                    gearSlot = gearSlot + " Hand";
                }

                if(parentEntity.gear.get(gearSlot) != null
                && gearSlot.length() > longestSlotSize) {
                    longestSlotSize = gearSlot.length();
                }
                if(parentEntity.gear.get(gearSlot) != null) {
                    lastLineSlot = gearSlot;
                    if(nakedCheck) {nakedCheck = false;}
                }
            }
        }

        if(nakedCheck) {
            userInterface.console.writeToConsole(new Line("-Nothing", "1DY7CONT", "", true, true));
        }

        boolean isLastLine = false;
        ArrayList<String> gearSlotList = new ArrayList<>(parentEntity.gear.keySet());
        for(int i = 0; i < Mob.gearSlotList.size(); i++) {
            String gearSlot = Mob.gearSlotList.get(i);
            int slotCount = 1;
            if(Arrays.asList("Neck", "Ring").contains(gearSlot)) {slotCount = 2;}

            for(int slotIndex = 0; slotIndex < slotCount; slotIndex++) {
                if(gearSlot.startsWith("Neck") || gearSlot.startsWith("Ring")) {
                    gearSlot = gearSlot.substring(0, 4) + " " + String.valueOf(slotIndex + 1);
                }
                else if(gearSlot.equals("Main") || gearSlot.equals("Off")) {
                    gearSlot = gearSlot + " Hand";
                }

                if(gearSlot.equals(lastLineSlot)) {isLastLine = true;}

                if(parentEntity.gear.containsKey(gearSlot)
                && parentEntity.gear.get(gearSlot) != null) {
                    String gearItemString = parentEntity.gear.get(gearSlot).prefix + parentEntity.gear.get(gearSlot).name.label;
                    String gearItemColorCode = String.valueOf(parentEntity.gear.get(gearSlot).prefix.length()) + "CONT" + parentEntity.gear.get(gearSlot).name.colorCode;

                    String spaceString = "";
                    if(gearSlot.length() < longestSlotSize) {
                        for(int ii = 0; ii < longestSlotSize - gearSlot.length(); ii++) {spaceString += " ";}
                    }
                    String gearSlotString = spaceString + "[" + gearSlot + "] - " + gearItemString;
                    String gearSlotColorCode = String.valueOf(spaceString.length()) + "W1DR" + String.valueOf(gearSlot).length() + "CONT1DR3DY" + gearItemColorCode;
                    userInterface.console.writeToConsole(new Line(gearSlotString, gearSlotColorCode, "", isLastLine, true));
                }
            }
        }
    }
}
