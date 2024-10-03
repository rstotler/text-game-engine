package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class OCLU extends Action {
    public OCLU(Mob parentEntity) {
        super(parentEntity);
    }

    public OCLU() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("open", "ope", "op", "close", "clos", "clo", "cl", "lock", "loc", "unlock", "unloc", "unlo", "unl").contains(inputList.get(0))) {
            OCLU ocluAction = new OCLU(parentEntity);

            if(inputList.get(0).charAt(0) == 'o') {ocluAction.actionType = "Open";}
            else if(inputList.get(0).charAt(0) == 'c') {ocluAction.actionType = "Close";}
            else if(inputList.get(0).charAt(0) == 'l') {ocluAction.actionType = "Lock";}
            else if(inputList.get(0).charAt(0) == 'u') {ocluAction.actionType = "Unlock";}

            // OCLU Direction //
            if(inputList.size() == 2
            && Location.directionList.contains(inputList.get(1))) {
                ocluAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
            }

            // OCLU Target //
            else if(inputList.size() == 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                ocluAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line(ocluAction.actionType + " what?", String.valueOf(ocluAction.actionType.length() + 1) + "CONT4CONT1DY", "", true, true));
                ocluAction.parentEntity = null;
            }

            return ocluAction;
        }

        return null;
    }

    public void initiate() {
    }
}
