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
        for(String gearSlot : parentEntity.gear.keySet()) {
            if(parentEntity.gear.get(gearSlot) != null
            && gearSlot.length() > longestSlotSize) {
                longestSlotSize = gearSlot.length();
            }
            if(parentEntity.gear.get(gearSlot) != null) {
                lastLineSlot = gearSlot;
                if(nakedCheck) {nakedCheck = false;}
            }
        }

        if(nakedCheck) {
            userInterface.console.writeToConsole(new Line("-Nothing", "1DY7CONT", "", true, true));
        }

        boolean isLastLine = false;
        ArrayList<String> gearSlotList = new ArrayList<>(parentEntity.gear.keySet());
        for(int i = 0; i < Mob.gearSlotList.size(); i++) {
            String gearSlot = Mob.gearSlotList.get(i);
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
