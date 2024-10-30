package com.jbs.textgameengine.screen.gamescreen.userinterface.roomview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class ImageManager {
    public HashMap<String, HashMap<String, HashMap<String, Texture>>> floorMap;
    public HashMap<String, HashMap<String, HashMap<String, Texture>>> wallMap;
    public HashMap<String, HashMap<String, HashMap<String, Texture>>> ceilingMap;

    public ImageManager() {
        floorMap = new HashMap<String, HashMap<String, HashMap<String, Texture>>>();
        wallMap = new HashMap<String, HashMap<String, HashMap<String, Texture>>>();
        ceilingMap = new HashMap<String, HashMap<String, HashMap<String, Texture>>>();

        loadImages();
    }

    public void loadImages() {
        disposeTextures();

        // Tiles //
        for(FileHandle tileTypeDirectoryHandle : Gdx.files.internal("assets/images/gamescreen/roomview/tile").list()) {
            String tileTypeString = tileTypeDirectoryHandle.toString().substring(tileTypeDirectoryHandle.toString().lastIndexOf("/") + 1);
            tileTypeString = tileTypeString.substring(0, 1).toUpperCase() + tileTypeString.substring(1);

            HashMap<String, HashMap<String, HashMap<String, Texture>>> targetMap;
            if(tileTypeString.equals("Ceiling")) {targetMap = ceilingMap;}
            else if(tileTypeString.equals("Floor")) {targetMap = floorMap;}
            else {targetMap = wallMap;}

            for(FileHandle rowDirectoryHandle : Gdx.files.internal(tileTypeDirectoryHandle.toString()).list()) {
                String rowString = rowDirectoryHandle.toString().substring(rowDirectoryHandle.toString().lastIndexOf("/") + 1);
                rowString = rowString.substring(0, 1).toUpperCase() + rowString.substring(1);

                targetMap.put(rowString, new HashMap<String, HashMap<String, Texture>>());

                for(FileHandle typeDirectoryHandle : Gdx.files.internal(rowDirectoryHandle.toString()).list()) {
                    String typeString = typeDirectoryHandle.toString().substring(typeDirectoryHandle.toString().lastIndexOf("/") + 1);
                    typeString = typeString.substring(0, 1).toUpperCase() + typeString.substring(1);

                    for(FileHandle fileDirectoryHandle : Gdx.files.internal(typeDirectoryHandle.toString()).list()) {
                        if(fileDirectoryHandle.toString().contains(".png")) {
                            String columnString = fileDirectoryHandle.toString().substring(fileDirectoryHandle.toString().lastIndexOf("/") + 1, fileDirectoryHandle.toString().length() - 4);
                            columnString = columnString.substring(0, 1).toUpperCase() + columnString.substring(1);

                            if(!targetMap.get(rowString).containsKey(columnString)) {
                                targetMap.get(rowString).put(columnString, new HashMap<String, Texture>());
                            }
                            if(!targetMap.get(rowString).get(columnString).containsKey(typeString)) {
                                Texture texture = new Texture(fileDirectoryHandle);
                                targetMap.get(rowString).get(columnString).put(typeString, texture);
                            }
                        }
                    }
                }
            }
        }
    }

    public void disposeTextures() {

        // Floor Tiles //
        for(String rowString : floorMap.keySet()) {
            for(String locationString : floorMap.get(rowString).keySet()) {
                for(String typeString : floorMap.get(rowString).get(locationString).keySet()) {
                    Texture texture = floorMap.get(rowString).get(locationString).get(typeString);
                    texture.dispose();
                }
            }
        }
        floorMap.clear();

        // Wall Tiles //
        for(String rowString : wallMap.keySet()) {
            for(String locationString : wallMap.get(rowString).keySet()) {
                for(String typeString : wallMap.get(rowString).get(locationString).keySet()) {
                    Texture texture = wallMap.get(rowString).get(locationString).get(typeString);
                    texture.dispose();
                }
            }
        }
        wallMap.clear();

        // Ceiling Tiles //
        for(String rowString : ceilingMap.keySet()) {
            for(String locationString : ceilingMap.get(rowString).keySet()) {
                for(String typeString : ceilingMap.get(rowString).get(locationString).keySet()) {
                    Texture texture = ceilingMap.get(rowString).get(locationString).get(typeString);
                    texture.dispose();
                }
            }
        }
        ceilingMap.clear();
    }
}
