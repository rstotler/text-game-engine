package com.jbs.textgameengine.gamedata.world.utility;

import com.jbs.textgameengine.gamedata.world.room.Room;

import java.util.ArrayList;

public class TargetRoomData {
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

    public static TargetRoomData getTargetRoomFromStartRoom(Room targetRoom, ArrayList<String> directionList, boolean ignoreDoors, boolean ignoreHiddenExits) {
        TargetRoomData targetRoomData = new TargetRoomData();

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
}
