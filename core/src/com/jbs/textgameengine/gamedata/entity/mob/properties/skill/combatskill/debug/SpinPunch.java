package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class SpinPunch extends Skill {
    public SpinPunch() {
        super(new Line("Spin Punch", "5CONT5CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("spin punch", "spin punc", "spin pun", "spin pu", "spin p", "spin", "spi", "spinpunch", "spinpunc", "spinpun", "spinpu", "spinp"));

        singleOnly = false;
        allOnly = true;
    }
}
