package com.jbs.textgameengine.gamedata.world.utility;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TargetRoomData {
    public static HashMap<String, ArrayList<String>> sideDirectionMap = loadSideDirectionMap();

    public Room targetRoom;
    public String targetDirection;
    public int distance;
    public String message;

    public TargetRoomData() {
        targetRoom = null;
        targetDirection = "";
        distance = -1;
        message = "";
    }

    public static TargetRoomData getTargetRoomFromStartRoom(Room startRoom, ArrayList<String> directionList, boolean ignoreDoors, boolean ignoreHiddenExits) {
        TargetRoomData targetRoomData = new TargetRoomData();
        Room targetRoom = startRoom;

        int distance = 0;
        for(String direction : directionList) {
            if(targetRoomData.targetDirection.isEmpty()) {
                targetRoomData.targetDirection = direction;
            }

            // Exit Doesn't Exist //
            if(!targetRoom.exitMap.containsKey(direction)
            || targetRoom.exitMap.get(direction) == null) {
                targetRoomData.message = "No Exit";
                break;
            }

            // Door Is Closed //
            else if(targetRoom.doorMap.containsKey(direction)
            && targetRoom.doorMap.get(direction) != null
            && !targetRoom.doorMap.get(direction).status.equals("Open")) {
                targetRoomData.message = "Door Is Closed";
                if(!ignoreDoors) {
                    break;
                }
            }

            // Hidden Exit Is Closed //
            else if(targetRoom.hiddenExitMap.containsKey(direction)
            && targetRoom.hiddenExitMap.get(direction) != null
            && !targetRoom.hiddenExitMap.get(direction).isOpen) {
                targetRoomData.message = "Hidden Exit Is Closed";
                if(!ignoreDoors) {
                    break;
                }
            }

            // Spaceship Exit Door //
            else if(targetRoom.location.spaceship != null
            && targetRoom.location.spaceship.boardingRoom == targetRoom
            && !targetRoom.location.spaceship.hatchStatus.equals("Open")) {
                targetRoomData.message = "Door Is Closed";
                if(!ignoreDoors) {
                    break;
                }
            }

            // Spaceship Exit //
            if(targetRoom.location.spaceship != null
            && targetRoom.location.spaceship.boardingRoom == targetRoom
            && targetRoom.location.spaceship.location.room != null
            && targetRoom.location.spaceship.status.equals("Landed")) {
                targetRoom = targetRoom.location.spaceship.location.room;
            }

            // Normal Room Exit //
            else if(targetRoom.exitMap.containsKey(direction)
            && targetRoom.exitMap.get(direction) != null) {
                targetRoom = targetRoom.exitMap.get(direction);
            }

            distance += 1;
        }

        targetRoomData.targetRoom = targetRoom;
        targetRoomData.distance = distance;

        return targetRoomData;
    }

    public static TargetRoomData getTargetEntityRoomFromStartRoom(Room startRoom, Entity targetEntity, int maxDistance) {
        boolean targetFound = false;
        int targetRange = 0;
        String targetDirection = "";
        String message = "";

        // Same Room Check //
        if((targetEntity.isPlayer && targetEntity.location.room == startRoom)
        || (targetEntity.isMob && startRoom.mobList.contains(targetEntity))
        || (targetEntity.isItem && startRoom.itemList.contains(targetEntity))
        || (targetEntity.isSpaceship && startRoom.spaceshipList.contains(targetEntity))) {
            TargetRoomData targetRoomData = new TargetRoomData();
            targetRoomData.targetRoom = startRoom;
            targetRoomData.distance = 0;
            return targetRoomData;
        }

        for(String direction : Arrays.asList("North", "East", "South", "West", "Up", "Down")) {
            targetDirection = direction;

            String masterMessage = "";
            Room currentRoom = startRoom;
            Area currentArea = currentRoom.location.area;

            for(int i = 0; i < maxDistance; i++) {
                message = masterMessage;
                if(!currentRoom.exitMap.containsKey(direction)
                || currentRoom.exitMap.get(direction) == null) {
                    break;
                }

                TargetRoomData targetRoomData = getTargetRoomFromStartRoom(currentRoom, new ArrayList<String>(Arrays.asList(direction)), true, true);
                if(targetRoomData.message.equals("Door Is Closed")) {
                    masterMessage = targetRoomData.message;
                }

                if((targetEntity.isPlayer && targetEntity.location.room == currentRoom)
                || (targetEntity.isMob && currentRoom.mobList.contains(targetEntity))
                || (targetEntity.isItem && currentRoom.itemList.contains(targetEntity))
                || (targetEntity.isSpaceship && currentRoom.spaceshipList.contains(targetEntity))) {
                    message = masterMessage;
                    targetFound = true;
                    targetRange = i + 1;
                    break;
                }

                for(String sideDirection : sideDirectionMap.get(direction)) {
                    message = masterMessage;
                    Room sideRoom = currentRoom;
                    Area sideArea = currentArea;

                    for(int sideIndex = 0; sideIndex < maxDistance - (i + 1); sideIndex++) {
                        if(!sideRoom.exitMap.containsKey(sideDirection)
                        || sideRoom.exitMap.get(sideDirection) == null) {
                            break;
                        }

                        TargetRoomData targetSideRoomData = getTargetRoomFromStartRoom(sideRoom, new ArrayList<String>(Arrays.asList(sideDirection)), true, true);
                        if(targetSideRoomData.message.equals("Door Is Closed")) {
                            message = targetSideRoomData.message;
                        }

                        if((targetEntity.isPlayer && targetEntity.location.room == currentRoom)
                        || (targetEntity.isMob && currentRoom.mobList.contains(targetEntity))
                        || (targetEntity.isItem && currentRoom.itemList.contains(targetEntity))
                        || (targetEntity.isSpaceship && currentRoom.spaceshipList.contains(targetEntity))) {
                            targetFound = true;
                            targetRange = (i + 1) + (sideIndex + 1);
                            break;
                        }
                    }
                    if(targetFound) {break;}
                }
                if(targetFound) {break;}
            }
            if(targetFound) {break;}
        }

        if(!targetFound) {
            TargetRoomData targetRoomData = new TargetRoomData();
            targetRoomData.message = message;
            return targetRoomData;
        }
        else {
            TargetRoomData targetRoomData = new TargetRoomData();
            targetRoomData.distance = targetRange;
            targetRoomData.targetDirection = targetDirection;
            targetRoomData.message = message;
            return targetRoomData;
        }
    }

    public static HashMap<String, ArrayList<String>> loadSideDirectionMap() {
        HashMap<String, ArrayList<String>> sideDirectionMap = new HashMap<>();
        sideDirectionMap.put("North", new ArrayList<String>(Arrays.asList("East", "West", "Up", "Down")));
        sideDirectionMap.put("East", new ArrayList<String>(Arrays.asList("North", "South", "Up", "Down")));
        sideDirectionMap.put("South", new ArrayList<String>(Arrays.asList("East", "West", "Up", "Down")));
        sideDirectionMap.put("West", new ArrayList<String>(Arrays.asList("North", "South", "Up", "Down")));
        sideDirectionMap.put("Up", new ArrayList<String>(Arrays.asList("North", "East", "South", "West")));
        sideDirectionMap.put("Down", new ArrayList<String>(Arrays.asList("North", "East", "South", "West")));

        return sideDirectionMap;
    }
}
