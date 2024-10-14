package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.room.door.Door;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

import static com.jbs.textgameengine.screen.gamescreen.GameScreen.userInterface;

public class OCLU extends Action {
    public OCLU(Mob parentEntity) {
        super(parentEntity);
    }

    public OCLU() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("open", "ope", "op", "close", "clos", "clo", "cl", "lock", "loc", "unlock", "unloc", "unlo", "unl").contains(inputList.get(0))) {
            OCLU ocluAction = new OCLU(parentEntity);

            if(inputList.get(0).charAt(0) == 'o') {ocluAction.actionType = "Open";}
            else if(inputList.get(0).charAt(0) == 'c') {ocluAction.actionType = "Close";}
            else if(inputList.get(0).charAt(0) == 'l') {ocluAction.actionType = "Lock";}
            else if(inputList.get(0).charAt(0) == 'u') {ocluAction.actionType = "Unlock";}

            // OCLU Direction //
            if(inputList.size() == 2
            && Location.directionList.contains(inputList.get(1))) {
                ocluAction.targetDirection = Location.getDirectionFromSubstring(inputList.get(1));
            }

            // OCLU Target //
            else if(inputList.size() >= 2) {
                List<String> targetEntityStringList = inputList.subList(1, inputList.size());
                ocluAction.targetEntityString = targetEntityStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                userInterface.console.writeToConsole(new Line(ocluAction.actionType + " what?", String.valueOf(ocluAction.actionType.length() + 1) + "CONT4CONT1DY", "", true, true));
                ocluAction.parentEntity = null;
            }

            return ocluAction;
        }

        return null;
    }

    public void initiate() {
        if(!targetDirection.isEmpty()) {initiateOCLUDoor();}
        else {initiateOCLUEntity();}
    }

    public void initiateOCLUDoor() {
        // Initiate OCLU Action Is At BOTTOM Of Function //
        Door targetDoor = null;

        // Get Target Door Data //
        if(parentEntity.location.room.exitMap.containsKey(targetDirection) && parentEntity.location.room.exitMap.get(targetDirection) != null
        && parentEntity.location.room.doorMap.containsKey(targetDirection) && parentEntity.location.room.doorMap.get(targetDirection) != null
        && !(parentEntity.location.room.hiddenExitMap.containsKey(targetDirection) && parentEntity.location.room.hiddenExitMap.get(targetDirection) != null && !parentEntity.location.room.hiddenExitMap.get(targetDirection).isOpen)) {
            targetDoor = parentEntity.location.room.doorMap.get(targetDirection);
        }

        // Message - There is no door to the Direction. //
        if(targetDoor == null) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("There is no door to the " + targetDirection.toLowerCase() + ".", "6CONT3CONT3CONT5CONT3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT1DY", "", true, true));
            }
        }

        // Message - That door opens/closes automatically. //
        else if(Arrays.asList("Open", "Close").contains(actionType)
        && targetDoor.type.equals("Automatic")) {
            if(parentEntity.isPlayer) {
                String openCloseString = "opens ";
                if(actionType.equals("Close")) {openCloseString = "closes ";}
                GameScreen.userInterface.console.writeToConsole(new Line("That door " + openCloseString + "automatically.", "5CONT5CONT" + String.valueOf(openCloseString.length()) + "CONT13CONT1DY", "", true, true));
            }
        }

        // Message - That door doesn't have a lock. //
        else if(Arrays.asList("Lock", "Unlock").contains(actionType)
        && targetDoor.keyNum == -9999) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("That door doesn't have a lock.", "5CONT5CONT5CONT1DY2DDW5CONT2W4CONT1DY", "", true, true));
            }
        }

        // Message - It's already TargetAction. //
        else if((actionType.equals("Open") && targetDoor.status.equals("Open"))
        || (actionType.equals("Close") && targetDoor.status.equals("Closed"))
        || (actionType.equals("Lock") && targetDoor.status.equals("Locked"))
        || (actionType.equals("Close") && targetDoor.status.equals("Locked"))
        || (actionType.equals("Unlock") && targetDoor.status.equals("Open"))
        || (actionType.equals("Unlock") && targetDoor.status.equals("Closed"))) {
            if(parentEntity.isPlayer) {
                String actionTypeString = actionType.toLowerCase();
                if(actionType.equals("Close")) {actionTypeString = actionTypeString + "d";}
                else if(!actionType.equals("Open")) {actionTypeString = actionTypeString + "ed";}
                GameScreen.userInterface.console.writeToConsole(new Line("It's already " + actionTypeString + ".", "2CONT1DY2DDW8CONT" + String.valueOf(actionTypeString.length()) + "CONT1DY", "", true, true));
            }
        }

        // Message - You lack the key. //
        else if((actionType.equals("Lock") && targetDoor.keyNum != -9999 && !parentEntity.hasKey(targetDoor.keyNum))
        || (targetDoor.status.equals("Locked") && actionType.equals("Unlock") && !parentEntity.hasKey(targetDoor.keyNum))) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You lack the key.", "4CONT5CONT4CONT3CONT1DY", "", true, true));
            }
        }

        // Message - It's locked. //
        else if(targetDoor.status.equals("Locked")
        && (actionType.equals("Open") || actionType.equals("Unlock"))
        && !parentEntity.hasKey(targetDoor.keyNum)) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("It's locked.", "2CONT1DY2DDW6CONT1DY", "", true, true));
            }
        }

        // Initiate Action & Message - You TargetAction the door to the Direction. //
        else {
            if(parentEntity.isPlayer) {
                String actionString = actionType.toLowerCase();
                String actionColorCode = String.valueOf(actionType.length()) + "CONT";
                if(actionType.equals("Open")
                && targetDoor.status.equals("Locked")) {
                    actionString = "unlock and open";
                    actionColorCode = "7CONT4CONT4CONT";
                }
                else if(actionType.equals("Lock")
                && targetDoor.status.equals("Open")) {
                    actionString = "close and lock";
                    actionColorCode = "6CONT4CONT4CONT";
                }

                String ocluString = "You " + actionString + " the door to the " + targetDirection.toLowerCase() + ".";
                String ocluColorCode = "4CONT" + actionColorCode + "CONT1W4CONT5CONT3CONT4CONT" + String.valueOf(targetDirection.length()) + "CONT1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(ocluString, ocluColorCode, "", true, true));
            }

            // Set New Door Status //
            String newDoorStatus = actionType;
            if(newDoorStatus.equals("Close")) {newDoorStatus = "Closed";}
            else if(newDoorStatus.equals("Lock")) {newDoorStatus = "Locked";}
            else if(newDoorStatus.equals("Unlock")) {newDoorStatus = "Closed";}
            targetDoor.status = newDoorStatus;
        }
    }

    public void initiateOCLUEntity() {

        // Open/Close/Lock/Unlock Entity //

        // Message - You can't find it. //
//        if() {
//
//        }
//
//        // Message - You can't TargetAction that. //
//        else if() {
//
//        }
//
//        // Message - It's already TargetAction. //
//        else if() {
//
//        }
//
//        // Message - It's locked. //
//        else if() {
//
//        }
//
//        // Message - You lack the key. //
//        else if() {
//
//        }
//
//        // Message - You TargetAction TargetContainer. //
//        else if() {
//
//        }
    }
}
