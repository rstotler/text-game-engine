package com.jbs.textgameengine.gamedata.entity.item.type.weapon;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Firearm extends Weapon {
    public String ammoType;

    public Firearm(int id, Location startLocation) {
        super(id, startLocation);
        type = "Firearm";
        pocket = "Weapons";
        isWeapon = true;

        maxDistance = -1;

        ammoType = "";
    }

    public static Firearm load(int id, Location startLocation) {
        Firearm firearmItem = new Firearm(id, startLocation);

        // 001 - A Bow //
        if(id == 1) {
            firearmItem.name = new Line("Bow", "3SHIAO", "", true, true);
            firearmItem.twoHanded = true;
            firearmItem.noDualWield = true;
            firearmItem.maxDistance = 3;
            firearmItem.ammoType = "Arrow";
        }

        // 002 - A Crossbow //
        else if(id == 2) {
            firearmItem.name = new Line("Crossbow", "8SHIAO", "", true, true);
            firearmItem.twoHanded = true;
            firearmItem.noDualWield = true;
            firearmItem.maxDistance = 5;
            firearmItem.ammoType = "Bolt";
        }

        // 003 - A 9mm Pistol //
        else if(id == 3) {
            firearmItem.name = new Line("9mm Pistol", "4CONT6SHIAGR", "", true, true);
            firearmItem.ammoType = "9mm";
        }

        // 004 - A .45 Pistol //
        else if(id == 4) {
            firearmItem.name = new Line(".45 Pistol", "1DY3CONT6SHIAGR", "", true, true);
            firearmItem.ammoType = ".45";
        }

        // 005 - A Sniper Rifle //
        else if(id == 5) {
            firearmItem.name = new Line("Sniper Rifle", "7SHIAGR5SHIAGR", "", true, true);
            firearmItem.twoHanded = true;
            firearmItem.noDualWield = true;
            firearmItem.minDistance = 1;
            firearmItem.ammoType = "5.56";
        }

        // 006 - A Shotgun //
        else if(id == 6) {
            firearmItem.name = new Line("Shotgun", "7SHIAGR", "", true, true);
            firearmItem.twoHanded = true;
            firearmItem.noDualWield = true;
            firearmItem.ammoType = "12-Gauge";
        }

        // 007 - A Rocket Launcher //
        else if(id == 7) {
            firearmItem.name = new Line("Rocket Launcher", "7SHIAGR8SHIAGR", "", true, true);
            firearmItem.twoHanded = true;
            firearmItem.noDualWield = true;
            firearmItem.ammoType = "Rocket";
        }

        // Default Firearm Item //
        else {
            firearmItem.name = new Line("Default Firearm Item", "8CONT8CONT4CONT", "", true, true);
        }

        return firearmItem;
    }

    public int getMaxDistance() {
        return maxDistance;
    }
}
