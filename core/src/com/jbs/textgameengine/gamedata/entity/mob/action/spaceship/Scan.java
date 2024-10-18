package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Scan extends Action {
    public Scan(Mob parentEntity) {
        super(parentEntity);
    }

    public Scan() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("scan", "sca", "sc").contains(inputList.get(0))) {
            Scan scanAction = new Scan(parentEntity);

            // Scan Planetoid //
            if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                scanAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            return scanAction;
        }

        return null;
    }

    public void initiate() {

        // Scan Planetoid //

        // Message - You must be in a cockpit to do that. //
        if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be in a cockpit to do that.", "4CONT5CONT3CONT3CONT2W8CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Message - (Syntax Error) //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("A computerized voice says, \"Scan a radar object with 'Scan Planetoid'.\"", "2W13CONT6CONT4CONT2DY1DY5CONT2W6CONT7CONT5CONT1DY5CONT9CONT2DY1DY", "", true, true));
            }
        }

        // Message - The ship's radar detects no such planet. //
        else if(false) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The ship's radar detects no such planet.", "4CONT4CONT1DY2DDW6CONT8CONT3CONT5CONT6CONT1DY", "", true, true));
            }
        }
    }
}
