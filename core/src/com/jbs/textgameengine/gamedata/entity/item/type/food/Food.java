package com.jbs.textgameengine.gamedata.entity.item.type.food;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Food extends Item {
    public Food(int id, Location startLocation) {
        super(id, startLocation);
        type = "Food";
        pocket = "Food";
        isEdible = true;

        decayStage = "Fresh";
        decayPercent = 0.0f;
    }

    public static Food load(int id, Location startLocation) {
        Food foodItem = new Food(id, startLocation);

        // 001 - A Sandwich //
        if(id == 1) {
            foodItem.name = new Line("Sandwich", "8SHIAO", "", true, true);
        }

        // Default Food Item //
        else {
            foodItem.name = new Line("Default Food Item", "8CONT5CONT4CONT", "", true, true);
        }

        return foodItem;
    }
}
