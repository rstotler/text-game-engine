package com.jbs.textgameengine.gamedata.entity.mob;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.gear.Gear;
import com.jbs.textgameengine.gamedata.entity.mob.inventory.Inventory;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Mob extends Entity {
    public boolean isPlayer;

    public Inventory inventory;
    public Gear gear;

    public ArrayList<Mob> targetList;
    public Action currentAction;

    public Mob(Room startRoom) {
        super(startRoom);

        isPlayer = false;

        inventory = new Inventory();
        gear = new Gear();

        targetList = new ArrayList<>();
        currentAction = null;
    }

    public static Mob load(int id, Room targetRoom) {
        Mob mob = new Mob(targetRoom);

        // 1 - Greeter Droid //
        if(id == 1) {
            mob.name = new Line("A Greeter Droid", "2CONT8CONT5CONT", "", true, true);
        }

        return mob;
    }

    public boolean hasKey(int keyNum) {
        return false;
    }
}
