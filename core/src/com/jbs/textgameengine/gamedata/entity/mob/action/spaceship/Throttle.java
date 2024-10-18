package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class Throttle extends Action {
    public int targetSpeed;

    public Throttle(Mob parentEntity) {
        super(parentEntity);

        targetSpeed = -1;
    }

    public Throttle() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("throttle", "throttl", "thrott", "throt", "speed", "spee").contains(inputList.get(0))) {
            Throttle throttleAction = new Throttle(parentEntity);

            // Throttle Speed //
            if(inputList.size() == 2
            && Utility.isInteger(inputList.get(1))) {
                throttleAction.targetSpeed = Integer.valueOf(inputList.get(1));
            }

            // Throttle Full/Max/Half/None/Off //
            else if(inputList.size() == 2
            && Arrays.asList("full", "max", "half", "none", "off").contains(inputList.get(1))) {
                throttleAction.targetEntityString = inputList.get(1).substring(0, 1).toUpperCase() + inputList.get(1).substring(1);
            }

            return throttleAction;
        }

        return null;
    }

    public void initiate() {
        Spaceship targetSpaceship = parentEntity.location.spaceship;
        float targetSpeedPercent = -1.0f;
        boolean speedAlreadySet = false;

        // Update Target Speed //
        if(targetSpeed != -1) {
            targetSpeedPercent = targetSpeed / 100.0f;
            if(targetSpeedPercent < 0) {targetSpeedPercent = 0;}
            else if(targetSpeedPercent > 1.0) {targetSpeedPercent = 1.0f;}
        }
        else if(!targetEntityString.isEmpty()) {
            if(targetEntityString.equals("Full")
            || targetEntityString.equals("Max")) {targetSpeedPercent = 1.0f;}
            else if(targetEntityString.equals("Half")) {targetSpeedPercent = .50f;}
            else if(targetEntityString.equals("None")
            || targetEntityString.equals("Off")) {targetSpeedPercent = 0;}
        }

        // Adjust Throttle //
        if(targetSpeedPercent != -1
        && targetSpaceship != null
        && parentEntity.location.room == targetSpaceship.cockpitRoom
        && Arrays.asList("Orbit", "Flight").contains(targetSpaceship.status)
        && !(targetSpaceship.status.equals("Flight") && targetSpaceship.headingPlanetoid == null && targetSpaceship.headingXY == null)) {
            if(targetSpeedPercent == targetSpaceship.targetSpeedPercent) {
                speedAlreadySet = true;
            }
            else {
                if(true) {
                    if(targetSpeedPercent > targetSpaceship.speedPercent) {
                        targetSpaceship.displaySpeedUpMessage = 1;
                        targetSpaceship.displaySpeedDownMessage = -1;
                    }
                    else {
                        targetSpaceship.displaySpeedDownMessage = 1;
                        targetSpaceship.displaySpeedUpMessage = -1;
                    }
                }
                targetSpaceship.targetSpeedPercent = targetSpeedPercent;
            }
        }

        // Message - You must be in a cockpit to do that. //
        if(!(targetSpaceship != null
        && parentEntity.location.room == targetSpaceship.cockpitRoom)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You must be in a cockpit to do that.", "4CONT5CONT3CONT3CONT2W8CONT3CONT3CONT4CONT1DY", "", true, true));
            }
        }

        // Message - You can't do that while landed/launching. //
        else if(targetSpaceship.status.startsWith("Land")
        || targetSpaceship.status.equals("Launch")) {
            if(parentEntity.isPlayer) {
                String actionString = "landed";
                if(targetSpaceship.status.equals("Land")) {actionString = "landing";}
                else if(targetSpaceship.status.equals("Launch")) {actionString = "launching";}
                String courseString = "You can't do that while " + actionString + ".";
                String courseColorCode = "4CONT3CONT1DY2DDW3CONT5CONT6CONT" + String.valueOf(actionString.length()) + "CONT1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(courseString, courseColorCode, "", true, true));
            }
        }

        // Message - (Syntax Error) //
        else if(targetSpeedPercent == -1
        && targetEntityString.isEmpty()) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("A computerized voice says, \"Adjust speed with 'Throttle %'.\"", "2W13CONT6CONT4CONT2DY1DY7CONT6CONT5CONT1DY10CONT1DY2DY", "", true, true));
            }
        }

        // Message - A computerized voice says, "Please choose a heading." //
        else if(targetSpaceship.status.equals("Flight")
        && targetSpaceship.headingPlanetoid == null
        && targetSpaceship.headingXY == null) {
            if(parentEntity.isPlayer) {
                userInterface.console.writeToConsole(new Line("A computerized voice says, \"Please choose a heading.\"", "2W13CONT6CONT4CONT2DY1DY7CONT7CONT2W7CONT2DY", "", true, true));
            }
        }

        // Message - The throttle is already set to Speed%. //
        else if(speedAlreadySet) {
            if(parentEntity.isPlayer) {
                int displaySpeed = (int) (targetSpeedPercent * 100);
                userInterface.console.writeToConsole(new Line("The throttle is already set to " + displaySpeed + "%.", "4CONT9CONT3CONT8CONT4CONT3CONT" + String.valueOf(displaySpeed).length() + "CONT2DY", "", true, true));
            }
        }

        // Message - You adjust the throttle to Speed%. //
        else {
            if(parentEntity.isPlayer) {
                int displaySpeed = (int) (targetSpeedPercent * 100);
                userInterface.console.writeToConsole(new Line("You adjust the throttle to " + displaySpeed + "%.", "4CONT7CONT4CONT9CONT3CONT" + String.valueOf(displaySpeed).length() + "CONT2DY", "", true, true));
            }
        }
    }
}
