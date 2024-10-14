package com.jbs.textgameengine.gamedata.entity.mob.action.other;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.ArrayList;
import java.util.Arrays;

public class Weather extends Action {
    public Weather(Mob parentEntity) {
        super(parentEntity);
    }

    public Weather() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("weather", "weathe", "weath", "weat").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Weather(parentEntity);
        }

        return null;
    }

    public void initiate() {
    }
}
