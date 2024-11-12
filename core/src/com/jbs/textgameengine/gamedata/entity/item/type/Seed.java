package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Seed extends Item {
    public String masterPrefix;
    public Line plantName;
    public Line plantType;

    public float growthPercent;
    public float saturationPercent;

    public Seed(int id, Location startLocation) {
        super(id, startLocation);
        type = "Seed";
        pocket = "Organic";

        masterPrefix = "";
        name = new Line("", "", "", true, true);
        plantName = new Line("", "", "", true, true);
        plantType = new Line("", "", "", true, true);

        growthPercent = 0.0f;
        saturationPercent = 0.0f;
    }

    public void updateGrowth() {

        // Update Seed Saturation //
        if(location.room.groundSaturation > 0
        && saturationPercent < 1.0) {
            saturationPercent += .01f;
            if(saturationPercent > 1.0) {saturationPercent = 1.0f;}
        }
        else if(location.room.groundSaturation == 0
        && saturationPercent > 0) {
            saturationPercent -= .01f;
            if(saturationPercent < 0) {saturationPercent = 0.0f;}
        }

        // Update Growth Percent/Stage //
        if(saturationPercent > 0) {
            growthPercent += .01f;
        }
    }

    public static Seed load(int id, Location startLocation) {
        Seed seedItem = new Seed(id, startLocation);
        seedItem.isQuantity = true;

        // 001 - An Apple Tree Seed //
        if(id == 1) {
            seedItem.prefix = "An ";
            seedItem.plantName = new Line("Apple", "5SHIAR", "", true, true);
            seedItem.plantType = new Line("Tree", "4SHIA", "", true, true);
        }

        // Default Seed Item //
        else {
            seedItem.name = new Line("Default Seed Item", "8CONT5CONT4CONT", "", true, true);
        }

        seedItem.masterPrefix = seedItem.prefix;
        seedItem.name.label = seedItem.plantName.label + " " + seedItem.plantType.label + " Seed";
        seedItem.name.colorCode = seedItem.plantName.colorCode + "1W" + seedItem.plantType.colorCode + "1W4CONT";

        return seedItem;
    }
}
