package com.jbs.textgameengine.gamedata.entity.mob;

import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.gear.Gear;
import com.jbs.textgameengine.gamedata.entity.mob.inventory.Inventory;
import com.jbs.textgameengine.gamedata.world.room.Room;

import java.util.ArrayList;

public class Mob {
    public boolean isPlayer;
    public Room room;

    public Inventory inventory;
    public Gear gear;

    public ArrayList<Mob> targetList;
    public Action currentAction;

    public Mob(Room startRoom) {
        isPlayer = false;
        room = startRoom;

        inventory = new Inventory();
        gear = new Gear();

        targetList = new ArrayList<>();
        currentAction = null;
    }

    public boolean hasKey(int keyNum) {
        return false;
    }
}
