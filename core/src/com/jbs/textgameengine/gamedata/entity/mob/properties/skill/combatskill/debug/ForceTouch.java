package com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug;

import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;

import java.util.ArrayList;
import java.util.Arrays;

public class ForceTouch extends Skill {
    public ForceTouch() {
        super();

        nameKeyList = new ArrayList<>(Arrays.asList("force touch", "force touc", "force tou", "force to", "force t", "force", "forc", "for", "forcetouch", "forcetouc", "forcetou", "forceto", "forcet"));

        maxDistance = 1;
        isHealing = true;
    }
}
