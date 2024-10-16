package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Fireball extends Skill {
    public Fireball() {
        super(new Line("Fireball", "8CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("fireball", "firebal", "fireba", "fireb", "fire", "fir"));

        maxDistance = 1;
    }
}
