package com.jbs.textgameengine.gamedata.entity;

import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class Entity {
    public int id;
    public Line name;
    public Room room;

    public Entity(Room startRoom) {
        id = -1;
        name = null;
        this.room = startRoom;
    }
}
