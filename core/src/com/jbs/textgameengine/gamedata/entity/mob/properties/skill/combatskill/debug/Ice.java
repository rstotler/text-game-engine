package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Ice extends Skill {
    public Ice() {
        super(new Line("Ice", "3CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("ice"));

        maxDistance = 2;
        singleOnly = false;
    }
}
