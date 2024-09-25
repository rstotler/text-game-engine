package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Star extends Planetoid {
    public Line name;
    public Location location;

    public Star(Line name, Location location) {
        super();

        this.name = name;
        this.location = location;
    }
}
