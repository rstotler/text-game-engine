package com.jbs.textgameengine.gamedata.entity.item.type.food;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Food extends Item {
    public String decayStage; // Fresh, Spoiled, Rotten
    public float decayPercent;

    public Food(int id, Location startLocation) {
        super(id, startLocation);
        type = "Food";
        pocket = "Food";

        decayStage = "Fresh";
        decayPercent = 0.0f;
    }

    public void updateDecay() {
        if(!(decayStage.equals("Rotten") && decayPercent >= 1.0)) {
            decayPercent += .01f;
            if(decayPercent >= 1.0) {
                if(!decayStage.equals("Rotten")) {decayPercent = 0.0f;}

                if(decayStage.equals("Fresh")) {decayStage = "Spoiled";}
                else if(decayStage.equals("Spoiled")) {decayStage = "Rotten";}
            }
        }
    }

    public static Food load(int id, Location startLocation) {
        Food foodItem = new Food(id, startLocation);

        // 001 - An Apple //
        if(id == 1) {
            foodItem.prefix = "An ";
            foodItem.name = new Line("Apple", "5SHIAR", "", true, true);
        }

        // Default Food Item //
        else {
            foodItem.name = new Line("Default Food Item", "8CONT5CONT4CONT", "", true, true);
        }

        return foodItem;
    }
}
