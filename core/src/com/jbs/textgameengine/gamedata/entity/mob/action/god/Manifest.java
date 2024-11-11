package com.jbs.textgameengine.gamedata.entity.mob.action.god;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.area.WeatherSystem;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

public class Manifest extends Action {
    public String entityType;
    public String entitySubType;
    public int targetNum;

    public Manifest(Mob parentEntity) {
        super(parentEntity);

        entityType = "";
        entitySubType = "";
        targetNum = -1;
    }

    public Manifest() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));
        ArrayList<String> entityTypeList = new ArrayList<>(Arrays.asList("mob", "item", "general", "gear", "weapon", "firearm", "throwable", "ammo", "magazine", "quiver", "food", "drink", "plant", "seed"));

        if(Arrays.asList("manifest", "manifes", "manife", "manif", "mani", "man").contains(inputList.get(0))) {
            Manifest manifestAction = new Manifest(parentEntity);

            // Manifest Weather WeatherType //
            if(inputList.size() == 3
            && inputList.get(1).equals("weather")
            && Arrays.asList("rain", "snow", "precipitation", "clouds", "cloudy", "stop").contains(inputList.get(2))) {
                manifestAction.entityType = "Weather";
                if(inputList.get(2).equals("stop")) {
                    manifestAction.entitySubType = "Stop";
                }
                else if(Arrays.asList("rain", "snow", "precipitation").contains(inputList.get(2))) {
                    manifestAction.entitySubType = "Precipitation";
                } else {
                    manifestAction.entitySubType = "Clouds";
                }
            }

            // Manifest EntityType EntityNum # //
            else if(inputList.size() == 4
            && entityTypeList.contains(inputList.get(1))
            && Utility.isInteger(inputList.get(2))
            && Utility.isInteger(inputList.get(3))) {
                manifestAction.entityType = inputList.get(1);
                manifestAction.targetNum = Integer.valueOf(inputList.get(2));
                manifestAction.targetCount = Integer.valueOf(inputList.get(3));
                manifestAction.parentEntity = parentEntity;
            }

            // Manifest EntityType EntityNum //
            else if(inputList.size() == 3
            && entityTypeList.contains(inputList.get(1))
            && Utility.isInteger(inputList.get(2))) {
                manifestAction.entityType = inputList.get(1);
                manifestAction.targetCount = 1;
                manifestAction.targetNum = Integer.valueOf(inputList.get(2));
                manifestAction.parentEntity = parentEntity;
            }

            else {
                manifestAction.parentEntity = null;
                if(parentEntity.isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("Manifest what?", "9CONT4CONT1DY", "", true, true));
                }
            }

            // Entity Type Check (Action Type) //
            if(!manifestAction.entityType.equals("Weather")
            && manifestAction.entityType.length() > 1) {
                manifestAction.entityType = String.valueOf(manifestAction.entityType.charAt(0)).toUpperCase() + manifestAction.entityType.substring(1);
                if(manifestAction.entityType.equals("Item")) {manifestAction.entityType = "General";}
                else if(manifestAction.entityType.equals("Seed")) {manifestAction.entityType = "Plant";}
            }

            return manifestAction;
        }

        return null;
    }

    public void initiate() {
        Entity targetEntity = null;
        String weatherType = "";

        // Manifest Weather //
        if(entityType.equals("Weather")) {
            WeatherSystem newWeatherSystem = new WeatherSystem(parentEntity.location.area);

            if(entitySubType.equals("Stop")) {
                newWeatherSystem.startTimer = -1;
            }
            else if(entitySubType.equals("Precipitation")) {
                newWeatherSystem.startTimer = -1;
                newWeatherSystem.weatherTimer = 500;
                newWeatherSystem.precipitationTimer = 500;
                newWeatherSystem.precipitationType = "Rain";
                if(parentEntity.location.area.currentTemperature < ((Planet) parentEntity.location.planetoid).getFreezingTemperature()) {
                    newWeatherSystem.precipitationType = "Snow";
                }
                weatherType = newWeatherSystem.precipitationType;
            }
            else {
                newWeatherSystem.startTimer = -1;
                newWeatherSystem.weatherTimer = 500;
            }

            parentEntity.location.area.weatherSystem = newWeatherSystem;
        }

        // Manifest Mob //
        else if(entityType.equals("Mob")) {
            for(int i = 0; i < targetCount; i++) {
                Entity mob = Mob.load(targetNum, parentEntity.location);
                if(!mob.name.label.equals("Default Mob")) {
                    if(targetEntity == null) {targetEntity = mob;}
                    parentEntity.location.room.mobList.add(mob);
                }
            }
        }

        // Manifest Item //
        else {
            for(int i = 0; i < targetCount; i++) {
                Entity item = Item.load(entityType, targetNum, parentEntity.location);
                if(!(item.name.label.length() >= 7 && item.name.label.substring(0, 7).equals("Default"))) {
                    if(targetEntity == null) {targetEntity = item;}
                    parentEntity.location.room.addItemToRoom(item);
                }
            }
        }

        // Message - The weather above dissipates. //
        if(entityType.equals("Weather")
        && entitySubType.equals("Stop")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The weather above dissipates.", "4CONT8CONT6CONT10CONT1DY", "", true, true));
            }
        }

        // Message - Clouds roll in overhead. //
        else if(entityType.equals("Weather")
        && entitySubType.equals("Clouds")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Clouds roll in overhead.", "7CONT5CONT3CONT8CONT1DY", "", true, true));
            }
        }

        // Message - It begins to rain/snow. //
        else if(entityType.equals("Weather")
        && !weatherType.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("It begins to " + weatherType.toLowerCase() + ".", "3CONT7CONT3CONT" + String.valueOf(weatherType.length()) + "CONT1DY", "", true, true));
            }
        }

        // Message - You utter an incantation, but nothing happens. //
        else if(targetEntity == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You utter an incantation, but nothing happens.", "4CONT6CONT3CONT11CONT2DY4CONT8CONT7CONT1DY", "", true, true));
            }
        }

        // Message - You utter an incantation and an Entity appears in a puff of smoke. //
        else if(targetCount == 1) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You utter an incantation and " + targetEntity.prefix.toLowerCase() + targetEntity.name.label + " appears in a puff of smoke.", "4CONT6CONT3CONT12CONT4CONT" + String.valueOf(targetEntity.prefix).length() + "CONT" + targetEntity.name.colorCode + "1W8CONT3CONT2CONT5CONT3CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You utter an incantation and some EntityType appears in a puff of smoke. //
        else if(targetCount > 1) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You utter an incantation and some " + entityType.toLowerCase() + " appears in a puff of smoke.", "4CONT6CONT3CONT12CONT4CONT5CONT" + String.valueOf(entityType.length() + 1) + "CONT8CONT3CONT2W5CONT3CONT5CONT1DY", "", true, true));
            }
        }
    }
}
