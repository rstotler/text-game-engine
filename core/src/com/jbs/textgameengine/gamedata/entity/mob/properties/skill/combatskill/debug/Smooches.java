package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Smooches extends Skill {
    public Smooches() {
        super(new Line("Smooches", "8CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("smooches", "smooche", "smooch", "smooc", "smoo", "smo"));

        singleOnly = false;
        allOnly = true;
        isHealing = true;
    }
}
