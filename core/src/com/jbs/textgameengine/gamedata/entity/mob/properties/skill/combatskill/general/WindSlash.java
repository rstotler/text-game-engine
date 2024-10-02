package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class WindSlash extends Skill {
    public WindSlash() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("wind slash", "wind slas", "wind sla", "wind sl", "wind s", "wind", "win", "windslash", "windslas", "windsla", "windsl", "winds"));

        singleOnly = false;
    }
}
