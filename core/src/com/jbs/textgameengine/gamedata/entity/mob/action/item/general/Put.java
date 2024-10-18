package com.jbs.textgameengine.gamedata.entity.mob.action.item.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.*;

public class Put extends Action {
    public Put(Mob parentEntity) {
        super(parentEntity);
    }

    public Put() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("put", "pu", "p").contains(inputList.get(0))) {
            Put putAction = new Put(parentEntity);

            // Put All Item In Container //
            // Put All In Container //
            // Put # Item In Container //
            // Put Item In Container //

            return putAction;
        }

        return null;
    }

    public void initiate() {
    }
}
