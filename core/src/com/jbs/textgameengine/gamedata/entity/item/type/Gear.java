package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Gear extends Item {
    public String gearSlot;

    public Gear(int id, Location startLocation) {
        super(id, startLocation);
        type = "Gear";
        pocket = "Gear";

        gearSlot = "";
    }

    public static Gear load(int id, Location startLocation) {
        Gear gearItem = new Gear(id, startLocation);

        // 001 - An Iron Helmet //
        if(id == 1) {
            gearItem.prefix = "An ";
            gearItem.name = new Line("Iron Helmet", "5SHIAGR6SHIAGR", "", true, true);
        }

        // 002 - An Ancient Mask //
        else if(id == 2) {
            gearItem.prefix = "An ";
            gearItem.name = new Line("Ancient Mask", "8SHIAY4SHIAY", "", true, true);
        }

        // 003 - A Heart Locket //
        else if(id == 3) {
            gearItem.name = new Line("Heart Locket", "6SHIAR6SHIAY", "", true, true);
        }

        // 004 - A Star Pendant //
        else if(id == 4) {
            gearItem.name = new Line("Star Pendant", "5SHIAM7SHIAY", "", true, true);
        }

        // 005 - A Silver Breastplate //
        else if(id == 5) {
            gearItem.name = new Line("Silver Breastplate", "7SHIA11SHIAGR", "", true, true);
        }

        // 006 - A pair of Leather Gloves //
        else if(id == 6) {
            gearItem.prefix = "A pair of ";
            gearItem.name = new Line("Leather Gloves", "8SHIAO6CONT", "", true, true);
            gearItem.roomDescription = new Line(" are lying on the ground.", "1W4CONT6CONT3CONT4CONT6CONT1DY", "", true, true);
        }

        // 007 - A Ruby Ring //
        else if(id == 7) {
            gearItem.name = new Line("Ruby Ring", "5SHIAR4SHIAY", "", true, true);
        }

        // 008 - An Emerald Ring //
        else if(id == 8) {
            gearItem.prefix = "An ";
            gearItem.name = new Line("Emerald Ring", "8SHIAG4SHIAY", "", true, true);
        }

        // 009 - A Gold Ring //
        else if(id == 9) {
            gearItem.name = new Line("Gold Ring", "5SHIAY4SHIAY", "", true, true);
        }

        // 010 - A pair of Iron Greaves //
        else if(id == 10) {
            gearItem.name = new Line("Iron Greaves", "5SHIAGR7SHIAGR", "", true, true);
            gearItem.roomDescription = new Line(" are lying on the ground.", "1W4CONT6CONT3CONT4CONT6CONT1DY", "", true, true);
        }

        // 011 - A pair of Leather Boots //
        else if(id == 11) {
            gearItem.prefix = "A pair of ";
            gearItem.name = new Line("Leather Boots", "8SHIAO5CONT", "", true, true);
            gearItem.roomDescription = new Line(" are lying on the ground.", "1W4CONT6CONT3CONT4CONT6CONT1DY", "", true, true);
        }

        // 012 - A Backpack //
        else if(id == 12) {
            gearItem.name = new Line("Backpack", "8SHIAO", "", true, true);
        }

        // Default Item //
        else {
            gearItem.name = new Line("Default Gear Item", "8CONT5CONT4CONT", "", true, true);
        }

        gearItem.nameKeyList = Entity.createNameKeyList(gearItem.prefix + gearItem.name.label);

        return gearItem;
    }
}
