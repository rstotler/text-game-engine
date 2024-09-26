package com.jbs.textgameengine.gamedata.entity.player;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.room.Room;

public class Player extends Mob {
    public Player(Room startRoom) {
        super(startRoom);

        isPlayer = true;

        loadDebugPlayer();
    }

    public void loadDebugPlayer() {
        addItemToInventory(Item.load(1, room));
    }
}
