package com.jbs.textgameengine.gamedata.entity.mob.action;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.Attack;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.CombatAction;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.Target;
import com.jbs.textgameengine.gamedata.entity.mob.action.god.Manifest;
import com.jbs.textgameengine.gamedata.entity.mob.action.menu.Equipment;
import com.jbs.textgameengine.gamedata.entity.mob.action.menu.Group;
import com.jbs.textgameengine.gamedata.entity.mob.action.menu.Inventory;
import com.jbs.textgameengine.gamedata.entity.mob.action.other.*;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.*;
import com.jbs.textgameengine.gamedata.entity.mob.action.spaceship.*;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;

public class Action {
    public static ArrayList<Action> actionList = loadActionList();

    public String actionType;

    public String targetEntityString;
    public String targetDirection;
    public int targetCount;
    public int targetDirectionCount;

    public boolean allCheck;
    public boolean groupCheck;
    public boolean selfCheck;

    public Mob parentEntity;
    public Skill skill;

    public Action(Mob parentEntity, Skill skill) {
        actionType = "";

        targetEntityString = "";
        targetDirection = "";
        targetCount = -1;
        targetDirectionCount = -1;

        allCheck = false;
        groupCheck = false;
        selfCheck = false;

        this.parentEntity = parentEntity;
        this.skill = skill;
    }

    public Action(Mob parentEntity) {
        this(parentEntity, null);
    }

    public Action() {
        this(null, null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        return null;
    }

    public void initiate() {}

    public void performAction() {}

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }

    public static ArrayList<Action> loadActionList() {
        ArrayList<Action> actionList = new ArrayList<>();

        actionList.add(new Look());
        actionList.add(new Move());
        actionList.add(new OCLU());
        actionList.add(new Get());
        actionList.add(new Drop());
        actionList.add(new Remove());
        actionList.add(new Disband());

        actionList.add(new Inventory());
        actionList.add(new Equipment());
        actionList.add(new Group());

        actionList.add(new Board());
        actionList.add(new Launch());
        actionList.add(new Radar());
        actionList.add(new Land());

        actionList.add(new Target());
        actionList.add(new CombatAction());
        actionList.add(new Attack());

        actionList.add(new Time());
        actionList.add(new Emote());
        actionList.add(new Manifest());

        return actionList;
    }
}
