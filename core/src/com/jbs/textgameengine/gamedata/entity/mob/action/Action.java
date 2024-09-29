package com.jbs.textgameengine.gamedata.entity.mob.action;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.Attack;
import com.jbs.textgameengine.gamedata.entity.mob.action.other.*;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.*;
import com.jbs.textgameengine.gamedata.entity.mob.action.spaceship.*;

import java.util.ArrayList;

public class Action {
    public static ArrayList<Action> actionList = loadActionList();

    public String actionType;

    public String targetEntityString;
    public String targetDirection;
    public int targetCount;
    public String targetContainerString;

    public Action() {
        actionType = "";

        targetEntityString = "";
        targetDirection = "";
        targetCount = -1;
        targetContainerString = "";
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        return null;
    }

    public void initiate(Mob parentEntity) {}

    public static ArrayList<Action> loadActionList() {
        ArrayList<Action> actionList = new ArrayList<>();

        actionList.add(new Look());
        actionList.add(new Move());

        actionList.add(new Board());
        actionList.add(new Launch());
        actionList.add(new Radar());
        actionList.add(new Land());

        actionList.add(new Attack());

        actionList.add(new Time());
        actionList.add(new Emote());

        return actionList;
    }
}
