package com.jbs.textgameengine.gamedata.entity.mob;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;

import java.util.ArrayList;

public class Mob extends Entity {
    public boolean isPlayer;

    public ArrayList<Mob> targetList;
    public Action currentAction;

    public Mob(Location location) {
        super(location);

        isPlayer = false;

        targetList = new ArrayList<>();
        currentAction = null;
    }
}
