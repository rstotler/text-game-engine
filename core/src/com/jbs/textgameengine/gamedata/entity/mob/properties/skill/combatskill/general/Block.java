package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Block extends Skill {
    public Block() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("block", "bloc", "blo", "bl"));
    }
}
