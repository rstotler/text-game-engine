package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

public class CombatSkill extends Skill {
    public int minRange;
    public int maxRange;
    public boolean allOnly;

    public CombatSkill() {
        super();

        minRange = 0;
        maxRange = 0;
        allOnly = false;
    }
}
