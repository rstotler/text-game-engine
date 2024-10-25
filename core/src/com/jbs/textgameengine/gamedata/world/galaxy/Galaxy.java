package com.jbs.textgameengine.gamedata.world.galaxy;

import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.planetoid.Moon;
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

    public Galaxy(Line name) {
        this.name = name;
        location = new Location(this);

        solarSystemMap = new HashMap<>();
    }

    public static HashMap<String, Galaxy> loadDebugGalaxy() {
        HashMap<String, Galaxy> debugGalaxy = new HashMap<>();

        // Galaxy - Cotton Tail //
        Line cottonTailName = new Line("Cotton Tail Nebula", "7CONT5CONT6CONT", "", true, true);
        Galaxy galaxyCottonTail = new Galaxy(cottonTailName);
        debugGalaxy.put(cottonTailName.label, galaxyCottonTail);

        // Solar System - Lago Morpha //
        Line lagoMorphaName = new Line("Lago Morpha", "5CONT6CONT", "", true, true);
        Location lagoMorphaLocation = new Location(galaxyCottonTail);
        SolarSystem systemLagoMorpha = new SolarSystem(lagoMorphaName, lagoMorphaLocation);
        galaxyCottonTail.solarSystemMap.put(lagoMorphaName.label, systemLagoMorpha);

        // Star - Rig Veda //
        Line rigVedaName = new Line("Rig Veda", "4CONT4CONT", "", true, true);
        Location rigVedaLocation = new Location(galaxyCottonTail, systemLagoMorpha);
        Star starRigVeda = new Star(rigVedaName, rigVedaLocation, 0, -1, 7.25f, 36000, 0);
        systemLagoMorpha.planetoidList.add(starRigVeda);

        // Planet - Lapine (365 Days In Year, 1440 Minutes In Day) //
        Line lapineName = new Line("Lapine", "6CONT", "", true, true);
        Location lapineLocation = new Location(galaxyCottonTail, systemLagoMorpha);
        Planet planetLapine = new Planet(lapineName, lapineLocation, 92598974, -1, 23.45f, 1440, 525600);
        planetLapine.generateAreas();
        systemLagoMorpha.planetoidList.add(planetLapine);

        // Area - Center Of The Universe //
        Line cotuAreaName = new Line("Center Of The Universe", "7CONT3CONT4CONT8CONT", "", true, true);
        Location cotuAreaLocation = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine);
        Area areaCOTU = new Area(cotuAreaName, cotuAreaLocation);
        planetLapine.areaMap.put(cotuAreaName.label, areaCOTU);

        // Room 00 - Center Of The Universe //
        Line cotuRoom00Name = new Line("Center Of The Universe", "7CONT3CONT4CONT8CONT", "", true, true);
        Location cotuRoom00Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom00 = new Room(areaCOTU.roomList.size(), cotuRoom00Name, null, cotuRoom00Location);
        areaCOTU.roomList.add(cotuRoom00);

        // Room 01 - Standing On A Crystal Bridge //
        Line cotuRoom01Name = new Line("Standing On A Crystal Bridge", "9CONT3CONT2CONT8SHIAC6CONT", "", true, true);
        Location cotuRoom01Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom01 = new Room(areaCOTU.roomList.size(), cotuRoom01Name, null, cotuRoom01Location);
        cotuRoom01.createExit("South", cotuRoom00);
        areaCOTU.roomList.add(cotuRoom01);

        // Room 02 - A Peaceful Garden //
        Line cotuRoom02Name = new Line("A Peaceful Garden", "2CONT9CONT6SHIAG", "", true, true);
        Location cotuRoom02Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom02 = new Room(areaCOTU.roomList.size(), cotuRoom02Name, null, cotuRoom02Location);
        cotuRoom02.createExit("South", cotuRoom01);
        areaCOTU.roomList.add(cotuRoom02);

        // Room 03 - In A Wooden Cabin //
        Line cotuRoom03Name = new Line("In A Wooden Cabin", "3CONT2CONT7SHIAO5CONT", "", true, true);
        Location cotuRoom03Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom03 = new Room(areaCOTU.roomList.size(), cotuRoom03Name, null, cotuRoom03Location);
        cotuRoom03.inside = true;
        cotuRoom03.createExit("East", cotuRoom02, "Manual");
        areaCOTU.roomList.add(cotuRoom03);

        // Room 04 - Spaceport Entrance //
        Line cotuRoom04Name = new Line("Spaceport Entrance", "10CONT8CONT", "", true, true);
        Location cotuRoom04Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom04 = new Room(areaCOTU.roomList.size(), cotuRoom04Name, null, cotuRoom04Location);
        cotuRoom04.inside = true;
        cotuRoom04.createExit("North", cotuRoom00, "Automatic", 1234);
        areaCOTU.roomList.add(cotuRoom04);

        // Room 05 - Bridge To The Spaceport //
        Line cotuRoom05Name = new Line("Bridge To The Spaceport", "7CONT3CONT4CONT9CONT", "", true, true);
        Location cotuRoom05Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom05 = new Room(areaCOTU.roomList.size(), cotuRoom05Name, null, cotuRoom05Location);
        cotuRoom05.inside = true;
        cotuRoom05.createExit("North", cotuRoom04);
        areaCOTU.roomList.add(cotuRoom05);

        // Room 06 - Landing Pad //
        Line cotuRoom06Name = new Line("Landing Pad", "8CONT3CONT", "", true, true);
        Location cotuRoom06Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom06 = new Room(areaCOTU.roomList.size(), cotuRoom06Name, null, cotuRoom06Location);
        cotuRoom06.createExit("North", cotuRoom05, "Automatic");
        areaCOTU.roomList.add(cotuRoom06);
        planetLapine.landingPadList.add(cotuRoom06);

        // Room 07 - A Passage Hidden By Shrubbery //
        Line cotuRoom07Name = new Line("A Passage Hidden By Shrubbery", "2W8CONT7CONT3CONT9CONT", "", true, true);
        Location cotuRoom07Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom07 = new Room(areaCOTU.roomList.size(), cotuRoom07Name, null, cotuRoom07Location);
        cotuRoom07.createHiddenExit("West", cotuRoom02);
        areaCOTU.roomList.add(cotuRoom07);

        // Room 08 - Spaceport Gift Shop //
        Line cotuRoom08Name = new Line("Spaceport Gift Shop", "10CONT5CONT4CONT", "", true, true);
        Location cotuRoom08Location = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine, areaCOTU);
        Room cotuRoom08 = new Room(areaCOTU.roomList.size(), cotuRoom08Name, null, cotuRoom08Location);
        cotuRoom08.inside = true;
        cotuRoom08.createExit("West", cotuRoom04, "Automatic");
        areaCOTU.roomList.add(cotuRoom08);

        // Moon - Luna //
        Line lunaName = new Line("Luna", "4CONT", "", true, true);
        Location lunaLocation = new Location(galaxyCottonTail, systemLagoMorpha, planetLapine);
        Moon moonLuna = new Moon(lunaName, lunaLocation, 233738, -1, 5.15f, 43200, 518400);
        planetLapine.moonMap.put(lunaName.label, moonLuna);

        // Planet - Seres (670 Days In Year, 1480 Minutes In Day) //
        Line seresName = new Line("Seres", "5CONT", "", true, true);
        Location seresLocation = new Location(galaxyCottonTail, systemLagoMorpha);
        Planet planetSeres = new Planet(seresName, seresLocation, 142753817, -1, 25.20f, 1480, 991600);
        systemLagoMorpha.planetoidList.add(planetSeres);

        loadDebugEntities(debugGalaxy);

        return debugGalaxy;
    }

    public static void loadDebugEntities(HashMap<String, Galaxy> debugGalaxy) {
        Area areaCOTU = ((Planet) debugGalaxy.get("Cotton Tail Nebula").solarSystemMap.get("Lago Morpha").planetoidList.get(1)).areaMap.get("Center Of The Universe");
        areaCOTU.roomList.get(0).createEntity("Mob", null, 1);
        areaCOTU.roomList.get(0).createEntity("Item", "General", 1);

        areaCOTU.roomList.get(6).createEntity("Spaceship", null, 1);
        areaCOTU.roomList.get(8).createEntity("Mob", null, 2);

        Room roomWoodenCabin = areaCOTU.roomList.get(3);
        Item ornateChest = Item.load("General", 4, roomWoodenCabin.location);
        ornateChest.status = "Open";
        for(int i = 1; i <= 12; i ++) {
            ornateChest.containerItemList.add(Item.load("Gear", i, roomWoodenCabin.location));
        }
        areaCOTU.roomList.get(3).addItemToRoom(ornateChest);

        Item weaponCabinet = Item.load("General", 5, roomWoodenCabin.location);
        weaponCabinet.status = "Open";
        for(int i = 1; i <= 11; i++) {
            weaponCabinet.containerItemList.add(Item.load("Weapon", i, roomWoodenCabin.location));
        }
        areaCOTU.roomList.get(3).addItemToRoom(weaponCabinet);

        Item gunCabinet = Item.load("General", 6, roomWoodenCabin.location);
        gunCabinet.status = "Open";
        for(int i = 1; i <= 7; i++) {
            gunCabinet.containerItemList.add(Item.load("Firearm", i, roomWoodenCabin.location));
        }
        for(int i = 1; i <= 5; i++) {
            gunCabinet.containerItemList.add(Item.load("Magazine", i, roomWoodenCabin.location));
        }
        areaCOTU.roomList.get(3).addItemToRoom(gunCabinet);

        Item ammoCrate = Item.load("General", 7, roomWoodenCabin.location);
        ammoCrate.status = "Open";
        for(int i = 1; i <= 11; i++) {
            int ammoQuantity = 50;
            if(i == 11) {ammoQuantity = 25;}
            ammoCrate.containerItemList.add(Item.load("Ammo", i, roomWoodenCabin.location, ammoQuantity));
        }
        areaCOTU.roomList.get(3).addItemToRoom(ammoCrate);
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
