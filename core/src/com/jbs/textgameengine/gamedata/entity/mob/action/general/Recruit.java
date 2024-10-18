package com.jbs.textgameengine.gamedata.entity.mob.action.general;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;

import java.util.*;

public class Recruit extends Action {
    public Recruit(Mob parentEntity) {
        super(parentEntity);
    }

    public Recruit() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("recruit", "recrui", "recru", "recr", "rec", "re").contains(inputList.get(0))) {
            Recruit recruitAction = new Recruit(parentEntity);

            // Recruit/Tame/Group //
            // Recruit All Mob //
            // Recruit # Mob //
            // Recruit List //
            // Recruit All //
            // Recruit Mob //
            // Recruit //

            return recruitAction;
        }

        return null;
    }

    public void initiate() {
    }
}
