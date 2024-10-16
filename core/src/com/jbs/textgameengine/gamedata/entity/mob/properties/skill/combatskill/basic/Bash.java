package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.basic;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Bash extends Skill {
    public Bash() {
        super(new Line("Bash", "4CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("bash", "bas", "ba"));
    }
}
