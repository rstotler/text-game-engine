package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class WhiteWind extends Skill {
    public WhiteWind() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("white wind", "white win", "white wi", "white w", "white", "whit", "whi", "whitewind", "whitewin", "whitewi", "whitew"));

        maxDistance = 1;
        singleOnly = false;
        allOnly = true;
        isHealing = true;
    }
}
