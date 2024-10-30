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
        return examineRoomData(targetRoom.location.area, targetRoom, maxDistance, "", new ArrayList<Area>(), new ArrayList<Room>(), new Point(0, 0, 0), new Point(0, 0), new Room[7][7], ignoreDoors, ignoreHiddenExits);
    }

    public static AreaAndRoomData examineRoomData(Area targetArea, Room targetRoom, int maxDistance, String targetDirection, ArrayList<Area> examinedAreaList, ArrayList<Room> examinedRoomList, Point currentLocationPoint, Point coordinatePoint, Room[][] roomViewRoomList, boolean ignoreDoors, boolean ignoreHiddenExits) {
        // Helper Function For GetSurroundingAreaAndRoomData()

        if(!examinedAreaList.contains(targetArea)) {
            examinedAreaList.add(targetArea);
        }
        if(!examinedRoomList.contains(targetRoom)) {
            examinedRoomList.add(targetRoom);

            // RoomView Data //
            int roomViewX = 3 + (int) coordinatePoint.x;
            int roomViewY = 3 + (int) coordinatePoint.y;
            if(roomViewX >= 0 && roomViewX < roomViewRoomList.length
            && roomViewY >= 0 && roomViewY < roomViewRoomList[0].length
            && roomViewRoomList[roomViewX][roomViewY] == null) {
                roomViewRoomList[roomViewX][roomViewY] = targetRoom;
            }
        }

        if(currentLocationPoint.x + currentLocationPoint.y + currentLocationPoint.z < maxDistance) {
            Point firstLocationPoint = new Point(currentLocationPoint);
            Point firstCoordinatePoint = new Point(coordinatePoint);
            ArrayList<String> potentialDirectionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West", "Up", "Down"));
            if(!targetDirection.isEmpty() && potentialDirectionList.contains(Location.getOppositeDirection(targetDirection))) {
                potentialDirectionList.remove(Location.getOppositeDirection(targetDirection));
            }

            for(String direction : potentialDirectionList) {
                if(!direction.equals("North")) {
                    currentLocationPoint = new Point(firstLocationPoint);
                    coordinatePoint = new Point(firstCoordinatePoint);
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

                        if(direction.equals("North")) {
                            coordinatePoint.y += 1;
                        }
                        else if(direction.equals("East")) {
                            coordinatePoint.x += 1;
                        }
                        else if(direction.equals("South")) {
                            coordinatePoint.y -= 1;
                        }
                        else if(direction.equals("West")) {
                            coordinatePoint.x -= 1;
                        }

                        AreaAndRoomData targetAreaAndRoomData = examineRoomData(targetRoomData.targetRoom.location.area, targetRoomData.targetRoom, maxDistance, direction, examinedAreaList, examinedRoomList, currentLocationPoint, coordinatePoint, roomViewRoomList, ignoreDoors, ignoreHiddenExits);
                        examinedAreaList = targetAreaAndRoomData.areaList;
                        examinedRoomList = targetAreaAndRoomData.roomList;
                        roomViewRoomList = targetAreaAndRoomData.roomViewRoomList;
                    }
                }
            }
        }

        return new AreaAndRoomData(examinedAreaList, examinedRoomList, roomViewRoomList);
    }
}
