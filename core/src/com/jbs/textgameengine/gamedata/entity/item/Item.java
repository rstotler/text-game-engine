package com.jbs.textgameengine.gamedata.entity.item;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Item extends Entity {
    public int id;
    public String pocket;

    public ArrayList<Integer> keyList;
    public ArrayList<Item> containerItemList;

    public Item(Room startRoom) {
        super(startRoom);

        pocket = "General";

        keyList = null;
        containerItemList = null;
    }

    public static Item load(int id, Room startRoom) {
        Item item = new Item(startRoom);

        // 1 - A Silver Key //
        if(id == 1) {
            item.name = new Line("A Silver Key", "2W7SHIAGR3CONT", "", true, true);
            item.keyList = new ArrayList<>();
            item.keyList.add(1234);
            item.keyList.add(12345);
        }

        return item;
    }
}
