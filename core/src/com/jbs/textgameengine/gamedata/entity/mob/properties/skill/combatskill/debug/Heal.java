package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Heal extends Skill {
    public Heal() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("heal", "hea", "he"));

        singleOnly = false;
        isHealing = true;
    }
}
