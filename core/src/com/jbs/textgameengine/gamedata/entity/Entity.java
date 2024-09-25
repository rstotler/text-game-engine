package com.jbs.textgameengine.gamedata.entity;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;

public class Entity {
    public Location location;

    public Entity(Location location) {
        this.location = location;
    }

    public Room getRoom() {
        if(GameScreen.galaxyList.containsKey(location.galaxy)
        && GameScreen.galaxyList.get(location.galaxy).solarSystemMap.containsKey(location.solarSystem)
        && location.planetoidIndex != -1 && location.planetoidIndex < GameScreen.galaxyList.get(location.galaxy).solarSystemMap.get(location.solarSystem).planetoidList.size()
        && GameScreen.galaxyList.get(location.galaxy).solarSystemMap.get(location.solarSystem).planetoidList.get(location.planetoidIndex).isPlanet
        && ((Planet) GameScreen.galaxyList.get(location.galaxy).solarSystemMap.get(location.solarSystem).planetoidList.get(location.planetoidIndex)).areaMap.containsKey(location.area)
        && location.roomIndex != -1 && location.roomIndex < ((Planet) GameScreen.galaxyList.get(location.galaxy).solarSystemMap.get(location.solarSystem).planetoidList.get(location.planetoidIndex)).areaMap.get(location.area).roomList.size()) {
            return ((Planet) GameScreen.galaxyList.get(location.galaxy).solarSystemMap.get(location.solarSystem).planetoidList.get(location.planetoidIndex)).areaMap.get(location.area).roomList.get(location.roomIndex);
        }

        return null;
    }
}
