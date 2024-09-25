package com.jbs.textgameengine.gamedata.world.room;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Room {
    public Line name;
    public Location location;

    public Room(Line name, Location location) {
        this.name = name;
        this.location = location;
    }

    public void display() {

    }
}
