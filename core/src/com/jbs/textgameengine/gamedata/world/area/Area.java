package com.jbs.textgameengine.gamedata.world.area;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Area {
    public Line name;
    public Location location;

    public ArrayList<Room> roomList;

    public Area(Line name, Location location) {
        this.name = name;
        this.location = location;
        this.location.area = this;

        roomList = new ArrayList<>();
    }
}
