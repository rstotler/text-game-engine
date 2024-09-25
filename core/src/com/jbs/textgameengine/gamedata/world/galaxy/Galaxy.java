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

        // Room 01 - Standing On A Crystal Bridge //
        Line cotuRoom01Name = new Line("Standing On A Crystal Bridge", "9CONT3CONT2CONT8SHIAC6CONT", "", true, true);
        Location cotuRoom01Location = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label, 1);
        Room cotuRoom01 = new Room(cotuRoom01Name, null, cotuRoom01Location);
        cotuRoom01.createExit("South", cotuRoom00);
        areaCOTU.roomList.add(cotuRoom01);

        // Room 02 - A Peaceful Garden //
        Line cotuRoom02Name = new Line("A Peaceful Garden", "2CONT9CONT6SHIAG", "", true, true);
        Location cotuRoom02Location = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label, 2);
        Room cotuRoom02 = new Room(cotuRoom02Name, null, cotuRoom02Location);
        cotuRoom02.createExit("South", cotuRoom01);
        areaCOTU.roomList.add(cotuRoom02);

        // Room 03 - In A Wooden Cabin //
        Line cotuRoom03Name = new Line("In A Wooden Cabin", "3CONT2CONT7SHIADO5CONT", "", true, true);
        Location cotuRoom03Location = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label, 3);
        Room cotuRoom03 = new Room(cotuRoom03Name, null, cotuRoom03Location);
        cotuRoom03.createExit("East", cotuRoom02, "Manual", 1234);
        areaCOTU.roomList.add(cotuRoom03);

        // Room 04 - Spaceport Entrance //
        Line cotuRoom04Name = new Line("Spaceport Entrance", "10CONT8CONT", "", true, true);
        Location cotuRoom04Location = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label, 4);
        Room cotuRoom04 = new Room(cotuRoom04Name, null, cotuRoom04Location);
        cotuRoom04.createExit("North", cotuRoom00, "Automatic");
        areaCOTU.roomList.add(cotuRoom04);

        // Room 05 - Bridge To The Spaceport //
        Line cotuRoom05Name = new Line("Bridge To The Spaceport", "7CONT3CONT4CONT9CONT", "", true, true);
        Location cotuRoom05Location = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label, 5);
        Room cotuRoom05 = new Room(cotuRoom05Name, null, cotuRoom05Location);
        cotuRoom05.createExit("North", cotuRoom04);
        areaCOTU.roomList.add(cotuRoom05);

        // Room 06 - Launch Pad //
        Line cotuRoom06Name = new Line("Launch Pad", "7CONT3CONT", "", true, true);
        Location cotuRoom06Location = new Location(cottonTailName.label, lagoMorphaName.label, 1, cotuAreaName.label, 6);
        Room cotuRoom06 = new Room(cotuRoom06Name, null, cotuRoom06Location);
        cotuRoom06.createExit("North", cotuRoom05);
        areaCOTU.roomList.add(cotuRoom06);

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
