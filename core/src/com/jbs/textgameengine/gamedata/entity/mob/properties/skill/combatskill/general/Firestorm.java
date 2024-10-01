package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.CombatSkill;

import java.util.ArrayList;
import java.util.Arrays;

public class Firestorm extends CombatSkill {
    public Firestorm() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("firestorm", "firestor", "firesto", "firest", "fires", "fire", "fir"));
    }
}
