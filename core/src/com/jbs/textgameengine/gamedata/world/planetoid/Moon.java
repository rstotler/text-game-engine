package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Moon extends Planetoid {
    public Moon(Line name, Location location, int distanceFromCenter, int orbitDirection, float axialTilt, int minutesInDay, int minutesInYear) {
        super(name, location, distanceFromCenter, orbitDirection, axialTilt, minutesInDay, minutesInYear);
    }

    public void update() {
        super.update();
    }
}
