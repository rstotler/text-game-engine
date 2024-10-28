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
        mapColor = new Color(0, 0, 0, 1);
        int randomIndex = new Random().nextInt(3);
        if(randomIndex == 0) {mapColor.r += new Random().nextFloat() * .5f;}
        else if(randomIndex == 1) {mapColor.g += new Random().nextFloat() * .5f;}
        else {mapColor.b += new Random().nextFloat() * .5f;}

        roomList = new ArrayList<>();
    }

    public void update() {
    }
}
