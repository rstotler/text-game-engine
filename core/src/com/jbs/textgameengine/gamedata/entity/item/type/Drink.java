package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Drink extends Item {
    public Drink(int id, Location startLocation) {
        super(id, startLocation);
        type = "Drink";
        pocket = "Food";
    }

    public static Drink load(int id, Location startLocation) {
        Drink drinkItem = new Drink(id, startLocation);

        // 001 - A Glass of Orange Juice //
        if(id == 1) {
            drinkItem.name = new Line("Glass of Orange Juice", "6CONT3CONT7SHIAO5SHIAO", "", true, true);
        }

        // Default Drink Item //
        else {
            drinkItem.name = new Line("Default Drink Item", "8CONT6CONT4CONT", "", true, true);
        }

        return drinkItem;
    }
}
