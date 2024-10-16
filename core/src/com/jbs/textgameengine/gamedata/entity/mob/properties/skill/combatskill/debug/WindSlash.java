package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class WindSlash extends Skill {
    public WindSlash() {
        super(new Line("Wind Slash", "5CONT5CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("wind slash", "wind slas", "wind sla", "wind sl", "wind s", "wind", "win", "windslash", "windslas", "windsla", "windsl", "winds"));

        singleOnly = false;
    }
}
