package com.jbs.textgameengine.gamedata.entity.mob.action.item.gear;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.item.type.weapon.Weapon;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Switch extends Action {
    public Switch(Mob parentEntity) {
        super(parentEntity);
    }

    public Switch() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("switch", "switc", "swit", "swi").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Switch(parentEntity);
        }

        return null;
    }

    public void initiate() {
        boolean switchCheck = false;

        // Switch Weapon(s) //
        if(!(parentEntity.gear.get("Main Hand") != null && (((Weapon) parentEntity.gear.get("Main Hand")).noDualWield || (((Weapon) parentEntity.gear.get("Main Hand")).twoHanded && !parentEntity.canDualWield())))
        && !(parentEntity.gear.get("Main Hand") == null && parentEntity.gear.get("Off Hand") == null)) {
            switchCheck = true;

            Item mainHandWeapon = parentEntity.gear.get("Main Hand");
            parentEntity.gear.put("Main Hand", parentEntity.gear.get("Off Hand"));
            parentEntity.gear.put("Off Hand", mainHandWeapon);
        }

        // Message - You aren't holding anything. //
        if(parentEntity.gear.get("Main Hand") == null && parentEntity.gear.get("Off Hand") == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You aren't holding anything.", "4CONT4CONT1DY2DDW8CONT8CONT1DY", "", true, true));
            }
        }

        // Message - You can't hold it any other way. //
        else if(!switchCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't hold it any other way.", "4CONT3CONT1DY2DDW5CONT3CONT4CONT6CONT3CONT1DY", "", true, true));
            }
        }

        // Message - You switch your weapons around. //
        else {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You switch your weapons around.", "4CONT7CONT5CONT8CONT6CONT1DY", "", true, true));
            }
        }
    }
}
