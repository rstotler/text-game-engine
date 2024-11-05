package com.jbs.textgameengine.gamedata.world.planetoid;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Point;

import java.util.ArrayList;
import java.util.Random;

public class Planetoid {
    public static String[][] nameGenVowels = loadNameGenVowels();
    public static int[][] nameGenMatrix = loadNameGenMatrix();
    public static int AU_DISTANCE = 92598974;

    public Line name;
    public ArrayList<String> nameKeyList;
    public Location location;
    public boolean isPlanet;
    public Point coordinates;

    public int distanceFromCenter; // In Miles
    public int orbitDirection;     // 1 - Clockwise, -1 - Counter-Clockwise
    public float axialTilt;        // 0.0 - No Tilt, 100.0 - Full 90.0 Tilt

    public int minuteCountDay;
    public int minuteCountYear;
    public int minutesInDay;
    public int minutesInYear;

    public float dawnPercent;
    public float sunrisePercent;
    public float noonPercent;
    public float duskPercent;
    public float sunsetPercent;

    public static String[][] loadNameGenVowels() {
        String vowels[][] = {
                {"b", "c", "d", "f", "g", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"},
                {"a", "e", "o", "u"},
                {"br", "cr", "dr", "fr", "gr", "pr", "str", "tr", "bl", "cl", "fl", "gl", "pl", "sl", "sc", "sk", "sm", "sn", "sp", "st", "sw", "ch", "sh", "th", "wh"},
                {"ae", "ai", "ao", "au", "a", "ay", "ea", "ei", "eo", "eu", "e", "ey", "ua", "ue", "ui", "uo", "u", "uy", "ia", "ie", "iu", "io", "iy", "oa", "oe", "ou", "oi", "o", "oy"},
                {"turn", "ter", "nus", "rus", "tania", "hiri", "hines", "gawa", "nides", "carro", "rilia", "stea", "lia", "lea", "ria", "nov", "phus", "mia", "nerth", "wei", "ruta", "tov", "zuno", "vis", "lara", "nia", "liv", "tera", "gantu", "yama", "tune", "ter", "nus", "cury", "bos", "pra", "thea", "nope", "tis", "clite"},
                {"una", "ion", "iea", "iri", "illes", "ides", "agua", "olla", "inda", "eshan", "oria", "ilia", "erth", "arth", "orth", "oth", "illon", "ichi", "ov", "arvis", "ara", "ars", "yke", "yria", "onoe", "ippe", "osie", "one", "ore", "ade", "adus", "urn", "ypso", "ora", "iuq", "orix", "apus", "ion", "eon", "eron", "ao", "omia"}
        };

        return vowels;
    }

    public static int[][] loadNameGenMatrix() {
        int[][] matrix = {
                {1, 1, 2, 2, 5, 5},
                {2, 2, 3, 3, 6, 6},
                {3, 3, 4, 4, 5, 5},
                {4, 4, 3, 3, 6, 6},
                {3, 3, 4, 4, 2, 2, 5, 5},
                {2, 2, 1, 1, 3, 3, 6, 6},
                {3, 3, 4, 4, 2, 2, 5, 5},
                {4, 4, 3, 3, 1, 1, 6, 6},
                {3, 3, 4, 4, 1, 1, 4, 4, 5, 5},
                {4, 4, 1, 1, 4, 4, 3, 3, 6, 6}
        };

        return matrix;
    }

    public Planetoid(Line name, Location location, int distanceFromCenter, int orbitDirection, float axialTilt, int minutesInDay, int minutesInYear) {
        this.name = name;
        nameKeyList = Entity.createNameKeyList(name.label);
        this.location = new Location(location.galaxy, location.solarSystem, location.planetoid, null, null, null);
        isPlanet = false;
        coordinates = new Point(0, 0);

        this.distanceFromCenter = distanceFromCenter;
        this.orbitDirection = orbitDirection;
        this.axialTilt = axialTilt;

        int randomYearMinutes = 0;
        if(minutesInYear > 0) {
            randomYearMinutes = new Random().nextInt((int) (minutesInYear * .95f));
        }
        minuteCountDay = randomYearMinutes % minutesInDay;
        minuteCountYear = randomYearMinutes;
        this.minutesInDay = minutesInDay;
        this.minutesInYear = minutesInYear;

        dawnPercent = 0.0f;
        sunrisePercent = 0.0f;
        noonPercent = 0.0f;
        duskPercent = 0.0f;
        sunsetPercent = 0.0f;

        updatePosition();
    }

    public void update() {
        minuteCountDay += 1;
        if(distanceFromCenter > 0
        && minutesInYear > 0) {
            minuteCountYear += 1;
            updatePosition();
        }

        if(minuteCountDay >= minutesInDay) {
            minuteCountDay = 0;
        }
        if(minuteCountYear >= minutesInYear) {
            minuteCountYear = 0;
        }
    }

    public void updatePosition() {
        coordinates.x = distanceFromCenter;
        coordinates.y = 0;

        if(minutesInYear > 0) {
            coordinates.x = (float) Math.cos(Math.toRadians((minuteCountYear / (minutesInYear + 0.0f)) * 360)) * distanceFromCenter;
            coordinates.y = (float) (Math.sin(Math.toRadians((minuteCountYear / (minutesInYear + 0.0f)) * 360)) * distanceFromCenter) * orbitDirection;
        }
    }

    public boolean isDay() {
        return false;
    }

    public static String generateName() {
        StringBuilder name = new StringBuilder();
        int[] comp = nameGenMatrix[new Random().nextInt(nameGenMatrix.length)];

        for(int i = 0; i < comp.length / 2; i++) {
            int vowelIndex = comp[i * 2];
            int countIndex = comp[i * 2 + 1];
            name.append(nameGenVowels[vowelIndex - 1][new Random().nextInt(nameGenVowels[vowelIndex - 1].length)]);
        }

        String stringName = name.toString();
        if(stringName.length() > 1) {
            stringName = stringName.substring(0, 1).toUpperCase() + stringName.substring(1);
        } else {
            stringName = stringName.toUpperCase();
        }

        return stringName;
    }

    public int getCurrentHoursInYear() {
        return minuteCountYear / 60;
    }

    public int getHoursInDay() {
        return minutesInDay / 60;
    }

    public int getHoursInYear() {
        return minutesInYear / 60;
    }

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }
}
