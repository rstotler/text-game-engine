package com.jbs.textgameengine.gamedata.entity;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Entity {
    public int id;
    public Location location;
    public Line name;
    public ArrayList<String> nameKeyList;

    public Entity(Location startLocation) {
        id = -1;
        this.location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, startLocation.area, startLocation.room, startLocation.spaceship);
        name = null;
        nameKeyList = null;
    }

    public static ArrayList<String> createNameKeyList(String targetString) {
        List<String> targetStringList = Arrays.asList(targetString.split(" "));

        ArrayList<String> nameKeyList = new ArrayList<>();
        for(int i = 0; i < targetStringList.size(); i++) {
            List<String> splitString = targetStringList.subList(0, i + 1);
            String substring = splitString.stream().collect(Collectors.joining(" "));

            System.out.println(substring);
        }

        return nameKeyList;
    }
}
