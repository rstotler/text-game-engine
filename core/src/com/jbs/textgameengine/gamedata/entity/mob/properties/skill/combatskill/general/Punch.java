package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Punch extends Skill {
    public Punch() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("punch", "punc", "pun", "pu"));
    }
}
