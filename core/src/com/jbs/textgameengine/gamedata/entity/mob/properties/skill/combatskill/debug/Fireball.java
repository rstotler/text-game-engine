package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class Fireball extends Skill {
    public Fireball() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("fireball", "firebal", "fireba", "fireb", "fire", "fir"));

        maxDistance = 1;
    }
}
