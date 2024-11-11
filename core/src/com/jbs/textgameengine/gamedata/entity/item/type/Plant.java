package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.item.type.food.Fruit;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Plant extends Item {
    public String masterPrefix;
    public Line plantName;
    public Line plantType;

    public String growthStage; // Seed, Seedling, Sapling, Adult, Mature
    public float growthPercent;
    public float saturationPercent;
    public float budTimer;
    public int maxBudCount;

    public Plant(int id, Location startLocation) {
        super(id, startLocation);
        type = "Plant";
        pocket = "Organic";
        containerItemList = new ArrayList<>();
        status = "Open";

        masterPrefix = "";
        name = new Line("", "", "", true, true);
        plantName = new Line("", "", "", true, true);
        plantType = new Line("", "", "", true, true);

        growthStage = "Seed";
        growthPercent = 0.0f;
        saturationPercent = 0.0f;
        budTimer = -1;
        maxBudCount = 5;
    }

    public void updateGrowth() {

        // Update Plant Saturation //
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
        if(!growthStage.equals("Mature")
        && saturationPercent > 0
        && location.planetoid.isDay()) {
            growthPercent += .01f;
            if(growthPercent >= 1.0) {
                growthPercent = 0;

                String previousNameLabel = prefix + name.label;
                String previousNameColorCode = String.valueOf(prefix.length()) + "CONT" + name.colorCode;

                if(growthStage.equals("Seed")) {growthStage = "Seedling";}
                else if(growthStage.equals("Seedling")) {growthStage = "Sapling";}
                else if(growthStage.equals("Sapling")) {growthStage = "Adult";}
                else if(growthStage.equals("Adult")) {growthStage = "Mature";}

                updateName();

                // Display Message - (Sprout From Ground/Grows Into Next Stage) //
                if(GameScreen.player.location.room == location.room) {
                    if(growthStage.equals("Seedling")) {
                        GameScreen.userInterface.console.writeToConsole(new Line(prefix + name.label + " sprouts out of the ground.", String.valueOf(prefix.length()) + "CONT" + name.colorCode + "1W8CONT4CONT3CONT4CONT6CONT1DY", "", true, true));
                    }
                    else {
                        GameScreen.userInterface.console.writeToConsole(new Line(previousNameLabel + " grows into " + prefix.toLowerCase() + name.label + ".", previousNameColorCode + "1W6CONT5CONT" + String.valueOf(prefix.length()) + "CONT" + name.colorCode + "1DY", "", true, true));
                    }
                }

                // Add New Seedling To Room Item List //
                if(growthStage.equals("Seedling")) {
                    roomDescription = new Line(" is planted in the ground.", "1W3CONT8CONT3CONT4CONT6CONT1DY", "", true, true);
                    isQuantity = false;
                    location.room.itemList.add(0, this);
                }

                // Set Adult Plant Bud Timer //
                else if(growthStage.equals("Adult")) {
                    budTimer = 10;
                }
            }
        }

        // Update Fruit Growth //
        ArrayList<Integer> removeFruitList = new ArrayList<>();
        for(int i = 0; i < containerItemList.size(); i++) {
            Fruit fruit = (Fruit) containerItemList.get(i);
            if(!(fruit.growthStage.equals("Mature") && growthPercent >= 0)) {
                if(saturationPercent > 0
                && location.planetoid.isDay()) {
                    fruit.updateGrowth();
                }
            }
            else {fruit.updateDecay();}

            // Rotten Fruit Fall From Plant //
            if(fruit.decayStage.equals("Rotten")
            && fruit.decayPercent >= 1.0) {
                fruit.decayPercent = 0.0f;
                removeFruitList.add(0, i);
                location.room.addItemToRoom(fruit);

                // Display Message //
                if(GameScreen.player.location.room == location.room
                && location.room.isLit()) {
                    String messageLabel = "A rotten " + fruit.name.label + " falls off of " + masterPrefix.toLowerCase() + plantName.label + " " + plantType.label + ".";
                    String messageColorCode = "2CONT7CONT" + fruit.name.colorCode + "1W6CONT4CONT3CONT" + String.valueOf(masterPrefix.length()) + "CONT" + plantName.colorCode + "1W" + plantType.colorCode + "1DY";
                    GameScreen.userInterface.console.writeToConsole(new Line(messageLabel, messageColorCode, "", true, true));
                }
            }
        }
        for(int deleteIndex : removeFruitList) {
            containerItemList.remove(deleteIndex);
        }

        // Update Bud Timer //
        if(budTimer != -1
        && containerItemList.size() < maxBudCount) {
            budTimer -= 1;
            if(budTimer == 0) {
                budTimer = 10;
                containerItemList.add(createFruitBud());
            }
        }
    }

    public void updateName() {
        if(Arrays.asList("Seed", "Seedling", "Sapling").contains(growthStage)) {
            name.label = plantName.label + " " + plantType.label + " " + growthStage;
            name.colorCode = plantName.colorCode + "1W" + plantType.colorCode + "1W" + String.valueOf(growthStage.length()) + "CONT";
        }
        else if(Arrays.asList("Adult", "Mature").contains(growthStage)) {
            name.label = growthStage + " " + plantName.label + " " + plantType.label;
            name.colorCode = String.valueOf(growthStage.length()) + "CONT1W" + plantName.colorCode + "1W" + plantType.colorCode;
        }
        if(growthStage.equals("Adult")) {prefix = "An ";}
        else if(growthStage.equals("Mature")) {prefix = "A ";}
        nameKeyList = Entity.createNameKeyList(prefix.toLowerCase() + name.label.toLowerCase());
    }

    public Fruit createFruitBud() {
        Fruit fruit = new Fruit(id, location, masterPrefix, plantName);
        fruit.updateName();

        return fruit;
    }

    public static Plant load(int id, Location startLocation) {
        Plant seedItem = new Plant(id, startLocation);

        // 001 - An Apple Tree Seed //
        if(id == 1) {
            seedItem.prefix = "An ";
            seedItem.plantName = new Line("Apple", "5SHIAR", "", true, true);
            seedItem.plantType = new Line("Tree", "4SHIA", "", true, true);
            seedItem.isQuantity = true;
        }

        // Default Seed Item //
        else {
            seedItem.name = new Line("Default Seed Item", "8CONT5CONT4CONT", "", true, true);
        }

        seedItem.masterPrefix = seedItem.prefix;
        seedItem.updateName();

        return seedItem;
    }
}
