package com.jbs.textgameengine.gamedata.entity.mob.action.combat;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.*;
import java.util.Arrays;

public class Reload extends Action {
    public Reload(Mob parentEntity) {
        super(parentEntity);
    }

    public Reload() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("reload", "reloa", "relo", "rel").contains(inputList.get(0))) {
            Reload reloadAction = new Reload(parentEntity);

            // Reload Left/Right Ammo //
            // Reload All Ammo //
            // Reload Weapon Ammo //
            // Reload All //
            // Reload Left/Right //
            // Reload Weapon/Ammo //
            // Reload //

            return reloadAction;
        }

        return null;
    }

    public void initiate() {
    }
}
