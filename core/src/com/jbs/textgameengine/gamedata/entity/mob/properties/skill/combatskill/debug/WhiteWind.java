package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class WhiteWind extends Skill {
    public WhiteWind() {
        super(new Line("White Wind", "6CONT4CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("white wind", "white win", "white wi", "white w", "white", "whit", "whi", "whitewind", "whitewin", "whitewi", "whitew"));

        maxDistance = 1;
        singleOnly = false;
        allOnly = true;
        isHealing = true;
    }
}
