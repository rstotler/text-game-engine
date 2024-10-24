package com.jbs.textgameengine.gamedata.world.utility;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.utility.Point;

import java.util.*;

public class TargetRoomData {
    public static HashMap<String, ArrayList<String>> sideDirectionMap = loadSideDirectionMap();
    public static Point lowestPoint = new Point(0, 0);  // Use With Caution: Variable Updates Automatically
    public static Point highestPoint = new Point(0, 0); // Use With Caution: Variable Updates Automatically

    public Room targetRoom;
    public String targetDirection;
    public int distance;
    public String message;

    public Point coordinates;
    public Point farthestPoint;
    public ArrayList<String> directionList;
    public boolean breakCheck;

    public TargetRoomData() {
        targetRoom = null;
        targetDirection = "";
        distance = -1;
        message = "";

        coordinates = null;
        farthestPoint = null;
        directionList = new ArrayList<>();
        breakCheck = false;
    }

    public static ArrayList<TargetRoomData> examineAreaAndRoomData(Room currentRoom, int maxDistance, String targetDirection, ArrayList<TargetRoomData> examinedAreaAndRoomDataList, ArrayList<Room> examinedRoomList, Point currentLocationPoint) {
        // Helper Function For SurroundingAreaAndRoomData.GetSurroundingAreaAndRoomData()

        if(!examinedRoomList.contains(currentRoom)) {
            examinedRoomList.add(currentRoom);

            TargetRoomData targetRoomData = new TargetRoomData();
            targetRoomData.targetRoom = currentRoom;
            targetRoomData.coordinates = new Point(currentLocationPoint);
            examinedAreaAndRoomDataList.add(targetRoomData);

            if(currentLocationPoint.x < lowestPoint.x) {
                lowestPoint.x = currentLocationPoint.x;
            }
            else if(currentLocationPoint.x > highestPoint.x) {
                highestPoint.x = currentLocationPoint.x;
            }
            if(currentLocationPoint.y < lowestPoint.y) {
                lowestPoint.y = currentLocationPoint.y;
            }
            else if(currentLocationPoint.y > highestPoint.y) {
                highestPoint.y = currentLocationPoint.y;
            }
        }

        if(maxDistance == -1 || currentLocationPoint.x + currentLocationPoint.y < maxDistance) {
            ArrayList<String> potentialDirectionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West", "Northeast", "Southeast", "Southwest", "Northwest"));
            if(!targetDirection.isEmpty() && potentialDirectionList.contains(Location.getOppositeDirection(targetDirection))) {
                potentialDirectionList.remove(Location.getOppositeDirection(targetDirection));
            }
            Point lastLocationPoint = new Point(currentLocationPoint);

            for(String direction : potentialDirectionList) {
                if(!direction.equals("North")) {
                    currentLocationPoint = new Point(lastLocationPoint);
                }
                if(currentRoom.exitMap.containsKey(direction)
                && currentRoom.exitMap.get(direction) != null) {
                    TargetRoomData targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(currentRoom, new ArrayList<>(Arrays.asList(direction)), true, true);

                    if(!examinedRoomList.contains(targetRoomData.targetRoom)) {
                        if(Arrays.asList("East").contains(direction)) {
                            currentLocationPoint.x += 1;
                        }
                        else if(Arrays.asList("West").contains(direction)) {
                            currentLocationPoint.x -= 1;
                        }
                        else if(Arrays.asList("North").contains(direction)) {
                            currentLocationPoint.y += 1;
                        }
                        else if(Arrays.asList("South").contains(direction)) {
                            currentLocationPoint.y -= 1;
                        }
                        else if(Arrays.asList("Northeast").contains(direction)) {
                            currentLocationPoint.x += 1;
                            currentLocationPoint.y += 1;
                        }
                        else if(Arrays.asList("Southeast").contains(direction)) {
                            currentLocationPoint.x += 1;
                            currentLocationPoint.y -= 1;
                        }
                        else if(Arrays.asList("Southwest").contains(direction)) {
                            currentLocationPoint.x -= 1;
                            currentLocationPoint.y -= 1;
                        }
                        else if(Arrays.asList("Northwest").contains(direction)) {
                            currentLocationPoint.x -= 1;
                            currentLocationPoint.y += 1;
                        }

                        examinedAreaAndRoomDataList = examineAreaAndRoomData(targetRoomData.targetRoom, maxDistance, direction, examinedAreaAndRoomDataList, examinedRoomList, currentLocationPoint);
                    }
                }
            }
        }

        return examinedAreaAndRoomDataList;
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

        Room targetRoom = startRoom;
        for(String direction : Arrays.asList("North", "East", "South", "West", "Up", "Down")) {
            targetDirection = direction;

            String masterMessage = "";
            Room currentRoom = startRoom;

            for(int i = 0; i < maxDistance; i++) {
                message = masterMessage;
                if(!currentRoom.exitMap.containsKey(direction)
                || currentRoom.exitMap.get(direction) == null) {
                    break;
                }

                TargetRoomData targetRoomData = getTargetRoomFromStartRoom(currentRoom, new ArrayList<String>(Arrays.asList(direction)), true, true);
                currentRoom = targetRoomData.targetRoom;
                if(targetRoomData.message.equals("Door Is Closed")) {
                    masterMessage = targetRoomData.message;
                }

                if((targetEntity.isPlayer && targetEntity.location.room == currentRoom)
                || (targetEntity.isMob && currentRoom.mobList.contains(targetEntity))
                || (targetEntity.isItem && currentRoom.itemList.contains(targetEntity))
                || (targetEntity.isSpaceship && currentRoom.spaceshipList.contains(targetEntity))) {
                    targetRoom = currentRoom;
                    message = masterMessage;
                    targetFound = true;
                    targetRange = i + 1;
                    break;
                }

                for(String sideDirection : sideDirectionMap.get(direction)) {
                    message = masterMessage;
                    Room sideRoom = currentRoom;

                    for(int sideIndex = 0; sideIndex < maxDistance - (i + 1); sideIndex++) {
                        if(!sideRoom.exitMap.containsKey(sideDirection)
                        || sideRoom.exitMap.get(sideDirection) == null) {
                            break;
                        }

                        TargetRoomData targetSideRoomData = getTargetRoomFromStartRoom(sideRoom, new ArrayList<String>(Arrays.asList(sideDirection)), true, true);
                        sideRoom = targetSideRoomData.targetRoom;

                        if(targetSideRoomData.message.equals("Door Is Closed")) {
                            message = targetSideRoomData.message;
                        }

                        if((targetEntity.isPlayer && targetEntity.location.room == sideRoom)
                        || (targetEntity.isMob && sideRoom.mobList.contains(targetEntity))
                        || (targetEntity.isItem && sideRoom.itemList.contains(targetEntity))
                        || (targetEntity.isSpaceship && sideRoom.spaceshipList.contains(targetEntity))) {
                            targetRoom = currentRoom;
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
            targetRoomData.targetRoom = targetRoom;
            targetRoomData.distance = targetRange;
            targetRoomData.targetDirection = targetDirection;
            targetRoomData.message = message;
            return targetRoomData;
        }
    }

    public static TargetRoomData findTargetRoomFromStartRoom(Room startRoom, Room targetRoom, int maxDistance) {
        return examineRoomData(startRoom, targetRoom, maxDistance, "", new ArrayList<String>(), new ArrayList<Room>(), new Point(0, 0, 0), "");
    }

    public static TargetRoomData examineRoomData(Room currentRoom, Room targetRoom, int maxDistance, String targetDirection, ArrayList<String> directionList, ArrayList<Room> examinedRoomList, Point currentLocationPoint, String masterMessage) {
        // Helper Function For FindTargetRoomFromStartRoom()

        TargetRoomData targetRoomData = new TargetRoomData();
        targetRoomData.targetRoom = currentRoom;

        if(!examinedRoomList.contains(currentRoom)) {
            examinedRoomList.add(currentRoom);
        }

        String masterMessageCopy = masterMessage;
        ArrayList<String> lastDirectionList = new ArrayList<>(directionList);

        if(currentLocationPoint.x + currentLocationPoint.y + currentLocationPoint.z < maxDistance) {
            Point lastLocationPoint = new Point(currentLocationPoint);
            ArrayList<String> potentialDirectionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West", "Up", "Down"));
            if(!targetDirection.isEmpty() && potentialDirectionList.contains(Location.getOppositeDirection(targetDirection))) {
                potentialDirectionList.remove(Location.getOppositeDirection(targetDirection));
            }

            for(String direction : potentialDirectionList) {
                if(!direction.equals("North")) {
                    currentLocationPoint = new Point(lastLocationPoint);
                    masterMessage = masterMessageCopy;
                    directionList = new ArrayList<>(lastDirectionList);
                }
                if(currentRoom.exitMap.containsKey(direction)) {
                    TargetRoomData targetRoomSubData = TargetRoomData.getTargetRoomFromStartRoom(currentRoom, new ArrayList<>(Arrays.asList(direction)), false, false);

                    if(!targetRoomSubData.message.isEmpty()
                    && masterMessage.isEmpty()) {
                        masterMessage = targetRoomSubData.message;
                    }
                    if(!examinedRoomList.contains(targetRoomSubData.targetRoom)) {
                        targetRoomData.targetRoom = targetRoomSubData.targetRoom;
                        targetRoomData.targetDirection = direction;

                        directionList.add(direction);
                        targetRoomData.directionList = directionList;

                        if(Arrays.asList("East", "West").contains(direction)) {
                            currentLocationPoint.x += 1;
                        }
                        else if(Arrays.asList("North", "South").contains(direction)) {
                            currentLocationPoint.y += 1;
                        }
                        else if(Arrays.asList("Up", "Down").contains(direction)) {
                            currentLocationPoint.z += 1;
                        }

                        targetRoomData.distance = (int) currentLocationPoint.x + (int) currentLocationPoint.y + (int) currentLocationPoint.z;

                        if(targetRoomData.targetRoom == targetRoom) {
                            targetRoomData.breakCheck = true;
                            return targetRoomData;
                        }
                        else {
                            ArrayList<String> directionListCopy = new ArrayList<>(directionList);
                            targetRoomData = examineRoomData(targetRoomData.targetRoom, targetRoom, maxDistance, direction, directionListCopy, examinedRoomList, currentLocationPoint, masterMessage);
                            if(targetRoomData.breakCheck) {
                                return targetRoomData;
                            }
                        }
                    }
                }
            }
        }

        return targetRoomData;
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

    public static void normalizeRoomCoordinates(ArrayList<TargetRoomData> targetRoomDataList, Point coordinateOffset) {
        for(TargetRoomData targetRoomData : targetRoomDataList) {
            targetRoomData.targetRoom.coordinates.x = targetRoomData.coordinates.x - coordinateOffset.x;
            targetRoomData.targetRoom.coordinates.y = targetRoomData.coordinates.y - coordinateOffset.y;
        }
    }
}
