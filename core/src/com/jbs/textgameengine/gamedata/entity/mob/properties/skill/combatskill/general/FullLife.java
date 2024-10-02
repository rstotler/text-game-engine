package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class FullLife extends Skill {
    public FullLife() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("full life", "full lif", "full li", "full l", "full", "ful", "fulllife", "fulllif", "fullli", "fulll"));

        maxDistance = 1;
        singleOnly = false;
        isHealing = true;
    }
}
