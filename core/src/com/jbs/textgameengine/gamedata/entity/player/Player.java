package com.jbs.textgameengine.gamedata.entity.player;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;

public class Player extends Mob {
    public Player(Location location) {
        super(location);

        isPlayer = true;
    }
}
