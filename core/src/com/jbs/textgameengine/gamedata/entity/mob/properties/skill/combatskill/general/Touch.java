package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Touch extends Skill {
    public Touch() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("touch", "touc", "tou"));

        isHealing = true;
    }
}
