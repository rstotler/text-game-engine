package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class Block extends Skill {
    public Block() {
        super(new Line("Block", "5CONT", "", true, true));

        nameKeyList = new ArrayList<>(Arrays.asList("block", "bloc", "blo", "bl"));
    }
}
