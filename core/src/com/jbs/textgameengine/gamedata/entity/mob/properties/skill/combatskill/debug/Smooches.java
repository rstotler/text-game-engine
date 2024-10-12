package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Smooches extends Skill {
    public Smooches() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("smooches", "smooche", "smooch", "smooc", "smoo", "smo"));

        singleOnly = false;
        allOnly = true;
        isHealing = true;
    }
}
