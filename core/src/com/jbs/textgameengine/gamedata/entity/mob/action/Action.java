package com.jbs.textgameengine.gamedata.entity.mob.action;

import com.jbs.textgameengine.gamedata.entity.mob.action.other.Emote;
import com.jbs.textgameengine.gamedata.entity.mob.action.spaceship.Board;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.Look;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.Move;
import com.jbs.textgameengine.gamedata.entity.mob.action.spaceship.Launch;

import java.util.ArrayList;

public class Action {
    public static ArrayList<Action> actionList = loadActionList();

    public String actionType;

    public String targetEntityString;
    public String targetDirection;
    public int directionCount;
    public String targetContainerString;

    public Action() {
        actionType = "";

        targetEntityString = "";
        targetDirection = "";
        directionCount = -1;
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
        actionList.add(new Emote());

        return actionList;
    }
}
