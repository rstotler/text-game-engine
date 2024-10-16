package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.basic;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Smash extends Skill {
    public Smash() {
        super(new Line("", "", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("smash", "smas", "sma", "sm"));
    }
}
