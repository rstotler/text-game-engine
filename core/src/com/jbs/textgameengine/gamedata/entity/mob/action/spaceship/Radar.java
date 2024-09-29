package com.jbs.textgameengine.gamedata.entity.mob.action.spaceship;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.ArrayList;
import java.util.Arrays;

public class Radar extends Action {
    public Radar() {
        super();
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("radar", "rada", "rad").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Radar();
        }

        return null;
    }

    public void initiate(Mob parentEntity) {
        if(parentEntity.isPlayer) {

        }
    }
}
