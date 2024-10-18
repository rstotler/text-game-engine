package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.*;

public class Search extends Action {
    public Search(Mob parentEntity) {
        super(parentEntity);
    }

    public Search() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("search", "searc", "sear", "sea").contains(inputList.get(0))) {
            Search searchAction = new Search(parentEntity);

            return searchAction;
        }

        return null;
    }

    public void initiate() {
    }
}
