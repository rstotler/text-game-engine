package com.jbs.textgameengine.gamedata.world;

import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;

import java.util.ArrayList;
import java.util.Arrays;

public class Location {
    public static ArrayList<String> directionList = new ArrayList<>(Arrays.asList("north", "nort", "nor", "no", "n", "east", "eas", "ea", "e", "south", "sout", "sou", "so", "s", "west", "wes", "we", "w", "northeast", "northeas", "northea", "northe", "ne", "southeast", "southeas", "southea", "southe", "se", "southwest", "southwes", "southwe", "southw", "sw", "northwest", "northwes", "northwe", "northw", "nw"));

    public String galaxy;
    public String solarSystem;
    public int planetoidIndex;
    public String area;
    public int roomIndex;

    public Spaceship spaceship;

    public Location(String galaxy, String solarSystem, int planetoidIndex, String area, int roomIndex) {
        this.galaxy = galaxy;
        this.solarSystem = solarSystem;
        this.planetoidIndex = planetoidIndex;
        this.area = area;
        this.roomIndex = roomIndex;

        spaceship = null;
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

    public static String getDirectionFromSubstring(String directionSubstring) {
        directionSubstring = directionSubstring.toLowerCase();

        if((directionSubstring.length() >= 2 && directionSubstring.substring(0, 2).equals("ne"))
        || (directionSubstring.length() >= 6 && directionSubstring.substring(0, 6).equals("northe"))) {
            return "Northeast";
        }
        else if((directionSubstring.length() >= 2 && directionSubstring.substring(0, 2).equals("se"))
        || (directionSubstring.length() >= 6 && directionSubstring.substring(0, 6).equals("southe"))) {
            return "Southeast";
        }
        else if((directionSubstring.length() >= 2 && directionSubstring.substring(0, 2).equals("sw"))
        || (directionSubstring.length() >= 6 && directionSubstring.substring(0, 6).equals("southw"))) {
            return "Southwest";
        }
        else if((directionSubstring.length() >= 2 && directionSubstring.substring(0, 2).equals("nw"))
        || (directionSubstring.length() >= 6 && directionSubstring.substring(0, 6).equals("northw"))) {
            return "Northwest";
        }
        else if(directionSubstring.substring(0, 1).equals("n")) {
            return "North";
        }
        else if(directionSubstring.substring(0, 1).equals("e")) {
            return "East";
        }
        else if(directionSubstring.substring(0, 1).equals("s")) {
            return "South";
        }
        else if(directionSubstring.substring(0, 1).equals("w")) {
            return "West";
        }

        return "";
    }

    public static String getOppositeDirection(String targetDirection) {
        if(targetDirection.equals("Northeast")) {return "Southwest";}
        else if(targetDirection.equals("Southeast")) {return "Northwest";}
        else if(targetDirection.equals("Southwest")) {return "Northeast";}
        else if(targetDirection.equals("Northwest")) {return "Southeast";}
        else if(targetDirection.equals("North")) {return "South";}
        else if(targetDirection.equals("East")) {return "West";}
        else if(targetDirection.equals("South")) {return "North";}
        else if(targetDirection.equals("West")) {return "East";}

        return "";
    }
}
