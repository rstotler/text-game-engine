package com.jbs.textgameengine.gamedata.world;

public class Location {
    public String galaxy;
    public String solarSystem;
    public int planetoidIndex;
    public String area;
    public int roomIndex;

    public Location(String galaxy, String solarSystem, int planetoidIndex, String area, int roomIndex) {
        this.galaxy = galaxy;
        this.solarSystem = solarSystem;
        this.planetoidIndex = planetoidIndex;
        this.area = area;
        this.roomIndex = roomIndex;
    }

    public Location(String galaxy, String solarSystem, int planetoidIndex, String area) {
        this(galaxy, solarSystem, planetoidIndex, area, -1);
    }

    public Location(String galaxy, String solarSystem, int planetoidIndex) {
        this(galaxy, solarSystem, planetoidIndex, "", -1);
    }

    public Location(String galaxy, String solarSystem) {
        this(galaxy, solarSystem, -1, "", -1);
    }

    public Location(String galaxy) {
        this(galaxy, "", -1, "", -1);
    }
}
