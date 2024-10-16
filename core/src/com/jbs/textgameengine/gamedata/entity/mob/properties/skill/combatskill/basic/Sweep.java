package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.basic;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Sweep extends Skill {
    public Sweep() {
        super(new Line("Sweep", "5CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("sweep", "swee", "swe"));
    }
}
