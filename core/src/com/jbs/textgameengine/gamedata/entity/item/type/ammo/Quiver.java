package com.jbs.textgameengine.gamedata.entity.item.type.ammo;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Quiver extends Item {
    public Quiver(int id, Location startLocation) {
        super(id, startLocation);
        type = "Quiver";
        pocket = "Ammo";

        status = "Open";
        containerItemList = new ArrayList<>();
    }

    public static Quiver load(int id, Location startLocation) {
        Quiver quiverItem = new Quiver(id, startLocation);

        // 001 - A Quiver //
        if(id == 1) {
            quiverItem.name = new Line("Quiver", "6CONT", "", true, true);
            quiverItem.containerItemTypeList = new ArrayList<>(Arrays.asList("Arrow", "Bolt"));
        }

        // Default Quiver Item //
        else {
            quiverItem.name = new Line("Default Quiver Item", "8CONT&CONT4CONT", "", true, true);
        }

        return quiverItem;
    }
}
