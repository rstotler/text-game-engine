package com.jbs.textgameengine.gamedata.entity.item.type.weapon;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Throwable extends Weapon {
    public Throwable(int id, Location startLocation) {
        super(id, startLocation);
        type = "Throwable";
        pocket = "Weapons";
        isWeapon = true;

        maxDistance = 2;
    }

    public static Throwable load(int id, Location startLocation) {
        Throwable throwableItem = new Throwable(id, startLocation);

        // 001 - A Shuriken //
        if(id == 1) {
            throwableItem.name = new Line("Shuriken", "8SHIAGR", "", true, true);
            throwableItem.isQuantity = true;
        }

        // 002 - A Grenade //
        else if(id == 2) {
            throwableItem.name = new Line("Grenade", "7SHIAG", "", true, true);
            throwableItem.isQuantity = true;
        }

        // Default Throwable Item //
        else {
            throwableItem.name = new Line("Default Throwable Item", "8CONT10CONT4CONT", "", true, true);
        }

        return throwableItem;
    }

    public int getMaxDistance() {
        return maxDistance;
    }
}
