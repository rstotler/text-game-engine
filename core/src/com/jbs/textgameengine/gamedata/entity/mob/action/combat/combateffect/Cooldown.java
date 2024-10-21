package com.jbs.textgameengine.gamedata.entity.mob.action.combat.combateffect;

import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

public class Cooldown extends Action {
    public Cooldown(int cooldownTime) {
        performActionTimer = cooldownTime;
    }
}
