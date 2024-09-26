package com.jbs.textgameengine.gamedata.entity.spaceship;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.HashMap;

public class Spaceship extends Entity {
    public HashMap<String, Area> areaMap;

    public Spaceship(Room startRoom) {
        super(startRoom);

        areaMap = new HashMap<>();
    }

    public static Spaceship load(int id, Room targetRoom) {
        Spaceship spaceship = new Spaceship(targetRoom);

        if(id == 1) {
            spaceship.name = new Line("A Spaceship", "2W9CONT", "", true, true);
        }

        return spaceship;
    }
}
