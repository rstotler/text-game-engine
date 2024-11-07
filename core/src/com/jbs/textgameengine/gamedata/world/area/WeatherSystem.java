package com.jbs.textgameengine.gamedata.world.area;

import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.Random;

public class WeatherSystem {
    public Area parentArea;

    public int startTimer;
    public int weatherTimer;
    public int startPrecipitationTimer;
    public int precipitationTimer;
    public String precipitationType;

    public WeatherSystem(Area parentArea) {
        this.parentArea = parentArea;

        startTimer = 1;
        weatherTimer = -1;
        startPrecipitationTimer = -1;
        precipitationTimer = -1;
        precipitationType = "";
    }

    public void update() {
        adjustWeather();

        // Start Weather //
        if(startTimer > 0) {
            startTimer -= 1;
            if(startTimer == 0) {
                startTimer = -1;
                startPrecipitationTimer = -1;
                weatherTimer = new Random().nextInt(1000) + 60;
                if(parentArea.currentTemperature < ((Planet) parentArea.location.planetoid).getEvaporateTemperature()
                && new Random().nextInt(2) == 0) {
                    precipitationTimer = new Random().nextInt((int) (weatherTimer * 1.5));
                    precipitationType = "Rain";
                    if(parentArea.currentTemperature <= ((Planet) parentArea.location.planetoid).getFreezingTemperature()) {precipitationType = "Snow";}
                }

                if(GameScreen.player.location.room.location.area == parentArea
                && !GameScreen.player.location.room.inside) {
                    displayMessage("Start", true, precipitationTimer != -1);
                }
            }
        }

        // Update Weather //
        else if(weatherTimer > 0) {
            weatherTimer -= 1;
            if(startPrecipitationTimer > 0) {startPrecipitationTimer -= 1;}
            else if(precipitationTimer > 0) {precipitationTimer -= 1;}

            // Stop Weather (And Precipitation) //
            if(weatherTimer == 0) {
                if(GameScreen.player.location.room.location.area == parentArea
                && !GameScreen.player.location.room.inside) {
                    displayMessage("Stop", true, precipitationTimer != -1);
                }

                weatherTimer = -1;
                precipitationTimer = -1;
                precipitationType = "";
                startTimer = new Random().nextInt(700) + 120;
            }

            // Start Precipitation //
            else if(startPrecipitationTimer == 0) {
                startPrecipitationTimer = -1;

                if(parentArea.currentTemperature < ((Planet) parentArea.location.planetoid).getEvaporateTemperature()) {
                    precipitationTimer = new Random().nextInt((int) (weatherTimer * 1.5));
                    precipitationType = "Rain";
                    if(parentArea.currentTemperature <= ((Planet) parentArea.location.planetoid).getFreezingTemperature()) {precipitationType = "Snow";}

                    if(GameScreen.player.location.room.location.area == parentArea
                    && !GameScreen.player.location.room.inside) {
                        displayMessage("Start", false, true);
                    }
                }
            }

            // Stop Precipitation Only //
            else if(precipitationTimer == 0) {
                if(GameScreen.player.location.room.location.area == parentArea
                && !GameScreen.player.location.room.inside) {
                    displayMessage("Stop", false, true);
                }

                precipitationTimer = -1;
                precipitationType = "";
                startPrecipitationTimer = new Random().nextInt((int) (weatherTimer * 1.5));
            }
        }
    }

    public void adjustWeather() {

        // Evaporate Rain //
        if(precipitationTimer > 1
        && parentArea.currentTemperature >= ((Planet) parentArea.location.planetoid).getEvaporateTemperature()) {
            precipitationTimer = -1;

            // Display Message //
            if(GameScreen.player.location.room.location.area == parentArea
            && !GameScreen.player.location.room.inside) {
                GameScreen.userInterface.console.writeToConsole(new Line("The rain boils away into the air.", "4CONT5SHIAB6CONT5CONT5CONT4CONT3CONT1DY", "", true, true));
            }
        }

        // Rain Turn To Snow //
        else if(precipitationTimer > 1
        && precipitationType.equals("Rain")
        && parentArea.currentTemperature <= ((Planet) parentArea.location.planetoid).getFreezingTemperature()) {
            precipitationType = "Snow";

            // Display Message //
            if(GameScreen.player.location.room.location.area == parentArea
            && !GameScreen.player.location.room.inside) {
                GameScreen.userInterface.console.writeToConsole(new Line("The rain turns into snow.", "4CONT5SHIAB6CONT5CONT4SHIA1DY", "", true, true));
            }
        }

        // Snow Turn To Rain //
        else if(precipitationTimer > 1
        && precipitationType.equals("Snow")
        && parentArea.currentTemperature > ((Planet) parentArea.location.planetoid).getFreezingTemperature()) {
            precipitationType = "Rain";

            // Display Message //
            if(GameScreen.player.location.room.location.area == parentArea
            && !GameScreen.player.location.room.inside) {
                GameScreen.userInterface.console.writeToConsole(new Line("The snow turns into rain.", "4CONT5SHIA6CONT5CONT4SHIAB1DY", "", true, true));
            }
        }
    }

    public void displayMessage(String messageType, boolean cloudCheck, boolean precipitationCheck) {
        String precipitationString = "";
        String precipitationColorCode = "";
        if(precipitationCheck) {
            precipitationString = "rain";
            precipitationColorCode = "4SHIAB";
            if(parentArea.currentTemperature <= ((Planet) parentArea.location.planetoid).getFreezingTemperature()) {
                precipitationString = "snow";
                precipitationColorCode = "4SHIA";
            }
        }

        if(messageType.equals("Start")) {
            if(cloudCheck && precipitationCheck) {
                GameScreen.userInterface.console.writeToConsole(new Line("Clouds roll in overhead as it starts to " + precipitationString + ".", "7CONT5CONT3CONT9CONT3CONT3CONT7CONT3CONT" + precipitationColorCode + "1DY", "", true, true));
            } else if(cloudCheck) {
                GameScreen.userInterface.console.writeToConsole(new Line("Clouds roll in overhead.", "7CONT5CONT3CONT8CONT1DY", "", true, true));
            } else if(precipitationCheck) {
                GameScreen.userInterface.console.writeToConsole(new Line("It starts to " + precipitationString + ".", "3CONT7CONT3CONT" + precipitationColorCode + "1DY", "", true, true));
            }
        }

        else if(messageType.equals("Stop")) {
            precipitationString = "raining";
            precipitationColorCode = "7SHIAB";
            if(parentArea.currentTemperature <= ((Planet) parentArea.location.planetoid).getFreezingTemperature()) {
                precipitationString = "snowing";
                precipitationColorCode = "7SHIA";
            }

            if(cloudCheck && precipitationCheck) {
                GameScreen.userInterface.console.writeToConsole(new Line("It stops " + precipitationString + " as the clouds above dissipate.", "3CONT6CONT" + precipitationColorCode + "1DW3CONT4CONT7CONT6CONT9CONT1DY", "", true, true));
            } else if(cloudCheck) {
                GameScreen.userInterface.console.writeToConsole(new Line("The clouds above dissipate.", "4CONT7CONT6CONT9CONT1DY", "", true, true));
            } else if(precipitationCheck) {
                GameScreen.userInterface.console.writeToConsole(new Line("It stops " + precipitationString + ".", "3CONT6CONT" + precipitationColorCode + "1DY", "", true, true));
            }
        }
    }
}
