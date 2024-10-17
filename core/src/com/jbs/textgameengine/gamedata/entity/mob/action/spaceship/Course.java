package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Point;
import com.jbs.textgameengine.screen.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class Course extends Action {
    public long targetX;
    public long targetY;
    public boolean clearCheck;

    public Course(Mob parentEntity) {
        super(parentEntity);

        targetX = -1;
        targetY = -1;
        clearCheck = false;
    }

    public Course() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("course", "cours", "cour", "cou").contains(inputList.get(0))) {
            Course courseAction = new Course(parentEntity);

            // Course X Y //
            if(inputList.size() == 3
            && Utility.isInteger(inputList.get(1))
            && Utility.isInteger(inputList.get(2))) {
                courseAction.targetX = Long.valueOf(inputList.get(1));
                courseAction.targetY = Long.valueOf(inputList.get(2));
            }

            // Course Clear //
            else if(inputList.size() == 2
            && inputList.get(1).equals("clear")) {
                courseAction.clearCheck = true;
            }

            // Course Target //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                courseAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            return courseAction;
        }

        return null;
    }

    public void initiate() {
        Planetoid targetPlanetoid = null;
        boolean alreadyOrbitingCheck = false;
        boolean alreadyClearCheck = false;
        Spaceship targetSpaceship = parentEntity.location.spaceship;

        // Set Planetoid Heading //
        if(!targetEntityString.isEmpty()) {
            for(Planetoid planetoid : targetSpaceship.location.solarSystem.planetoidList) {
                if(planetoid.nameKeyList.contains(targetEntityString)) {
                    targetPlanetoid = planetoid;

                    if(targetSpaceship.status.equals("Orbit")
                    && targetSpaceship.location.planetoid == planetoid) {
                        alreadyOrbitingCheck = true;
                    } else {
                        targetSpaceship.headingPlanetoid = planetoid;
                    }

                    break;
                }
            }
        }

        // Set X Y Heading //
        else if(targetX != -1 && targetY != -1) {
            if(targetX < -999999999999999L) {targetX = -999999999999999L;}
            else if(targetX > 999999999999999L) {targetX = 999999999999999L;}
            if(targetY < -999999999999999L) {targetY = -999999999999999L;}
            else if(targetY > 999999999999999L) {targetY = 999999999999999L;}

            targetSpaceship.headingXY = new Point(targetX, targetY);
        }

        // Clear Course //
        else if(clearCheck) {
            if(targetSpaceship.headingPlanetoid == null && targetSpaceship.headingXY == null) {
                alreadyClearCheck = true;
            } else {
                targetSpaceship.headingPlanetoid = null;
                targetSpaceship.headingXY = null;
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

        // Message - Your heading is already clear. //
        else if(alreadyClearCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Your heading is already clear.", "5CONT8CONT3CONT8CONT5CONT1DY", "", true, true));
            }
        }

        // Message - (Syntax Error Message) //
        else if(!clearCheck
        && targetEntityString.isEmpty()
        && targetX == -1
        && targetY == -1) {
            if(parentEntity.isPlayer) {
                String noInputString = "A computerized voice says, \"Set your course with 'Course Planet/X Y'\".";
                String noInputColorCode = "2W13CONT6CONT4CONT2DY1DY4SHIA5SHIA7SHIA5SHIA1DY7CONT6CONT1DR2W1W1DY2DY";
                GameScreen.userInterface.console.writeToConsole(new Line(noInputString, noInputColorCode, "", true, true));
            }
        }

        // Message - You can't find it on radar. //
        else if(!targetEntityString.isEmpty()
        && targetPlanetoid == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You can't find it on radar.", "4CONT3CONT1DY2DDW5CONT3CONT3CONT5CONT1DY", "", true, true));
            }
        }

        // Message - You are already orbiting Planet. //
        else if(!targetEntityString.isEmpty()
        && targetPlanetoid != null
        && alreadyOrbitingCheck) {
            if(parentEntity.isPlayer) {
                String courseString = "You are already orbiting " + targetPlanetoid.name.label + ".";
                String courseColorCode = "4CONT4CONT8CONT9CONT" + targetPlanetoid.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(courseString, courseColorCode, "", true, true));
            }
        }

        // Message - You clear the ship's heading. //
        else if(clearCheck
        && !alreadyClearCheck) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You clear the ship's heading.", "4CONT6CONT4CONT4CONT1DY2DDW7CONT1DY", "", true, true));
            }
        }

        // Message - You punch in some coordinates into the ship's computer. //
        else if(targetX != -1
        && targetY != -1) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You punch in some coordinates into the ship's computer.", "4CONT6CONT3CONT5CONT12CONT5CONT4CONT4CONT1DY2DDW8CONT1DY", "", true, true));
            }
        }

        // Message - You punch in the coordinates for Planet. //
        else if(targetPlanetoid != null) {
            if(parentEntity.isPlayer) {
                String courseString = "You punch in the coordinates for " + targetPlanetoid.name.label + ".";
                String courseColorCode = "4CONT6CONT3CONT4CONT12CONT4CONT" + targetPlanetoid.name.colorCode + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(courseString, courseColorCode, "", true, true));
            }
        }
    }
}
