package com.jbs.textgameengine.gamedata.entity.item.type.weapon;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

// Polearms Receive A Damage Boost When Single-Wielding

public class Weapon extends Item {
    public float cutLimbChance;

    public boolean twoHanded;   // Can Be Dual-Wielded ONLY With High Strength
    public boolean noDualWield; // Can NOT Be Dual-Wielded

    public int maxDistance;
    public int minDistance;

    public Weapon(int id, Location startLocation) {
        super(id, startLocation);
        type = "Weapon";
        pocket = "Weapons";
        isWeapon = true;

        cutLimbChance = -1.0f;

        twoHanded = false;
        noDualWield = false;

        maxDistance = 0;
        minDistance = 0;
    }

    public static Weapon load(int id, Location startLocation) {
        Weapon weaponItem = new Weapon(id, startLocation);

        // 001 - A Sword //
        if(id == 1) {
            weaponItem.name = new Line("Sword", "5SHIAGR", "", true, true);
        }

        // 002 - A Greatsword //
        else if(id == 2) {
            weaponItem.name = new Line("Greatsword", "10SHIAGR", "", true, true);
            weaponItem.twoHanded = true;
        }

        // 003 - A Dagger //
        else if(id == 3) {
            weaponItem.name = new Line("Dagger", "6SHIAGR", "", true, true);
        }

        // 004 - An Axe //
        else if(id == 4) {
            weaponItem.prefix = "An ";
            weaponItem.name = new Line("Axe", "3SHIAGR", "", true, true);
        }

        // 005 - A Battle Axe //
        else if(id == 5) {
            weaponItem.name = new Line("Battle Axe", "7SHIAGR3SHIAGR", "", true, true);
            weaponItem.twoHanded = true;
        }

        // 006 - A Mace //
        else if(id == 6) {
            weaponItem.name = new Line("Mace", "4SHIAGR", "", true, true);
        }

        // 007 - A Spear //
        else if(id == 7) {
            weaponItem.name = new Line("Spear", "5SHIAGR", "", true, true);
            weaponItem.maxDistance = 1;
        }

        // 008 - A Halberd //
        else if(id == 8) {
            weaponItem.name = new Line("Halberd", "7SHIAGR", "", true, true);
            weaponItem.maxDistance = 1;
        }

        // 009 - A pair of Claws //
        else if(id == 9) {
            weaponItem.prefix = "A pair of ";
            weaponItem.name = new Line("Claws", "5SHIAGR", "", true, true);
            weaponItem.roomDescription = new Line(" are lying on the ground.", "1W4CONT6CONT3CONT4CONT6CONT1DY", "", true, true);
        }

        // 010 - A Shield //
        else if(id == 10) {
            weaponItem.name = new Line("Shield", "6SHIAGR", "", true, true);
        }

        // 011 - A Staff //
        else if(id == 11) {
            weaponItem.name = new Line("Staff", "5SHIAO", "", true, true);
        }

        // Default Weapon Item //
        else {
            weaponItem.name = new Line("Default Weapon Item", "8CONT7CONT4CONT", "", true, true);
        }

        return weaponItem;
    }

    public int getMaxDistance() {
        return maxDistance;
    }
}
