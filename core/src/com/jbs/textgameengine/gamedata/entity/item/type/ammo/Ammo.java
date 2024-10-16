package com.jbs.textgameengine.gamedata.entity.item.type.ammo;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Ammo extends Item {
    public String ammoType;

    public Ammo(int id, Location startLocation) {
        super(id, startLocation);
        type = "Ammo";
        pocket = "Ammo";

        weight = .025f;

        ammoType = "";
    }

    public static Ammo load(int id, Location startLocation) {
        Ammo ammoItem = new Ammo(id, startLocation);

        // 001 - An Arrow //
        if(id == 1) {
            ammoItem.prefix = "An ";
            ammoItem.name = new Line("Arrow", "5CONT", "", true, true);
            ammoItem.ammoType = "Arrow";
            ammoItem.isQuantity = true;
        }

        // 002 - A Bolt //
        else if(id == 2) {
            ammoItem.name = new Line("Bolt", "4CONT", "", true, true);
            ammoItem.ammoType = "Bolt";
            ammoItem.isQuantity = true;
        }

        // 003 - A 9mm Round //
        else if(id == 3) {
            ammoItem.name = new Line("9mm Round", "4CONT5CONT", "", true, true);
            ammoItem.ammoType = "9mm";
            ammoItem.isQuantity = true;
        }

        // 004 - A .45 Round //
        else if(id == 4) {
            ammoItem.name = new Line(".45 Round", "1DY1W1W1W5CONT", "", true, true);
            ammoItem.ammoType = ".45";
            ammoItem.isQuantity = true;
        }

        // 005 - A 5.56 Round //
        else if(id == 5) {
            ammoItem.name = new Line("5.56 Round", "1W1DY1W1W1W5CONT", "", true, true);
            ammoItem.ammoType = "5.56";
            ammoItem.isQuantity = true;
        }

        // 006 - A 12-Gauge Slug //
        else if(id == 6) {
            ammoItem.name = new Line("12-Gauge Slug", "2W1DY6CONT4CONT", "", true, true);
            ammoItem.ammoType = "12-Gauge";
            ammoItem.isQuantity = true;
        }

        // 007 - A Rocket-Propelled Missile //
        else if(id == 7) {
            ammoItem.name = new Line("Rocket-Propelled Missile", "6CONT1DY10CONT7CONT", "", true, true);
            ammoItem.ammoType = "Rocket";
            ammoItem.isQuantity = true;
        }

        // Default Ammo Item //
        else {
            ammoItem.name = new Line("Default Ammo Item", "8CONT5CONT4CONT", "", true, true);
        }

        return ammoItem;
    }
}
