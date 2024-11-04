package com.jbs.textgameengine.gamedata.world.solarsystem;

import com.jbs.textgameengine.gamedata.entity.spaceship.Spaceship;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.planetoid.Planetoid;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class SolarSystem {
    public Line name;
    public Location location;

    public ArrayList<Planetoid> planetoidList;
    public ArrayList<Spaceship> spaceshipList;

    public SolarSystem(Line name, Location location) {
        this.name = name;
        this.location = location;
        this.location.solarSystem = this;

        planetoidList = new ArrayList<>();
        spaceshipList = new ArrayList<>();
    }

    public void update() {
        for(Planetoid planetoid : planetoidList) {
            planetoid.update();

            if(GameScreen.player.location.planetoid == planetoid) {
                GameScreen.userInterface.roomView.buffer(GameScreen.player.location, GameScreen.player.facingDirection);
            }
        }

        int iMod = 0;
        for(int i = 0; i < spaceshipList.size(); i++) {
            Spaceship spaceship = spaceshipList.get(i + iMod);
            boolean landCheck = spaceship.update();
            if(landCheck) {iMod -= 1;}
        }
    }
}
