package com.jbs.textgameengine.gamedata.world.planetoid.planet;

public class WeatherSystem {
    public int timer;

    public WeatherSystem() {
        timer = 10;
    }

    public void update() {
        timer -= 1;
    }

    public void displayMessage(String messageType) {
    }
}
