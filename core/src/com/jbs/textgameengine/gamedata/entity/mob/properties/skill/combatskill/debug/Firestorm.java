package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Firestorm extends Skill {
    public Firestorm() {
        super(new Line("Firestorm", "9CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("firestorm", "firestor", "firesto", "firest", "fires", "fire", "fir"));

        maxDistance = 1;
        singleOnly = false;
        allOnly = true;
    }
}
