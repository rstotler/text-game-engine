package com.jbs.textgameengine.gamedata.entity.mob.action;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.Attack;
import com.jbs.textgameengine.gamedata.entity.mob.action.other.*;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.*;
import com.jbs.textgameengine.gamedata.entity.mob.action.spaceship.*;
import com.jbs.textgameengine.gamedata.entity.mob.combat.combataction.CombatAction;

import java.util.ArrayList;

public class Action {
    public static ArrayList<Action> actionList = loadActionList();

    public String actionType;
    public ArrayList<String> nameKeyList;

    public String targetEntityString;
    public String targetDirection;
    public int targetCount;
    public String targetContainerString;

    public boolean allCheck;
    public boolean groupCheck;
    public boolean selfCheck;

    public int maxRange;

    public Action() {
        actionType = "";
        nameKeyList = new ArrayList<>();

        targetEntityString = "";
        targetDirection = "";
        targetCount = -1;
        targetContainerString = "";

        allCheck = false;
        groupCheck = false;
        selfCheck = false;

        maxRange = 0;
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

        actionList.add(new CombatAction());
        actionList.add(new Attack());

        actionList.add(new Time());
        actionList.add(new Emote());

        return actionList;
    }
}
