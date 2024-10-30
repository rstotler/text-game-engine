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

    public String getTargetDirectionFromKey(String key) {
        int facingIndex = 0;
        ArrayList<String> directionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West"));

        if(directionList.contains(facingDirection)) {
            facingIndex = Arrays.asList("North", "East", "South", "West").indexOf(facingDirection);
        }

        String targetDirection = "";

        if(key.equals("Right")) {facingIndex += 1;}
        else if(key.equals("Down")) {facingIndex += 2;}
        else if(key.equals("Left")) {facingIndex += 3;}

        if(facingIndex >= directionList.size()) {
            facingIndex -= directionList.size();
        }

        targetDirection = directionList.get(facingIndex);

        return targetDirection;
    }
}
