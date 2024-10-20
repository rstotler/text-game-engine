package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class Scan extends Action {
    public Scan(Mob parentEntity) {
        super(parentEntity);
    }

    public Scan() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("scan", "sca", "sc").contains(inputList.get(0))) {
            Scan scanAction = new Scan(parentEntity);

            // Scan Planetoid //
            if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                scanAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            return scanAction;
        }

        return null;
    }

    public void initiate() {
        Planetoid targetPlanetoid = null;

        // Scan Planetoid //
        if(parentEntity.location.spaceship != null
        && parentEntity.location.room == parentEntity.location.spaceship.cockpitRoom
        && !targetEntityString.isEmpty()) {
            for(Planetoid planetoid : parentEntity.location.spaceship.location.solarSystem.planetoidList) {
                if(planetoid.nameKeyList.contains(targetEntityString)) {
                    targetPlanetoid = planetoid;
                    break;
                }
            }
        }

        if(targetPlanetoid != null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Scanning " + targetPlanetoid.name.label + "..", "9CONT" + targetPlanetoid.name.colorCode + "2DY", "", true, true));

                GameScreen.userInterface.console.writeToConsole(new Line("Type: " + targetPlanetoid.toString(), "4CONT2DY" + String.valueOf(targetPlanetoid.toString().length()) + "CONT", "", false, true));

                if(targetPlanetoid.isPlanet) {
                    Line locationXLine = Utility.insertCommas((int) targetPlanetoid.coordinates.x);
                    Line locationYLine = Utility.insertCommas((int) targetPlanetoid.coordinates.y);
                    GameScreen.userInterface.console.writeToConsole(new Line("Orbit Location: X: " + locationXLine.label + ", Y: " + locationYLine.label, "6CONT8CONT2DY1W2DY" + locationXLine.colorCode + "2DY1W2DY" + locationYLine.colorCode, "", false, true));

                    Line centerDistanceLine = Utility.insertCommas(targetPlanetoid.distanceFromCenter);
                    GameScreen.userInterface.console.writeToConsole(new Line("Distance From Sun: " + centerDistanceLine.label + " Miles", "9CONT5CONT3CONT2DY" + centerDistanceLine.colorCode + "1W5CONT", "", false, true));
                }

                boolean isLastLine = !targetPlanetoid.isPlanet;
                Line tiltLine = Utility.insertCommas(targetPlanetoid.axialTilt);
                GameScreen.userInterface.console.writeToConsole(new Line("Axial Tilt: " + tiltLine.label + " Degrees", "6CONT4CONT2DY" + tiltLine.colorCode + "1W7CONT", "", isLastLine, true));

                if(targetPlanetoid.isPlanet) {
                    int dayCount = targetPlanetoid.minuteCountYear / targetPlanetoid.minutesInDay;
                    int daysInYear = targetPlanetoid.minutesInYear / targetPlanetoid.minutesInDay;
                    String dayString = dayCount + "/" + daysInYear;
                    String dayColorCode = String.valueOf(dayCount).length() + "DDW1DY" + String.valueOf(daysInYear).length() + "DDW";
                    int yearPercent = (int) (((targetPlanetoid.minuteCountYear + 0.0f) / targetPlanetoid.minutesInYear) * 100);
                    GameScreen.userInterface.console.writeToConsole(new Line("Day: " + dayString + " (Year: " + String.valueOf(yearPercent) + "%)", "3CONT2DY" + dayColorCode + "2DY4CONT2DY" + String.valueOf(yearPercent).length() + "DDW2DY", "", true, true));
                }
            }
        }

        // Message - You must be in a cockpit to do that. //
        if(parentEntity.location.spaceship == null
        || parentEntity.location.spaceship.cockpitRoom != parentEntity.location.room) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be in a cockpit to do that.", "4CONT5CONT3CONT3CONT2W8CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Message - (Syntax Error) //
        else if(targetEntityString.isEmpty()) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("A computerized voice says, \"Scan a radar object with 'Scan Planetoid'.\"", "2W13CONT6CONT4CONT2DY1DY5CONT2W6CONT7CONT5CONT1DY5CONT9CONT2DY1DY", "", true, true));
            }
        }

        // Message - The ship's radar detects no such planet. //
        else if(targetPlanetoid == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("The ship's radar detects no such planet.", "4CONT4CONT1DY2DDW6CONT8CONT3CONT5CONT6CONT1DY", "", true, true));
            }
        }
    }
}
