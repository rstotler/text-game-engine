package com.jbs.textgameengine.gamedata.world.area;

import com.badlogic.gdx.graphics.Color;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Random;

public class Area {
    public Line name;
    public Location location;

    public String mapKey;
    public Color mapColor;

    public ArrayList<Room> roomList;

    public Area(Line name, Location location) {
        this.name = name;
        this.location = location;
        this.location.area = this;

        mapKey = "";
        mapColor = new Color(new Random().nextFloat() * .8f,
                             new Random().nextFloat() * .8f,
                             new Random().nextFloat() * .8f,
                          1);

        roomList = new ArrayList<>();
    }

    public void update() {
    }
}
