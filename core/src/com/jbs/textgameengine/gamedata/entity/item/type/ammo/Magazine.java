package com.jbs.textgameengine.gamedata.entity.item.type.ammo;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Magazine extends Item {
    public Magazine(int id, Location startLocation) {
        super(id, startLocation);
        type = "Magazine";
        pocket = "Ammo";
    }

    public static Magazine load(int id, Location startLocation) {
        Magazine magazineItem = new Magazine(id, startLocation);

        // 001 - A 9mm 8-Round Magazine //
        if(id == 1) {
            magazineItem.name = new Line("9mm 8-Round Magazine", "1W3DDY1W1DY6CONT8CONT", "", true, true);
            magazineItem.containerItemList = new ArrayList<>();
            magazineItem.containerItemTypeList = new ArrayList<>(Arrays.asList("9mm"));
        }

        // 002 - A 9mm 12-Round Magazine //
        else if(id == 2) {
            magazineItem.name = new Line("9mm 12-Round Magazine", "1W3DDY2W1DY6CONT8CONT", "", true, true);
            magazineItem.containerItemList = new ArrayList<>();
            magazineItem.containerItemTypeList = new ArrayList<>(Arrays.asList("9mm"));
        }

        // 003 - A .45 8-Round Magazine //
        else if(id == 3) {
            magazineItem.name = new Line(".45 8-Round Magazine", "1DY4W1DY6CONT8CONT", "", true, true);
            magazineItem.containerItemList = new ArrayList<>();
            magazineItem.containerItemTypeList = new ArrayList<>(Arrays.asList(".45"));
        }

        // 004 - A .45 12-Round Magazine //
        else if(id == 4) {
            magazineItem.name = new Line(".45 12-Round Magazine", "1DY5W1DY6CONT8CONT", "", true, true);
            magazineItem.containerItemList = new ArrayList<>();
            magazineItem.containerItemTypeList = new ArrayList<>(Arrays.asList(".45"));
        }

        // 005 - A 5.56 6-Round Magazine //
        else if(id == 5) {
            magazineItem.name = new Line("5.56 6-Round Magazine", "1W1DY4W1DY6CONT8CONT", "", true, true);
            magazineItem.containerItemList = new ArrayList<>();
            magazineItem.containerItemTypeList = new ArrayList<>(Arrays.asList("5.56"));
        }

        // Default Magazine Item //
        else {
            magazineItem.name = new Line("Default Magazine Item", "8CONT9CONT4CONT", "", true, true);
        }

        return magazineItem;
    }
}
