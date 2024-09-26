package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Star extends Planetoid {
    public Star(Line name, Location location, int distanceFromCenter, int orbitDirection, int minutesInDay, int minutesInYear) {
        super(name, location, distanceFromCenter, orbitDirection, minutesInDay, minutesInYear);

        this.location.planetoid = this;
    }

    public void update() {
        super.update();
    }
}
