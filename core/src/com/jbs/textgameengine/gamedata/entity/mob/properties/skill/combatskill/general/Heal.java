package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.CombatSkill;

import java.util.ArrayList;
import java.util.Arrays;

public class Heal extends CombatSkill {
    public Heal() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("heal", "hea", "he"));
    }
}
