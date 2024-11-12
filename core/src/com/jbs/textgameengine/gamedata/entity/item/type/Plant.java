package com.jbs.textgameengine.gamedata.entity.item.type;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.item.type.food.Fruit;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;

public class Plant extends Item {
    public String masterPrefix;
    public Line plantName;
    public Line plantType;

    public String growthStage; // Seedling, Sapling, Adult, Mature
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
        roomDescription = new Line(" is planted in the ground.", "1W3CONT8CONT3CONT4CONT6CONT1DY", "", true, true);

        growthStage = "Seedling";
        growthPercent = 0.0f;
        saturationPercent = 0.0f;
        budTimer = -1;
        maxBudCount = 5;
        decayIntoSeed = true;
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
        && location.planetoid.isDay()
        && !location.room.inside) {
            growthPercent += .01f;
            if(growthPercent >= 1.0) {
                growthPercent = 0;

                String previousNameLabel = prefix + name.label;
                String previousNameColorCode = String.valueOf(prefix.length()) + "CONT" + name.colorCode;

                if(growthStage.equals("Seedling")) {growthStage = "Sapling";}
                else if(growthStage.equals("Sapling")) {growthStage = "Adult";}
                else if(growthStage.equals("Adult")) {growthStage = "Mature";}

                updateName();

                // Display Message - Grows Into Next Stage //
                if(GameScreen.player.location.room == location.room) {
                    GameScreen.userInterface.console.writeToConsole(new Line(previousNameLabel + " grows into " + prefix.toLowerCase() + name.label + ".", previousNameColorCode + "1W6CONT5CONT" + String.valueOf(prefix.length()) + "CONT" + name.colorCode + "1DY", "", true, true));
                }

                // Set Adult Plant Bud Timer //
                if(growthStage.equals("Adult")) {
                    budTimer = 10;
                }
            }
        }

        // Update Fruit Growth //
        Fruit rottenFruit = null;
        ArrayList<Integer> removeFruitList = new ArrayList<>();
        for(int i = 0; i < containerItemList.size(); i++) {
            Fruit fruit = (Fruit) containerItemList.get(i);
            if(!(fruit.growthStage.equals("Mature") && growthPercent >= 0)) {
                if(saturationPercent > 0
                && location.planetoid.isDay()
                && !location.room.inside) {
                    fruit.updateGrowth();
                }
            }
            else {fruit.updateDecay(false);}

            // Rotten Fruit Fall From Plant //
            if(fruit.decayStage.equals("Rotten")
            && fruit.decayPercent >= 1.0) {
                fruit.decayPercent = 0.0f;
                removeFruitList.add(0, i);
                location.room.addItemToRoom(fruit);
                if(rottenFruit == null) {rottenFruit = fruit;}
            }
        }
        for(int deleteIndex : removeFruitList) {
            containerItemList.remove(deleteIndex);
        }

        // Display Message - A rotten Fruit falls off of Plant. //
        if(rottenFruit != null
        && GameScreen.player.location.room == location.room
        && location.room.isLit()) {
            String countString = "";
            String countColorCode = "";
            if(removeFruitList.size() > 1) {
                Line countLine = Utility.insertCommas(removeFruitList.size());
                countString = " (" + countLine.label + ")";
                countColorCode = "2DR" + countLine.colorCode + "1DR";
            }
            String messageLabel = "A rotten " + rottenFruit.name.label + countString + " falls off of " + masterPrefix.toLowerCase() + plantName.label + " " + plantType.label + ".";
            String messageColorCode = "2CONT7CONT" + rottenFruit.name.colorCode + countColorCode + "1W6CONT4CONT3CONT" + String.valueOf(masterPrefix.length()) + "CONT" + plantName.colorCode + "1W" + plantType.colorCode + "1DY";
            GameScreen.userInterface.console.writeToConsole(new Line(messageLabel, messageColorCode, "", true, true));
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
        if(Arrays.asList("Seedling", "Sapling").contains(growthStage)) {
            name.label = plantName.label + " " + plantType.label + " " + growthStage;
            name.colorCode = plantName.colorCode + "1W" + plantType.colorCode + "1W" + String.valueOf(growthStage.length()) + "CONT";
        }
        else if(Arrays.asList("Adult", "Mature").contains(growthStage)) {
            name.label = growthStage + " " + plantName.label + " " + plantType.label;
            name.colorCode = String.valueOf(growthStage.length()) + "CONT1W" + plantName.colorCode + "1W" + plantType.colorCode;
        }
        if(growthStage.equals("Adult")) {prefix = "An ";}
        else if(growthStage.equals("Mature")) {prefix = "A ";}
        else {prefix = masterPrefix;}
        nameKeyList = Entity.createNameKeyList(prefix.toLowerCase() + name.label.toLowerCase());
    }

    public Fruit createFruitBud() {
        Fruit fruit = new Fruit(id, location, masterPrefix, plantName);
        fruit.updateName();

        return fruit;
    }

    public static Plant load(Seed targetSeed, Location startLocation) {
        Plant plantItem = new Plant(targetSeed.id, startLocation);
        plantItem.masterPrefix = targetSeed.masterPrefix;
        plantItem.plantName = targetSeed.plantName;
        plantItem.plantType = targetSeed.plantType;
        plantItem.updateName();

        return plantItem;
    }
}
