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
    }

    public static Quiver load(int id, Location startLocation) {
        Quiver ammoItem = new Quiver(id, startLocation);

        // 001 - A Quiver //
        if(id == 1) {
            ammoItem.name = new Line("A Quiver", "2W6CONT", "", true, true);
            ammoItem.containerItemList = new ArrayList<>();
            ammoItem.containerItemTypeList = new ArrayList<>(Arrays.asList("Arrow", "Bolt"));
        }

        // Default Item //
        else {
            ammoItem.name = new Line("Default Ammo Item", "8CONT5CONT4CONT", "", true, true);
        }

        return ammoItem;
    }
}
