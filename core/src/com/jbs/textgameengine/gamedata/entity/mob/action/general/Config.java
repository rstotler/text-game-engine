package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Config extends Action {
    public static boolean autoLoot = false;
    public static boolean autoReload = false;
    public static boolean autoCombat = false;

    public Config(Mob parentEntity) {
        super(parentEntity);
    }

    public Config() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("config", "confi", "conf", "con").contains(inputList.get(0))
        && inputList.size() > 1) {
            Config configAction = new Config(parentEntity);

            List<String> targetEntityStringList = inputList.subList(1, inputList.size());
            configAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));

            return configAction;
        }

        return null;
    }

    public void initiate() {

        // Auto Loot //
        if(Arrays.asList("autoloot", "auto loot", "loot").contains(targetEntityString)) {
        }

        // Auto Reload //
        else if(Arrays.asList("autoreload", "auto reload", "reload").contains(targetEntityString)) {
        }

        // Auto Combat //
        else if(Arrays.asList("autocombat", "auto combat", "combat").contains(targetEntityString)) {
            autoCombat = !autoCombat;

            String enabledDisabledString = "enabled";
            if(!autoCombat) {
                enabledDisabledString = "disabled";
            }
            GameScreen.userInterface.console.writeToConsole(new Line("Auto-Combat " + enabledDisabledString + ".", "4CONT1DY7CONT" + String.valueOf(enabledDisabledString.length()) + "CONT1DY", "", true, true));
        }
    }
}
