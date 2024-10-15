package com.jbs.textgameengine.gamedata.entity;

import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Entity {
    public int id;
    public Location location;
    public String prefix;
    public Line name;
    public Line roomDescription;
    public ArrayList<String> nameKeyList;

    public boolean isPlayer;
    public boolean isMob;
    public boolean isItem;
    public boolean isSpaceship;

    public Entity(int id, Location startLocation) {
        this.id = id;
        this.location = new Location(startLocation.galaxy, startLocation.solarSystem, startLocation.planetoid, startLocation.area, startLocation.room, startLocation.spaceship);
        prefix = "A ";
        name = null;
        roomDescription = null;
        nameKeyList = null;

        isPlayer = false;
        isMob = false;
        isItem = false;
        isSpaceship = false;
    }

    public void displayLookDescription() {
        GameScreen.userInterface.console.writeToConsole(new Line("You see nothing special.", "4CONT4CONT8CONT7CONT1DY", "", true, true));
    }

    public static ArrayList<String> createNameKeyList(String targetString) {
        List<String> targetStringList = Arrays.asList(targetString.toLowerCase().split(" "));

        ArrayList<String> nameKeyList = new ArrayList<>();
        for(int i = 0; i < targetStringList.size(); i++) {
            List<String> splitString = targetStringList.subList(0, i + 1);
            String substring = splitString.stream().collect(Collectors.joining(" "));

            if(!nameKeyList.contains(targetStringList.get(i))) {
                nameKeyList.add(targetStringList.get(i));
            }
            if(!nameKeyList.contains(substring)) {
                nameKeyList.add(substring);
            }

            for(int ii = 0; ii < targetStringList.get(i).length(); ii++) {
                String substringPart = targetStringList.get(i).substring(0, ii + 1);
                if(!nameKeyList.contains(substringPart)) {
                    nameKeyList.add(substringPart);
                }
                if(ii + 1 >= 5 || ii + 1 >= targetStringList.get(i).length()) {
                    break;
                }
            }

            if(splitString.size() > 2) {
                for(int ii = 0; ii < splitString.size() - 2; ii++) {
                    List<String> splitSubstring = targetStringList.subList(1 + ii, targetStringList.size());
                    String subSubstring = splitSubstring.stream().collect(Collectors.joining(" "));
                    if(!nameKeyList.contains(subSubstring)) {
                        nameKeyList.add(subSubstring);
                    }
                }
            }
        }

        return nameKeyList;
    }
}
