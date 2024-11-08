package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Plant extends Item {
    public Line plantName;

    public String growthStage;
    public float growthPercent;
    public float saturationPercent;

    public Plant(int id, Location startLocation) {
        super(id, startLocation);
        type = "Plant";
        pocket = "Organic";

        plantName = new Line("", "", "", true, true);

        growthStage = "Seed";
        growthPercent = 0.0f;
        saturationPercent = 0.0f;
    }

    public void updateGrowth() {
        if(location.room.plantedPlantList.contains(this)) {
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
        }

        // Update Growth Percent/Stage //
        if(!growthStage.equals("Mature")
        && saturationPercent > 0) {
            growthPercent += .01f;
            if(growthPercent >= 1.0) {
                growthPercent = 0;

                // Change Item Name //
                if(growthStage.equals("Seed")) {growthStage = "Sprout";}
                else if(growthStage.equals("Sprout")) {growthStage = "Sapling";}
                else if(growthStage.equals("Sapling")) {growthStage = "Adult";}
                else if(growthStage.equals("Adult")) {growthStage = "Mature";}

                if(growthStage.equals("Sprout") || growthStage.equals("Sapling")) {
                    name.label = plantName.label + " " + growthStage;
                    name.colorCode = plantName.colorCode + "1W" + String.valueOf(growthStage.length()) + "CONT";
                }
                else {
                    name.label = growthStage + " " + plantName.label;
                    name.colorCode = String.valueOf(growthStage.length()) + "CONT1W" + plantName.colorCode;
                }
                if(growthStage.equals("Mature")) {prefix = "A ";}
                nameKeyList = Entity.createNameKeyList(prefix + name.label);

                // Add New Sprout To Room Item List //
                if(growthStage.equals("Sprout")) {
                    roomDescription = new Line(" is planted in the ground.", "1W3CONT8CONT3CONT4CONT6CONT1DY", "", true, true);
                    location.room.itemList.add(0, this);
                }
            }
        }
    }

    public static Plant load(int id, Location startLocation) {
        Plant seedItem = new Plant(id, startLocation);

        // 001 - An Apple Tree Seed //
        if(id == 1) {
            seedItem.plantName = new Line("Apple Tree", "6SHIAR4SHIA", "", true, true);
            seedItem.prefix = "An ";
            seedItem.name = new Line(seedItem.plantName.label + " Seed", seedItem.plantName.colorCode + "1W4SHIA", "", true, true);
            seedItem.isQuantity = true;
        }

        // Default Seed Item //
        else {
            seedItem.name = new Line("Default Seed Item", "8CONT5CONT4CONT", "", true, true);
        }

        return seedItem;
    }
}
