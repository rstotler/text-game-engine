package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class FullLife extends Skill {
    public FullLife() {
        super(new Line("Full Life", "5CONT4CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("full life", "full lif", "full li", "full l", "full", "ful", "fulllife", "fulllif", "fullli", "fulll"));

        maxDistance = 1;
        singleOnly = false;
        isHealing = true;
    }
}
