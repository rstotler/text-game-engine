package com.jbs.textgameengine.gamedata.entity.mob.combat.combataction.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.combat.combataction.CombatAction;

import java.util.ArrayList;
import java.util.Arrays;

public class Punch extends CombatAction {
    public Punch() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("punch", "punc", "pun", "pu"));
    }

    public Punch getNewObject() {
        return new Punch();
    }

    public void initiate(Mob parentEntity) {
        if(actionType.equals("CombatAction All/Group Direction #")) {

        }

        else if(actionType.equals("CombatAction Entity Direction #")) {

        }

        else if(actionType.equals("CombatAction All/Group Direction")) {

        }

        else if(actionType.equals("CombatAction Entity Direction")) {

        }

        else if(actionType.equals("CombatAction Direction #")) {

        }

        else if(actionType.equals("CombatAction All/Group")) {

        }

        else if(actionType.equals("CombatAction Direction")) {

        }

        else if(actionType.equals("CombatAction Entity/Self")) {

        }

        else if(actionType.equals("CombatAction")) {

        }
    }
}
