package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.basic;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Dodge extends Skill {
    public Dodge() {
        super(new Line("Dodge", "5CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("dodge", "dodg", "dod", "do"));
    }
}
