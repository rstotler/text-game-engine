package com.jbs.textgameengine.gamedata.world.galaxy;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.planetoid.Star;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.solarsystem.SolarSystem;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.HashMap;

public class Galaxy {
    public Line name;
    public Location location;

    public HashMap<String, SolarSystem> solarSystemMap;

    public Galaxy(Line name, Location location) {
        this.name = name;
        this.location = location;

        solarSystemMap = new HashMap<>();
    }

    public static HashMap<String, Galaxy> loadDebugGalaxy() {
        HashMap<String, Galaxy> debugGalaxy = new HashMap<>();

        // Galaxy - Cotton Tail //
        Line cottonTailName = new Line("Cotton Tail Nebula", "7CONT5CONT6CONT", "", true, true);
        Location cottonTailLocation = new Location(cottonTailName.label);
        Galaxy galaxyCottonTail = new Galaxy(cottonTailName, cottonTailLocation);
        debugGalaxy.put(cottonTailName.label, galaxyCottonTail);

        // Solar System - Lago Morpha //
        Line lagoMorphaName = new Line("Lago Morpha", "5CONT6CONT", "", true, true);
        Location lagoMorphaLocation = new Location(cottonTailName.label, lagoMorphaName.label);
        SolarSystem systemLagoMorpha = new SolarSystem(lagoMorphaName, lagoMorphaLocation);
        galaxyCottonTail.solarSystemMap.put(lagoMorphaName.label, systemLagoMorpha);

        // Star - Rig Veda //
        Line rigVedaName = new Line("Rig Veda", "4CONT4CONT", "", true, true);
        Location rigVedaLocation = new Location(cottonTailName.label, lagoMorphaName.label, 0);
        Star starRigVeda = new Star(rigVedaName, rigVedaLocation);
        systemLagoMorpha.planetoidList.add(starRigVeda);

        // Planet - Lapine //
        Line lapineName = new Line("Lapine", "6CONT", "", true, true);
        Location lapineLocation = new Location(cottonTailName.label, lagoMorphaName.label, 1);
        Planet planetLapine = new Planet(lapineName, lapineLocation);
        systemLagoMorpha.planetoidList.add(planetLapine);

        // Area - Center Of The Universe //
        Line cotuAreaName = new Line("Center Of The Universe", "7CONT3CONT4CONT8CONT", "", true, true);
        Location cotuAreaLocation = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label);
        Area areaCOTU = new Area(cotuAreaName, cotuAreaLocation);
        planetLapine.areaMap.put(cotuAreaName.label, areaCOTU);

        // Room 00 - Center Of The Universe //
        Line cotuRoom00Name = new Line("Center Of The Universe", "7CONT3CONT4CONT8CONT", "", true, true);
        Location cotuRoom00Location = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label, 0);
        Room cotuRoom00 = new Room(cotuRoom00Name, null, cotuRoom00Location);
        areaCOTU.roomList.add(cotuRoom00);

        return debugGalaxy;
    }

    public static Room getRoom(String targetGalaxy, String targetSolarSystem, int planetoidIndex, String targetArea, int roomIndex) {
        if(GameScreen.galaxyList.containsKey(targetGalaxy)
        && GameScreen.galaxyList.get(targetGalaxy).solarSystemMap.containsKey(targetSolarSystem)
        && planetoidIndex < GameScreen.galaxyList.get(targetGalaxy).solarSystemMap.get(targetSolarSystem).planetoidList.size()
        && GameScreen.galaxyList.get(targetGalaxy).solarSystemMap.get(targetSolarSystem).planetoidList.get(planetoidIndex).isPlanet
        && ((Planet) GameScreen.galaxyList.get(targetGalaxy).solarSystemMap.get(targetSolarSystem).planetoidList.get(planetoidIndex)).areaMap.containsKey(targetArea)
        && roomIndex < ((Planet) GameScreen.galaxyList.get(targetGalaxy).solarSystemMap.get(targetSolarSystem).planetoidList.get(planetoidIndex)).areaMap.get(targetArea).roomList.size()) {
            return ((Planet) GameScreen.galaxyList.get(targetGalaxy).solarSystemMap.get(targetSolarSystem).planetoidList.get(planetoidIndex)).areaMap.get(targetArea).roomList.get(roomIndex);
        }

        return null;
    }
}
