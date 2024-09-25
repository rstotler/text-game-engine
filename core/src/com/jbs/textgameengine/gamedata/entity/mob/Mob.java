package com.jbs.textgameengine.gamedata.entity.mob;

import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.room.Room;

import java.util.ArrayList;

public class Mob {
    public Room room;
    public boolean isPlayer;

    public ArrayList<Mob> targetList;
    public Action currentAction;

    public Mob(Room startRoom) {
        room = startRoom;
        isPlayer = false;

        targetList = new ArrayList<>();
        currentAction = null;
    }
}
