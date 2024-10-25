package com.jbs.textgameengine.gamedata.entity.player;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Player extends Mob {
    public int updateTimer;

    public Player(Location startLocation) {
        super(-1, startLocation);
        isPlayer = true;
        name = new Line("Player Entity", "7CONT6CONT");
        nameKeyList = new ArrayList<>(Arrays.asList("player"));

        updateTimer = 0;
    }

    public void update() {
        updateTimer += 1;
        if(updateTimer >= 30) {
            updateTimer = 0;
            super.update();
        }
    }
}
