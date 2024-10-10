package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

// Polearms Receive A Natural Damage Boost When Single-Wielding
// Rifles Are ALWAYS 2-Handed

public class Weapon extends Item {
    public String gearSlot;

    public String weaponType;
    public boolean twoHanded;     // Can Be Dual-Wielded ONLY With High Strength
    public boolean twoHandedOnly; // Can Not Be Dual-Wielded
    public float cutLimbChance;

    public Weapon(int id, Location startLocation) {
        super(id, startLocation);
        type = "Weapon";
        pocket = "Weapons";

        gearSlot = "";

        weaponType = "Unarmed";
        twoHanded = false;
        twoHandedOnly = false;
        cutLimbChance = -1.0f;
    }

    public static Weapon load(int id, Location startLocation) {
        Weapon gearItem = new Weapon(id, startLocation);

        // 001 - A Sword //
        if(id == 1) {
            gearItem.name = new Line("Sword", "5SHIAGR", "", true, true);
        }

        // 002 - A Greatsword //
        else if(id == 2) {
            gearItem.name = new Line("Greatsword", "10SHIAGR", "", true, true);
        }

        // 003 - A Dagger //
        else if(id == 3) {
            gearItem.name = new Line("Dagger", "6SHIAGR", "", true, true);
        }

        // 004 - An Axe //
        else if(id == 4) {
            gearItem.prefix = "An ";
            gearItem.name = new Line("Axe", "3SHIAGR", "", true, true);
        }

        // 005 - A Battle Axe //
        else if(id == 5) {
            gearItem.name = new Line("Battle Axe", "7SHIAGR3SHIAGR", "", true, true);
        }

        // 006 - A Mace //
        else if(id == 6) {
            gearItem.name = new Line("Mace", "4SHIAGR", "", true, true);
        }

        // 007 - A Spear //
        else if(id == 7) {
            gearItem.name = new Line("Spear", "5SHIAGR", "", true, true);
        }

        // 008 - A Halberd //
        else if(id == 8) {
            gearItem.name = new Line("Halberd", "7SHIAGR", "", true, true);
        }

        // 009 - A pair of Claws //
        else if(id == 9) {
            gearItem.prefix = "A pair of ";
            gearItem.name = new Line("Claws", "5SHIAGR", "", true, true);
            gearItem.roomDescription = new Line(" are lying on the ground.", "1W4CONT6CONT3CONT4CONT6CONT1DY", "", true, true);
        }

        // 010 - A Shield //
        else if(id == 10) {
            gearItem.name = new Line("Shield", "6SHIAGR", "", true, true);
        }

        // 011 - A Bow //
        else if(id == 11) {
            gearItem.name = new Line("Bow", "3SHIAO", "", true, true);
        }

        // 012 - A 9mm Pistol //
        else if(id == 12) {
            gearItem.name = new Line("9mm Pistol", "4SHIAGR6SHIAGR", "", true, true);
        }

        // 013 - A .45 Pistol //
        else if(id == 13) {
            gearItem.name = new Line(".45 Pistol", "1DY3SHIAGR6SHIAGR", "", true, true);
        }

        // 014 - A Sniper Rifle //
        else if(id == 14) {
            gearItem.name = new Line("Sniper Rifle", "7SHIAGR5SHIAGR", "", true, true);
        }

        // 015 - A Shotgun //
        else if(id == 15) {
            gearItem.name = new Line("Shotgun", "7SHIAGR", "", true, true);
        }

        // 016 - A Rocket Launcher //
        else if(id == 16) {
            gearItem.name = new Line("Rocket Launcher", "7SHIAGR8SHIAGR", "", true, true);
        }

        // 017 - A Shuriken //
        else if(id == 17) {
            gearItem.name = new Line("Shuriken", "8SHIAGR", "", true, true);
        }

        // 018 - A Grenade //
        else if(id == 18) {
            gearItem.name = new Line("Grenade", "7SHIAG", "", true, true);
        }

        // 019 - A Staff //
        else if(id == 19) {
            gearItem.name = new Line("Staff", "5SHIAO", "", true, true);
        }

        // Default Item //
        else {
            gearItem.name = new Line("Default Weapon Item", "8CONT7CONT4CONT", "", true, true);
        }

        gearItem.nameKeyList = Entity.createNameKeyList(gearItem.prefix + gearItem.name.label);

        return gearItem;
    }
}
