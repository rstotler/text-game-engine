package com.jbs.textgameengine.gamedata.entity.mob.action.combat;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.*;

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

            // Reload Main/Off Ammo //
            if(false) {
            }

            // Reload All Ammo //
            else if(false) {
            }

            // Reload Weapon Ammo //
            else if(false) {
            }

            // Reload All //
            else if(false) {
            }

            // Reload Main/Off //
            else if(false) {
            }

            // Reload Weapon/Ammo //
            else if(false) {
            }

            // Reload //
            else if(false) {
            }

            return reloadAction;
        }

        return null;
    }

    public void initiate() {
        // Message - It's too dark to see. //
        // Message - You can't reload that. //
        // Message - It's already loaded/Your weapons are already loaded. //
        // Message - You don't have anything to reload. //
        // Message - You can't find it. //
        // Message - You aren't holding anything. //
        // Message - It requires a AmmoType. //
        // Message - You don't have a AmmoType magazine. //
        // Message - You don't have that type of ammo. //
        // Message - You don't have any ammo. //
        // Message - You start reloading some weapons.. //
        // Message - You start reloading Weapon.. //
    }
}
