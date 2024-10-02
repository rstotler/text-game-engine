package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Ice extends Skill {
    public Ice() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("ice"));

        maxDistance = 2;
        singleOnly = false;
    }
}
