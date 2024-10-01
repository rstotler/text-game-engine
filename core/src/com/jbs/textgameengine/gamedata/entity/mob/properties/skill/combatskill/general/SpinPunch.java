package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.CombatSkill;

import java.util.ArrayList;
import java.util.Arrays;

public class SpinPunch extends CombatSkill {
    public SpinPunch() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("spin punch", "spin punc", "spin pun", "spin pu", "spin p", "spin", "spi", "spinpunch", "spinpunc", "spinpun", "spinpu", "spinp"));
    }
}
