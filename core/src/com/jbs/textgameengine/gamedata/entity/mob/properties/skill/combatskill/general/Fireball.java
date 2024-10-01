package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.CombatSkill;

import java.util.ArrayList;
import java.util.Arrays;

public class Fireball extends CombatSkill {
    public Fireball() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("fireball", "firebal", "fireba", "fireb", "fire", "fir"));
    }
}
