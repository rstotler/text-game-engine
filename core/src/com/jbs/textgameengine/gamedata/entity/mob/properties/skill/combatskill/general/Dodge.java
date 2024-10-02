package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Dodge extends Skill {
    public Dodge() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("dodge", "dodg", "dod", "do"));
    }
}
