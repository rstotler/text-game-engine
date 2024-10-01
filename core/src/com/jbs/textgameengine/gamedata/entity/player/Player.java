package com.jbs.textgameengine.gamedata.entity.player;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;

public class Player extends Mob {
    public int updateActionTimer;

    public Player(Location startLocation) {
        super(startLocation);
        isPlayer = true;

        updateActionTimer = 0;

        loadDebugPlayer();
    }

    public void loadDebugPlayer() {
        addItemToInventory(Item.load(1, location));
    }

    public void update() {
        if(currentAction != null) {
            updateActionTimer += 1;
            if(updateActionTimer >= 30) {
                updateActionTimer = 0;
                super.update();
            }
        }
    }
}
