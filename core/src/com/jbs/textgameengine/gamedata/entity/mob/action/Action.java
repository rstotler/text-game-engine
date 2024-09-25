package com.jbs.textgameengine.gamedata.entity.mob.action;

import com.jbs.textgameengine.gamedata.entity.mob.action.general.Look;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.Move;

import java.util.ArrayList;
import java.util.Arrays;

public class Action {
    public static ArrayList<Action> actionList = loadActionList();

    public String actionType;
    public boolean interruptAction;

    public String targetEntityString;
    public String targetDirection;
    public int directionCount;
    public String targetContainerString;

    public Action() {
        actionType = "";
        interruptAction = false;

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

        return actionList;
    }
}
