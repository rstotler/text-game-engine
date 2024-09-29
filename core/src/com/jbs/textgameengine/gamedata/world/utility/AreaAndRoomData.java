package com.jbs.textgameengine.gamedata.world.utility;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.utility.Point;

import java.util.*;

public class AreaAndRoomData {
    public ArrayList<Area> areaList;
    public ArrayList<Room> roomList;

    public AreaAndRoomData(ArrayList<Area> areaList, ArrayList<Room> roomList) {
        this.areaList = areaList;
        this.roomList = roomList;
    }

    public static AreaAndRoomData getSurroundingAreaAndRoomData(Room targetRoom, int targetRange) {
        return examineRoomData(targetRoom.location.area, targetRoom, targetRange, "", new ArrayList<Area>(), new ArrayList<Room>(), new Point(0, 0, 0));
    }

    public static AreaAndRoomData examineRoomData(Area targetArea, Room targetRoom, int targetRange, String targetDirection, ArrayList<Area> examinedAreaList, ArrayList<Room> examinedRoomList, Point viewLocation) {
        // Helper Function For GetSurroundingAreaAndRoomData()

        if(!examinedAreaList.contains(targetArea)) {
            examinedAreaList.add(targetArea);
        }
        if(!examinedRoomList.contains(targetRoom)) {
            examinedRoomList.add(targetRoom);
        }

        if(viewLocation.x + viewLocation.y + viewLocation.z < targetRange) {
            Point firstLocation = new Point(viewLocation);
            ArrayList<String> directionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West", "Up", "Down"));
            if(!targetDirection.isEmpty() && directionList.contains(Location.getOppositeDirection(targetDirection))) {
                directionList.remove(Location.getOppositeDirection(targetDirection));
            }

            for(String direction : directionList) {
                if(!direction.equals("North")) {
                    viewLocation = new Point(firstLocation);
                }
                if(targetRoom.exitMap.containsKey(direction)) {
                    TargetRoomData targetRoomData = getTargetRoomFromStartRoom(targetRoom, new ArrayList<>(Arrays.asList(direction)), true, true);
                    if(Arrays.asList("East", "West").contains(direction)) {
                        viewLocation.x += 1;
                    }
                    else if(Arrays.asList("North", "South").contains(direction)) {
                        viewLocation.y += 1;
                    }
                    else if(Arrays.asList("Up", "Down").contains(direction)) {
                        viewLocation.z += 1;
                    }

                    if(!examinedRoomList.contains(targetRoomData.targetRoom)
                    && targetRoomData.targetRoom.location.area != null) {
                        AreaAndRoomData targetAreaAndRoomData = examineRoomData(targetRoomData.targetRoom.location.area, targetRoomData.targetRoom, targetRange, direction, examinedAreaList, examinedRoomList, viewLocation);
                        examinedAreaList = targetAreaAndRoomData.areaList;
                        examinedRoomList = targetAreaAndRoomData.roomList;
                    }
                }
            }
        }

        return new AreaAndRoomData(examinedAreaList, examinedRoomList);
    }

    public static TargetRoomData getTargetRoomFromStartRoom(Room targetRoom, ArrayList<String> directionList, boolean ignoreDoors, boolean ignoreHiddenExits) {
        TargetRoomData targetRoomData = new TargetRoomData();

        int distance = 0;
        for(String direction : directionList) {

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
