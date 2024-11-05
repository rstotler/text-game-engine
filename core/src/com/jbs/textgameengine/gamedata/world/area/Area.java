package com.jbs.textgameengine.gamedata.world.area;

import com.badlogic.gdx.graphics.Color;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.gamedata.world.planetoid.Star;
import com.jbs.textgameengine.gamedata.world.planetoid.planet.Planet;
import com.jbs.textgameengine.gamedata.world.planetoid.planet.WeatherSystem;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;
import java.util.Random;

public class Area {
    public Line name;
    public Location location;

    public String mapKey;
    public Color mapColor;

    public ArrayList<Room> roomList;

    float latitude; // 1.0 - Equator, 0.0 - Pole (Only Affects Weather, Not Day/Night Cycle)
    int weatherStartTimer;
    public WeatherSystem weatherSystem;

    public Area(Line name, Location location) {
        this.name = name;
        this.location = location;
        this.location.area = this;

        mapKey = "";
        mapColor = new Color(0, 0, 0, 1);
        int randomIndex = new Random().nextInt(3);
        if(randomIndex == 0) {mapColor.r += new Random().nextFloat() * .10f;}
        else if(randomIndex == 1) {mapColor.g += new Random().nextFloat() * .35f;}
        else {mapColor.b += new Random().nextFloat() * .35f;}

        roomList = new ArrayList<>();

        latitude = 0.50f;
        weatherStartTimer = 1;
        weatherSystem = null;
    }

    public void update() {

        getTemperature();

        // Update Weather Timer //
        if(weatherSystem == null) {
            if(weatherStartTimer != -1 && weatherStartTimer > 0) {
                weatherStartTimer -= 1;
                if(weatherStartTimer == 0) {
                    weatherSystem = new WeatherSystem();

                    // Weather Message //
                    if(GameScreen.player.location.area == this) {
                        weatherSystem.displayMessage("Weather Start");
                    }
                }
            }
        }
        else {
            weatherSystem.update();
            if(weatherSystem.timer <= 0) {

                // Weather Message //
                if(GameScreen.player.location.area == this) {
                    weatherSystem.displayMessage("Weather Stop");
                }

                weatherSystem = null;
            }
        }
    }

    public float getTemperature() {
        float starBrightness = ((Star) location.solarSystem.planetoidList.get(0)).brightness;
        Planet parentPlanet = (Planet) location.planetoid;

        float parentPlanetAUDistance = parentPlanet.distanceFromCenter / (Planetoid.AU_DISTANCE + 0.0f);
        float temperature = (float) (278.3f * (Math.pow(starBrightness, .25f) / Math.sqrt(parentPlanetAUDistance)));
        temperature = (int) Math.ceil(((temperature - 273) * 1.8f) + 32);
        int temperaturePercentage35 = (int) (temperature * .35f);
        float distancePercent = 1.0f - (parentPlanetAUDistance * 0.02f);
        if(distancePercent > 1.0) {distancePercent = 1.0f;}
        else if(distancePercent < 0) {distancePercent = 0;}
        int nightDayRatio = (int) Math.ceil(Math.cos(Math.toRadians(((parentPlanet.minuteCountDay + 0.0f) / (parentPlanet.getHoursInDay() * 60)) * 360)) * 100) * -1;

        // Atmosphere //
        int atmosphereTemp = 0;
        int distanceTempMod = (int) (Math.ceil(distancePercent * temperature));
        if(distanceTempMod < 0) {distanceTempMod *= -1;}
        atmosphereTemp += ((int) Math.round(parentPlanet.atmosphereLevel * distanceTempMod)) * 2;
        nightDayRatio += parentPlanet.atmosphereLevel * 2;

        // Latitude //
        int latitudeTemp = 0;
        if(temperature >= 0) {
            latitudeTemp += (int) Math.ceil(latitude * (distancePercent * temperature));
        }
        else {
            latitudeTemp += (int) Math.ceil(latitude * (distancePercent * (temperature * -1)));
        }

        // Day/Night Atmosphere (Applied To NightDayRatio) //
        int nightDayTemp = 0;
        if(temperature >= 0) {
            nightDayTemp += (int) Math.ceil((nightDayRatio / 100.0f) * (distancePercent * temperaturePercentage35));
        }
        else {
            nightDayTemp += (int) Math.ceil((nightDayRatio / 100.0f) * (distancePercent * (temperaturePercentage35 * -1)));
        }

        // Season/Axial Tilt //
        int seasonTemp = 0;
        float seasonPercentage = (float) Math.cos(Math.toRadians((parentPlanet.getCurrentHoursInYear() / (parentPlanet.getHoursInYear() + 0.0f)) * 360)) * -1;
        float tiltPercent = (float) Math.ceil(parentPlanet.axialTilt / 90.0f);
        int distanceTemp = (int) Math.ceil(distancePercent * temperature);
        int seasonTempMod = (int) Math.ceil(tiltPercent * distanceTemp);
        if(parentPlanet.axialTilt != 0 && seasonPercentage != 0
        && seasonPercentage >= -1.0 && seasonPercentage <= 1.0) {
            seasonTemp += (int) Math.ceil(seasonPercentage * seasonTempMod);
        }

        return temperature + latitudeTemp + nightDayTemp + seasonTemp + atmosphereTemp;
    }
}
