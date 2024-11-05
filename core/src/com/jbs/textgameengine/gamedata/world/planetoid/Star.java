package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Star extends Planetoid {
    public float brightness;

    public Star(Line name, Location location, int distanceFromCenter, int orbitDirection, float axialTilt, int minutesInDay, int minutesInYear) {
        super(name, location, distanceFromCenter, orbitDirection, axialTilt, minutesInDay, minutesInYear);

        this.location.planetoid = this;

        brightness = 1.0f;
    }

    public void update() {
        super.update();
    }
}
