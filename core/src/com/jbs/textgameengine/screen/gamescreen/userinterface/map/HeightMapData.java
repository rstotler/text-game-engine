package com.jbs.textgameengine.screen.gamescreen.userinterface.map;

import com.badlogic.gdx.graphics.Color;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

public class HeightMapData {
    public float[][] elevation;

    public HeightMapData(int width, int height) {
        elevation = new float[width][height];
    }

    public void setData(int x, int y, float value) {
        elevation[x][y] = value;
    }

    public String getTileType(int x, int y) {
        if(elevation[x][y] > .60) {
            return "Grass (Dark)";
        }
        else if(elevation[x][y] > .10) {
            return "Grass (Light)";
        }
        else if(elevation[x][y] > 0) {
            return "Beach";
        }
        else if(elevation[x][y] > -0.125) {
            return "Water (Shallow)";
        }
        else {
            return "Water (Deep)";
        }
    }

    public static Color getTileColor(String tileType) {
        if(tileType.equals("Grass (Dark)")) {
            return new Color(72/255f, 95/255f, 41/255f, 1);
        }
        else if(tileType.equals("Grass (Light)")) {
            return new Color(92/255f, 153/255f, 23/255f, 1);
        }
        else if(tileType.equals("Beach")) {
            return new Color(204/255f, 197/255f, 127/255f, 1);
        }
        else if(tileType.equals("Water (Shallow)")) {
            return new Color(52/255f, 105/255f, 201/255f, 1);
        }
        else if(tileType.equals("Water (Deep)")) {
            return new Color(45/255f, 83/255f, 158/255f, 1);
        }

        return new Color(0/255f, 0/255f, 0/255f, 1);
    }

    public static Line getTileName(String tileType) {
        if(tileType.startsWith("Grass")) {
            return new Line("Grass", "5CONT", "", true, true);
        }
        else if(tileType.equals("Beach")) {
            return new Line("Beach", "5CONT", "", true, true);
        }
        else if(tileType.startsWith("Water")) {
            return new Line("Water", "5CONT", "", true, true);
        }

        return new Line("Default Name", "8CONT4CONT", "", true, true);
    }
}
