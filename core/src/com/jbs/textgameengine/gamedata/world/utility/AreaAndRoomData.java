package com.jbs.textgameengine.gamedata.world.utility;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.utility.Point;

import java.util.*;

public class AreaAndRoomData {
    public ArrayList<Area> areaList;
    public ArrayList<Room> roomList;
    public Room[][] roomViewRoomList; // Coordinates Offset By Center Location (Center Becomes Bottom-Left Point)

    public AreaAndRoomData(ArrayList<Area> areaList, ArrayList<Room> roomList, Room[][] roomViewRoomList) {
        this.areaList = areaList;
        this.roomList = roomList;
        this.roomViewRoomList = roomViewRoomList;
    }

    public static AreaAndRoomData getSurroundingAreaAndRoomData(Room targetRoom, int maxDistance, boolean ignoreDoors, boolean ignoreHiddenExits) {
        return AreaAndRoomData.examineRoomData(targetRoom.location.area, targetRoom, maxDistance, "", new ArrayList<Area>(), new ArrayList<Room>(), new Point(0, 0, 0), ignoreDoors, ignoreHiddenExits);
    }

    public static AreaAndRoomData examineRoomData(Area targetArea, Room targetRoom, int maxDistance, String targetDirection, ArrayList<Area> examinedAreaList, ArrayList<Room> examinedRoomList, Point currentLocationPoint, boolean ignoreDoors, boolean ignoreHiddenExits) {
        // Helper Function For GetSurroundingAreaAndRoomData()

        if(!examinedAreaList.contains(targetArea)) {
            examinedAreaList.add(targetArea);
        }
        if(!examinedRoomList.contains(targetRoom)) {
            examinedRoomList.add(targetRoom);
        }

        if(currentLocationPoint.x + currentLocationPoint.y + currentLocationPoint.z < maxDistance) {
            Point firstLocationPoint = new Point(currentLocationPoint);
            ArrayList<String> potentialDirectionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West", "Up", "Down"));
            if(!targetDirection.isEmpty() && potentialDirectionList.contains(Location.getOppositeDirection(targetDirection))) {
                potentialDirectionList.remove(Location.getOppositeDirection(targetDirection));
            }

            for(String direction : potentialDirectionList) {
                if(!direction.equals("North")) {
                    currentLocationPoint = new Point(firstLocationPoint);
                }
                if(targetRoom.exitMap.containsKey(direction)) {
                    TargetRoomData targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(targetRoom, new ArrayList<>(Arrays.asList(direction)), ignoreDoors, ignoreHiddenExits);

                    if(!examinedRoomList.contains(targetRoomData.targetRoom)
                    && targetRoomData.targetRoom.location.area != null
                    && !(!ignoreDoors && targetRoomData.message.equals("Door Is Closed"))
                    && !(!ignoreHiddenExits && targetRoomData.message.equals("Hidden Exit Is Closed"))) {
                        if(Arrays.asList("East", "West").contains(direction)) {
                            currentLocationPoint.x += 1;
                        }
                        else if(Arrays.asList("North", "South").contains(direction)) {
                            currentLocationPoint.y += 1;
                        }
                        else if(Arrays.asList("Up", "Down").contains(direction)) {
                            currentLocationPoint.z += 1;
                        }

                        AreaAndRoomData targetAreaAndRoomData = examineRoomData(targetRoomData.targetRoom.location.area, targetRoomData.targetRoom, maxDistance, direction, examinedAreaList, examinedRoomList, currentLocationPoint, ignoreDoors, ignoreHiddenExits);
                        examinedAreaList = targetAreaAndRoomData.areaList;
                        examinedRoomList = targetAreaAndRoomData.roomList;
                    }
                }
            }
        }

        return new AreaAndRoomData(examinedAreaList, examinedRoomList, null);
    }
}
