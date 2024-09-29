package com.jbs.textgameengine.gamedata.world.utility;

import com.jbs.textgameengine.gamedata.world.room.Room;

public class TargetRoomData {
    public Room targetRoom;
    public int distance;
    public String message;

    public TargetRoomData() {
        targetRoom = null;
        distance = -1;
        message = "";
    }
}
