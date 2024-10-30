package com.jbs.textgameengine.screen.gamescreen.userinterface.roomview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class ImageManager {
    public HashMap<String, HashMap<String, HashMap<String, Texture>>> tileMap;

    public ImageManager() {
        tileMap = new HashMap<String, HashMap<String, HashMap<String, Texture>>>();

        loadImages();
    }

    public void loadImages() {
        disposeTextures();

        // Tiles //
        for(FileHandle directoryHandle : Gdx.files.internal("assets/images/gamescreen/roomview/tile").list()) {
            String rowString = directoryHandle.toString().substring(directoryHandle.toString().lastIndexOf("/") + 1);
            rowString = rowString.substring(0, 1).toUpperCase() + rowString.substring(1);

            tileMap.put(rowString, new HashMap<String, HashMap<String, Texture>>());

            for(FileHandle fileHandle : Gdx.files.internal(directoryHandle.toString()).list()) {
                if(fileHandle.toString().contains(".png")) {
                    String locationString = fileHandle.toString().substring(fileHandle.toString().lastIndexOf("/") + 1, fileHandle.toString().length() - 4);
                    locationString = locationString.substring(0, 1).toUpperCase() + locationString.substring(1);

                    tileMap.get(rowString).put(locationString, new HashMap<String, Texture>());

                    Texture texture = new Texture(fileHandle);
                    tileMap.get(rowString).get(locationString).put("Default", texture);
                }
            }
        }
    }

    public void disposeTextures() {

        // Tiles //
        for(String rowString : tileMap.keySet()) {
            for(String locationString : tileMap.get(rowString).keySet()) {
                for(String typeString : tileMap.get(rowString).get(locationString).keySet()) {
                    Texture texture = tileMap.get(rowString).get(locationString).get(typeString);
                    texture.dispose();
                }
            }
        }
        tileMap.clear();
    }
}
