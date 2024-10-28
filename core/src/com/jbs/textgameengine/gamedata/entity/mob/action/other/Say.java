package com.jbs.textgameengine.gamedata.entity.mob.action.other;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.AreaAndRoomData;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Say extends Action {
    public String targetSayString;
    public boolean shoutCheck;

    public Say(Mob parentEntity) {
        super(parentEntity);

        targetSayString = "";
        shoutCheck = false;
    }

    public Say() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("say", "sa", "shout", "shou", "sho", "yell", "yel").contains(inputList.get(0))) {
            Say sayAction = new Say(parentEntity);
            if(!inputList.get(0).startsWith("sa")) {
                sayAction.shoutCheck = true;
            }

            // Say SayString //
            if(inputList.size() > 1) {
                List<String> targetSayStringList = inputList.subList(1, inputList.size());
                sayAction.targetSayString = targetSayStringList.stream().collect(Collectors.joining(" "));
            }

            else {
                GameScreen.userInterface.console.writeToConsole(new Line("Say what?", "4CONT4CONT1DY", "", true, true));
                sayAction.parentEntity = null;
            }

            return sayAction;
        }

        return null;
    }

    public void initiate() {

        // Get Say Message Data //
        String punctuation = ".";
        String sayStringRaw = "";
        if(!targetSayString.isEmpty()) {
            sayStringRaw = targetSayString.substring(0, 1).toUpperCase();
            if(targetSayString.length() > 1) {
                sayStringRaw += targetSayString.substring(1);
            }
        }

        String sayAskWord = "say";
        if(sayStringRaw.charAt(sayStringRaw.length() - 1) == '?') {
            punctuation = "?";
            sayStringRaw = sayStringRaw.substring(0, sayStringRaw.length() - 1);
            sayAskWord = "ask";
        }
        else if(sayStringRaw.charAt(sayStringRaw.length() - 1) == '!') {
            punctuation = "!";
            sayStringRaw = sayStringRaw.substring(0, sayStringRaw.length() - 1);
            sayAskWord = "exclaim";
        }
        else if(sayStringRaw.charAt(sayStringRaw.length() - 1) == '.'
        && sayStringRaw.length() >= 2
        && sayStringRaw.charAt(sayStringRaw.length() - 2) == '.') {
            punctuation = "..";
            sayStringRaw = sayStringRaw.substring(0, sayStringRaw.length() - 2);
        }
        else if(sayStringRaw.charAt(sayStringRaw.length() - 1) == '.') {
            punctuation = ".";
            sayStringRaw = sayStringRaw.substring(0, sayStringRaw.length() - 1);
        }
        if(shoutCheck) {
            sayAskWord = "shout";
            punctuation = "!";
        }

        String sayAskString = "";
        String sayAskColorCode = "";
        if(parentEntity.isPlayer) {
            sayAskString = "You " + sayAskWord;
            sayAskColorCode = "4CONT" + String.valueOf(sayAskWord.length()) + "CONT";
        }

        ArrayList<Room> roomList = new ArrayList<>(Arrays.asList(parentEntity.location.room));
        if(shoutCheck) {
            AreaAndRoomData surroundingRoomData = AreaAndRoomData.getSurroundingAreaAndRoomData(parentEntity.location.room, parentEntity.getMaxViewDistance(), false, false);
            roomList = surroundingRoomData.roomList;
        }

        for(Room room : roomList) {
            if(!parentEntity.isPlayer) {
                if(!parentEntity.location.room.isLit() || room != parentEntity.location.room) {
                    if(room != parentEntity.location.room) {
                        String shoutDirectionString = "";
                        String shoutDirectionColorCode = "";
                        TargetRoomData targetRoomData = TargetRoomData.findTargetRoomFromStartRoom(parentEntity.location.room, room, parentEntity.getMaxViewDistance());
                        if(!targetRoomData.directionList.isEmpty()) {
                            shoutDirectionString = "from the " + Location.getOppositeDirection(targetRoomData.directionList.get(targetRoomData.directionList.size() - 1)).toLowerCase() + " ";
                            shoutDirectionColorCode = "5CONT4CONT" + String.valueOf(Location.getOppositeDirection(targetRoomData.directionList.get(targetRoomData.directionList.size() - 1)).length()) + "CONT1W";
                        }
                        sayAskString = "Someone " + shoutDirectionString + "shouts";
                        sayAskColorCode = "8CONT" + shoutDirectionColorCode + "6CONT";
                    }
                    else {
                        sayAskString = "Someone " + sayAskWord + "s";
                        sayAskColorCode = "8CONT" + String.valueOf(sayAskWord.length()) + "CONT1DDW";
                    }
                }
                else {
                    sayAskString = parentEntity.prefix + parentEntity.name.label + " " + sayAskWord + "s";
                    sayAskColorCode = String.valueOf(parentEntity.prefix.length()) + "CONT" + parentEntity.name.colorCode + "1W" + String.valueOf(sayAskWord.length() + 1) + "CONT";
                }
            }

            // Player Message //
            if(GameScreen.player.location.room == room) {
                String sayString = sayAskString + ", \"" + sayStringRaw + punctuation + "\"";
                String sayColorCode = sayAskColorCode + "2DY1DY" + String.valueOf(sayStringRaw.length()) + "SHIA" + String.valueOf(punctuation.length()) + "DY" + "1DY";
                GameScreen.userInterface.console.writeToConsole(new Line(sayString, sayColorCode, "", true, true));
            }
        }
    }
}
