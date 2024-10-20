package com.jbs.textgameengine.gamedata.entity.mob.action.item.shop;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.*;

public class List extends Action {
    public List(Mob parentEntity) {
        super(parentEntity);
    }

    public List() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("list", "lis", "li").contains(inputList.get(0))) {
            List listAction = new List(parentEntity);

            return listAction;
        }

        return null;
    }

    public void initiate() {
    }
}
