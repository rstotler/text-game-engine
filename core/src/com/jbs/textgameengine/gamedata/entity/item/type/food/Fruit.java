package com.jbs.textgameengine.gamedata.entity.item.type.food;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.Arrays;

public class Fruit extends Food {
    public String masterPrefix;
    public Line plantName;

    public String growthStage; // Bud, Blossom, Immature, Unripe, Mature
    public float growthPercent;

    public Fruit(int id, Location startLocation, String prefix, Line plantName) {
        super(id, startLocation);
        masterPrefix = prefix;
        this.prefix = prefix;
        this.plantName = plantName;
        name = new Line("", "", "", true, true);

        growthStage = "Bud";
        growthPercent = 0.0f;
    }

    public void updateGrowth() {
        if(!(growthStage.equals("Mature") && growthPercent >= 1.0)) {
            growthPercent += .01f;
            if(growthPercent >= 1.0) {
                growthPercent = 0.0f;

                if(growthStage.equals("Bud")) {growthStage = "Blossom";}
                else if(growthStage.equals("Blossom")) {growthStage = "Immature";}
                else if(growthStage.equals("Immature")) {growthStage = "Unripe";}
                else if(growthStage.equals("Unripe")) {growthStage = "Mature";}

                updateName();
            }
        }
    }

    public void updateName() {
        if(Arrays.asList("Bud", "Blossom").contains(growthStage)) {
            name.label = plantName.label + " Flower " + growthStage;
            name.colorCode = plantName.colorCode + "1W7CONT" + String.valueOf(growthStage.length()) + "CONT";
        }
        else if(Arrays.asList("Immature", "Unripe", "Mature").contains(growthStage)) {
            prefix = masterPrefix;
            name.label = plantName.label;
            name.colorCode = plantName.colorCode;
        }
        nameKeyList = Entity.createNameKeyList(prefix.toLowerCase() + name.label.toLowerCase());
    }
}
