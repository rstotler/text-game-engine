package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Heal extends Skill {
    public Heal() {
        super(new Line("Heal", "4CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("heal", "hea", "he"));

        singleOnly = false;
        isHealing = true;
    }
}
