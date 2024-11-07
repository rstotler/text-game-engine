package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Seed extends Item {
    public Seed(int id, Location startLocation) {
        super(id, startLocation);
        type = "Seed";
        pocket = "Organic";
    }

    public static Seed load(int id, Location startLocation) {
        Seed seedItem = new Seed(id, startLocation);

        // 001 - A Tree Seed //
        if(id == 1) {
            seedItem.name = new Line("Tree Seed", "5SHIAG4SHIAO", "", true, true);
            seedItem.isQuantity = true;
        }

        // Default Seed Item //
        else {
            seedItem.name = new Line("Default Seed Item", "8CONT5CONT4CONT", "", true, true);
        }

        return seedItem;
    }
}
